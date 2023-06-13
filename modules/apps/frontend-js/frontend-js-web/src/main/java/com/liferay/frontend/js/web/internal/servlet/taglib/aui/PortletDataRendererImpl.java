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

package com.liferay.frontend.js.web.internal.servlet.taglib.aui;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.frontend.esm.FrontendESMUtil;
import com.liferay.portal.kernel.servlet.taglib.aui.AMDRequire;
import com.liferay.portal.kernel.servlet.taglib.aui.ESImport;
import com.liferay.portal.kernel.servlet.taglib.aui.JSFragment;
import com.liferay.portal.kernel.servlet.taglib.aui.PortletData;
import com.liferay.portal.kernel.servlet.taglib.aui.PortletDataRenderer;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.Writer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Iván Zaera Avellón
 */
@Component(immediate = true, service = PortletDataRenderer.class)
public class PortletDataRendererImpl implements PortletDataRenderer {

	@Override
	public void write(Collection<PortletData> portletDatas, Writer writer)
		throws IOException {

		String rawCode = _computeRawCode(portletDatas);

		if (!Validator.isBlank(rawCode)) {
			writer.write("<script type=\"text/javascript\">\n");
			writer.write(rawCode);
			writer.write("\n</script>");
		}

		Map<String, IntegerWrapper> usedAliases = new HashMap<>();

		Map<ESImport, ESImport> esImportsMap = _computeESImportsMap(
			portletDatas, usedAliases);

		if (esImportsMap.isEmpty()) {
			writer.write("<script>\n");
		}
		else {
			writer.write("<script type=\"");
			writer.write(FrontendESMUtil.getScriptType());
			writer.write("\">\n");
		}

		// Write ES prologue

		if (!esImportsMap.isEmpty()) {
			for (ESImport esImport : esImportsMap.values()) {
				writer.write("import {");
				writer.write(esImport.getSymbol());

				String alias = esImport.getAlias();

				if (!alias.equals(esImport.getSymbol())) {
					writer.write(" as ");
					writer.write(alias);
				}

				writer.write("} from '");
				writer.write(esImport.getModule());
				writer.write("';\n");
			}
		}

		// Write AMD prologue

		Map<AMDRequire, AMDRequire> amdRequiresMap = _computeAMDRequiresMap(
			portletDatas, usedAliases);

		if (!amdRequiresMap.isEmpty()) {
			writer.write("Liferay.Loader.require(\n");

			for (AMDRequire amdRequire : amdRequiresMap.values()) {
				writer.write(StringPool.APOSTROPHE);
				writer.write(amdRequire.getModule());
				writer.write("',\n");
			}

			writer.write("function(");

			String delimiter = StringPool.BLANK;

			for (AMDRequire amdRequire : amdRequiresMap.values()) {
				writer.write(delimiter);
				writer.write(amdRequire.getAlias());

				delimiter = StringPool.COMMA_AND_SPACE;
			}

			writer.write(") {\n");
			writer.write("try {\n");
		}

		// Write AUI prologue

		Set<String> auiUseSet = _computeAUIUseSet(portletDatas);

		if (!auiUseSet.isEmpty()) {
			writer.write("AUI().use(\n");

			for (String auiUse : auiUseSet) {
				writer.write("  '");
				writer.write(auiUse);
				writer.write("',\n");
			}

			writer.write("function(A) {\n");
		}

		// Write actual JS code

		writer.write(
			_computeNonrawCode(amdRequiresMap, esImportsMap, portletDatas));

		// Write AUI epilogue

		if (!auiUseSet.isEmpty()) {
			writer.write("});\n");
		}

		// Write AMD epilogue

		if (!amdRequiresMap.isEmpty()) {
			writer.write("} catch (err) {\n");
			writer.write("\tconsole.error(err);\n");
			writer.write("}\n");

			writer.write("});\n");
		}

		writer.write("\n</script>");
	}

	private Map<AMDRequire, AMDRequire> _computeAMDRequiresMap(
		Collection<PortletData> portletDatas,
		Map<String, IntegerWrapper> usedAliases) {

		Map<AMDRequire, AMDRequire> amdRequiresMap = new HashMap<>();

		for (PortletData portletData : portletDatas) {
			for (JSFragment jsFragment : portletData.getJSFragments()) {
				Collection<AMDRequire> amdRequires =
					jsFragment.getAMDRequires();

				if ((amdRequires == null) || amdRequires.isEmpty()) {
					continue;
				}

				for (AMDRequire amdRequire : amdRequires) {
					if (amdRequiresMap.containsKey(amdRequire)) {
						continue;
					}

					String alias = amdRequire.getAlias();

					if (usedAliases.containsKey(alias)) {
						IntegerWrapper integerWrapper = usedAliases.get(alias);

						alias += integerWrapper.getValue();

						integerWrapper.increment();
					}
					else {
						usedAliases.put(alias, new IntegerWrapper(1));
					}

					amdRequiresMap.put(
						amdRequire,
						new AMDRequire(alias, amdRequire.getModule()));
				}
			}
		}

		return amdRequiresMap;
	}

