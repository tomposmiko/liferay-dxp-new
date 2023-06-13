create unique index IX_63EAFC82 on LayoutClassedModelUsage (classNameId, classPK, containerKey[$COLUMN_LENGTH:200$], containerType, plid, ctCollectionId);
create index IX_2644723E on LayoutClassedModelUsage (classNameId, classPK, ctCollectionId);
create index IX_C03C3E53 on LayoutClassedModelUsage (classNameId, classPK, type_, ctCollectionId);
create index IX_60CAED43 on LayoutClassedModelUsage (classNameId, companyId, ctCollectionId);
create index IX_D53CA505 on LayoutClassedModelUsage (classNameId, groupId, ctCollectionId);
create index IX_148A4E98 on LayoutClassedModelUsage (companyId, classNameId, containerType, ctCollectionId);
create index IX_C5027D4F on LayoutClassedModelUsage (companyId, classNameId, ctCollectionId);
create index IX_F1220AB7 on LayoutClassedModelUsage (containerKey[$COLUMN_LENGTH:200$], containerType, plid, ctCollectionId);
create index IX_C6AEACD on LayoutClassedModelUsage (groupId, classNameId, ctCollectionId);
create index IX_A3F4F834 on LayoutClassedModelUsage (plid, ctCollectionId);
create index IX_B0FEE15D on LayoutClassedModelUsage (uuid_[$COLUMN_LENGTH:75$], companyId, ctCollectionId);
create index IX_ACA55FE7 on LayoutClassedModelUsage (uuid_[$COLUMN_LENGTH:75$], ctCollectionId);
create unique index IX_8A32D79F on LayoutClassedModelUsage (uuid_[$COLUMN_LENGTH:75$], groupId, ctCollectionId);

create unique index IX_E26CFF79 on LayoutLocalization (languageId[$COLUMN_LENGTH:75$], plid, ctCollectionId);
create index IX_1E12A3F2 on LayoutLocalization (plid, ctCollectionId);
create index IX_7232E01B on LayoutLocalization (uuid_[$COLUMN_LENGTH:75$], companyId, ctCollectionId);
create index IX_763D2BE9 on LayoutLocalization (uuid_[$COLUMN_LENGTH:75$], ctCollectionId);
create unique index IX_9336DDD on LayoutLocalization (uuid_[$COLUMN_LENGTH:75$], groupId, ctCollectionId);