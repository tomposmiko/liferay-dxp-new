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

package com.liferay.portal.search.tuning.synonyms.web.internal.synchronizer;

import com.liferay.portal.search.tuning.synonyms.index.name.SynonymSetIndexName;
import com.liferay.portal.search.tuning.synonyms.web.internal.filter.SynonymSetFilterReader;
import com.liferay.portal.search.tuning.synonyms.web.internal.filter.name.SynonymSetFilterNameHolder;
import com.liferay.portal.search.tuning.synonyms.web.internal.index.SynonymSet;
import com.liferay.portal.search.tuning.synonyms.web.internal.storage.SynonymSetStorageAdapter;

import java.util.Collections;
import java.util.LinkedHashSet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adam Brandizzi
 */
@Component(service = FilterToIndexSynchronizer.class)
public class FilterToIndexSynchronizerImpl
	implements FilterToIndexSynchronizer {

	@Override
	public void copyToIndex(
		String companyIndexName, SynonymSetIndexName synonymSetIndexName) {

		for (String synonyms : _getSynonymsFromFilters(companyIndexName)) {
			_addSynonymSetToIndex(synonymSetIndexName, synonyms);
		}
	}

	private void _addSynonymSetToIndex(
		SynonymSetIndexName synonymSetIndexName, String synonyms) {

		SynonymSet.SynonymSetBuilder synonymSetBuilder =
			new SynonymSet.SynonymSetBuilder();

		synonymSetBuilder.synonyms(synonyms);

		_synonymSetStorageAdapter.create(
			synonymSetIndexName, synonymSetBuilder.build());
	}

	private String[] _getSynonymsFromFilters(String companyIndexName) {
		LinkedHashSet<String> synonyms = new LinkedHashSet<>();

		for (String filterName : _synonymSetFilterNameHolder.getFilterNames()) {
			Collections.addAll(
				synonyms,
				_synonymSetFilterReader.getSynonymSets(
					companyIndexName, filterName));
		}

		return synonyms.toArray(new String[0]);
	}

	@Reference
	private SynonymSetFilterNameHolder _synonymSetFilterNameHolder;

	@Reference
	private SynonymSetFilterReader _synonymSetFilterReader;

	@Reference
	private SynonymSetStorageAdapter _synonymSetStorageAdapter;

}