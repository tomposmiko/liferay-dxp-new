create table AssetDisplayPageEntry (
	assetDisplayPageEntryId LONG not null primary key,
	assetEntryId LONG,
	layoutPageTemplateEntryId LONG,
	type_ INTEGER
);