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

import 'codemirror/addon/display/autorefresh';

import 'codemirror/addon/edit/closebrackets';

import 'codemirror/addon/edit/closetag';

import 'codemirror/addon/edit/matchbrackets';

import 'codemirror/addon/fold/brace-fold';

import 'codemirror/addon/fold/comment-fold';

import 'codemirror/addon/fold/foldcode';

import 'codemirror/addon/fold/foldgutter.css';

import 'codemirror/addon/fold/foldgutter';

import 'codemirror/addon/fold/indent-fold';

import 'codemirror/addon/fold/xml-fold';

import 'codemirror/addon/hint/css-hint';

import 'codemirror/addon/hint/html-hint';

import 'codemirror/addon/hint/show-hint.css';

import 'codemirror/addon/hint/show-hint';

import 'codemirror/addon/hint/xml-hint';

import 'codemirror/lib/codemirror.css';

import 'codemirror/mode/css/css';

import 'codemirror/mode/htmlmixed/htmlmixed';

import 'codemirror/mode/xml/xml';
import classNames from 'classnames';
import CodeMirror from 'codemirror';
import React, {useEffect, useRef} from 'react';

const noop = () => {};

const CodeMirrorEditor = ({
	className,
	initialContent = '',
	mode = 'text/html',
	onChange = noop,
}) => {
	const ref = useRef();

	useEffect(() => {
		if (ref.current) {
			const codeMirror = CodeMirror(ref.current, {
				autoCloseTags: true,
				autoRefresh: true,
				extraKeys: {
					'Ctrl-Space': 'autocomplete',
				},
				globalVars: true,
				gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
				hintOptions: {
					completeSingle: false,
				},
				lineNumbers: true,
				mode,
				showHint: true,
				tabSize: 2,
				value: initialContent,
			});

			codeMirror.on('change', (cm) => {
				onChange(cm.getValue());
			});

			codeMirror.setSize(null, '100%');
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	return <div className={classNames(className, 'h-100')} ref={ref} />;
};

export default CodeMirrorEditor;
