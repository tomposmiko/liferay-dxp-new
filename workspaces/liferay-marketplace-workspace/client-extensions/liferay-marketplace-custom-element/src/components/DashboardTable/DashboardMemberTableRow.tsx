import ClayTable from '@clayui/table';

import './PublishedAppsDashboardTableRow.scss';
import {MemberProps} from '../../pages/PublishedAppsDashboardPage/PublishedDashboardPageUtil';

interface DashboardMemberTableRowProps {
	item: MemberProps;
	onSelectedMemberChange: (value: MemberProps | undefined) => void;
}

export function DashboardMemberTableRow({
	item,
	onSelectedMemberChange,
}: DashboardMemberTableRowProps) {
	const {email, image, name, role} = item;

	return (
		<ClayTable.Row>
			<ClayTable.Cell>
				<div
					className="dashboard-table-row-name-container"
					onClick={() => onSelectedMemberChange(item)}
				>
					<img
						alt="Member Image"
						className="dashboard-table-row-name-logo"
						src={image}
					/>

					<span className="dashboard-table-row-name-text">
						{name}
					</span>
				</div>
			</ClayTable.Cell>

			<ClayTable.Cell>
				<span className="dashboard-table-row-text">{email}</span>
			</ClayTable.Cell>

			<ClayTable.Cell>
				<span className="dashboard-table-row-text">{role}</span>
			</ClayTable.Cell>
		</ClayTable.Row>
	);
}
