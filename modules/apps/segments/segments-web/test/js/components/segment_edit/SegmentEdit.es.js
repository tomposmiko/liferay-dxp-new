import React from 'react';
import SegmentEdit from 'components/segment_edit/SegmentEdit.es';
import {SOURCES} from 'utils/constants.es';
import {cleanup, render} from 'react-testing-library';
import 'jest-dom/extend-expect';

const SOURCE_ICON_TESTID = 'source-icon';

describe(
	'SegmentEdit',
	() => {
		afterEach(cleanup);

		it(
			'should render',
			() => {
				const {asFragment} = render(
					<SegmentEdit
						locale="en_US"
					/>
				);

				expect(asFragment()).toMatchSnapshot();
			}
		);

		it(
			'should render with an analytics cloud icon',
			() => {
				const {icon, name} = SOURCES.ASAH_FARO_BACKEND;

				const {getByTestId} = render(
					<SegmentEdit
						locale="en_US"
						source={name}
					/>
				);

				const image = getByTestId(SOURCE_ICON_TESTID);

				expect(image).toHaveAttribute('src', icon);
			}
		);

		it(
			'should render with a dxp icon',
			() => {
				const {icon, name} = SOURCES.DEFAULT;

				const {getByTestId} = render(
					<SegmentEdit
						locale="en_US"
						source={name}
					/>
				);

				const image = getByTestId(SOURCE_ICON_TESTID);

				expect(image).toHaveAttribute('src', icon);
			}
		);
	}
);