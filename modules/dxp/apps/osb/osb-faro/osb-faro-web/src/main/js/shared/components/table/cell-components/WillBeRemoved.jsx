import Label from 'shared/components/Label';
import React from 'react';
import {PropTypes} from 'prop-types';
import {toPairs} from 'lodash';

export default class WillBeRemovedCell extends React.Component {
	static propTypes = {
		data: PropTypes.shape({
			dataSourceIndividualPKs: PropTypes.array
		})
	};

	render() {
		const {
			data: {dataSourceIndividualPKs}
		} = this.props;

		return (
			<td
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				{toPairs(dataSourceIndividualPKs).length === 1 && (
					<Label display='danger' size='lg' uppercase>
						{Liferay.Language.get('will-be-removed')}
					</Label>
				)}
			</td>
		);
	}
}
