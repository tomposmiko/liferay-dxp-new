create table AccountEntry (
	mvccVersion LONG default 0 not null,
	externalReferenceCode VARCHAR(75) null,
	accountEntryId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentAccountEntryId LONG,
	name VARCHAR(100) null,
	description STRING null,
	domains STRING null,
	logoId LONG,
	taxIdNumber VARCHAR(75) null,
	type_ VARCHAR(75) null,
	status INTEGER
);

create table AccountEntryOrganizationRel (
	mvccVersion LONG default 0 not null,
	accountEntryOrganizationRelId LONG not null primary key,
	companyId LONG,
	accountEntryId LONG,
	organizationId LONG
);

create table AccountEntryUserRel (
	mvccVersion LONG default 0 not null,
	accountEntryUserRelId LONG not null primary key,
	companyId LONG,
	accountEntryId LONG,
	accountUserId LONG
);

create table AccountGroup (
	mvccVersion LONG default 0 not null,
	externalReferenceCode VARCHAR(75) null,
	accountGroupId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	description VARCHAR(75) null
);

create table AccountGroupAccountEntryRel (
	mvccVersion LONG default 0 not null,
	AccountGroupAccountEntryRelId LONG not null primary key,
	companyId LONG,
	accountGroupId LONG,
	accountEntryId LONG
);

create table AccountRole (
	mvccVersion LONG default 0 not null,
	accountRoleId LONG not null primary key,
	companyId LONG,
	accountEntryId LONG,
	roleId LONG
);