	private Set<String> _computeAUIUseSet(
		Collection<PortletData> portletDatas) {

		Set<String> auiUseSet = new HashSet<>();

		for (PortletData portletData : portletDatas) {
			for (JSFragment jsFragment : portletData.getJSFragments()) {
				auiUseSet.addAll(jsFragment.getAUIUses());
			}
		}

		return auiUseSet;
	}

	private Map<ESImport, ESImport> _computeESImportsMap(
		Collection<PortletData> portletDatas,
		Map<String, IntegerWrapper> usedAliases) {

		Map<ESImport, ESImport> esImportsMap = new HashMap<>();

		for (PortletData portletData : portletDatas) {
			for (JSFragment jsFragment : portletData.getJSFragments()) {
				Collection<ESImport> esImports = jsFragment.getESImports();

				if ((esImports == null) || esImports.isEmpty()) {
					continue;
				}

				for (ESImport esImport : esImports) {
					if (esImportsMap.containsKey(esImport)) {
						continue;
					}

					String alias = esImport.getAlias();

					if (usedAliases.containsKey(alias)) {
						IntegerWrapper integerWrapper = usedAliases.get(alias);

						alias += integerWrapper.getValue();

						integerWrapper.increment();
					}
					else {
						usedAliases.put(alias, new IntegerWrapper(0));
					}

					esImportsMap.put(
						esImport,
						new ESImport(
							alias, esImport.getModule(), esImport.getSymbol()));
				}
			}
		}

		return esImportsMap;
	}

	private String _computeNonrawCode(
		Map<AMDRequire, AMDRequire> amdRequiresMap,
		Map<ESImport, ESImport> esImportsMap,
		Collection<PortletData> portletDatas) {

		StringBuilder sb = new StringBuilder();

		for (PortletData portletData : portletDatas) {
			for (JSFragment jsFragment : portletData.getJSFragments()) {
				String code = jsFragment.getCode();

				if (Validator.isNull(code) || jsFragment.isRaw()) {
					continue;
				}

				List<AMDRequire> amdRequires = jsFragment.getAMDRequires();
				List<String> auiUses = jsFragment.getAUIUses();

				boolean legacyJSFragment = false;

				if (!amdRequires.isEmpty() || !auiUses.isEmpty()) {
					legacyJSFragment = true;
				}

				if (legacyJSFragment) {
					sb.append("(function() {\n");
				}
				else {
					sb.append("{\n");
				}

				// Map AMD requires to their requested aliases

				List<AMDRequire> fragmentAMDRequires =
					jsFragment.getAMDRequires();

				for (AMDRequire fragmentAMDRequire : fragmentAMDRequires) {
					AMDRequire amdRequire = amdRequiresMap.get(
						fragmentAMDRequire);

					if (!Objects.equals(
							amdRequire.getAlias(),
							fragmentAMDRequire.getAlias())) {

						sb.append("const ");
						sb.append(fragmentAMDRequire.getAlias());
						sb.append(" = ");
						sb.append(amdRequire.getAlias());
						sb.append(";\n");
					}
				}

				// Map ES imports to their requested aliases

				List<ESImport> fragmentESImports = jsFragment.getESImports();

				for (ESImport fragmentESImport : fragmentESImports) {
					ESImport esImport = esImportsMap.get(fragmentESImport);

					if (!Objects.equals(
							esImport.getAlias(), fragmentESImport.getAlias())) {

						sb.append("const ");
						sb.append(fragmentESImport.getAlias());
						sb.append(" = ");
						sb.append(esImport.getAlias());
						sb.append(";\n");
					}
				}

				sb.append(code);

				if (!code.endsWith(StringPool.NEW_LINE)) {
					sb.append(StringPool.NEW_LINE);
				}

				if (legacyJSFragment) {
					sb.append("})();\n");
				}
				else {
					sb.append("}\n");
				}
			}
		}

		return sb.toString();
	}

	private String _computeRawCode(Collection<PortletData> portletDatas) {
		StringBuilder sb = new StringBuilder();

		for (PortletData portletData : portletDatas) {
			for (JSFragment jsFragment : portletData.getJSFragments()) {
				String code = jsFragment.getCode();

				if (Validator.isNull(code) || !jsFragment.isRaw()) {
					continue;
				}

				sb.append(code);
				sb.append("\n");
			}
		}

		return sb.toString();
	}

}