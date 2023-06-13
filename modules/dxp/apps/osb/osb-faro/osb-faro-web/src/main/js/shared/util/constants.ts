import {Align} from 'metal-position';

export const DEVELOPER_MODE = FARO_DEV_MODE;

export const PROD_MODE = FARO_ENV === 'prd';

// LRAC-11571 Disable temporarily Accounts

export const ENABLE_ACCOUNTS = false;

// LRAC - 11652 Hide Displayed Asset Card on the Page report

export const ENABLE_ASSET_CARD = false;

// LRAC-11651 Disable temporarily CSV File

export const ENABLE_CSVFILE = false;

// LRAC-11650 Hide Form Abandonment Card

export const ENABLE_FORM_ABANDONMENT = false;

// LRAC-10908 Hide global filters

export const ENABLE_GLOBAL_FILTER = false;

// LRAC-11571 Disable temporarily Salesforce

export const ENABLE_SALESFORCE = false;

/**
 * Languages
 */
export const LANGUAGES = [
	{
		id: 'en_US',
		label: Liferay.Language.get('english')
	},
	{
		id: 'ja_JP',
		label: Liferay.Language.get('japanese')
	},
	{
		id: 'pt_BR',
		label: Liferay.Language.get('portuguese')
	},
	{
		id: 'es_ES',
		label: Liferay.Language.get('spanish')
	}
];

/**
 * Metal-Position Alignments
 */
export const ALIGNMENTS_MAP = {
	bottom: Align.Bottom,
	'bottom-left': Align.BottomLeft,
	'bottom-right': Align.BottomRight,
	left: Align.Left,
	right: Align.Right,
	top: Align.Top,
	'top-left': Align.TopLeft,
	'top-right': Align.TopRight
};

export const POSITIONS = [
	'top',
	'top',
	'right',
	'bottom',
	'bottom',
	'bottom',
	'left',
	'top'
];

/**
 * Assets
 */
export enum AcquisitionTypes {
	Channel = 'CHANNEL',
	Referrer = 'REFERRER',
	SourceMedium = 'SOURCE_MEDIUM'
}

export enum AssetNames {
	BlogViewed = 'blogViewed',
	CommentPosted = 'commentPosted',
	DocumentDownloaded = 'documentDownloaded',
	DocumentPreviewed = 'documentPreviewed',
	FormSubmitted = 'formSubmitted',
	FormViewed = 'formViewed',
	PageViewed = 'pageViewed',
	WebContentViewed = 'webContentViewed'
}

export enum AssetTypes {
	Asset = 'Asset',
	Blog = 'Blog',
	Document = 'Document',
	Form = 'Form',
	WebContent = 'WebContent',
	WebPage = 'Page'
}

const average = Liferay.Language.get('average').toLowerCase();
const ratio = Liferay.Language.get('ratio').toLowerCase();
const sum = Liferay.Language.get('sum').toLowerCase();

export const ASSET_METRICS = [
	{
		key: 'abandonmentsMetric',
		selectTitle: `${Liferay.Language.get('form-abandonment')} (${ratio})`,
		title: Liferay.Language.get('form-abandonment'),
		type: 'percentage'
	},
	{
		key: 'clicksMetric',
		selectTitle: `${Liferay.Language.get('asset-clicks')} (${sum})`,
		title: Liferay.Language.get('clicks'),
		type: 'number'
	},
	{
		key: 'completionTimeMetric',
		selectTitle: `${Liferay.Language.get(
			'form-completion-time'
		)} (${average})`,
		title: Liferay.Language.get('completion-time'),
		type: 'time'
	},
	{
		key: 'downloadsMetric',
		selectTitle: `${Liferay.Language.get('asset-downloads')} (${sum})`,
		title: Liferay.Language.get('downloads'),
		type: 'number'
	},
	{
		key: 'readingTimeMetric',
		selectTitle: `${Liferay.Language.get(
			'asset-interaction-time'
		)} (${average})`,
		title: Liferay.Language.get('interaction-time'),
		type: 'time'
	},
	{
		key: 'submissionsMetric',
		selectTitle: `${Liferay.Language.get('form-submissions')} (${sum})`,
		title: Liferay.Language.get('form-submissions'),
		type: 'number'
	},
	{
		key: 'viewsMetric',
		selectTitle: `${Liferay.Language.get('asset-views')} (${sum})`,
		title: Liferay.Language.get('views'),
		type: 'number'
	}
];

