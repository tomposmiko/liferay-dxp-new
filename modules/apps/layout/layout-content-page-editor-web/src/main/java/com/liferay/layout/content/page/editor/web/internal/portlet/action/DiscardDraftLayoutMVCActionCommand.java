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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.util.LayoutCopyHelper;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/content_layout/discard_draft_layout"
	},
	service = MVCActionCommand.class
)
public class DiscardDraftLayoutMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		DiscardDraftLayoutCallable discardDraftLayoutCallable =
			new DiscardDraftLayoutCallable(actionRequest);

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig, discardDraftLayoutCallable);
		}
		catch (Throwable t) {
			throw new Exception(t);
		}

		sendRedirect(actionRequest, actionResponse);
	}

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private LayoutCopyHelper _layoutCopyHelper;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	private class DiscardDraftLayoutCallable implements Callable<Void> {

		@Override
		public Void call() throws Exception {
			long plid = ParamUtil.getLong(_actionRequest, "classPK");

			Layout draftLayout = _layoutLocalService.getLayout(plid);

			if ((draftLayout.getClassPK() == 0) ||
				(_portal.getClassNameId(Layout.class) !=
					draftLayout.getClassNameId())) {

				return null;
			}

			Layout layout = _layoutLocalService.getLayout(
				draftLayout.getClassPK());

			_layoutCopyHelper.copyLayout(layout, draftLayout);

			draftLayout.setModifiedDate(layout.getPublishDate());

			_layoutLocalService.updateLayout(draftLayout);

			return null;
		}

		private DiscardDraftLayoutCallable(ActionRequest actionRequest) {
			_actionRequest = actionRequest;
		}

		private final ActionRequest _actionRequest;

	}

}