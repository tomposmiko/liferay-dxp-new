<@liferay_ui["panel-container"]
	extended=true
	id="${namespace + 'facetModifiedPanelContainer'}"
	markupView="lexicon"
	persistState=true
>
	<@liferay_ui.panel
		collapsible=true
		cssClass="search-facet"
		id="${namespace + 'facetModifiedPanel'}"
		markupView="lexicon"
		persistState=true
		title="last-modified"
	>
		<ul class="list-unstyled modified">
			<#if entries?has_content>
				<#list entries as entry>
					<li class="facet-value">
						<div class="custom-control custom-radio">
							<label class="facet-checkbox-label" for="${entry.getBucketText()}">
								<input
									autocomplete="off"
									${(entry.isSelected())?then("checked", "")}
									class="custom-control-input facet-term"
									disabled
									id="${entry.getBucketText()}"
									name="${entry.getBucketText()}"
									onChange='${"window.location.href = \"${entry.getFilterValue()}\";"}'
									role="radio"
									type="radio"
								/>

								<span class="custom-control-label term-name ${(entry.isSelected())?then('facet-term-selected', 'facet-term-unselected')}">
									<span class="custom-control-label-text">
										<@liferay_ui["message"] key="${htmlUtil.escape(entry.getBucketText())}" />
									</span>
								</span>

								<small class="term-count">
									(${entry.getFrequency()})
								</small>
							</label>
						</div>
					</li>
				</#list>
			</#if>

			<li class="facet-value">
				<div class="custom-control custom-radio">
					<label class="facet-checkbox-label" for="${customRangeBucketDisplayContext.getBucketText()}">
						<input
							autocomplete="off"
							${(customRangeBucketDisplayContext.isSelected())?then("checked", "")}
							class="custom-control-input facet-term"
							disabled
							id="${customRangeBucketDisplayContext.getBucketText()}"
							name="${customRangeBucketDisplayContext.getBucketText()}"
							onChange='${"window.location.href = \"${customRangeBucketDisplayContext.getFilterValue()}\";"}'
							role="radio"
							type="radio"
						/>

						<span class="custom-control-label term-name ${(customRangeBucketDisplayContext.isSelected())?then('facet-term-selected', 'facet-term-unselected')}">
							<span class="custom-control-label-text">
								<@liferay_ui["message"] key="${htmlUtil.escape(customRangeBucketDisplayContext.getBucketText())}" />
							</span>
						</span>

						<#if customRangeBucketDisplayContext.isSelected()>
							<small class="term-count">
								(${customRangeBucketDisplayContext.getFrequency()})
							</small>
						</#if>
					</label>
				</div>
			</li>

			<div class="${(!modifiedFacetCalendarDisplayContext.isSelected())?then("hide", "")} modified-custom-range" id="${namespace}customRange">
				<div class="col-md-6" id="${namespace}customRangeFrom">
					<@liferay_aui["field-wrapper"] label="from">
						<@liferay_ui["input-date"]
							cssClass="modified-facet-custom-range-input-date-from"
							dayParam="fromDay"
							dayValue=modifiedFacetCalendarDisplayContext.getFromDayValue()
							disabled=false
							firstDayOfWeek=modifiedFacetCalendarDisplayContext.getFromFirstDayOfWeek()
							monthParam="fromMonth"
							monthValue=modifiedFacetCalendarDisplayContext.getFromMonthValue()
							name="fromInput"
							yearParam="fromYear"
							yearValue=modifiedFacetCalendarDisplayContext.getFromYearValue()
						/>
					</@>
				</div>

				<div class="col-md-6" id="${namespace}customRangeTo">
					<@liferay_aui["field-wrapper"] label="to">
						<@liferay_ui["input-date"]
							cssClass="modified-facet-custom-range-input-date-to"
							dayParam="toDay"
							dayValue=modifiedFacetCalendarDisplayContext.getToDayValue()
							disabled=false
							firstDayOfWeek=modifiedFacetCalendarDisplayContext.getToFirstDayOfWeek()
							monthParam="toMonth"
							monthValue=modifiedFacetCalendarDisplayContext.getToMonthValue()
							name="toInput"
							yearParam="toYear"
							yearValue=modifiedFacetCalendarDisplayContext.getToYearValue()
						/>
					</@>
				</div>

				<@liferay_aui.button
					cssClass="modified-facet-custom-range-filter-button"
					disabled=modifiedFacetCalendarDisplayContext.isRangeBackwards()
					name="searchCustomRangeButton"
					value="search"
				/>
			</div>
		</ul>

		<#if !modifiedFacetDisplayContext.isNothingSelected()>
			<@liferay_aui.button
				cssClass="btn-link btn-unstyled facet-clear-btn"
				onClick="Liferay.Search.FacetUtil.clearSelections(event);"
				value="clear"
			/>
		</#if>
	</@>
</@>