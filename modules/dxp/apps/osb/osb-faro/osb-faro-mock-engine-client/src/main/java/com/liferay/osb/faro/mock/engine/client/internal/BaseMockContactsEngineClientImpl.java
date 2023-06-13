/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.mock.engine.client.internal;

import com.liferay.osb.faro.engine.client.BaseEngineClient;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.exception.FaroEngineClientException;
import com.liferay.osb.faro.engine.client.model.Account;
import com.liferay.osb.faro.engine.client.model.Activity;
import com.liferay.osb.faro.engine.client.model.ActivityAggregation;
import com.liferay.osb.faro.engine.client.model.ActivityAsset;
import com.liferay.osb.faro.engine.client.model.ActivityGroup;
import com.liferay.osb.faro.engine.client.model.Asset;
import com.liferay.osb.faro.engine.client.model.Author;
import com.liferay.osb.faro.engine.client.model.BlockedKeyword;
import com.liferay.osb.faro.engine.client.model.Channel;
import com.liferay.osb.faro.engine.client.model.Credentials;
import com.liferay.osb.faro.engine.client.model.DXPGroup;
import com.liferay.osb.faro.engine.client.model.DXPOrganization;
import com.liferay.osb.faro.engine.client.model.DXPUserGroup;
import com.liferay.osb.faro.engine.client.model.DataSource;
import com.liferay.osb.faro.engine.client.model.DataSourceField;
import com.liferay.osb.faro.engine.client.model.DataSourceProgress;
import com.liferay.osb.faro.engine.client.model.Distribution;
import com.liferay.osb.faro.engine.client.model.Event;
import com.liferay.osb.faro.engine.client.model.Field;
import com.liferay.osb.faro.engine.client.model.FieldMapping;
import com.liferay.osb.faro.engine.client.model.FieldMappingMap;
import com.liferay.osb.faro.engine.client.model.Individual;
import com.liferay.osb.faro.engine.client.model.IndividualSegment;
import com.liferay.osb.faro.engine.client.model.IndividualSegmentMembership;
import com.liferay.osb.faro.engine.client.model.IndividualSegmentMembershipChange;
import com.liferay.osb.faro.engine.client.model.IndividualSegmentMembershipChangeAggregation;
import com.liferay.osb.faro.engine.client.model.IndividualTransformation;
import com.liferay.osb.faro.engine.client.model.Interest;
import com.liferay.osb.faro.engine.client.model.PageVisited;
import com.liferay.osb.faro.engine.client.model.Provider;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.engine.client.model.provider.LiferayProvider;
import com.liferay.osb.faro.engine.client.util.FilterBuilder;
import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.model.FaroProject;

