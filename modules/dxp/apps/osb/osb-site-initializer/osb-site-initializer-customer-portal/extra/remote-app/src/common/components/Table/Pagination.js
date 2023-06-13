import {ClayPaginationBarWithBasicItems} from '@clayui/pagination-bar';
import {getIconSpriteMap} from '../../providers/ClayProvider';

const TablePagination = ({
	activeDelta = 5,
	activePage,
	ellipsisBuffer = 3,
	itemsPerPage,
	setActivePage,
	showDeltasDropDown = false,
	totalItems,
}) => {
	if (totalItems > itemsPerPage) {
		return (
			<div className="mb-3 mx-3">
				<ClayPaginationBarWithBasicItems
					activeDelta={activeDelta}
					activePage={activePage}
					ellipsisBuffer={ellipsisBuffer}
					onPageChange={(page) => setActivePage(page)}
					showDeltasDropDown={showDeltasDropDown}
					spritemap={getIconSpriteMap()}
					totalItems={totalItems}
				/>
			</div>
		);
	}

	return (
		<p className="mb-4 mx-4 pagination-results">{`Showing ${
			itemsPerPage * activePage + 1 - itemsPerPage
		} to ${totalItems} of ${totalItems} entries.`}</p>
	);
};

export default TablePagination;
