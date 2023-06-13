<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
JournalEditDDMTemplateDisplayContext journalEditDDMTemplateDisplayContext = new JournalEditDDMTemplateDisplayContext(request);

JournalDDMTemplateHelper journalDDMTemplateHelper = (JournalDDMTemplateHelper)request.getAttribute(JournalDDMTemplateHelper.class.getName());
%>

<aui:input name="scriptContent" type="hidden" value="<%= journalEditDDMTemplateDisplayContext.getScript() %>" />

<div id="templateScriptContainer">
	<clay:row
		cssClass="form-group lfr-template-editor-container"
	>
		<c:if test="<%= journalEditDDMTemplateDisplayContext.isAutocompleteEnabled() %>">
			<clay:col
				cssClass="lfr-template-palette-container"
				id='<%= liferayPortletResponse.getNamespace() + "templatePaletteContainer" %>'
				md="3"
			>
				<div class="search" id="<portlet:namespace />paletteSearchContainer">
					<input class="form-control mb-3" id="<portlet:namespace />paletteSearch" placeholder="<liferay-ui:message key="search" />" type="text" />
				</div>

				<div class="lfr-template-palette" id="<portlet:namespace />paletteDataContainer">
					<div id="<portlet:namespace />paletteData">
						<liferay-frontend:fieldset-group>

							<%
							for (TemplateVariableGroup templateVariableGroup : journalEditDDMTemplateDisplayContext.getTemplateVariableGroups()) {
								if (templateVariableGroup.isEmpty()) {
									continue;
								}
							%>

								<liferay-frontend:fieldset
									collapsible="<%= true %>"
									cssClass="palette-section"
									id="<%= HtmlUtil.getAUICompatibleId(templateVariableGroup.getLabel()) %>"
									label="<%= LanguageUtil.get(request, journalEditDDMTemplateDisplayContext.getTemplateHandlerResourceBundle(), HtmlUtil.escape(templateVariableGroup.getLabel())) %>"
								>
									<ul class="list-unstyled palette-item-content">

										<%
										for (TemplateVariableDefinition templateVariableDefinition : templateVariableGroup.getTemplateVariableDefinitions()) {
										%>

											<li class="palette-item-container">
												<span class="palette-item" data-content="<%= HtmlUtil.escapeAttribute(journalDDMTemplateHelper.getDataContent(templateVariableDefinition, journalEditDDMTemplateDisplayContext.getLanguage())) %>" data-title="<%= HtmlUtil.escapeAttribute(journalDDMTemplateHelper.getPaletteItemTitle(request, journalEditDDMTemplateDisplayContext.getTemplateHandlerResourceBundle(), templateVariableDefinition)) %>">
													<%= HtmlUtil.escape(LanguageUtil.get(request, journalEditDDMTemplateDisplayContext.getTemplateHandlerResourceBundle(), templateVariableDefinition.getLabel())) %>

													<c:if test="<%= templateVariableDefinition.isCollection() || templateVariableDefinition.isRepeatable() %>">*</c:if>
												</span>
											</li>

										<%
										}
										%>

									</ul>
								</liferay-frontend:fieldset>

							<%
							}
							%>

						</liferay-frontend:fieldset-group>
					</div>
				</div>
			</clay:col>
		</c:if>

		<%
		String editorContainerClass = "col-md-9";

		if (!journalEditDDMTemplateDisplayContext.isAutocompleteEnabled()) {
			editorContainerClass = "col-md-12";
		}
		%>

		<div class="lfr-editor-container <%= editorContainerClass %>" id="<portlet:namespace />editorContainer">

			<%
			DDMTemplate ddmTemplate = journalEditDDMTemplateDisplayContext.getDDMTemplate();
			String[] templateLanguageTypes = journalEditDDMTemplateDisplayContext.getTemplateLanguageTypes();
			%>

			<clay:alert
				cssClass='<%= ((ddmTemplate == null) && (templateLanguageTypes.length > 1)) || ((ddmTemplate != null) && !Objects.equals(ddmTemplate.getLanguage(), journalEditDDMTemplateDisplayContext.getLanguage())) ? "mb-3" : "hide mb-3" %>'
				dismissible="<%= true %>"
				displayType="warning"
				id="languageMessageContainer"
				message="changing-the-language-does-not-automatically-translate-the-existing-template-script"
			/>

			<clay:alert
				cssClass='<%= journalEditDDMTemplateDisplayContext.isCacheable() ? "mb-3" : "hide mb-3" %>'
				dismissible="<%= true %>"
				displayType="warning"
				id="cacheableMessageContainer"
				message="this-template-is-marked-as-cacheable.-avoid-using-code-that-uses-request-handling,-the-cms-query-api,-taglibs,-or-other-dynamic-features.-uncheck-the-cacheable-property-if-dynamic-behavior-is-needed"
			/>

			<div class="lfr-rich-editor" id="<portlet:namespace />richEditor"></div>

			<aui:input label="script-file" name="script" type="file" wrapperCssClass="mt-4" />
		</div>
	</clay:row>
