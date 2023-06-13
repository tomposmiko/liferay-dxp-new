create index IX_42E60133 on NQueueEntryAttachment (notificationQueueEntryId);

create unique index IX_8F1205E1 on NTemplateAttachment (notificationTemplateId, objectFieldId);

create index IX_943FC830 on NotificationQueueEntry (companyId, sentDate);
create index IX_2EBDE225 on NotificationQueueEntry (companyId, type_[$COLUMN_LENGTH:75$], status);
create index IX_83DBCE06 on NotificationQueueEntry (notificationTemplateId);
create index IX_3B9F9C6C on NotificationQueueEntry (sentDate);
create index IX_4A9516F8 on NotificationQueueEntry (status);
create index IX_74855369 on NotificationQueueEntry (type_[$COLUMN_LENGTH:75$], status);

create index IX_470340CF on NotificationRecipient (classPK);
create index IX_827DDE88 on NotificationRecipient (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_B6D4DBB0 on NotificationRecipientSetting (notificationRecipientId, name[$COLUMN_LENGTH:75$]);
create index IX_8A397C5C on NotificationRecipientSetting (uuid_[$COLUMN_LENGTH:75$], companyId);

create unique index IX_66991536 on NotificationTemplate (externalReferenceCode[$COLUMN_LENGTH:75$], companyId);
create index IX_AE7F6E5F on NotificationTemplate (uuid_[$COLUMN_LENGTH:75$], companyId);