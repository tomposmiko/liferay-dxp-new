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

package com.liferay.osb.faro.engine.client.constants;

import com.liferay.osb.faro.engine.client.model.Activity;
import com.liferay.osb.faro.engine.client.model.Asset;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class ActivityConstants {

	public static final int ACTION_ANY = -1;

	public static final int ACTION_COMMENTS = 3;

	public static final int ACTION_DOWNLOADS = 0;

	public static final String ACTION_KEY_COMMENT_POSTED =
		Asset.AssetType.Blog.name() + StringPool.POUND +
			Activity.EventId.commentPosted.name();

	public static final String ACTION_KEY_DOCUMENT_DOWNLOADED =
		Asset.AssetType.Document.name() + StringPool.POUND +
			Activity.EventId.documentDownloaded.name();

	public static final String ACTION_KEY_DOCUMENT_PREVIEWED =
		Asset.AssetType.Document.name() + StringPool.POUND +
			Activity.EventId.documentPreviewed.name();

	public static final String ACTION_KEY_FORM_SUBMITTED =
		Asset.AssetType.Form.name() + StringPool.POUND +
			Activity.EventId.formSubmitted.name();

	public static final String ACTION_KEY_FORM_VIEWED =
		Asset.AssetType.Form.name() + StringPool.POUND +
			Activity.EventId.formViewed.name();

	public static final String ACTION_KEY_PAGE_VIEWED =
		Asset.AssetType.Page.name() + StringPool.POUND +
			Activity.EventId.pageViewed.name();

	public static final int ACTION_PREVIEWS = 4;

	public static final int ACTION_SUBMISSIONS = 1;

	public static final int ACTION_VISITS = 2;

	public static int getAction(String eventId) {
		if (StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.commentPosted.name())) {

			return ACTION_COMMENTS;
		}

		if (StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.documentDownloaded.name())) {

			return ACTION_DOWNLOADS;
		}

		if (StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.documentPreviewed.name())) {

			return ACTION_PREVIEWS;
		}

		if (StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.formSubmitted.name())) {

			return ACTION_SUBMISSIONS;
		}

		if (StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.formViewed.name()) ||
			StringUtil.equalsIgnoreCase(
				eventId, Activity.EventId.pageViewed.name())) {

			return ACTION_VISITS;
		}

		return ACTION_ANY;
	}

	public static List<String> getActionKeys(int action) {
		if (action == ACTION_ANY) {
			return Arrays.asList(
				ACTION_KEY_COMMENT_POSTED, ACTION_KEY_DOCUMENT_DOWNLOADED,
				ACTION_KEY_DOCUMENT_PREVIEWED, ACTION_KEY_FORM_SUBMITTED,
				ACTION_KEY_FORM_VIEWED, ACTION_KEY_PAGE_VIEWED);
		}

		if (action == ACTION_COMMENTS) {
			return Collections.singletonList(ACTION_KEY_COMMENT_POSTED);
		}

		if (action == ACTION_DOWNLOADS) {
			return Collections.singletonList(ACTION_KEY_DOCUMENT_DOWNLOADED);
		}

		if (action == ACTION_PREVIEWS) {
			return Collections.singletonList(ACTION_KEY_DOCUMENT_PREVIEWED);
		}

		if (action == ACTION_SUBMISSIONS) {
			return Collections.singletonList(ACTION_KEY_FORM_SUBMITTED);
		}

		if (action == ACTION_VISITS) {
			return Arrays.asList(
				ACTION_KEY_FORM_VIEWED, ACTION_KEY_PAGE_VIEWED);
		}

		return Collections.emptyList();
	}

	public static Map<String, Integer> getActions() {
		return _actions;
	}

	private static final Map<String, Integer> _actions =
		new HashMap<String, Integer>() {
			{
				put("comments", ACTION_COMMENTS);
				put("downloads", ACTION_DOWNLOADS);
				put("previews", ACTION_PREVIEWS);
				put("submissions", ACTION_SUBMISSIONS);
				put("visits", ACTION_VISITS);
			}
		};

}