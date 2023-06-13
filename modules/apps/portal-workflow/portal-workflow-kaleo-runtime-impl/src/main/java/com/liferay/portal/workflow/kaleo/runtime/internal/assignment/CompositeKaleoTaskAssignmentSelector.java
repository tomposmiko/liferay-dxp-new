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

package com.liferay.portal.workflow.kaleo.runtime.internal.assignment;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskAssignment;
import com.liferay.portal.workflow.kaleo.runtime.ExecutionContext;
import com.liferay.portal.workflow.kaleo.runtime.assignment.KaleoTaskAssignmentSelector;
import com.liferay.portal.workflow.kaleo.runtime.assignment.KaleoTaskAssignmentSelectorRegistry;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CompositeKaleoTaskAssignmentSelector.class)
public class CompositeKaleoTaskAssignmentSelector
	implements KaleoTaskAssignmentSelector {

	@Override
	public Collection<KaleoTaskAssignment> getKaleoTaskAssignments(
			KaleoTaskAssignment kaleoTaskAssignment,
			ExecutionContext executionContext)
		throws PortalException {

		String assigneeClassName = kaleoTaskAssignment.getAssigneeClassName();

		KaleoTaskAssignmentSelector kaleoTaskAssignmentSelector =
			_kaleoTaskAssignmentSelectorRegistry.getKaleoTaskAssignmentSelector(
				assigneeClassName);

		if (kaleoTaskAssignmentSelector == null) {
			throw new IllegalArgumentException(
				"No task assignment selector found for class name " +
					assigneeClassName);
		}

		return kaleoTaskAssignmentSelector.getKaleoTaskAssignments(
			kaleoTaskAssignment, executionContext);
	}

	@Reference
	private KaleoTaskAssignmentSelectorRegistry
		_kaleoTaskAssignmentSelectorRegistry;

}