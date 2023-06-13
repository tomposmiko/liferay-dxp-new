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

package com.liferay.talend.runtime.reader;

import com.liferay.talend.common.schema.constants.BatchSchemaConstants;
import com.liferay.talend.runtime.LiferayBatchOutputSink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.avro.generic.IndexedRecord;

import org.joda.time.Instant;

import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.Source;

/**
 * @author Igor Beslic
 */
public class LiferayBatchFileReader implements Reader<IndexedRecord> {

	public LiferayBatchFileReader(
		LiferayBatchOutputSink liferayBatchOutputSink) {

		_liferayBatchOutputSink = liferayBatchOutputSink;
	}

	@Override
	public boolean advance() throws IOException {
		_batchSegmentFile = new File(
			_batchFile.getParent() + "/batch_segment.jsonl");

		FileWriter fileWriter = new FileWriter(_batchSegmentFile);

		String line = _bufferedReader.readLine();

		while ((line != null) && (++_current < _batchSize)) {
			fileWriter.write(line);
			fileWriter.write(System.lineSeparator());

			line = _bufferedReader.readLine();
		}

		try {
			if (_current > 0) {
				_currentTimestamp = Instant.now();

				return true;
			}

			return false;
		}
		finally {
			fileWriter.flush();
			fileWriter.close();
		}
	}

	@Override
	public void close() throws IOException {
		_bufferedReader.close();
	}

	@Override
	public IndexedRecord getCurrent() throws NoSuchElementException {
		_liferayBatchOutputSink.submit(_batchSegmentFile);

		String batchSegmentFilePath = _batchSegmentFile.getPath();

		_batchSegmentFile.delete();

		_batchSegmentFile = null;

		return BatchSchemaConstants.asBatchSchemaIndexedRecord(
			_batchFile.getPath(), batchSegmentFilePath,
			String.valueOf(_current));
	}

	@Override
	public Source getCurrentSource() {
		return _liferayBatchOutputSink;
	}

	@Override
	public Instant getCurrentTimestamp() throws NoSuchElementException {
		return _currentTimestamp;
	}

	@Override
	public Map<String, Object> getReturnValues() {
		return null;
	}

	@Override
	public boolean start() throws IOException {
		_batchFile = new File(_liferayBatchOutputSink.getBatchFilePath());

		if (_batchFile.exists() && (_batchFile.length() > 0)) {
			_bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(_batchFile)));

			return advance();
		}

		return false;
	}

	private File _batchFile;
	private File _batchSegmentFile;
	private int _batchSize = 10;
	private BufferedReader _bufferedReader;
	private int _current;
	private Instant _currentTimestamp = Instant.now();
	private final transient LiferayBatchOutputSink _liferayBatchOutputSink;

}