export enum CompositionTypes {
	AccountInterests = 'accountInterests',
	Acquisitions = 'acquisitions',
	IndividualInterests = 'individualInterests',
	SearchTerms = 'searchTerms',
	SegmentInterests = 'individualSegmentInterests',
	SiteInterests = 'siteInterests'
}

export enum CredentialTypes {
	OAuth1 = 'OAuth 1 Authentication',
	OAuth2 = 'OAuth 2 Authentication',
	Token = 'Token Authentication'
}

export enum DataSourceStates {
	ActionNeeded = 'ACTION_NEEDED',
	AnalyticsClientConfigurationFailure = 'ANALYTICS_CLIENT_CONFIGURATION_FAILURE',
	CredentialsInvalid = 'CREDENTIALS_INVALID',
	CredentialsValid = 'CREDENTIALS_VALID',
	Disconnected = 'DISCONNECTED',
	InProgressDeleting = 'IN_PROGRESS_DELETING',
	LiferayVersionInvalid = 'LIFERAY_VERSION_INVALID',
	UndefinedError = 'UNDEFINED_ERROR',
	Ready = 'READY',
	UrlInvalid = 'URL_INVALID',
	Unconfigured = 'UNCONFIGURED'
}

export enum UserStatuses {
	Approved = 0,
	Pending = 1,
	Requested = 2
}

/**
 * GDPR
 */
export enum GDPRRequestStatuses {
	Completed = 'COMPLETED',
	Error = 'ERROR',
	Expired = 'EXPIRED',
	Pending = 'PENDING',
	Running = 'RUNNING'
}

export enum GDPRRequestTypes {
	Access = 'ACCESS',
	Delete = 'DELETE',
	Suppress = 'SUPPRESS',
	Unsuppress = 'UNSUPPRESS'
}

/**
 * RangeKey TimeRange
 */

export enum RangeKeyTimeRanges {
	CustomRange = 'CUSTOM',
	Last180Days = '180',
	Last24Hours = '0',
	Last28Days = '28',
	Last30Days = '30',
	Last7Days = '7',
	Last90Days = '90',
	LastYear = '365',
	Yesterday = '1'
}

export const TIME_RANGE_LABELS = {
	[RangeKeyTimeRanges.Last180Days]: Liferay.Language.get('last-180-days'),
	[RangeKeyTimeRanges.Last24Hours]: Liferay.Language.get('last-24-hours'),
	[RangeKeyTimeRanges.Last28Days]: Liferay.Language.get('last-28-days'),
	[RangeKeyTimeRanges.Last30Days]: Liferay.Language.get('last-30-days'),
	[RangeKeyTimeRanges.Last7Days]: Liferay.Language.get('last-seven-days'),
	[RangeKeyTimeRanges.Last90Days]: Liferay.Language.get('last-90-days'),
	[RangeKeyTimeRanges.LastYear]: Liferay.Language.get('last-year'),
	[RangeKeyTimeRanges.Yesterday]: Liferay.Language.get('yesterday')
};

/**
 * Sprite
 */
export const spritemap = '/o/osb-faro-web/dist/sprite.svg';

/**
 * Jobs
 */
export enum JobRunStatuses {
	Completed = 'COMPLETED',
	Failed = 'FAILED',
	Published = 'PUBLISHED',
	Running = 'RUNNING'
}

export enum JobStatuses {
	Failed = 'FAILED',
	Pending = 'PENDING',
	Ready = 'READY',
	Running = 'RUNNING',
	Scheduled = 'SCHEDULED'
}

export enum JobRunFrequencies {
	Every7Days = 'EVERY_7_DAYS',
	Every14Days = 'EVERY_14_DAYS',
	Every30Days = 'EVERY_30_DAYS',
	Manual = 'MANUAL'
}

