create index IX_F7FFBCCA on CDiscountCAccountGroupRel (commerceAccountGroupId);
create unique index IX_9D768AF5 on CDiscountCAccountGroupRel (commerceDiscountId, commerceAccountGroupId);

create unique index IX_A7A710FC on CommerceDiscount (companyId, couponCode[$COLUMN_LENGTH:75$], active_);
create index IX_D294CDB7 on CommerceDiscount (companyId, externalReferenceCode[$COLUMN_LENGTH:75$]);
create index IX_122C15C4 on CommerceDiscount (displayDate, status);
create index IX_2FBF0739 on CommerceDiscount (expirationDate, status);
create index IX_687F1796 on CommerceDiscount (uuid_[$COLUMN_LENGTH:75$], companyId);

create unique index IX_E082887A on CommerceDiscountAccountRel (commerceAccountId, commerceDiscountId);
create index IX_6EA2AA99 on CommerceDiscountAccountRel (commerceDiscountId);
create index IX_D365BDE2 on CommerceDiscountAccountRel (uuid_[$COLUMN_LENGTH:75$], companyId);

create index IX_6B4EEC38 on CommerceDiscountRel (classNameId, classPK);
create index IX_DDFDEF40 on CommerceDiscountRel (commerceDiscountId, classNameId);

create index IX_CB9E6769 on CommerceDiscountRule (commerceDiscountId);

create index IX_B0E2A9F5 on CommerceDiscountUsageEntry (commerceAccountId, commerceDiscountId);
create index IX_70440FFF on CommerceDiscountUsageEntry (commerceAccountId, commerceOrderId, commerceDiscountId);
create index IX_EDFADCFE on CommerceDiscountUsageEntry (commerceDiscountId);
create index IX_FF593016 on CommerceDiscountUsageEntry (commerceOrderId, commerceDiscountId);