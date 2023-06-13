import ClayButton from '@clayui/button';
import ClayLoadingIndicator from '@clayui/loading-indicator';
import getCN from 'classnames';
import Modal from 'shared/components/modal';
import ModalInfoBar from 'shared/components/ModalInfoBar';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import React, {useEffect, useState} from 'react';
import Spinner from 'shared/components/Spinner';
import Toolbar from 'shared/components/toolbar';
import {sub} from 'shared/util/lang';
import {useRequest, useStatefulPagination} from 'shared/hooks';

interface ISearchableModalProps {
	children: React.ReactNode;
	className?: string;
	countLabel: string;
	dataSourceFn: (params: any) => any;
	fitContent?: boolean;
	footer?: React.ReactNode;
	initialDelta?: boolean;
	initialOrderIOMap: boolean;
	items?: any[];
	noResultsIcon: string;
	noResultsName: string;
	noResultsTitle: string;
	onChange: (items: any[]) => void;
	onClose: () => void;
	showSortButton?: boolean;
	showToolbar?: boolean;
	title?: string;
}

const SearchableModal: React.FC<ISearchableModalProps> = ({
	children,
	className,
	countLabel = Liferay.Language.get('x-items'),
	dataSourceFn,
	fitContent = false,
	footer = null,
	initialDelta = 10,
	initialOrderIOMap,
	items = [],
	noResultsIcon,
	noResultsName,
	noResultsTitle,
	onChange,
	onClose,
	showSortButton = true,
	showToolbar = true,
	title = Liferay.Language.get('see-all'),
	...otherProps
}) => {
	const [searchValue, setSearchValue] = useState<any[]>([]);

	const {
		delta,
		onOrderIOMapChange,
		onPageChange,
		onQueryChange,
		orderIOMap,
		page,
		query
	} = useStatefulPagination(null, {
		initialDelta,
		initialOrderIOMap,
		initialPage: 1
	});

	const {data, loading} = useRequest({
		dataSourceFn,
		variables: {
			delta,
			orderIOMap,
			page,
			query
		}
	});

	useEffect(() => {
		if (page !== 1) {
			onPageChange(1);
		}
	}, [orderIOMap, query]);

	useEffect(() => {
		if (page === 1 && data) {
			onChange(data?.items);
		} else if (data) {
			onChange([...items, ...data?.items]);
		}
	}, [data]);

	const renderChildren = () => {
		if (items?.length < data?.total) {
			return (
				<div>
					{children}

					<div className='load-more-container'>
						<ClayButton
							className='button-root'
							displayType='secondary'
							onClick={() => onPageChange(page + 1)}
						>
							{loading && (
								<ClayLoadingIndicator
									className='d-inline-block mr-2'
									displayType='secondary'
									size='sm'
								/>
							)}

							{Liferay.Language.get('load-more')}
						</ClayButton>
					</div>
				</div>
			);
		} else if (loading) {
			return <Spinner spacer />;
		} else if (!data?.total) {
			return (
				<NoResultsDisplay
					icon={noResultsIcon ? {symbol: noResultsIcon} : undefined}
					spacer
					title={getFormattedTitle(noResultsName, noResultsTitle)}
				/>
			);
		} else {
			return <div>{children}</div>;
		}
	};

	const contentClasses = getCN('scroll-container', {
		'fit-content': fitContent
	});

	return (
		<Modal
			{...otherProps}
			className={getCN('searchable-modal-root', className)}
			size='lg'
		>
			<Modal.Header onClose={onClose} title={title} />

			{showToolbar && (
				<Toolbar
					alwaysShowSearch
					autoFocus
					onOrderIOMapChange={onOrderIOMapChange}
					onSearchSubmit={onQueryChange}
					onSearchValueChange={setSearchValue}
					orderIOMap={orderIOMap}
					searchValue={searchValue}
					showCheckbox={false}
					showFilterAndOrder={showSortButton}
				/>
			)}

			{!!data?.total && (
				<ModalInfoBar>{sub(countLabel, [data?.total])}</ModalInfoBar>
			)}

			<div className={contentClasses}>{renderChildren()}</div>

			<Modal.Footer>{footer}</Modal.Footer>
		</Modal>
	);
};

export default SearchableModal;
