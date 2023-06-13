/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.synonyms.web.internal.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.tuning.synonyms.index.name.SynonymSetIndexNameBuilder;
import com.liferay.portal.search.tuning.synonyms.web.internal.index.SynonymSet;
import com.liferay.portal.search.tuning.synonyms.web.internal.index.SynonymSetIndexReader;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kevin Tan
 */
public class EditSynonymSetsDisplayBuilder {

	public EditSynonymSetsDisplayBuilder(
		HttpServletRequest httpServletRequest, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse,
		SynonymSetIndexNameBuilder synonymSetIndexNameBuilder,
		SynonymSetIndexReader synonymSetIndexReader) {

		_httpServletRequest = httpServletRequest;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_synonymSetIndexNameBuilder = synonymSetIndexNameBuilder;
		_synonymSetIndexReader = synonymSetIndexReader;
	}

	public EditSynonymSetsDisplayContext build() {
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext =
			new EditSynonymSetsDisplayContext();

		_synonymSet = _getSynonymSet(_getCompanyId());

		_setBackURL(editSynonymSetsDisplayContext);
		_setData(editSynonymSetsDisplayContext);
		_setFormName(editSynonymSetsDisplayContext);
		_setInputName(editSynonymSetsDisplayContext);
		_setRedirect(editSynonymSetsDisplayContext);
		_setSynonymSetId(editSynonymSetsDisplayContext);

		return editSynonymSetsDisplayContext;
	}

	private String _getBackURL() {
		return ParamUtil.getString(
			_httpServletRequest, "backURL", _getRedirect());
	}

	private long _getCompanyId() {
		return _portal.getCompanyId(_renderRequest);
	}

	private String _getFormName() {
		return "synonymSetsForm";
	}

	private String _getInputName() {
		return "synonymSet";
	}

	private String _getRedirect() {
		return ParamUtil.getString(_httpServletRequest, "redirect");
	}

	private SynonymSet _getSynonymSet(long companyId) {
		String synonymSetId = ParamUtil.getString(
			_renderRequest, "synonymSetId", null);

		if (synonymSetId == null) {
			return null;
		}

		return _synonymSetIndexReader.fetch(
			_synonymSetIndexNameBuilder.getSynonymSetIndexName(companyId),
			synonymSetId);
	}

	private String _getSynonymSets() {
		if (_synonymSet == null) {
			return StringPool.BLANK;
		}

		String synonyms = _synonymSet.getSynonyms();

		if (synonyms == null) {
			return StringPool.BLANK;
		}

		return synonyms;
	}

	private void _setBackURL(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		editSynonymSetsDisplayContext.setBackURL(_getBackURL());
	}

	private void _setData(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		editSynonymSetsDisplayContext.setData(
			HashMapBuilder.<String, Object>put(
				"formName", _renderResponse.getNamespace() + _getFormName()
			).put(
				"inputName", _renderResponse.getNamespace() + _getInputName()
			).put(
				"synonymSets", _getSynonymSets()
			).build());
	}

	private void _setFormName(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		editSynonymSetsDisplayContext.setFormName(_getFormName());
	}

	private void _setInputName(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		editSynonymSetsDisplayContext.setInputName(_getInputName());
	}

	private void _setRedirect(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		editSynonymSetsDisplayContext.setRedirect(_getRedirect());
	}

	private void _setSynonymSetId(
		EditSynonymSetsDisplayContext editSynonymSetsDisplayContext) {

		if (_synonymSet != null) {
			editSynonymSetsDisplayContext.setSynonymSetId(
				_synonymSet.getSynonymSetDocumentId());
		}
	}

	private final HttpServletRequest _httpServletRequest;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SynonymSet _synonymSet;
	private final SynonymSetIndexNameBuilder _synonymSetIndexNameBuilder;
	private final SynonymSetIndexReader _synonymSetIndexReader;

}