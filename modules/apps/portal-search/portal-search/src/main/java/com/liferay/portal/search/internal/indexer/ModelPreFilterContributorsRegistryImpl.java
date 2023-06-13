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

package com.liferay.portal.search.internal.indexer;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.search.spi.model.query.contributor.ModelPreFilterContributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author André de Oliveira
 */
@Component(service = ModelPreFilterContributorsRegistry.class)
public class ModelPreFilterContributorsRegistryImpl
	implements ModelPreFilterContributorsRegistry {

	@Override
	public List<ModelPreFilterContributor> filterModelPreFilterContributor(
		String entryClassName, Collection<String> excludes,
		Collection<String> includes, boolean mandatoryOnly) {

		List<ModelPreFilterContributor> modelPreFilterContributors =
			new ArrayList<>();

		_addAll(modelPreFilterContributors, _getAllClassesContributors());
		_addAll(
			modelPreFilterContributors, _getClassContributors(entryClassName));

		if (mandatoryOnly) {
			_retainAll(modelPreFilterContributors, _getMandatoryContributors());
		}
		else {
			List<String> mandatoryContributorClassNames =
				_getMandatoryContributorNames(_getMandatoryContributors());

			if ((includes != null) && !includes.isEmpty()) {
				modelPreFilterContributors.removeIf(
					modelPreFilterContributor -> {
						String className = _getClassName(
							modelPreFilterContributor);

						return !mandatoryContributorClassNames.contains(
							className) &&
							   !includes.contains(className);
					});
			}

			if ((excludes != null) && !excludes.isEmpty()) {
				modelPreFilterContributors.removeIf(
					modelPreFilterContributor -> {
						String className = _getClassName(
							modelPreFilterContributor);

						return !mandatoryContributorClassNames.contains(
							className) &&
							   excludes.contains(className);
					});
			}
		}

		return modelPreFilterContributors;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_classNameServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, ModelPreFilterContributor.class,
				"indexer.class.name");

		_mandatoryServiceTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, ModelPreFilterContributor.class,
				"indexer.clauses.mandatory");
	}

	@Deactivate
	protected void deactivate() {
		_classNameServiceTrackerMap.close();
		_mandatoryServiceTrackerMap.close();
	}

	private void _addAll(
		List<ModelPreFilterContributor> list1,
		List<ModelPreFilterContributor> list2) {

		if (list2 == null) {
			return;
		}

		list1.addAll(list2);
	}

	private List<ModelPreFilterContributor> _getAllClassesContributors() {
		return _classNameServiceTrackerMap.getService("ALL");
	}

	private List<ModelPreFilterContributor> _getClassContributors(
		String entryClassName) {

		return _classNameServiceTrackerMap.getService(entryClassName);
	}

	private String _getClassName(Object object) {
		Class<?> clazz = object.getClass();

		return clazz.getName();
	}

	private List<String> _getMandatoryContributorNames(
		List<ModelPreFilterContributor> mandatoryContributors) {

		List<String> mandatoryContributorNames = new ArrayList<>();

		for (ModelPreFilterContributor modelPreFilterContributor :
				mandatoryContributors) {

			mandatoryContributorNames.add(
				_getClassName(modelPreFilterContributor));
		}

		return mandatoryContributorNames;
	}

	private List<ModelPreFilterContributor> _getMandatoryContributors() {
		return _mandatoryServiceTrackerMap.getService("true");
	}

	private void _retainAll(
		List<ModelPreFilterContributor> list1,
		List<ModelPreFilterContributor> list2) {

		if (list2 == null) {
			return;
		}

		list1.retainAll(list2);
	}

	private ServiceTrackerMap<String, List<ModelPreFilterContributor>>
		_classNameServiceTrackerMap;
	private ServiceTrackerMap<String, List<ModelPreFilterContributor>>
		_mandatoryServiceTrackerMap;

}