export enum JobRunDataPeriods {
	Last7Days = 'LAST_7_DAYS',
	Last30Days = 'LAST_30_DAYS',
	Last180Days = 'LAST_180_DAYS',
	Last365Days = 'LAST_365_DAYS'
}

export enum JobTypes {
	ItemSimilarity = 'CONTENT_RECOMMENDATION_ITEM_SIMILARITY'
}

/**
 * FaroConstants
 */

export enum ActivityActions {
	Comments = 3,
	Downloads = 0,
	Previews = 4,
	Submissions = 1,
	Visits = 2
}

export enum Applications {
	Contacts = 'contacts',
	Main = 'main'
}

export enum ChannelPermissionTypes {
	AllUsers = 0,
	SelectUsers = 1
}

export enum DataSourceDisplayStatuses {
	Active = 'ACTIVE',
	Configuring = 'CONFIGURING',
	DeleteError = 'DELETE_ERROR',
	InDeletion = 'IN_DELETION',
	Inactive = 'INACTIVE'
}

export enum DataSourceProgressStatuses {
	Completed = 'COMPLETED',
	Failed = 'FAILED',
	InProgress = 'IN_PROGRESS',
	Started = 'STARTED'
}

export enum DataSourceStatuses {
	Active = 'ACTIVE',
	Inactive = 'INACTIVE'
}

export enum DataSourceTypes {
	Csv = 'CSV',
	Liferay = 'LIFERAY',
	Salesforce = 'SALESFORCE'
}

export enum EntityTypes {
	Account = 0,
	AccountsSegment = 3,
	Asset = 5,
	DataSource = 1,
	Individual = 2,
	IndividualsSegment = 4,
	Page = 6
}

export enum FieldContexts {
	Custom = 'custom',
	Demographics = 'demographics',
	Interests = 'interests',
	Organization = 'organization'
}

export enum SessionEntityTypes {
	Account = 'ACCOUNT',
	Individual = 'INDIVIDUAL'
}

export enum FieldOwnerTypes {
	Account = 'account',
	Individual = 'individual',
	Organization = 'organization'
}

export enum FieldTypes {
	Boolean = 'Boolean',
	Date = 'Date',
	Number = 'Number',
	String = 'Text'
}

export enum OrderByDirections {
	Ascending = 'ASC',
	Descending = 'DESC'
}

export enum PreferencesScopes {
	User = 'user',
	Group = 'group'
}

export enum ProjectStates {
	Activating = 'ACTIVATING',
	AutoRedeployFailed = 'AUTO_REDEPLOY_FAILED',
	Deactivated = 'DEACTIVATED',
	Maintenance = 'MAINTENANCE',
	NotReady = 'NOT READY',
	Ready = 'READY',
	Scheduled = 'SCHEDULED',
	Unavailable = 'UNAVAILABLE',
	Unconfigured = 'UNCONFIGURED'
}

export enum SegmentStates {
	Disabled = 'DISABLED',
	InProgress = 'IN_PROGRESS',
	Ready = 'READY'
}

export enum SegmentTypes {
	Dynamic = 'DYNAMIC',
	Static = 'STATIC'
}

export enum Sizes {
	Small = 'sm',
	Medium = 'md',
	Large = 'lg',
	XLarge = 'xl',
	XXLarge = 'xxl',
	XXXLarge = 'xxxl'
}

export enum SubscriptionStatuses {
	Approaching = 1,
	Ok = 0,
	Over = 2
}

export enum TimeIntervals {
	Day = 'day',
	Month = 'month',
	Quarter = 'quarter',
	Week = 'week',
	Year = 'year'
}

export enum TimeSpans {
	AllTime = 'ever',
	LastYear = 'lastYear',
	Last24Hours = 'last24Hours',
	Last7Days = 'last7Days',
	Last28Days = 'last28Days',
	Last30Days = 'last30Days',
	Last90Days = 'last90Days',
	Today = 'today',
	Yesterday = 'yesterday'
}

export enum UserRoleNames {
	Administrator = 'Site Administrator',
	Member = 'Site Member',
	Owner = 'Site Owner'
}

const Constants: Window['faroConstants'] = window.faroConstants;

export default Constants;
