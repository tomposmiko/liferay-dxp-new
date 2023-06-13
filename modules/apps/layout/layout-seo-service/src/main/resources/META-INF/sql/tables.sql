create table LayoutSEOEntry (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	layoutSEOEntryId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	privateLayout BOOLEAN,
	layoutId LONG,
	canonicalURL STRING null,
	canonicalURLEnabled BOOLEAN,
	DDMStorageId LONG,
	openGraphDescription STRING null,
	openGraphDescriptionEnabled BOOLEAN,
	openGraphImageAlt STRING null,
	openGraphImageFileEntryId LONG,
	openGraphTitle STRING null,
	openGraphTitleEnabled BOOLEAN,
	lastPublishDate DATE null
);

create table LayoutSEOSite (
	mvccVersion LONG default 0 not null,
	uuid_ VARCHAR(75) null,
	layoutSEOSiteId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	openGraphEnabled BOOLEAN,
	openGraphImageAlt STRING null,
	openGraphImageFileEntryId LONG
);