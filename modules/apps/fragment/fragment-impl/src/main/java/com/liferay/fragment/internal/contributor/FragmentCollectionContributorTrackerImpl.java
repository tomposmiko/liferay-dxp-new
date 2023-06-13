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

package com.liferay.fragment.internal.contributor;

import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.contributor.FragmentCollectionContributor;
import com.liferay.fragment.contributor.FragmentCollectionContributorTracker;
import com.liferay.fragment.model.FragmentEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true, service = FragmentCollectionContributorTracker.class
)
public class FragmentCollectionContributorTrackerImpl
	implements FragmentCollectionContributorTracker {

	@Override
	public Map<String, FragmentEntry>
		getFragmentCollectionContributorEntries() {

		return new HashMap<>(_fragmentCollectionContributorEntries);
	}

	@Override
	public List<FragmentCollectionContributor>
		getFragmentCollectionContributors() {

		return new ArrayList<>(_fragmentCollectionContributors);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void setFragmentCollectionContributor(
		FragmentCollectionContributor fragmentCollectionContributor) {

		_fragmentCollectionContributors.add(fragmentCollectionContributor);

		for (int type : _SUPPORTED_FRAGMENT_TYPES) {
			for (FragmentEntry fragmentEntry :
					fragmentCollectionContributor.getFragmentEntries(type)) {

				_fragmentCollectionContributorEntries.put(
					fragmentEntry.getFragmentEntryKey(), fragmentEntry);
			}
		}
	}

	protected void unsetFragmentCollectionContributor(
		FragmentCollectionContributor fragmentCollectionContributor) {

		for (int type : _SUPPORTED_FRAGMENT_TYPES) {
			for (FragmentEntry fragmentEntry :
					fragmentCollectionContributor.getFragmentEntries(type)) {

				_fragmentCollectionContributorEntries.remove(
					fragmentEntry.getFragmentEntryKey());
			}
		}

		_fragmentCollectionContributors.remove(fragmentCollectionContributor);
	}

	private static final int[] _SUPPORTED_FRAGMENT_TYPES = {
		FragmentConstants.TYPE_COMPONENT, FragmentConstants.TYPE_SECTION
	};

	private final Map<String, FragmentEntry>
		_fragmentCollectionContributorEntries = new ConcurrentHashMap<>();
	private final List<FragmentCollectionContributor>
		_fragmentCollectionContributors = new CopyOnWriteArrayList<>();

}