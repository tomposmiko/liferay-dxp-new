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

import * as vscode from 'vscode';

import {isGoToDefinitionEnabled} from '../configurationProvider';
import {RipgrepMatch, ripgrepMatches} from '../ripgrep';
import {getToken} from '../tokens';

const getFileLocations = async (
	glob: string
): Promise<vscode.Location[] | undefined> => {
	const files = await vscode.workspace.findFiles(glob);

	if (!files.length) {
		return;
	}

	return files.map(
		(uri) => new vscode.Location(uri, new vscode.Position(0, 0))
	);
};

const getFileMethodLocations = async (files: vscode.Uri[], search: string) => {
	const lines = await ripgrepMatches({
		paths: files.map((uri) => uri.fsPath),
		search,
	});

	return lines.map((ripgrepMatch: RipgrepMatch) => ripgrepMatch.location);
};

const getMethodLocations = async (
	glob: string,
	search: string
): Promise<vscode.Location[] | undefined> => {
	const files = await vscode.workspace.findFiles(glob);

	if (!files.length) {
		return;
	}

	return getFileMethodLocations(files, search);
};

export class DefinitionProviderImpl implements vscode.DefinitionProvider {
	async provideDefinition(
		document: vscode.TextDocument,
		position: vscode.Position
	): Promise<vscode.Definition | undefined> {
		if (!isGoToDefinitionEnabled()) {
			return;
		}

		const line = document.lineAt(position);

		const token = getToken(line.text, position.character);

		if (!token) {
			return;
		}

		switch (token.type) {
			case 'className':
				return getFileLocations(
					`**/${token.match.captures[1]}.{function,macro}`
				);
			case 'methodInvocation': {
				const [, className, methodName] = token.match.captures;

				return getMethodLocations(
					`**/${className}.{function,macro}`,
					`(?:macro|function) (${methodName}) \\{`
				);
			}
			case 'pathFileName':
				return getFileLocations(`**/${token.match.captures[1]}.path`);
			case 'pathLocator': {
				const [, fileName, locatorName] = token.match.captures;

				return getMethodLocations(
					`**/${fileName}.path`,
					`<td>(${locatorName})</td>`
				);
			}
			case 'variable': {
				const [, variableName] = token.match.captures;

				const variableLocations = await getFileMethodLocations(
					[document.uri],
					`var (${variableName}) `
				);

				return variableLocations
					.filter((location) =>
						location.range.start.isBefore(position)
					)
					.pop();
			}
			case 'liferaySelenium':
				return getFileLocations(
					`**/poshi-runner/**/selenium/BaseWebDriverImpl.java`
				);
			case 'liferaySeleniumMethod':
				return getMethodLocations(
					`**/poshi-runner/**/selenium/BaseWebDriverImpl.java`,
					`public .* (${token.match.captures[2]})\\(`
				);
			case 'utilClass':
				return getFileLocations(
					`**/poshi/**/${token.match.captures[1]}.java`
				);
			case 'utilClassMethod': {
				const [, utilFileName, utilMethodName] = token.match.captures;

				return getMethodLocations(
					`**/poshi/**/${utilFileName}.java`,
					`public static .* (${utilMethodName})\\(`
				);
			}
			default:
				break;
		}
	}
}
