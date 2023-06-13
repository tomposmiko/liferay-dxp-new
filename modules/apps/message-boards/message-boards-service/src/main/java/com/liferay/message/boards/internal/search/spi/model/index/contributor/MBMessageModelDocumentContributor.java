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

package com.liferay.message.boards.internal.search.spi.model.index.contributor;

import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.message.boards.model.MBDiscussion;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.service.MBDiscussionLocalService;
import com.liferay.message.boards.service.MBMessageLocalService;
import com.liferay.message.boards.service.MBThreadLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.parsers.bbcode.BBCodeTranslatorUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.RelatedEntryIndexer;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistryUtil;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.ratings.kernel.model.RatingsStats;
import com.liferay.ratings.kernel.service.RatingsStatsLocalService;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luan Maoski
 */
@Component(
	property = "indexer.class.name=com.liferay.message.boards.model.MBMessage",
	service = ModelDocumentContributor.class
)
public class MBMessageModelDocumentContributor
	implements ModelDocumentContributor<MBMessage> {

	@Override
	public void contribute(Document document, MBMessage mbMessage) {
		document.addKeyword(Field.CATEGORY_ID, mbMessage.getCategoryId());

		for (Locale locale :
				_language.getAvailableLocales(mbMessage.getGroupId())) {

			String languageId = LocaleUtil.toLanguageId(locale);

			document.addText(
				_localization.getLocalizedName(Field.CONTENT, languageId),
				_processContent(mbMessage));
			document.addText(
				_localization.getLocalizedName(Field.TITLE, languageId),
				mbMessage.getSubject());
		}

		document.addKeyword(
			Field.ROOT_ENTRY_CLASS_PK, mbMessage.getRootMessageId());
		document.addKeyword(
			Field.TREE_PATH,
			StringUtil.split(mbMessage.getTreePath(), CharPool.SLASH));

		if (mbMessage.isAnonymous()) {
			document.remove(Field.USER_NAME);
		}

		document.addKeywordSortable("answer", mbMessage.isAnswer());

		MBDiscussion discussion =
			mbDiscussionLocalService.fetchThreadDiscussion(
				mbMessage.getThreadId());

		if (discussion == null) {
			document.addKeyword("discussion", false);
		}
		else {
			document.addKeyword("discussion", true);
		}

		document.addKeyword("parentMessageId", mbMessage.getParentMessageId());
		document.addKeyword("threadId", mbMessage.getThreadId());
		document.addKeywordSortable("urlSubject", mbMessage.getUrlSubject());

		if (mbMessage.getMessageId() == mbMessage.getRootMessageId()) {
			document.addKeyword(
				"answered",
				Stream.of(
					_mbMessageLocalService.getChildMessages(
						mbMessage.getMessageId(),
						WorkflowConstants.STATUS_APPROVED)
				).flatMap(
					List::stream
				).anyMatch(
					MBMessage::isAnswer
				));
			document.addKeyword(
				"childMessagesCount",
				_mbMessageLocalService.getChildMessagesCount(
					mbMessage.getMessageId(),
					WorkflowConstants.STATUS_APPROVED));

			MBThread mbThread = mbThreadLocalService.fetchMBThread(
				mbMessage.getThreadId());

			document.addKeyword("question", mbThread.isQuestion());

			RatingsStats ratingsStats = _ratingsStatsLocalService.fetchStats(
				MBMessage.class.getName(), mbThread.getRootMessageId());

			if (ratingsStats != null) {
				document.addNumber(
					"ratingsStatTotalScore", ratingsStats.getTotalScore());
			}

			document.addNumber("viewCount", mbThread.getViewCount());
		}

		if (!mbMessage.isDiscussion()) {
			return;
		}

		List<RelatedEntryIndexer> relatedEntryIndexers =
			RelatedEntryIndexerRegistryUtil.getRelatedEntryIndexers(
				mbMessage.getClassName());

		if (relatedEntryIndexers != null) {
			for (RelatedEntryIndexer relatedEntryIndexer :
					relatedEntryIndexers) {

				Comment comment = commentManager.fetchComment(
					mbMessage.getMessageId());

				if (comment != null) {
					try {
						relatedEntryIndexer.addRelatedEntryFields(
							document, comment);
					}
					catch (Exception exception) {
						throw new SystemException(exception);
					}

					document.addKeyword(Field.RELATED_ENTRY, true);
				}
			}
		}
	}

	@Reference
	protected CommentManager commentManager;

	@Reference
	protected MBDiscussionLocalService mbDiscussionLocalService;

	@Reference
	protected MBThreadLocalService mbThreadLocalService;

	private String _processContent(MBMessage message) {
		String content = message.getBody();

		try {
			if (message.isFormatBBCode()) {
				content = BBCodeTranslatorUtil.getHTML(content);
			}
		}
		catch (Exception exception) {
			_log.error(
				StringBundler.concat(
					"Unable to parse message ", message.getMessageId(), ": ",
					exception.getMessage()),
				exception);
		}

		return _htmlParser.extractText(content);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MBMessageModelDocumentContributor.class);

	@Reference
	private AssetTagLocalService _assetTagLocalService;

	@Reference
	private HtmlParser _htmlParser;

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

	@Reference
	private MBMessageLocalService _mbMessageLocalService;

	@Reference
	private RatingsStatsLocalService _ratingsStatsLocalService;

}