</div>

<aui:script use="aui-ace-autocomplete-freemarker,aui-ace-autocomplete-plugin,aui-ace-autocomplete-velocity,aui-toggler,aui-tooltip,autocomplete-base,autocomplete-filters,event-mouseenter,event-outside,liferay-util-window,resize,transition">
	var ACPlugin = A.Plugin.AceAutoComplete;
	var Util = Liferay.Util;

	var STR_HEIGHT = 'height';

	var selectLanguageNode = A.one('#<portlet:namespace />language');

	var panelScriptContainer = A.one('#templateScriptContainer');

	if (Util.getTop() !== A.config.win) {
		var dialog = Util.getWindow();

		if (dialog && A.Lang.isFunction(dialog._detachUIHandlesAutohide)) {
			dialog._detachUIHandlesAutohide();

			if (dialog.iframe) {
				dialog.iframe.set('closeOnEscape', false);
			}
		}
	}

	var richEditor;

	<c:if test="<%= journalEditDDMTemplateDisplayContext.isAutocompleteEnabled() %>">
		var paletteContainer = panelScriptContainer.one(
			'#<portlet:namespace />templatePaletteContainer'
		);
		var paletteDataContainer = panelScriptContainer.one(
			'#<portlet:namespace />paletteDataContainer'
		);

		function createLiveSearch() {
			var PaletteSearch = A.Component.create({
				AUGMENTS: [A.AutoCompleteBase],

				EXTENDS: A.Base,

				NAME: 'searchpalette',

				prototype: {
					initializer: function () {
						var instance = this;

						instance._bindUIACBase();
						instance._syncUIACBase();
					},
				},
			});

			var getItems = function () {
				var results = [];

				paletteItems.each(function (item, index) {
					results.push({
						data: item.text().trim(),
						node: item.ancestor(),
					});
				});

				return results;
			};

			var getNoResultsNode = function () {
				if (!noResultsNode) {
					noResultsNode = A.Node.create(
						'<div class="alert"><%= UnicodeLanguageUtil.get(request, "there-are-no-results") %></div>'
					);
				}

				return noResultsNode;
			};

			var paletteItems = paletteDataContainer.all('.palette-item');
			var paletteSectionsNode = paletteDataContainer.all('.palette-section');

			var noResultsNode;

			var paletteSearch = new PaletteSearch({
				inputNode: '#<portlet:namespace />paletteSearch',
				minQueryLength: 0,
				nodes: '.palette-item-container',
				resultFilters: 'phraseMatch',
				resultTextLocator: 'data',
				source: getItems(),
			});

			paletteSearch.on('results', function (event) {
				paletteItems.each(function (item, index) {
					item.ancestor().addClass('hide');
				});

				event.results.forEach(function (item, index) {
					item.raw.node.removeClass('hide');
				});

				var foundVisibleSection;

				paletteSectionsNode.each(function (item, index) {
					var visibleItem = item.one('.palette-item-container:not(.hide)');

					if (visibleItem) {
						foundVisibleSection = true;
					}

					item.toggleClass('hide', !visibleItem);
				});

				var noResultsNode = getNoResultsNode();

				if (foundVisibleSection) {
					noResultsNode.remove();
				}
				else {
					paletteDataContainer.appendChild(noResultsNode);
				}
			});
		}

		function onPaletteItemChosen(event) {
			var editor = richEditor.getEditor();

			var item = event.currentTarget;

			var aceAutocomplete = richEditor['ace-autocomplete-plugin'];

			aceAutocomplete._lockEditor = true;

			var content = item.attr('data-content');

			var fragments = content.split('[$CURSOR$]');

			var cursorPos;
			var processed;

			A.Object.each(fragments, function (item, index) {
				if (processed) {
					cursorPos = editor.getCursorPosition();
				}

				processed = true;

				editor.insert(item);
			});

			if (cursorPos) {
				editor.moveCursorToPosition(cursorPos);
			}

			editor.focus();

			aceAutocomplete._lockEditor = false;
		}
	</c:if>

	function getEditorContent() {
		var content = richEditor.getSession().getValue();

		return content;
	}

	var paletteSearchContainer = panelScriptContainer.one(
		'#<portlet:namespace />paletteSearchContainer'
	);

	function resizeEditor(event) {
		var info = event.info;

		richEditor.set(STR_HEIGHT, info.offsetHeight);
		richEditor.set('width', info.offsetWidth);

		if (!Util.isPhone()) {
			paletteDataContainer.setStyle(
				STR_HEIGHT,
				info.offsetHeight - paletteSearchContainer.height()
			);
		}
	}

	function setEditorContent(content) {
		richEditor.getSession().setValue(content);
	}

	function setEditorPlugins(event) {
		var AutoComplete;

		<c:choose>
			<c:when test="<%= Objects.equals(journalEditDDMTemplateDisplayContext.getLanguage(), TemplateConstants.LANG_TYPE_FTL) %>">
				AutoComplete = A.AceEditor.AutoCompleteFreemarker;
			</c:when>
			<c:when test="<%= Objects.equals(journalEditDDMTemplateDisplayContext.getLanguage(), TemplateConstants.LANG_TYPE_VM) %>">
				AutoComplete = A.AceEditor.AutoCompleteVelocity;
			</c:when>
		</c:choose>

		if (AutoComplete) {
			var processor = new AutoComplete({
				variables: <%= journalEditDDMTemplateDisplayContext.getAutocompleteJSON() %>,
			});

			if (processor) {
				richEditor.unplug(ACPlugin);

				richEditor.plug(ACPlugin, {
					processor: processor,
					render: true,
					visible: false,
					zIndex: 10000,
				});
			}
			else {
				richEditor.unplug(ACPlugin);
			}
		}
	}

	var editorContentElement = A.one('#<portlet:namespace />scriptContent');

	var editorNode = A.one('#<portlet:namespace />richEditor');

	A.on(
		'domready',
		function (event) {
			richEditor = new A.AceEditor({
				boundingBox: editorNode,
				height: 400,
				mode: '<%= journalEditDDMTemplateDisplayContext.getEditorMode() %>',
				width: '100%',
			}).render();

			new A.Resize({
				handles: ['br'],
				node: editorNode,
				on: {
					resize: resizeEditor,
				},
			});

			if (editorContentElement) {
				setEditorContent(editorContentElement.val());
			}

			Liferay.on('<portlet:namespace />saveTemplate', function (event) {
				editorContentElement.val(getEditorContent());
			});

			selectLanguageNode.on('change', function (event) {
				Liferay.fire('<portlet:namespace />refreshEditor');
			});

			setEditorPlugins();

			<c:if test="<%= journalEditDDMTemplateDisplayContext.isAutocompleteEnabled() %>">
				paletteContainer.delegate(
					'click',
					onPaletteItemChosen,
					'.palette-item'
				);

				new A.TogglerDelegate({
					animated: true,
					container: paletteDataContainer,
					content: '.palette-item-content',
					header: '.palette-item-header',
				});

				new A.TooltipDelegate({
					align: {
						points: [A.WidgetPositionAlign.LC, A.WidgetPositionAlign.RC],
					},
					duration: 0.5,
					html: true,
					position: 'right',
					trigger:
						'#<portlet:namespace />templatePaletteContainer .palette-item',
					visible: false,
					zIndex: 6,
				});

				createLiveSearch();
			</c:if>
		},
		'#<portlet:namespace />richEditor'
	);

	Liferay.on('<portlet:namespace />refreshEditor', function (event) {
		var form = A.one('#<portlet:namespace />fm');

		<portlet:renderURL var="refreshDDMTemplateURL">
			<portlet:param name="mvcPath" value="/edit_ddm_template.jsp" />
		</portlet:renderURL>

		form.attr('action', '<%= refreshDDMTemplateURL %>');

		if (richEditor.getEditor().getSession().getUndoManager().hasUndo()) {
			Liferay.fire('<portlet:namespace />saveTemplate');
		}
		<c:if test="<%= journalEditDDMTemplateDisplayContext.getDDMTemplate() == null %>">
			else {
				editorContentElement.val('');
			}
		</c:if>

		submitForm(form, null, null, false);
	});

	Liferay.Util.toggleBoxes(
		'<portlet:namespace />cacheable',
		'cacheableMessageContainer'
	);
</aui:script>