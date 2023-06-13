import React from 'react';
import PropTypes from 'prop-types';
import ClayButton from '../shared/ClayButton.es';
import getCN from 'classnames';

class Conjunction extends React.Component {
	static propTypes = {
		className: PropTypes.string,
		conjunctionName: PropTypes.string.isRequired,
		editing: PropTypes.bool.isRequired,
		onClick: PropTypes.func,
		supportedConjunctions: PropTypes.array.isRequired
	};

	_getConjunctionLabel(conjunctionName, conjunctions) {
		const conjunction = conjunctions.find(
			({name}) => name === conjunctionName
		);

		return conjunction ? conjunction.label : undefined;
	}

	render() {
		const {
			className,
			conjunctionName,
			editing,
			onClick,
			supportedConjunctions
		} = this.props;

		const classnames = getCN(
			{
				'btn-sm conjunction-button': editing,
				'conjunction-label': !editing
			},
			className
		);

		return (
			editing ?
				<ClayButton
					className={classnames}
					label={this._getConjunctionLabel(
						conjunctionName,
						supportedConjunctions
					)}
					onClick={onClick}
				/> :
				<div className={classnames}>
					{this._getConjunctionLabel(
						conjunctionName,
						supportedConjunctions
					)}
				</div>
		);
	}
}

export default Conjunction;