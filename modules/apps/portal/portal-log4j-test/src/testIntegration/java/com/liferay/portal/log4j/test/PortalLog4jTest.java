/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.log4j.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.io.unsync.UnsyncStringWriter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.events.StartupHelperUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogContext;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogWrapper;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.log.Log4jLogContextUpgradeLogWrapper;
import com.liferay.portal.log.Log4jLogFactoryImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Hai Yu
 */
@RunWith(Arquillian.class)
public class PortalLog4jTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_tempLogFileDirPath = Files.createTempDirectory(
			PortalLog4jTest.class.getName());

		Logger logger = (Logger)LogManager.getLogger(PortalLog4jTest.class);

		logger.setAdditive(false);
		logger.setLevel(Level.TRACE);

		Logger upgradeLogger = (Logger)LogManager.getLogger(
			TestUpgradeProcess.class.getName());

		upgradeLogger.setAdditive(false);
		upgradeLogger.setLevel(Level.TRACE);

		Logger rootLogger = (Logger)LogManager.getRootLogger();

		Map<String, Appender> appenders = rootLogger.getAppenders();

		for (Appender appender : appenders.values()) {
			if ((appender instanceof ConsoleAppender) &&
				Objects.equals("CONSOLE", appender.getName())) {

				ConsoleAppender consoleAppender =
					ConsoleAppender.createDefaultAppenderForLayout(
						appender.getLayout());

				OutputStreamManager outputStreamManager =
					consoleAppender.getManager();

				_testOutputStream = new TestOutputStream(
					(OutputStream)ReflectionTestUtil.getFieldValue(
						outputStreamManager, "outputStream"));

				ReflectionTestUtil.getAndSetFieldValue(
					outputStreamManager, "outputStream", _testOutputStream);

				consoleAppender.start();

				logger.addAppender(consoleAppender);

				upgradeLogger.addAppender(consoleAppender);
			}
			else if (appender instanceof RollingFileAppender) {
				if (Objects.equals("TEXT_FILE", appender.getName())) {
					_textLogFilePath = _initFileAppender(
						logger, appender, _tempLogFileDirPath.toString());
				}
				else if (Objects.equals("XML_FILE", appender.getName())) {
					_xmlLogFilePath = _initFileAppender(
						logger, appender, _tempLogFileDirPath.toString());
				}
			}
		}
	}

	@AfterClass
	public static void tearDownClass() throws IOException {
		Logger logger = (Logger)LogManager.getLogger(PortalLog4jTest.class);

		Map<String, Appender> appenders = logger.getAppenders();

		for (Appender appender : appenders.values()) {
			logger.removeAppender(appender);

			appender.stop();
		}

		Logger upgradeLogger = (Logger)LogManager.getLogger(
			TestUpgradeProcess.class.getName());

		Map<String, Appender> upgradeAppenders = upgradeLogger.getAppenders();

		for (Appender appender : upgradeAppenders.values()) {
			upgradeLogger.removeAppender(appender);

			appender.stop();
		}

		Files.deleteIfExists(_textLogFilePath);
		Files.deleteIfExists(_xmlLogFilePath);

		Files.deleteIfExists(_tempLogFileDirPath);
	}

	@Test
	public void testDefaultLevel() {
		Logger logger = (Logger)LogManager.getLogger("test.logger");

		Assert.assertFalse(logger.isDebugEnabled());
		Assert.assertTrue(logger.isInfoEnabled());
	}

	@Test
	public void testLogOutput() throws Exception {
		_testLogOutput("DEBUG");
		_testLogOutput("ERROR");
		_testLogOutput("FATAL");
		_testLogOutput("INFO");
		_testLogOutput("TRACE");
		_testLogOutput("WARN");
	}

	@Test
	public void testLogOutputWithLogContext() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(PortalLog4jTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		String logContextName = "TestLogContext";

		String key1 = "test.key.1";
		String key2 = "test.key.2";
		String value1 = "test.value.1";
		String value2 = "test.value.2";

		ServiceRegistration<LogContext> serviceRegistration =
			bundleContext.registerService(
				LogContext.class,
				new LogContext() {

					@Override
					public Map<String, String> getContext() {
						return HashMapBuilder.put(
							key1, value1
						).put(
							key2, value2
						).build();
					}

					@Override
					public String getName() {
						return logContextName;
					}

				},
				new HashMapDictionary());

		PatternLayout.Builder builder = PatternLayout.newBuilder();

		builder.withPattern("%level - %m%n %X");

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		Appender logContextWriterAppender = WriterAppender.createAppender(
			builder.build(), null, unsyncStringWriter,
			"logContextWriterAppender", false, false);

		logContextWriterAppender.start();

		Logger logger = (Logger)LogManager.getLogger(PortalLog4jTest.class);

		logger.addAppender(logContextWriterAppender);

		String logContextMessage = StringBundler.concat(
			StringPool.OPEN_CURLY_BRACE, logContextName, StringPool.PERIOD,
			key1, StringPool.EQUAL, value1, ", ", logContextName,
			StringPool.PERIOD, key2, StringPool.EQUAL, value2,
			StringPool.CLOSE_CURLY_BRACE);

		_testLogOutputWithLogContext(
			"DEBUG", unsyncStringWriter, logContextMessage);
		_testLogOutputWithLogContext(
			"ERROR", unsyncStringWriter, logContextMessage);
		_testLogOutputWithLogContext(
			"FATAL", unsyncStringWriter, logContextMessage);
		_testLogOutputWithLogContext(
			"INFO", unsyncStringWriter, logContextMessage);
		_testLogOutputWithLogContext(
			"TRACE", unsyncStringWriter, logContextMessage);
		_testLogOutputWithLogContext(
			"WARN", unsyncStringWriter, logContextMessage);

		serviceRegistration.unregister();

		logger.removeAppender(logContextWriterAppender);
	}

	@Test
	public void testLogOutputWithLogContextUpgradeEnabledClassWhenNotUpgrading()
		throws Exception {

		String logContextMessage =
			StringPool.OPEN_CURLY_BRACE + StringPool.CLOSE_CURLY_BRACE;

		_testOutputForUpgrades(logContextMessage, true, false);
	}

	@Test
	public void testLogOutputWithLogContextUpgradeEnabledClassWhenUpgrading()
		throws Exception {

		String contextValue = ReflectionTestUtil.getFieldValue(
			Log4jLogContextUpgradeLogWrapper.class,
			"_UPGRADE_LOG_CONTEXT_NAME");

		String logContextMessage = StringBundler.concat(
			StringPool.OPEN_CURLY_BRACE, contextValue, StringPool.EQUAL,
			contextValue, StringPool.CLOSE_CURLY_BRACE);

		_testOutputForUpgrades(logContextMessage, true, true);
	}

	@Test
	public void testLogOutputWithLogContextUpgradeNotEnabledClassWhenNotUpgrading()
		throws Exception {

		String logContextMessage =
			StringPool.OPEN_CURLY_BRACE + StringPool.CLOSE_CURLY_BRACE;

		_testOutputForUpgrades(logContextMessage, false, false);
	}

	@Test
	public void testLogOutputWithLogContextUpgradeNotEnabledClassWhenUpgrading()
		throws Exception {

		String logContextMessage =
			StringPool.OPEN_CURLY_BRACE + StringPool.CLOSE_CURLY_BRACE;

		_testOutputForUpgrades(logContextMessage, false, true);
	}

	protected void outputLog(
		String level, Log log, String message, Throwable throwable) {

		if (level.equals("DEBUG")) {
			if ((message == null) && (throwable != null)) {
				log.debug(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.debug(message);
			}
			else {
				log.debug(message, throwable);
			}
		}
		else if (level.equals("ERROR")) {
			if ((message == null) && (throwable != null)) {
				log.error(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.error(message);
			}
			else {
				log.error(message, throwable);
			}
		}
		else if (level.equals("FATAL")) {
			if ((message == null) && (throwable != null)) {
				log.fatal(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.fatal(message);
			}
			else {
				log.fatal(message, throwable);
			}
		}
		else if (level.equals("INFO")) {
			if ((message == null) && (throwable != null)) {
				log.info(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.info(message);
			}
			else {
				log.info(message, throwable);
			}
		}
		else if (level.equals("TRACE")) {
			if ((message == null) && (throwable != null)) {
				log.trace(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.trace(message);
			}
			else {
				log.trace(message, throwable);
			}
		}
		else if (level.equals("WARN")) {
			if ((message == null) && (throwable != null)) {
				log.warn(throwable);
			}
			else if ((message != null) && (throwable == null)) {
				log.warn(message);
			}
			else {
				log.warn(message, throwable);
			}
		}
	}

	private static Path _initFileAppender(
		Logger logger, Appender appender, String tempLogDir) {

		RollingFileAppender portalRollingFileAppender =
			(RollingFileAppender)appender;

		String testFilePattern = StringBundler.concat(
			StringUtil.replace(tempLogDir, '\\', '/'), StringPool.SLASH,
			StringUtil.extractLast(
				portalRollingFileAppender.getFilePattern(), StringPool.SLASH));

		LoggerContext loggerContext = (LoggerContext)LogManager.getContext();

		RollingFileAppender testRollingFileAppender =
			RollingFileAppender.createAppender(
				null, testFilePattern, Boolean.TRUE.toString(),
				portalRollingFileAppender.getName(), Boolean.TRUE.toString(),
				String.valueOf(_BUFFER_SIZE), Boolean.TRUE.toString(),
				portalRollingFileAppender.getTriggeringPolicy(), null,
				portalRollingFileAppender.getLayout(), null,
				Boolean.FALSE.toString(), null, null,
				loggerContext.getConfiguration());

		testRollingFileAppender.start();

		logger.addAppender(testRollingFileAppender);

		RollingFileManager testRollingFileManager =
			testRollingFileAppender.getManager();

		return Paths.get(testRollingFileManager.getFileName());
	}

	private void _assertTextLog(
		String expectedLevel, String expectedMessage,
		Throwable expectedThrowable, String actualOutput) {

		String[] outputLines = StringUtil.splitLines(actualOutput);

		Assert.assertTrue(
			"The log output should have at least 1 line",
			outputLines.length > 0);

		String messageLine = outputLines[0];

		// Timestamp

		Matcher dateMatcher = _datePattern.matcher(
			messageLine.substring(0, _DATE_FORMAT.length()));

		Assert.assertTrue(
			"Output date format should be yyyy-MM-dd HH:mm:ss.SSS",
			dateMatcher.matches());

		// Level

		messageLine = messageLine.substring(_DATE_FORMAT.length());

		Assert.assertEquals(
			StringBundler.concat(
				StringPool.SPACE, expectedLevel, StringPool.SPACE),
			messageLine.substring(0, expectedLevel.length() + 2));

		// [ThreadName]

		messageLine = messageLine.substring(
			messageLine.indexOf(StringPool.OPEN_BRACKET));

		Thread currentThread = Thread.currentThread();

		String expectedThreadName = StringBundler.concat(
			StringPool.OPEN_BRACKET, currentThread.getName(),
			StringPool.CLOSE_BRACKET);

		Assert.assertEquals(
			expectedThreadName,
			messageLine.substring(0, expectedThreadName.length()));

		// [ClassName:LineNumber]

		messageLine = messageLine.substring(expectedThreadName.length());

		String expectedClassName = StringBundler.concat(
			StringPool.OPEN_BRACKET, PortalLog4jTest.class.getSimpleName(),
			StringPool.COLON);

		Assert.assertEquals(
			expectedClassName,
			messageLine.substring(0, expectedClassName.length()));

		messageLine = messageLine.substring(expectedClassName.length());

		int classNameEndIndex = messageLine.indexOf(StringPool.CLOSE_BRACKET);

		Integer.valueOf(messageLine.substring(0, classNameEndIndex - 1));

		// Message

		messageLine = messageLine.substring(classNameEndIndex + 1);

		Assert.assertEquals(
			String.valueOf(expectedMessage), messageLine.trim());

		// Throwable

		if (expectedThrowable != null) {
			Class<?> expectedThrowableClass = expectedThrowable.getClass();

			Assert.assertEquals(
				expectedThrowableClass.getName() + ": " +
					expectedThrowable.getMessage(),
				outputLines[1]);

			String actualFirstPrefixStackTraceElement = outputLines[2].trim();

			Assert.assertTrue(
				"A throwable should be logged and the first stack should be " +
					PortalLog4jTest.class.getName(),
				actualFirstPrefixStackTraceElement.startsWith(
					"at " + PortalLog4jTest.class.getName()));
		}
	}

	private void _assertXmlLog(
		String expectedLevel, String expectedMessage,
		Throwable expectedThrowable, String actualOutput) {

		String[] outputLines = StringUtil.splitLines(actualOutput);

		Assert.assertTrue(
			"The log output should have at least 1 line",
			outputLines.length > 0);

		// <log4j:event />

		String log4JEventLine = outputLines[0];

		String log4JEvent = log4JEventLine.substring(
			log4JEventLine.indexOf(StringPool.SPACE),
			log4JEventLine.indexOf(StringPool.GREATER_THAN));

		// <log4j:event logger="..." />

		String expectedLog4JEventLogger = StringBundler.concat(
			" logger=\"", PortalLog4jTest.class.getName(), "\" ");

		Assert.assertEquals(
			expectedLog4JEventLogger,
			log4JEvent.substring(0, expectedLog4JEventLogger.length()));

		// <log4j:event timestamp="..." />

		log4JEvent = log4JEvent.substring(expectedLog4JEventLogger.length());

		String actualLog4JEventTimestamp = log4JEvent.substring(
			log4JEvent.indexOf(StringPool.QUOTE) + 1,
			log4JEvent.indexOf(StringPool.SPACE) - 1);

		Long.valueOf(actualLog4JEventTimestamp);

		// <log4j:event level="..." />

		log4JEvent = log4JEvent.substring(
			"timestamp=".length() + actualLog4JEventTimestamp.length() + 2);

		String expectedLog4JEventLevel = StringBundler.concat(
			" level=\"", expectedLevel, "\" ");

		Assert.assertEquals(
			expectedLog4JEventLevel,
			log4JEvent.substring(0, expectedLog4JEventLevel.length()));

		// <log4j:event thread="..." />

		log4JEvent = log4JEvent.substring(expectedLog4JEventLevel.length());

		Thread currentThread = Thread.currentThread();

		String expectedLog4JEventThread = StringBundler.concat(
			"thread=\"", currentThread.getName(), StringPool.QUOTE);

		Assert.assertEquals(
			expectedLog4JEventThread,
			log4JEvent.substring(0, expectedLog4JEventThread.length()));

		// <log4j:message>...</log4j:message>

		Assert.assertEquals(
			StringBundler.concat(
				"<log4j:message><![CDATA[", expectedMessage,
				"]]></log4j:message>"),
			outputLines[1]);

		// <log4j:throwable>...</log4j:throwable>

		if (expectedThrowable != null) {
			Class<?> expectedThrowableClass = expectedThrowable.getClass();

			Assert.assertEquals(
				"<log4j:throwable><![CDATA[" + expectedThrowableClass.getName(),
				outputLines[2]);

			String actualFirstPrefixStackTraceElement = outputLines[3].trim();

			Assert.assertTrue(
				"A throwable should be logged and the first stack should be " +
					PortalLog4jTest.class.getName(),
				actualFirstPrefixStackTraceElement.startsWith(
					"at " + PortalLog4jTest.class.getName()));
		}

		// <log4j:locationInfo />

		String log4JLocationInfoLine = outputLines[outputLines.length - 2];

		String log4JLocationInfo = log4JLocationInfoLine.substring(
			log4JLocationInfoLine.indexOf(StringPool.SPACE),
			log4JLocationInfoLine.indexOf(StringPool.FORWARD_SLASH));

		// <log4j:locationInfo class="..." />

		String expectedLog4JLocationInfoClassName = StringBundler.concat(
			" class=\"", PortalLog4jTest.class.getName(), "\" ");

		Assert.assertEquals(
			expectedLog4JLocationInfoClassName,
			log4JLocationInfo.substring(
				0, expectedLog4JLocationInfoClassName.length()));

		// <log4j:locationInfo file="..." />

		log4JLocationInfo = log4JLocationInfo.substring(
			expectedLog4JLocationInfoClassName.length());
		log4JLocationInfo = log4JLocationInfo.substring(
			log4JLocationInfo.indexOf("file"));

		String expectedLog4JLocationInfoFile = StringBundler.concat(
			"file=\"", PortalLog4jTest.class.getSimpleName(), ".java\"");

		Assert.assertEquals(
			expectedLog4JLocationInfoFile,
			log4JLocationInfo.substring(
				0, expectedLog4JLocationInfoFile.length()));
	}

	private void _testLogOutput(String level) throws Exception {
		String testMessage = level + " message";

		_testLogOutput(level, testMessage, null);

		TestException testException = new TestException();

		_testLogOutput(level, testMessage, testException);

		_testLogOutput(level, null, testException);
	}

	private void _testLogOutput(
			String level, String message, Throwable throwable)
		throws Exception {

		outputLog(level, _log, message, throwable);

		try {
			_assertTextLog(
				level, message, throwable, _unsyncStringWriter.toString());

			_assertTextLog(
				level, message, throwable,
				new String(Files.readAllBytes(_textLogFilePath)));

			_assertXmlLog(
				level, message, throwable,
				new String(Files.readAllBytes(_xmlLogFilePath)));
		}
		finally {
			_unsyncStringWriter.reset();

			Files.write(
				_textLogFilePath, new byte[0],
				StandardOpenOption.TRUNCATE_EXISTING);
			Files.write(
				_xmlLogFilePath, new byte[0],
				StandardOpenOption.TRUNCATE_EXISTING);
		}
	}

	private void _testLogOutputUpgradeProcess(
			String level, UnsyncStringWriter unsyncStringWriter,
			String logContextMessage)
		throws Exception {

		TestUpgradeProcess upgradeProcess = new TestUpgradeProcess(
			level, level + " message", null);

		upgradeProcess.executeUpgrade();

		String[] outputLines = StringUtil.splitLines(
			unsyncStringWriter.toString());

		Assert.assertTrue(
			"The log output should have at least 1 line",
			outputLines.length > 0);

		Assert.assertEquals(
			StringBundler.concat(level, " - ", level, " message"),
			outputLines[0]);

		Assert.assertEquals(logContextMessage, outputLines[1].trim());

		unsyncStringWriter.reset();
	}

	private void _testLogOutputWithLogContext(
		String level, UnsyncStringWriter unsyncStringWriter,
		String logContextMessage) {

		outputLog(level, _log, level + " message", null);

		String[] outputLines = StringUtil.splitLines(
			unsyncStringWriter.toString());

		Assert.assertTrue(
			"The log output should have at least 1 line",
			outputLines.length > 0);

		Assert.assertEquals(
			StringBundler.concat(level, " - ", level, " message"),
			outputLines[0]);

		Assert.assertEquals(logContextMessage, outputLines[1].trim());

		unsyncStringWriter.reset();
	}

	private void _testOutputForUpgrades(
			String logContextMessage, boolean upgradeLogEnabled,
			boolean upgrading)
		throws Exception {

		boolean currentUpgradeLogEnabled =
			ReflectionTestUtil.getAndSetFieldValue(
				Log4jLogFactoryImpl.class, "_upgradeLogContextEnabled",
				upgradeLogEnabled);

		boolean currentUpgrading = StartupHelperUtil.isUpgrading();

		StartupHelperUtil.setUpgrading(upgrading);

		ConcurrentMap<String, LogWrapper> logWrappers =
			ReflectionTestUtil.getFieldValue(
				LogFactoryUtil.class, "_logWrappers");

		logWrappers.remove(TestUpgradeProcess.class.getName());

		try {
			PatternLayout.Builder builder = PatternLayout.newBuilder();

			builder.withPattern("%level - %m%n %X");

			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			Appender logContextWriterAppender = WriterAppender.createAppender(
				builder.build(), null, unsyncStringWriter,
				"logUpgradeContextWriterAppender", false, false);

			logContextWriterAppender.start();

			Logger logger = (Logger)LogManager.getLogger(
				TestUpgradeProcess.class.getName());

			logger.addAppender(logContextWriterAppender);

			_testLogOutputUpgradeProcess(
				"DEBUG", unsyncStringWriter, logContextMessage);
			_testLogOutputUpgradeProcess(
				"ERROR", unsyncStringWriter, logContextMessage);
			_testLogOutputUpgradeProcess(
				"FATAL", unsyncStringWriter, logContextMessage);
			_testLogOutputUpgradeProcess(
				"INFO", unsyncStringWriter, logContextMessage);
			_testLogOutputUpgradeProcess(
				"TRACE", unsyncStringWriter, logContextMessage);
			_testLogOutputUpgradeProcess(
				"WARN", unsyncStringWriter, logContextMessage);

			logger.removeAppender(logContextWriterAppender);
		}
		finally {
			StartupHelperUtil.setUpgrading(currentUpgrading);

			ReflectionTestUtil.setFieldValue(
				Log4jLogFactoryImpl.class, "_upgradeLogContextEnabled",
				currentUpgradeLogEnabled);
		}
	}

	private static final int _BUFFER_SIZE = 8192;

	private static final String _DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalLog4jTest.class);

	private static final Pattern _datePattern = Pattern.compile(
		"\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d");
	private static Path _tempLogFileDirPath;
	private static TestOutputStream _testOutputStream;
	private static Path _textLogFilePath;
	private static final UnsyncStringWriter _unsyncStringWriter =
		new UnsyncStringWriter();
	private static Path _xmlLogFilePath;

	private static class TestOutputStream extends CloseShieldOutputStream {

		public TestOutputStream(OutputStream originalOutputStream) {
			super(originalOutputStream);
		}

		@Override
		public void write(byte[] bytes) throws IOException {
			_unsyncStringWriter.write(new String(bytes));
		}

		@Override
		public void write(byte[] bytes, int offset, int length)
			throws IOException {

			_unsyncStringWriter.write(new String(bytes), offset, length);
		}

		@Override
		public void write(int b) throws IOException {
			_unsyncStringWriter.write(b);
		}

	}

	private class TestException extends Exception {
	}

	private class TestUpgradeProcess extends UpgradeProcess {

		public TestUpgradeProcess(
			String level, String message, Throwable throwable) {

			_level = level;
			_message = message;
			_throwable = throwable;
		}

		public void executeUpgrade() throws Exception {
			doUpgrade();
		}

		@Override
		protected void doUpgrade() throws Exception {
			outputLog(_level, _log, _message, _throwable);
		}

		private final String _level;
		private final Log _log = LogFactoryUtil.getLog(
			TestUpgradeProcess.class);
		private final String _message;
		private final Throwable _throwable;

	}

}