import java.io.OutputStream;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
public abstract class BaseMockContactsEngineClientImpl
	extends BaseEngineClient implements ContactsEngineClient {

	@Override
	public Results<BlockedKeyword> addBlockedKeywords(
		FaroProject faroProject, List<String> keywords) {

		return contactsEngineClient.addBlockedKeywords(faroProject, keywords);
	}

	@Override
	public Channel addChannel(FaroProject faroProject, Channel channel) {
		return contactsEngineClient.addChannel(faroProject, channel);
	}

	@Override
	public void addCSVIndividuals(
			FaroProject faroProject, List<Map<String, Object>> fieldsMaps,
			String dataSourceId, List<String> individualSegmentIds)
		throws Exception {

		contactsEngineClient.addCSVIndividuals(
			faroProject, fieldsMaps, dataSourceId, individualSegmentIds);
	}

	@Override
	public void addData(
		FaroProject faroProject, String weDeployDataServiceName,
		String collectionName, List<Map<String, Object>> objects) {

		contactsEngineClient.addData(
			faroProject, weDeployDataServiceName, collectionName, objects);
	}

	@Override
	public DataSource addDataSource(
		FaroProject faroProject, Credentials credentials, Author author,
		String name, String url, Provider provider, Event event,
		String status) {

		return contactsEngineClient.addDataSource(
			faroProject, credentials, author, name, url, provider, event,
			status);
	}

	@Override
	public DataSource addDataSource(
		FaroProject faroProject, Credentials credentials, long userId,
		String name, String url, Provider provider, Event event,
		String status) {

		return contactsEngineClient.addDataSource(
			faroProject, credentials, userId, name, url, provider, event,
			status);
	}

	@Override
	public FieldMapping addFieldMapping(
		FaroProject faroProject, String context,
		Map<String, String> dataSourceFieldNames, String fieldName,
		String fieldType, String ownerType, Boolean repeatable) {

		return contactsEngineClient.addFieldMapping(
			faroProject, context, dataSourceFieldNames, fieldName, fieldType,
			ownerType, repeatable);
	}

	@Override
	public List<FieldMapping> addFieldMappings(
		FaroProject faroProject, String dataSourceId, String context,
		String ownerType, List<FieldMappingMap> fieldMappingMaps) {

		return contactsEngineClient.addFieldMappings(
			faroProject, dataSourceId, context, ownerType, fieldMappingMaps);
	}

	@Override
	public IndividualSegment addIndividualSegment(
		FaroProject faroProject, long userId, String channelId, String filter,
		boolean includeAnonymousUsers, String name, String segmentType,
		String status) {

		return contactsEngineClient.addIndividualSegment(
			faroProject, userId, channelId, filter, includeAnonymousUsers, name,
			segmentType, status);
	}

	@Override
	public IndividualSegmentMembership addMembership(
		FaroProject faroProject, String individualSegmentId,
		String individualId) {

		return contactsEngineClient.addMembership(
			faroProject, individualSegmentId, individualId);
	}

	@Override
	public void addMemberships(
		FaroProject faroProject, String individualSegmentId,
		List<String> individualIds) {

		contactsEngineClient.addMemberships(
			faroProject, individualSegmentId, individualIds);
	}

	@Override
	public void addNanite(
		FaroProject faroProject, String className,
		Map<String, Object> context) {

		contactsEngineClient.addNanite(faroProject, className, context);
	}

	@Override
	public void addNanites(FaroProject faroProject, List<String> classNames) {
		contactsEngineClient.addNanites(faroProject, classNames);
	}

	@Override
	public String addProject(FaroProject faroProject) throws Exception {
		return contactsEngineClient.addProject(faroProject);
	}

	@Override
	public void assignChannelToIndividualSegment(
		FaroProject faroProject, String individualSegmentId, String channelId) {

		contactsEngineClient.assignChannelToIndividualSegment(
			faroProject, individualSegmentId, channelId);
	}

	@Override
	public void clearChannel(FaroProject faroProject, List<String> ids) {
		contactsEngineClient.clearChannel(faroProject, ids);
	}

	@Override
	public void deleteBlockedKeywords(FaroProject faroProject, List<String> ids)
		throws FaroEngineClientException {

		contactsEngineClient.deleteBlockedKeywords(faroProject, ids);
	}

	@Override
	public void deleteChannels(FaroProject faroProject, List<String> ids) {
		contactsEngineClient.deleteChannels(faroProject, ids);
	}

	@Override
	public void deleteData(
		FaroProject faroProject, String weDeployDataServiceName,
		String collectionName) {

		contactsEngineClient.deleteData(
			faroProject, weDeployDataServiceName, collectionName);
	}

	@Override
	public void deleteDataSource(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		contactsEngineClient.deleteDataSource(faroProject, id);
	}

	@Override
	public void deleteFieldMapping(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		contactsEngineClient.deleteFieldMapping(faroProject, id);
	}

	@Override
	public void deleteFields(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		contactsEngineClient.deleteFields(faroProject, id);
	}

	@Override
	public void deleteIndividualSegment(FaroProject faroProject, String id)
		throws Exception {

		contactsEngineClient.deleteIndividualSegment(faroProject, id);
	}

	@Override
	public void deleteMembership(
		FaroProject faroProject, String individualSegmentId,
		String individualId) {

		contactsEngineClient.deleteMembership(
			faroProject, individualSegmentId, individualId);
	}

	@Override
	public void deleteProject(FaroProject faroProject, boolean deleteData)
		throws Exception {

		contactsEngineClient.deleteProject(faroProject, deleteData);
	}

	@Override
	public void disconnectDataSource(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		contactsEngineClient.disconnectDataSource(faroProject, id);
	}

	@Override
	public <T> T get(
			FaroProject faroProject, Map<String, String> headers, String path,
			Map<String, List<String>> queryParameters, Class<T> returnType)
		throws Exception {

		return contactsEngineClient.get(
			faroProject, headers, path, queryParameters, returnType);
	}

	@Override
	public Account getAccount(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		return contactsEngineClient.getAccount(faroProject, id);
	}

	@Override
	public Results<IndividualSegment> getAccountIndividualSegments(
		FaroProject faroProject, String accountId, String channelId,
		String query, String status, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getAccountIndividualSegments(
			faroProject, accountId, channelId, query, status, cur, delta,
			orderByFields);
	}

	@Override
	public Results<Account> getAccounts(
		FaroProject faroProject, String channelId, String dataSourceId,
		String individualSegmentId, String filter, String query,
		List<String> fields, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getAccounts(
			faroProject, channelId, dataSourceId, individualSegmentId, filter,
			query, fields, cur, delta, orderByFields);
	}

	@Override
	public Results<Distribution> getAccountsDistribution(
		FaroProject faroProject, String channelId, String fieldMappingFieldName,
		String filter, String individualSegmentId, int count, int numberOfBins,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getAccountsDistribution(
			faroProject, channelId, fieldMappingFieldName, filter,
			individualSegmentId, count, numberOfBins, orderByFields);
	}

	@Override
	public Results<Activity> getActivities(
		FaroProject faroProject, String ownerId, String ownerType,
		String groupId, String query, Date startDate, Date endDate, int action,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getActivities(
			faroProject, ownerId, ownerType, groupId, query, startDate, endDate,
			action, cur, delta, orderByFields);
	}

	@Override
	public List<List<Activity>> getActivitiesList(
		FaroProject faroProject, List<String> groupIds, String query,
		int action, int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getActivitiesList(
			faroProject, groupIds, query, action, cur, delta, orderByFields);
	}

	@Override
	public Activity getActivity(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		return contactsEngineClient.getActivity(faroProject, id);
	}

	@Override
	public Results<ActivityAggregation> getActivityAggregations(
		FaroProject faroProject, String channelId, String ownerId,
		String ownerType, String rangeEnd, String rangeStart, String interval,
		int delta) {

		return contactsEngineClient.getActivityAggregations(
			faroProject, channelId, ownerId, ownerType, rangeEnd, rangeStart,
			interval, delta);
	}

	@Override
	public Results<ActivityAsset> getActivityAssets(
		FaroProject faroProject, String query, String applicationId,
		String channelId, String eventId, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getActivityAssets(
			faroProject, query, applicationId, channelId, eventId, cur, delta,
			orderByFields);
	}

	@Override
	public Results<ActivityGroup> getActivityGroups(
		FaroProject faroProject, String channelId, String ownerId,
		String ownerType, String query, Date startDate, Date endDate, int cur,
		int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getActivityGroups(
			faroProject, channelId, ownerId, ownerType, query, startDate,
			endDate, cur, delta, orderByFields);
	}

	@Override
	public Asset getAsset(FaroProject faroProject, String id) {
		return contactsEngineClient.getAsset(faroProject, id);
	}

	@Override
	public Results<Asset> getAssets(
		FaroProject faroProject, String dataSourceId, String query, int action,
		String assetType, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getAssets(
			faroProject, dataSourceId, query, action, assetType, cur, delta,
			orderByFields);
	}

	@Override
	public DataSource getAvailableTokenDataSource(FaroProject faroProject) {
		return contactsEngineClient.getAvailableTokenDataSource(faroProject);
	}

	@Override
	public BlockedKeyword getBlockedKeyword(
		FaroProject faroProject, String id) {

		return contactsEngineClient.getBlockedKeyword(faroProject, id);
	}

	@Override
	public Results<BlockedKeyword> getBlockedKeywords(
		FaroProject faroProject, String query, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getBlockedKeywords(
			faroProject, query, cur, delta, orderByFields);
	}

	@Override
	public Channel getChannel(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		return contactsEngineClient.getChannel(faroProject, id);
	}

	@Override
	public Results<Channel> getChannels(
		FaroProject faroProject, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getChannels(
			faroProject, cur, delta, orderByFields);
	}

	@Override
	public Results<Individual> getCoworkerIndividuals(
		FaroProject faroProject, String individualId, String query,
		List<String> fields, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getCoworkerIndividuals(
			faroProject, individualId, query, fields, cur, delta,
			orderByFields);
	}

	@Override
	public DataSource getDataSource(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		return contactsEngineClient.getDataSource(faroProject, id);
	}

	@Override
	public List<DXPGroup> getDataSourceDXPGroups(
		FaroProject faroProject, String id, List<Long> groupIds) {

		return contactsEngineClient.getDataSourceDXPGroups(
			faroProject, id, groupIds);
	}

	@Override
	public Results<DXPGroup> getDataSourceDXPGroups(
		FaroProject faroProject, String id, long parentGroupId, boolean site,
		String name, int cur, int delta) {

		return contactsEngineClient.getDataSourceDXPGroups(
			faroProject, id, parentGroupId, site, name, cur, delta);
	}

	@Override
	public List<DXPOrganization> getDataSourceDXPOrganizations(
		FaroProject faroProject, String id, List<Long> organizationIds) {

		return contactsEngineClient.getDataSourceDXPOrganizations(
			faroProject, id, organizationIds);
	}

	@Override
	public Results<DXPOrganization> getDataSourceDXPOrganizations(
		FaroProject faroProject, String id, long parentOrganizationId,
		String name, int cur, int delta) {

		return contactsEngineClient.getDataSourceDXPOrganizations(
			faroProject, id, parentOrganizationId, name, cur, delta);
	}

	@Override
	public long getDataSourceDXPTotal(FaroProject faroProject, String id) {
		return contactsEngineClient.getDataSourceDXPTotal(faroProject, id);
	}

	@Override
	public long getDataSourceDXPTotal(
		FaroProject faroProject, String id,
		LiferayProvider.ContactsConfiguration contactsConfiguration) {

		return contactsEngineClient.getDataSourceDXPTotal(
			faroProject, id, contactsConfiguration);
	}

	@Override
	public List<DXPUserGroup> getDataSourceDXPUserGroups(
		FaroProject faroProject, String id, List<Long> userGroupIds) {

		return contactsEngineClient.getDataSourceDXPUserGroups(
			faroProject, id, userGroupIds);
	}

	@Override
	public Results<DXPUserGroup> getDataSourceDXPUserGroups(
		FaroProject faroProject, String id, String name, int cur, int delta) {

		return contactsEngineClient.getDataSourceDXPUserGroups(
			faroProject, id, name, cur, delta);
	}

	@Override
	public List<DataSourceField> getDataSourceFields(
		FaroProject faroProject, String id, String context, int count) {

		return contactsEngineClient.getDataSourceFields(
			faroProject, id, context, count);
	}

	@Override
	public Map<String, DataSourceProgress> getDataSourceProgressMap(
		FaroProject faroProject, String id) {

		return contactsEngineClient.getDataSourceProgressMap(faroProject, id);
	}

	@Override
	public Results<DataSource> getDataSources(
		FaroProject faroProject, List<String> channelIds) {

		return contactsEngineClient.getDataSources(faroProject, channelIds);
	}

	@Override
	public Results<DataSource> getDataSources(
		FaroProject faroProject, String faroEntityId, String query, String name,
		String providerType, List<String> states, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getDataSources(
			faroProject, faroEntityId, query, name, providerType, states, cur,
			delta, orderByFields);
	}

	@Override
	public Long getEnrichedProfilesCount(
		FaroProject faroProject, Long channelId) {

		return contactsEngineClient.getEnrichedProfilesCount(
			faroProject, channelId);
	}

	@Override
	public Field getField(FaroProject faroProject, String id)
		throws FaroEngineClientException {

		return contactsEngineClient.getField(faroProject, id);
	}

	@Override
	public FieldMapping getFieldMapping(FaroProject faroProject, String id) {
		return contactsEngineClient.getFieldMapping(faroProject, id);
	}

	@Override
	public FieldMapping getFieldMapping(
		FaroProject faroProject, String context, String fieldName) {

		return contactsEngineClient.getFieldMapping(
			faroProject, context, fieldName);
	}

	@Override
	public Results<FieldMapping> getFieldMappings(
		FaroProject faroProject, String context, List<String> fieldNames,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getFieldMappings(
			faroProject, context, fieldNames, cur, delta, orderByFields);
	}

	@Override
	public Results<FieldMapping> getFieldMappings(
		FaroProject faroProject, String context, String dataSourceId,
		String dataSourceFieldName) {

		return contactsEngineClient.getFieldMappings(
			faroProject, context, dataSourceId, dataSourceFieldName);
	}

	@Override
	public Results<FieldMapping> getFieldMappings(
		FaroProject faroProject, String context, String displayName,
		String ownerType, String query, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getFieldMappings(
			faroProject, context, displayName, ownerType, query, cur, delta,
			orderByFields);
	}

	@Override
	public List<String> getFieldNames(
		FaroProject faroProject, String label, String ownerType,
		Object values) {

		return contactsEngineClient.getFieldNames(
			faroProject, label, ownerType, values);
	}

	@Override
	public List<List<String>> getFieldNamesList(
		FaroProject faroProject, List<String> labels, String ownerType,
		List<Object> valuesList) {

		return contactsEngineClient.getFieldNamesList(
			faroProject, labels, ownerType, valuesList);
	}

	@Override
	public Results<Field> getFields(
		FaroProject faroProject, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getFields(
			faroProject, cur, delta, orderByFields);
	}

	@Override
	public Results<Field> getFields(
		FaroProject faroProject, String context, String name, int cur,
		int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getFields(
			faroProject, context, name, cur, delta, orderByFields);
	}

	@Override
	public Results<Field> getFields(
		FaroProject faroProject, String context, String name, String ownerId,
		String ownerType, Date startDate, Date endDate, int interval,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getFields(
			faroProject, context, name, ownerId, ownerType, startDate, endDate,
			interval, orderByFields);
	}

	@Override
	public List<List<Field>> getFieldsList(
		FaroProject faroProject, String context, List<String> names, int cur,
		int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getFieldsList(
			faroProject, context, names, cur, delta, orderByFields);
	}

	@Override
	public Results<Object> getFieldValues(
		FaroProject faroProject, Long channelId, String query,
		String fieldMappingFieldName, int cur, int delta) {

		return contactsEngineClient.getFieldValues(
			faroProject, channelId, query, fieldMappingFieldName, cur, delta);
	}

	@Override
	public Individual getIndividual(
			FaroProject faroProject, String id, String channelId)
		throws FaroEngineClientException {

		return contactsEngineClient.getIndividual(faroProject, id, channelId);
	}

	@Override
	public List<FieldMapping> getIndividualAttributes(
		FaroProject faroProject, String displayName) {

		return contactsEngineClient.getIndividualAttributes(
			faroProject, displayName);
	}

	@Override
	public Results<IndividualSegment> getIndividualIndividualSegments(
		FaroProject faroProject, String channelId, String individualId,
		String query, String status, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualIndividualSegments(
			faroProject, channelId, individualId, query, status, cur, delta,
			orderByFields);
	}

	@Override
	public Results<Individual> getIndividuals(
		FaroProject faroProject, FilterBuilder filterBuilder,
		boolean includeAnonymousUsers, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividuals(
			faroProject, filterBuilder, includeAnonymousUsers, cur, delta,
			orderByFields);
	}

	@Override
	public Results<Individual> getIndividuals(
		FaroProject faroProject, String dataSourceId,
		boolean includeAnonymousUsers, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividuals(
			faroProject, dataSourceId, includeAnonymousUsers, cur, delta,
			orderByFields);
	}

	@Override
	public Results<Individual> getIndividuals(
		FaroProject faroProject, String accountId, String channelId,
		String dataSourceId, String individualSegmentId,
		String notIndividualSegmentId, String interestName, String filter,
		String query, List<String> fields, boolean includeAnonymousUsers,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividuals(
			faroProject, accountId, channelId, dataSourceId,
			individualSegmentId, notIndividualSegmentId, interestName, filter,
			query, fields, includeAnonymousUsers, cur, delta, orderByFields);
	}

	@Override
	public Results<Individual> getIndividualsByIndividualSegment(
		FaroProject faroProject, String individualSegmentsId, String query,
		List<String> fields, FilterBuilder filterBuilder,
		boolean includeAnonymousUsers, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualsByIndividualSegment(
			faroProject, individualSegmentsId, query, fields, filterBuilder,
			includeAnonymousUsers, cur, delta, orderByFields);
	}

	@Override
	public Results<Individual> getIndividualsByIndividualSegment(
		FaroProject faroProject, String individualSegmentId, String filter,
		String query, List<String> fields, boolean includeAnonymousUsers,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualsByIndividualSegment(
			faroProject, individualSegmentId, filter, query, fields,
			includeAnonymousUsers, cur, delta, orderByFields);
	}

	@Override
	public Results<Distribution> getIndividualsDistribution(
		FaroProject faroProject, String channelId, String fieldMappingFieldName,
		String individualSegmentId, int count, int numberOfBins,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualsDistribution(
			faroProject, channelId, fieldMappingFieldName, individualSegmentId,
			count, numberOfBins, orderByFields);
	}

	@Override
	public IndividualSegment getIndividualSegment(
			FaroProject faroProject, String id,
			boolean includeReferencedObjects)
		throws FaroEngineClientException {

		return contactsEngineClient.getIndividualSegment(
			faroProject, id, includeReferencedObjects);
	}

	@Override
	public IndividualSegmentMembership getIndividualSegmentMembership(
		FaroProject faroProject, String individualSegmentId,
		String individualId) {

		return contactsEngineClient.getIndividualSegmentMembership(
			faroProject, individualSegmentId, individualId);
	}

	@Override
	public Results<IndividualSegmentMembershipChangeAggregation>
		getIndividualSegmentMembershipChangeAggregations(
			FaroProject faroProject, String individualSegmentId,
			String interval, int delta) {

		return contactsEngineClient.
			getIndividualSegmentMembershipChangeAggregations(
				faroProject, individualSegmentId, interval, delta);
	}

	@Override
	public Results<IndividualSegmentMembershipChange>
		getIndividualSegmentMembershipChanges(
			FaroProject faroProject, String individualSegmentId, String query,
			Date startDate, Date endDate, int cur, int delta,
			List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualSegmentMembershipChanges(
			faroProject, individualSegmentId, query, startDate, endDate, cur,
			delta, orderByFields);
	}

	@Override
	public Results<IndividualSegmentMembership> getIndividualSegmentMemberships(
		FaroProject faroProject, String individualSegmentId, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualSegmentMemberships(
			faroProject, individualSegmentId, cur, delta, orderByFields);
	}

	@Override
	public Results<IndividualSegment> getIndividualSegments(
		FaroProject faroProject, String channelId, String dataSourceId,
		String query, List<String> fields, String name, String segmentType,
		String state, String status, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualSegments(
			faroProject, channelId, dataSourceId, query, fields, name,
			segmentType, state, status, cur, delta, orderByFields);
	}

	@Override
	public Results<IndividualTransformation> getIndividualTransformations(
		FaroProject faroProject, String individualSegmentId, String query,
		List<String> fields, String fieldMappingFieldName, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getIndividualTransformations(
			faroProject, individualSegmentId, query, fields,
			fieldMappingFieldName, cur, delta, orderByFields);
	}

	@Override
	public Results<String> getInterestKeywords(
		FaroProject faroProject, String query, int cur, int delta) {

		return contactsEngineClient.getInterestKeywords(
			faroProject, query, cur, delta);
	}

	@Override
	public Results<Interest> getInterests(
		FaroProject faroProject, String ownerId, String ownerType, String name,
		String query, Date startDate, Date endDate, String expand, int cur,
		int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getInterests(
			faroProject, ownerId, ownerType, name, query, startDate, endDate,
			expand, cur, delta, orderByFields);
	}

	@Override
	public Interest getLatestInterest(
		FaroProject faroProject, String ownerId, String ownerType, String query,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getLatestInterest(
			faroProject, ownerId, ownerType, query, cur, delta, orderByFields);
	}

	@Override
	public Results<PageVisited> getPagesVisited(
		FaroProject faroProject, String ownerId, String ownerType, String query,
		String interestName, Date startDate, Date endDate, boolean visitedPages,
		int cur, int delta, List<OrderByField> orderByFields) {

		return contactsEngineClient.getPagesVisited(
			faroProject, ownerId, ownerType, query, interestName, startDate,
			endDate, visitedPages, cur, delta, orderByFields);
	}

	@Override
	public PageVisited getPageVisited(FaroProject faroProject, String id) {
		return contactsEngineClient.getPageVisited(faroProject, id);
	}

	@Override
	public Results<String> getSessionValues(
		FaroProject faroProject, String channelId, String fieldName,
		String filter, String query, int cur, int delta) {

		return contactsEngineClient.getSessionValues(
			faroProject, channelId, fieldName, filter, query, cur, delta);
	}

	@Override
	public Results<Individual> getSimilarIndividuals(
		FaroProject faroProject, String individualId, String query,
		List<String> fields, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getSimilarIndividuals(
			faroProject, individualId, query, fields, cur, delta,
			orderByFields);
	}

	@Override
	public void getToOutputStream(
			FaroProject faroProject, Map<String, String> headers, String path,
			Map<String, List<String>> queryParameters,
			OutputStream outputStream)
		throws Exception {

		contactsEngineClient.getToOutputStream(
			faroProject, headers, path, queryParameters, outputStream);
	}

	@Override
	public Results<IndividualSegment> getUnassignedIndividualSegments(
		FaroProject faroProject, int cur, int delta,
		List<OrderByField> orderByFields) {

		return contactsEngineClient.getUnassignedIndividualSegments(
			faroProject, cur, delta, orderByFields);
	}

	@Override
	public Channel patchChannel(
		FaroProject faroProject, String id, String name) {

		return contactsEngineClient.patchChannel(faroProject, id, name);
	}

	@Override
	public Channel patchChannel(
		FaroProject faroProject, String id, String dataSourceId,
		List<Map<String, String>> groups) {

		return contactsEngineClient.patchChannel(
			faroProject, id, dataSourceId, groups);
	}

	@Override
	public DataSource patchDataSource(
		FaroProject faroProject, String id, Credentials credentials,
		long userId, String name, String url, Provider provider, Event event,
		String status) {

		return contactsEngineClient.patchDataSource(
			faroProject, id, credentials, userId, name, url, provider, event,
			status);
	}

	@Override
	public FieldMapping patchFieldMapping(
		FaroProject faroProject, String id, String dataSourceId,
		String fieldName) {

		return contactsEngineClient.patchFieldMapping(
			faroProject, id, dataSourceId, fieldName);
	}

	@Override
	public void patchFieldMappings(
		FaroProject faroProject, String dataSourceId, String context,
		String ownerType, List<FieldMappingMap> fieldMappingMaps) {

		contactsEngineClient.patchFieldMappings(
			faroProject, dataSourceId, context, ownerType, fieldMappingMaps);
	}

	@Override
	public <T> T post(
			FaroProject faroProject, Map<String, String> headers, String path,
			Map<String, List<String>> queryParameters, Object requestBody,
			Class<T> returnType)
		throws Exception {

		return contactsEngineClient.post(
			faroProject, headers, path, queryParameters, requestBody,
			returnType);
	}

	@Override
	public List<Map<String, Object>> refreshLiferay(FaroProject faroProject) {
		return contactsEngineClient.refreshLiferay(faroProject);
	}

	@Override
	public void setEngineURL(String engineURL) {
		super.setEngineURL(engineURL);

		contactsEngineClient.setEngineURL(engineURL);
	}

	@Override
	public DataSource updateDataSource(
		FaroProject faroProject, String id, Credentials credentials,
		long userId, String name, String url, Provider provider, Event event,
		String status) {

		return contactsEngineClient.updateDataSource(
			faroProject, id, credentials, userId, name, url, provider, event,
			status);
	}

	@Override
	public FieldMapping updateFieldMapping(
		FaroProject faroProject, String context,
		Map<String, String> dataSourceFieldNames, String fieldName,
		String fieldType, String ownerType) {

		return contactsEngineClient.updateFieldMapping(
			faroProject, context, dataSourceFieldNames, fieldName, fieldType,
			ownerType);
	}

	@Override
	public IndividualSegment updateIndividualSegment(
		FaroProject faroProject, String id, long userId, String channelId,
		String filter, boolean includeAnonymousUsers, String name,
		String segmentType) {

		return contactsEngineClient.updateIndividualSegment(
			faroProject, id, userId, channelId, filter, includeAnonymousUsers,
			name, segmentType);
	}

	@Reference(
		target = "(component.name=com.liferay.osb.faro.engine.client.internal.ContactsEngineClientImpl)"
	)
	protected ContactsEngineClient contactsEngineClient;

}