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

package com.liferay.headless.admin.workflow.client.dto.v1_0;

import com.liferay.headless.admin.workflow.client.function.UnsafeSupplier;
import com.liferay.headless.admin.workflow.client.serdes.v1_0.WorkflowLogSerDes;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class WorkflowLog {

	public Creator getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(Creator auditPerson) {
		this.auditPerson = auditPerson;
	}

	public void setAuditPerson(
		UnsafeSupplier<Creator, Exception> auditPersonUnsafeSupplier) {

		try {
			auditPerson = auditPersonUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Creator auditPerson;

	public String getCommentLog() {
		return commentLog;
	}

	public void setCommentLog(String commentLog) {
		this.commentLog = commentLog;
	}

	public void setCommentLog(
		UnsafeSupplier<String, Exception> commentLogUnsafeSupplier) {

		try {
			commentLog = commentLogUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String commentLog;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateCreated(
		UnsafeSupplier<Date, Exception> dateCreatedUnsafeSupplier) {

		try {
			dateCreated = dateCreatedUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Date dateCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setId(UnsafeSupplier<Long, Exception> idUnsafeSupplier) {
		try {
			id = idUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long id;

	public Creator getPerson() {
		return person;
	}

	public void setPerson(Creator person) {
		this.person = person;
	}

	public void setPerson(
		UnsafeSupplier<Creator, Exception> personUnsafeSupplier) {

		try {
			person = personUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Creator person;

	public Creator getPreviousPerson() {
		return previousPerson;
	}

	public void setPreviousPerson(Creator previousPerson) {
		this.previousPerson = previousPerson;
	}

	public void setPreviousPerson(
		UnsafeSupplier<Creator, Exception> previousPersonUnsafeSupplier) {

		try {
			previousPerson = previousPersonUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Creator previousPerson;

	public String getPreviousState() {
		return previousState;
	}

	public void setPreviousState(String previousState) {
		this.previousState = previousState;
	}

	public void setPreviousState(
		UnsafeSupplier<String, Exception> previousStateUnsafeSupplier) {

		try {
			previousState = previousStateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String previousState;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setState(
		UnsafeSupplier<String, Exception> stateUnsafeSupplier) {

		try {
			state = stateUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String state;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public void setTaskId(
		UnsafeSupplier<Long, Exception> taskIdUnsafeSupplier) {

		try {
			taskId = taskIdUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Long taskId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setType(UnsafeSupplier<String, Exception> typeUnsafeSupplier) {
		try {
			type = typeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String type;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof WorkflowLog)) {
			return false;
		}

		WorkflowLog workflowLog = (WorkflowLog)object;

		return Objects.equals(toString(), workflowLog.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		return WorkflowLogSerDes.toJSON(this);
	}

}