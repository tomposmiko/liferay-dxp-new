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

import ClayButton from '@clayui/button';
import {useIsMounted} from 'frontend-js-react-web';
import React from 'react';
import ReactDOM from 'react-dom';

import useLazy from '../../core/hooks/useLazy';
import useLoad from '../../core/hooks/useLoad';
import usePlugins from '../../core/hooks/usePlugins';
import * as Actions from '../actions/index';
import {ConfigContext} from '../config/index';
import {useSelector, useDispatch} from '../store/index';
import NetworkStatusBar from './NetworkStatusBar';
import Translation from './Translation';
import UnsafeHTML from './UnsafeHTML';

const {Suspense, useCallback, useContext, useRef} = React;

function ToolbarBody() {
	const config = useContext(ConfigContext);
	const dispatch = useDispatch();
	const {getInstance, register} = usePlugins();
	const isMounted = useIsMounted();
	const load = useLoad();
	const store = useSelector(state => state);

	const {portletNamespace} = config;
	const {segmentsExperienceId} = store;

	const {draft, singleSegmentsExperienceMode} = store;

	const {
		classPK,
		discardDraftRedirectURL,
		discardDraftURL,
		publishURL,
		redirectURL,
		toolbarPlugins
	} = config;

	const loading = useRef(() => {
		Promise.all(
			toolbarPlugins.map(toolbarPlugin => {
				const {pluginEntryPoint} = toolbarPlugin;
				const promise = load(pluginEntryPoint, pluginEntryPoint);

				const app = {
					Actions,
					config,
					dispatch,
					store
				};

				return register(pluginEntryPoint, promise, {
					app,
					toolbarPlugin
				}).then(plugin => {
					if (!plugin) {
						throw new Error(
							`Failed to get instance from ${pluginEntryPoint}`
						);
					} else if (isMounted()) {
						if (typeof plugin.activate === 'function') {
							plugin.activate();
						}
					}
				});
			})
		).catch(error => {
			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		});
	});

	if (loading.current) {
		// Do this once only.
		loading.current();
		loading.current = null;
	}

	const ToolbarSection = useLazy(
		useCallback(({instance}) => {
			if (typeof instance.renderToolbarSection === 'function') {
				return instance.renderToolbarSection();
			} else {
				return null;
			}
		}, [])
	);

	const handleDiscardDraft = event => {
		if (
			!confirm(
				Liferay.Language.get(
					'are-you-sure-you-want-to-discard-current-draft-and-apply-latest-published-changes'
				)
			)
		) {
			event.preventDefault();
		}
	};

	return (
		<div className="container-fluid container-fluid-max-xl">
			<ul className="navbar-nav">
				{toolbarPlugins.map(
					({loadingPlaceholder, pluginEntryPoint}) => {
						return (
							<li className="nav-item" key={pluginEntryPoint}>
								<ErrorBoundary>
									<Suspense
										fallback={
											<UnsafeHTML
												markup={loadingPlaceholder}
											/>
										}
									>
										<ToolbarSection
											getInstance={getInstance}
											pluginId={pluginEntryPoint}
										/>
									</Suspense>
								</ErrorBoundary>
							</li>
						);
					}
				)}
				<li className="nav-item">
					<Translation
						availableLanguages={config.availableLanguages}
						defaultLanguageId={config.defaultLanguageId}
						dispatch={dispatch}
						fragmentEntryLinks={store.fragmentEntryLinks}
						languageId={store.languageId}
						segmentsExperienceId={segmentsExperienceId}
					/>
				</li>
			</ul>

			<ul className="navbar-nav">
				<NetworkStatusBar {...store.network} />
				<li className="nav-item">
					<form action={discardDraftURL} method="POST">
						<input
							name={`${portletNamespace}classPK`}
							type="hidden"
							value={classPK ? classPK : ''}
						/>

						<input
							name={`${portletNamespace}redirect`}
							type="hidden"
							value={discardDraftRedirectURL}
						/>

						<ClayButton
							className="btn btn-secondary nav-btn"
							disabled={!draft}
							displayType="secondary"
							onClick={handleDiscardDraft}
							small
							type="submit"
						>
							{singleSegmentsExperienceMode
								? Liferay.Language.get('discard-variant')
								: Liferay.Language.get('discard-draft')}
						</ClayButton>
					</form>
				</li>
				<li className="nav-item">
					<form action={publishURL} method="POST">
						<input
							name={`${portletNamespace}classPK`}
							type="hidden"
							value={classPK}
						/>

						<input
							name={`${portletNamespace}redirect`}
							type="hidden"
							value={redirectURL}
						/>

						<ClayButton
							className="nav-btn"
							displayType="primary"
							small
							type="submit"
						>
							{singleSegmentsExperienceMode
								? Liferay.Language.get('save-variant')
								: Liferay.Language.get('publish')}
						</ClayButton>
					</form>
				</li>
			</ul>
		</div>
	);
}

class ErrorBoundary extends React.Component {
	static getDerivedStateFromError(_error) {
		return {hasError: true};
	}

	constructor(props) {
		super(props);

		this.state = {hasError: false};
	}

	componentDidCatch(error) {
		if (process.env.NODE_ENV === 'development') {
			console.error(error);
		}
	}

	render() {
		if (this.state.hasError) {
			return null;
		} else {
			return this.props.children;
		}
	}
}

export default function Toolbar() {
	const isMounted = useIsMounted();
	const {toolbarId} = useContext(ConfigContext);

	const container = document.getElementById(toolbarId);

	if (!isMounted()) {
		// First time here, must empty JSP-rendered markup from container.
		while (container.firstChild) {
			container.removeChild(container.firstChild);
		}
	}

	return ReactDOM.createPortal(<ToolbarBody />, container);
}
