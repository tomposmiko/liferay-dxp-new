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

function initArticle() {

	// Table of contents reading indicator

	const headings = document.querySelectorAll('.article-body h2');

	let activeIndex;
	const targets = [];

	if (headings) {
		const articleTOC = document.getElementById('articleTOC');

		if (articleTOC) {
			articleTOC.innerHTML = '';
		}

		headings.forEach((heading) => {
			const id = heading.querySelector('a').hash.replace('#', '');

			if (articleTOC) {
				articleTOC.innerHTML += `
				<li class="nav-item">
					<a class="nav-link" data-senna-off="true" href="#${id}" id="toc-${id}">
						${heading.innerText}
					</a>
				</li>`;
			}

			targets.push({id, isIntersecting: false});
		});
	}

	const callback = (entries) => {
		entries.forEach((entry) => {
			const index = targets.findIndex(
				(target) => target.id === entry.target.id
			);

			targets[index].isIntersecting = entry.isIntersecting;

			if (!targets[activeIndex] || !targets[activeIndex].isIntersecting) {
				setActiveIndex();
			}
		});

		if (targets[activeIndex]) {
			toggleActiveClass(targets[activeIndex].id);
		}
	};

	const headerSelectors = [
		'.control-menu-container',
		'.info-bar',
		'.public-sites-navigation',
	];

	const headerOffset = headerSelectors
		.map((headerSelector) => {
			const headerElement = document.querySelector(headerSelector);
			if (headerElement) {
				return -1 * headerElement.offsetHeight;
			}

			return 0;
		})
		.reduce(
			(previousValue, currentValue) => previousValue + currentValue,
			0
		);

	const observer = new IntersectionObserver(callback, {
		rootMargin: headerOffset + 'px',
		threshold: [0, 0.2, 0.4, 0.6, 0.8, 1],
	});

	const setActiveIndex = () => {
		activeIndex = targets.findIndex(
			(target) => target.isIntersecting === true
		);
	};

	const toggleActiveClass = (id) => {
		targets.forEach((target) => {
			const node = document.getElementById(`toc-${target.id}`);

			if (node) {
				node.classList.remove('active');
			}
		});

		const activeNode = document.getElementById(`toc-${id}`);

		if (activeNode) {
			activeNode.classList.add('active');
		}
	};

	targets.forEach((target) => {
		const node = target.id ? document.getElementById(target.id) : null;

		if (node) {
			observer.observe(node);

			const offsetMargin =
				'margin-top: ' +
				headerOffset +
				'px; padding-top: ' +
				headerOffset * -1 +
				'px;';

			node.style.cssText = offsetMargin;
		}
	});

	// Left Nav mobile interaction

	const docNavWrapper = document.querySelector('.doc-nav-wrapper');
	const mobileDocNavToggler = document.getElementById('mobileDocNavToggler');

	if (docNavWrapper && mobileDocNavToggler) {
		const togglers = mobileDocNavToggler.querySelectorAll('button');

		togglers.forEach((toggler) =>
			toggler.addEventListener('click', () => {
				docNavWrapper.classList.toggle('mobile-nav-hide');
			})
		);
	}
}

document.addEventListener('DOMContentLoaded', initArticle);

Liferay.on('endNavigate', initArticle);
