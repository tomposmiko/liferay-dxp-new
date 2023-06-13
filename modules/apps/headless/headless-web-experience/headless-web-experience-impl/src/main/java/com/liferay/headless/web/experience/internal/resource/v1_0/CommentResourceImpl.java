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

package com.liferay.headless.web.experience.internal.resource.v1_0;

import com.liferay.headless.web.experience.dto.v1_0.Comment;
import com.liferay.headless.web.experience.internal.dto.v1_0.util.CommentUtil;
import com.liferay.headless.web.experience.resource.v1_0.CommentResource;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.comment.DiscussionPermission;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.Collections;

import javax.ws.rs.NotFoundException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/comment.properties",
	scope = ServiceScope.PROTOTYPE, service = CommentResource.class
)
public class CommentResourceImpl extends BaseCommentResourceImpl {

	@Override
	public Comment getComment(Long commentId) throws Exception {
		com.liferay.portal.kernel.comment.Comment comment =
			_commentManager.fetchComment(commentId);

		_checkViewPermission(comment);

		return CommentUtil.toComment(comment, _portal);
	}

	@Override
	public Page<Comment> getCommentCommentsPage(
			Long commentId, Pagination pagination)
		throws Exception {

		_checkViewPermission(_commentManager.fetchComment(commentId));

		return Page.of(
			transform(
				_commentManager.getChildComments(
					commentId, WorkflowConstants.STATUS_APPROVED,
					pagination.getStartPosition(), pagination.getEndPosition()),
				comment -> CommentUtil.toComment(comment, _portal)),
			pagination,
			_commentManager.getChildCommentsCount(
				commentId, WorkflowConstants.STATUS_APPROVED));
	}

	@Override
	public Page<Comment> getStructuredContentCommentsPage(
			Long structuredContentId, Pagination pagination)
		throws Exception {

		JournalArticle journalArticle = _journalArticleService.getLatestArticle(
			structuredContentId);

		int count = _commentManager.getRootCommentsCount(
			journalArticle.getModelClassName(), structuredContentId,
			WorkflowConstants.STATUS_APPROVED);

		if (count == 0) {
			return Page.of(Collections.emptyList());
		}

		_checkViewPermission(
			journalArticle.getGroupId(), journalArticle.getModelClassName(),
			structuredContentId);

		return Page.of(
			transform(
				_commentManager.getRootComments(
					journalArticle.getModelClassName(), structuredContentId,
					WorkflowConstants.STATUS_APPROVED,
					pagination.getStartPosition(), pagination.getEndPosition()),
				comment -> CommentUtil.toComment(comment, _portal)),
			pagination, count);
	}

	private void _checkViewPermission(
			com.liferay.portal.kernel.comment.Comment comment)
		throws Exception {

		if (comment == null) {
			throw new NotFoundException();
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		DiscussionPermission discussionPermission =
			_commentManager.getDiscussionPermission(permissionChecker);

		discussionPermission.checkViewPermission(
			permissionChecker.getCompanyId(), comment.getGroupId(),
			comment.getClassName(), comment.getClassPK());
	}

	private void _checkViewPermission(
			long groupId, String className, long classPK)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		DiscussionPermission discussionPermission =
			_commentManager.getDiscussionPermission(permissionChecker);

		discussionPermission.checkViewPermission(
			permissionChecker.getCompanyId(), groupId, className, classPK);
	}

	@Reference
	private CommentManager _commentManager;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private Portal _portal;

}