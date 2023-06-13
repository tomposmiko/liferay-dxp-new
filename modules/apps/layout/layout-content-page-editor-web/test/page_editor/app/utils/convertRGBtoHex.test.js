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

import {convertRGBtoHex} from '../../../../src/main/resources/META-INF/resources/page_editor/app/utils/convertRGBtoHex';

describe('convertRGBtoHex', () => {
	it('converts rgb colors', () => {
		expect(convertRGBtoHex('rgb(100, 100, 100)')).toBe('#646464');
		expect(convertRGBtoHex('rgb(100,100,   100)')).toBe('#646464');
	});

	it('converts rgba colors', () => {
		expect(convertRGBtoHex('rgba(100, 100, 100,   0)')).toBe('#64646400');
		expect(convertRGBtoHex('rgba(100,100,   100,1)')).toBe('#646464FF');
		expect(convertRGBtoHex('rgba(100, 100, 100, 0.827)')).toBe('#646464D2');
	});

	it('ignores invalid colors', () => {
		expect(convertRGBtoHex('rgb(0, 0, 0, 1)')).toBe('rgb(0, 0, 0, 1)');
		expect(convertRGBtoHex('rgba(0, 0, 0)')).toBe('rgba(0, 0, 0)');
		expect(convertRGBtoHex('rgb(1, 2, -2)')).toBe('rgb(1, 2, -2)');
		expect(convertRGBtoHex('rgba(0,0,0,-0.1)')).toBe('rgba(0,0,0,-0.1)');
	});

	it('ignores unknown colors', () => {
		expect(convertRGBtoHex('#646464')).toBe('#646464');
		expect(convertRGBtoHex('#646464AA')).toBe('#646464AA');
		expect(convertRGBtoHex('hsl(0, 0%, 50%)')).toBe('hsl(0, 0%, 50%)');
		expect(convertRGBtoHex('var(--my-red)')).toBe('var(--my-red)');
	});
});
