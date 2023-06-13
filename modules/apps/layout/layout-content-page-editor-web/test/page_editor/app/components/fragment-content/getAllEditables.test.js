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

import getAllEditables from '../../../../../src/main/resources/META-INF/resources/page_editor/app/components/fragment-content/getAllEditables';
import {BACKGROUND_IMAGE_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/backgroundImageFragmentEntryProcessor';
import {EDITABLE_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/editableFragmentEntryProcessor';
import BackgroundImageProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/BackgroundImageProcessor';
import HTMLProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/HTMLProcessor';
import ImageProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/ImageProcessor';
import LinkProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/LinkProcessor';
import RichTextProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/RichTextProcessor';
import TextProcessor from '../../../../../src/main/resources/META-INF/resources/page_editor/app/processors/TextProcessor';

describe('getAllEditables', () => {
	it('returns all editables for a given Element', () => {
		const element = document.createElement('div');

		element.innerHTML = `
      <lfr-editable id="img-old" type="image"><img src="placeholder.jpg" alt="Placeholder"></lfr-editable>
      <img data-lfr-editable-id="img-new" data-lfr-editable-type="image" src="placeholder.jpg" alt="Placeholder">

      <lfr-editable id="link-old" type="link"><a href="#placeholder" target="_blank">Go to placeholder</a></lfr-editable>
      <a data-lfr-editable-id="link-new" data-lfr-editable-type="link" href="#placeholder" target="_blank">Go to placeholder</a>

      <lfr-editable id="html-old" type="html"><h1>Placeholder</h1></lfr-editable>
      <article data-lfr-editable-id="html-new" data-lfr-editable-type="html"><h1>Placeholder</h1></article>

      <lfr-editable id="text-old" type="text">Placeholder</lfr-editable>
      <p data-lfr-editable-id="text-new" data-lfr-editable-type="text">Placeholder</p>

      <lfr-editable id="rich-text-old" type="rich-text">Placeholder</lfr-editable>
      <p data-lfr-editable-id="rich-text-new" data-lfr-editable-type="rich-text">Placeholder</p>

      <div data-lfr-background-image-id="background-image"></div>
    `;

		expect(getAllEditables(element)).toEqual([
			{
				editableId: 'img-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: ImageProcessor,
				type: 'image',
			},
			{
				editableId: 'link-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: LinkProcessor,
				type: 'link',
			},
			{
				editableId: 'html-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: HTMLProcessor,
				type: 'html',
			},
			{
				editableId: 'text-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: TextProcessor,
				type: 'text',
			},
			{
				editableId: 'rich-text-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: RichTextProcessor,
				type: 'rich-text',
			},
			{
				editableId: 'img-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLImageElement),
				processor: ImageProcessor,
				type: 'image',
			},
			{
				editableId: 'link-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLAnchorElement),
				processor: LinkProcessor,
				type: 'link',
			},
			{
				editableId: 'html-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: HTMLProcessor,
				type: 'html',
			},
			{
				editableId: 'text-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLParagraphElement),
				processor: TextProcessor,
				type: 'text',
			},
			{
				editableId: 'rich-text-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLParagraphElement),
				processor: RichTextProcessor,
				type: 'rich-text',
			},
			{
				editableId: 'background-image',
				editableValueNamespace: BACKGROUND_IMAGE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLDivElement),
				processor: BackgroundImageProcessor,
				type: 'background-image',
			},
		]);
	});

	it('filters dropzone editables', () => {
		const element = document.createElement('div');

		element.innerHTML = `
      <lfr-editable id="img-old" type="image"><img src="placeholder.jpg" alt="Placeholder"></lfr-editable>
      <img data-lfr-editable-id="img-new" data-lfr-editable-type="image" src="placeholder.jpg" alt="Placeholder">
      <div data-lfr-background-image-id="background-image"></div>

      <lfr-drop-zone id="dropzone">
        <lfr-editable id="link-old" type="link"><a href="#placeholder" target="_blank">Go to placeholder</a></lfr-editable>
        <a data-lfr-editable-id="link-new" data-lfr-editable-type="link" href="#placeholder" target="_blank">Go to placeholder</a>
        <div data-lfr-background-image-id="background-image-duplo"></div>
      </lfr-dropzone>
    `;

		expect(getAllEditables(element)).toEqual([
			{
				editableId: 'img-old',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLElement),
				processor: ImageProcessor,
				type: 'image',
			},
			{
				editableId: 'img-new',
				editableValueNamespace: EDITABLE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLImageElement),
				processor: ImageProcessor,
				type: 'image',
			},
			{
				editableId: 'background-image',
				editableValueNamespace: BACKGROUND_IMAGE_FRAGMENT_ENTRY_PROCESSOR,
				element: expect.any(HTMLDivElement),
				processor: BackgroundImageProcessor,
				type: 'background-image',
			},
		]);
	});
});
