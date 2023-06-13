package ${configYAML.apiPackagePath}.internal.resource.${escapedVersion};

<#list allSchemas?keys as schemaName>
	import ${configYAML.apiPackagePath}.dto.${escapedVersion}.${schemaName};
</#list>

import ${configYAML.apiPackagePath}.resource.${escapedVersion}.${schemaName}Resource;

import com.liferay.petra.function.UnsafeBiConsumer;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.filter.ExpressionConvert;
import com.liferay.portal.odata.filter.FilterParser;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.sort.SortField;
import com.liferay.portal.odata.sort.SortParser;
import com.liferay.portal.odata.sort.SortParserProvider;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegate;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineExportTaskResource;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.multipart.MultipartBody;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.permission.ModelPermissionsUtil;
import com.liferay.portal.vulcan.permission.Permission;
import com.liferay.portal.vulcan.permission.PermissionUtil;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.LocalDateTimeUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author ${configYAML.author}
 * @generated
 */
@Generated("")
<#if configYAML.application??>
	@javax.ws.rs.Path("/${openAPIYAML.info.version}")
</#if>
public abstract class Base${schemaName}ResourceImpl
	implements ${schemaName}Resource

	<#assign
		javaDataType = freeMarkerTool.getJavaDataType(configYAML, openAPIYAML, schemaName)!""
		javaMethodSignatures = freeMarkerTool.getResourceJavaMethodSignatures(configYAML, openAPIYAML, schemaName)
		generateBatch = freeMarkerTool.generateBatch(configYAML, javaDataType, javaMethodSignatures, schemaName)
	/>

	<#if generateBatch>
		, EntityModelResource, VulcanBatchEngineTaskItemDelegate<${javaDataType}>
	</#if>

	{

	<#assign
		generateGetPermissionCheckerMethods = false
		generatePatchMethods = false
		getParentBatchJavaMethodSignatures = []
		postParentBatchJavaMethodSignatures = []
	/>

	<#list javaMethodSignatures as javaMethodSignature>
		<#assign
			parentSchemaName = javaMethodSignature.parentSchemaName!
		/>

		<#if stringUtil.equals(javaMethodSignature.methodName, "delete" + schemaName)>
			<#assign deleteBatchJavaMethodSignature = javaMethodSignature />
		<#elseif stringUtil.equals(javaMethodSignature.methodName, "get" + parentSchemaName + schemaName + "sPage")>
			<#if stringUtil.equals(javaMethodSignature.methodName, "getAssetLibrary" + schemaName + "sPage")>
				<#assign getAssetLibraryBatchJavaMethodSignature = javaMethodSignature />
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "getSite" + schemaName + "sPage")>
				<#assign getSiteBatchJavaMethodSignature = javaMethodSignature />
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "get" + parentSchemaName + schemaName + "sPage")>
				<#if parentSchemaName?has_content>
					<#assign getParentBatchJavaMethodSignatures = getParentBatchJavaMethodSignatures + [javaMethodSignature] />
				<#else>
					<#assign getBatchJavaMethodSignature = javaMethodSignature />
				</#if>
			</#if>
		<#elseif stringUtil.equals(javaMethodSignature.methodName, "patch" + schemaName)>
			<#assign patchBatchJavaMethodSignature = javaMethodSignature />
		<#elseif stringUtil.equals(javaMethodSignature.methodName, "post" + parentSchemaName + schemaName)>
			<#if stringUtil.equals(javaMethodSignature.methodName, "postAssetLibrary" + schemaName)>
				<#assign postAssetLibraryBatchJavaMethodSignature = javaMethodSignature />
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "postSite" + schemaName)>
				<#assign postSiteBatchJavaMethodSignature = javaMethodSignature />
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "post" + parentSchemaName + schemaName)>
				<#if parentSchemaName?has_content>
					<#assign postParentBatchJavaMethodSignatures = postParentBatchJavaMethodSignatures + [javaMethodSignature] />
				<#else>
					<#assign postBatchJavaMethodSignature = javaMethodSignature />
				</#if>
			</#if>
		<#elseif stringUtil.equals(javaMethodSignature.methodName, "put" + schemaName)>
			<#assign putBatchJavaMethodSignature = javaMethodSignature />
		<#elseif stringUtil.equals(javaMethodSignature.methodName, "putByExternalReferenceCode") || stringUtil.equals(javaMethodSignature.methodName, "put" + parentSchemaName + schemaName + "ByExternalReferenceCode")>
			<#assign putByERCBatchJavaMethodSignature = javaMethodSignature />
		</#if>

		<#if configYAML.application??>
			/**
			* ${freeMarkerTool.getRESTMethodJavadoc(configYAML, javaMethodSignature, openAPIYAML)}
			*/
		</#if>
		@Override
		${freeMarkerTool.getResourceMethodAnnotations(javaMethodSignature)}
		public ${javaMethodSignature.returnType} ${javaMethodSignature.methodName}(${freeMarkerTool.getResourceParameters(javaMethodSignature.javaMethodParameters, openAPIYAML, javaMethodSignature.operation, true)}) throws Exception {
			<#if stringUtil.equals(javaMethodSignature.returnType, "boolean")>
				return false;
			<#elseif generateBatch && stringUtil.equals(javaMethodSignature.methodName, "delete" + schemaName + "Batch")>
				vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(contextAcceptLanguage);
				vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
				vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(contextHttpServletRequest);
				vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
				vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

				javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.accepted();

				return responseBuilder.entity(
					vulcanBatchEngineImportTaskResource.deleteImportTask(${javaDataType}.class.getName(), callbackURL, object)
				).build();
			<#elseif generateBatch && stringUtil.equals(javaMethodSignature.methodName, "post" + parentSchemaName + schemaName + "sPageExportBatch")>
				vulcanBatchEngineExportTaskResource.setContextAcceptLanguage(contextAcceptLanguage);
				vulcanBatchEngineExportTaskResource.setContextCompany(contextCompany);
				vulcanBatchEngineExportTaskResource.setContextHttpServletRequest(contextHttpServletRequest);
				vulcanBatchEngineExportTaskResource.setContextUriInfo(contextUriInfo);
				vulcanBatchEngineExportTaskResource.setContextUser(contextUser);
				vulcanBatchEngineExportTaskResource.setGroupLocalService(groupLocalService);

				javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.accepted();

				return responseBuilder.entity(
					vulcanBatchEngineExportTaskResource.postExportTask(${javaDataType}.class.getName(), callbackURL, contentType, fieldNames)
				).build();
			<#elseif generateBatch && (stringUtil.equals(javaMethodSignature.methodName, "post" + parentSchemaName + schemaName + "Batch") || stringUtil.equals(javaMethodSignature.methodName, "post" + parentSchemaName + "Id" + schemaName + "Batch"))>
				vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(contextAcceptLanguage);
				vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
				vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(contextHttpServletRequest);
				vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
				vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

				javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.accepted();

				return responseBuilder.entity(
					vulcanBatchEngineImportTaskResource.postImportTask(${javaDataType}.class.getName(), callbackURL, null, object)
				).build();
			<#elseif generateBatch && stringUtil.equals(javaMethodSignature.methodName, "put" + schemaName + "Batch")>
				vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(contextAcceptLanguage);
				vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
				vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(contextHttpServletRequest);
				vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
				vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

				javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.accepted();

				return responseBuilder.entity(
					vulcanBatchEngineImportTaskResource.putImportTask(${javaDataType}.class.getName(), callbackURL, object)
				).build();
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "get" + schemaName + "PermissionsPage")>
				<#if freeMarkerTool.hasParameter(javaMethodSignature, schemaVarName + "Id")>
					<#assign generateGetPermissionCheckerMethods = true />

					String resourceName = getPermissionCheckerResourceName(${schemaVarName}Id);
					Long resourceId = getPermissionCheckerResourceId(${schemaVarName}Id);

					PermissionUtil.checkPermission(ActionKeys.PERMISSIONS, groupLocalService, resourceName, resourceId, getPermissionCheckerGroupId(${schemaVarName}Id));

					return toPermissionPage(
						<@getActions
							resourceId="resourceId"
							resourceName="resourceName"
							source=schemaName
						/>,
						resourceId, resourceName, roleNames);
				<#else>
					throw new UnsupportedOperationException("This method needs to be implemented");
				</#if>
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "getAssetLibrary" + schemaName + "PermissionsPage")>
				<#assign generateGetPermissionCheckerMethods = true />

				String portletName = getPermissionCheckerPortletName(assetLibraryId);

				PermissionUtil.checkPermission(ActionKeys.PERMISSIONS, groupLocalService, portletName, assetLibraryId, assetLibraryId);

				return toPermissionPage(
					<@getActions
						resourceId="assetLibraryId"
						resourceName="portletName"
						source="AssetLibrary" + schemaName
					/>,
					assetLibraryId, portletName, roleNames);
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "getSite" + schemaName + "PermissionsPage")>
				<#assign generateGetPermissionCheckerMethods = true />

				String portletName = getPermissionCheckerPortletName(siteId);

				PermissionUtil.checkPermission(ActionKeys.PERMISSIONS, groupLocalService, portletName, siteId, siteId);

				return toPermissionPage(
					<@getActions
						resourceId="siteId"
						resourceName="portletName"
						source="Site" + schemaName
					/>,
					siteId, portletName, roleNames);
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "put" + schemaName + "PermissionsPage")>
				<#if freeMarkerTool.hasParameter(javaMethodSignature, schemaVarName + "Id")>
					<#assign generateGetPermissionCheckerMethods = true />

					String resourceName = getPermissionCheckerResourceName(${schemaVarName}Id);
					Long resourceId = getPermissionCheckerResourceId(${schemaVarName}Id);

					<@updateResourcePermissions
						groupId="getPermissionCheckerGroupId(${schemaVarName}Id)"
						resourceId="resourceId"
						resourceName="resourceName"
					>
						<@getActions
							resourceId="resourceId"
							resourceName="resourceName"
							source=schemaName
						/>
					</@updateResourcePermissions>
				<#else>
					throw new UnsupportedOperationException("This method needs to be implemented");
				</#if>
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "putAssetLibrary" + schemaName + "PermissionsPage")>
				<#assign generateGetPermissionCheckerMethods = true />

				String portletName = getPermissionCheckerPortletName(assetLibraryId);

				<@updateResourcePermissions
					groupId="assetLibraryId"
					resourceId="assetLibraryId"
					resourceName="portletName"
				>
					<@getActions
						resourceId="assetLibraryId"
						resourceName="portletName"
						source="AssetLibrary" + schemaName
					/>
				</@updateResourcePermissions>
			<#elseif stringUtil.equals(javaMethodSignature.methodName, "putSite" + schemaName + "PermissionsPage")>
				<#assign generateGetPermissionCheckerMethods = true />

				String portletName = getPermissionCheckerPortletName(siteId);

				<@updateResourcePermissions
					groupId="siteId"
					resourceId="siteId"
					resourceName="portletName"
				>
					<@getActions
						resourceId="siteId"
						resourceName="portletName"
						source="Site" + schemaName
					/>
				</@updateResourcePermissions>
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Boolean")>
				return false;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Double") ||
					 stringUtil.equals(javaMethodSignature.returnType, "java.lang.Number")>

				return 0.0;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Float")>
				return 0f;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Integer")>
				return 0;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Long")>
				return 0L;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.Object")>
				return null;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.lang.String")>
				return StringPool.BLANK;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.math.BigDecimal")>
				return java.math.BigDecimal.ZERO;
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "java.util.Date")>
				return new java.util.Date();
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "javax.ws.rs.core.Response")>
				javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.ok();

				return responseBuilder.build();
			<#elseif stringUtil.equals(javaMethodSignature.returnType, "void")>
			<#elseif javaMethodSignature.returnType?contains("Page<")>
				return Page.of(Collections.emptyList());
			<#elseif freeMarkerTool.hasHTTPMethod(javaMethodSignature, "patch") && freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "get" + javaMethodSignature.methodName?remove_beginning("patch")) && freeMarkerTool.hasJavaMethodSignature(javaMethodSignatures, "put" + javaMethodSignature.methodName?remove_beginning("patch")) && !javaMethodSignature.operation.requestBody.content?keys?seq_contains("multipart/form-data")>
				<#assign
					generatePatchMethods = true
					javaMethodParameters = javaMethodSignature.javaMethodParameters[0..javaMethodSignature.javaMethodParameters?size-2]
					javaMethodParameterName = ""
				/>

				<#if javaMethodSignature.methodName?contains("ByExternalReferenceCode")>
					<#assign javaMethodParameterName = javaMethodSignature.methodName?replace("patch", "get") />
				<#else>
					<#assign javaMethodParameterName = "get" + schemaName />
				</#if>

				${javaDataType} existing${schemaName} = ${javaMethodParameterName}(
					<#list javaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterName}

						<#sep>, </#sep>
					</#list>
				);

				<#assign properties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, schema) />

				<#list properties?keys as propertyName>
					<#if !freeMarkerTool.isDTOSchemaProperty(openAPIYAML, propertyName, schema) && !stringUtil.equals(propertyName, "id")>
						if (${schemaVarName}.get${propertyName?cap_first}() != null) {
							existing${schemaName}.set${propertyName?cap_first}(${schemaVarName}.get${propertyName?cap_first}());
						}
					</#if>
				</#list>

				preparePatch(${schemaVarName}, existing${schemaName});

				<#if javaMethodSignature.methodName?contains("ByExternalReferenceCode")>
					<#assign javaMethodParameterName = javaMethodSignature.methodName?replace("patch", "put") />
				<#else>
					<#assign javaMethodParameterName = "put" + schemaName />
				</#if>

				return ${javaMethodParameterName}(
					<#list javaMethodParameters as javaMethodParameter>
						${javaMethodParameter.parameterName}

						<#sep>, </#sep>
					</#list>

					, existing${schemaName}
				);
			<#else>
				return new ${javaMethodSignature.returnType}();
			</#if>
		}
	</#list>

	<#if generateBatch>
		<#assign
			properties = freeMarkerTool.getDTOProperties(configYAML, openAPIYAML, schema)

			createStrategies = freeMarkerTool.getVulcanBatchImplementationCreateStrategies(javaMethodSignatures, properties)
			updateStrategies = freeMarkerTool.getVulcanBatchImplementationUpdateStrategies(javaMethodSignatures)
		/>
		@Override
		@SuppressWarnings("PMD.UnusedLocalVariable")
		public void create(Collection<${javaDataType}> ${schemaVarNames}, Map<String, Serializable> parameters) throws Exception {

			<#if createStrategies?has_content>
				UnsafeConsumer<${javaDataType}, Exception> ${schemaVarName}UnsafeConsumer = null;

				String createStrategy = (String) parameters.getOrDefault("createStrategy", "INSERT");
			</#if>

			<#if createStrategies?seq_contains("INSERT")>
				<#assign parentParameterNames = []/>

				if ("INSERT".equalsIgnoreCase(createStrategy)) {

					<#if postBatchJavaMethodSignature??>
						${schemaVarName}UnsafeConsumer = ${schemaVarName} -> ${postBatchJavaMethodSignature.methodName}(
							<@getPOSTBatchJavaMethodParameters
								javaMethodParameters=postBatchJavaMethodSignature.javaMethodParameters
								schemaVarName=schemaVarName
							/>
						);
					</#if>

					<#if postParentBatchJavaMethodSignatures?has_content>
						<#list postParentBatchJavaMethodSignatures as parentBatchJavaMethodSignature>
							<#assign parentParameterNames = parentParameterNames + [parentBatchJavaMethodSignature.parentSchemaName!?uncap_first + "Id"]/>

							if (parameters.containsKey("${parentBatchJavaMethodSignature.parentSchemaName?uncap_first}Id")) {
								${schemaVarName}UnsafeConsumer = ${schemaVarName} -> ${parentBatchJavaMethodSignature.methodName}(
									<@getPOSTBatchJavaMethodParameters
										javaMethodParameters=parentBatchJavaMethodSignature.javaMethodParameters
										schemaVarName=schemaVarName
									/>
								);
							}

							<#if parentBatchJavaMethodSignature?has_next>
								else
							</#if>
						</#list>
					</#if>

					<#if postAssetLibraryBatchJavaMethodSignature??>
						<#assign parentParameterNames = parentParameterNames + ["assetLibraryId"]/>

						<#if postParentBatchJavaMethodSignatures?has_content>
							else
						</#if>

						if (parameters.containsKey("assetLibraryId")) {
							${schemaVarName}UnsafeConsumer = ${schemaVarName} -> ${postAssetLibraryBatchJavaMethodSignature.methodName}(
								<@getPOSTBatchJavaMethodParameters
									javaMethodParameters=postAssetLibraryBatchJavaMethodSignature.javaMethodParameters
									schemaVarName=schemaVarName
								/>
							);
						}
					</#if>

					<#if postSiteBatchJavaMethodSignature??>
						<#assign parentParameterNames = parentParameterNames + ["siteId"]/>

						<#if postParentBatchJavaMethodSignatures?has_content || postAssetLibraryBatchJavaMethodSignature??>
							else
						</#if>

						if (parameters.containsKey("siteId")) {
							${schemaVarName}UnsafeConsumer = ${schemaVarName} -> ${postSiteBatchJavaMethodSignature.methodName}(
								<@getPOSTBatchJavaMethodParameters
									javaMethodParameters=postSiteBatchJavaMethodSignature.javaMethodParameters
									schemaVarName=schemaVarName
								/>
							);
						}
					</#if>

					<#if !postBatchJavaMethodSignature?? && parentParameterNames?has_content>
						else {
							throw new NotSupportedException("One of the following parameters must be specified: [${parentParameterNames?join(", ")}]");
						}
					</#if>
				}
			</#if>

			<#if createStrategies?seq_contains("UPSERT")>
				if ("UPSERT".equalsIgnoreCase(createStrategy)) {
					${schemaVarName}UnsafeConsumer = ${schemaVarName} -> ${putByERCBatchJavaMethodSignature.methodName}(

					<#list putByERCBatchJavaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if stringUtil.equals(javaMethodParameter.parameterName, "externalReferenceCode")>
							${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}()
						<#elseif putByERCBatchJavaMethodSignature.parentSchemaName?? && stringUtil.equals(javaMethodParameter.parameterName, putByERCBatchJavaMethodSignature.parentSchemaName!?uncap_first + "Id")>
							<#if properties?keys?seq_contains(javaMethodParameter.parameterName)>
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}() != null ?
								${schemaVarName}.get${javaMethodParameter.parameterName?cap_first}() :
							</#if>

							<@castParameters
								type=javaMethodParameter.parameterType
								value="${javaMethodParameter.parameterName}"
							/>
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
							${schemaVarName}
						<#else>
							null
						</#if>

						<#sep>, </#sep>
					</#list>

					);
				}
			</#if>

			<#if createStrategies?has_content>
				if (${schemaVarName}UnsafeConsumer == null) {
					throw new NotSupportedException("Create strategy \"" + createStrategy + "\" is not supported for ${schemaVarName?cap_first}");
				}

				if (contextBatchUnsafeConsumer != null) {
					contextBatchUnsafeConsumer.accept(${schemaVarNames}, ${schemaVarName}UnsafeConsumer);
				}
				else {
					for (${javaDataType} ${schemaVarName} : ${schemaVarNames}) {
						${schemaVarName}UnsafeConsumer.accept(${schemaVarName});
					}
				}
			<#else>
				throw new UnsupportedOperationException("This method needs to be implemented");
			</#if>
		}

		@Override
		public void delete(Collection<${javaDataType}> ${schemaVarNames}, Map<String, Serializable> parameters) throws Exception {
			<#if deleteBatchJavaMethodSignature?? && properties?keys?seq_contains("id")>
				for (${javaDataType} ${schemaVarName} : ${schemaVarNames}) {
					delete${schemaName}(${schemaVarName}.getId());
				}
			<#elseif deleteBatchJavaMethodSignature?? && properties?keys?seq_contains(schemaVarName + "Id")>
				for (${javaDataType} ${schemaVarName} : ${schemaVarNames}) {
					delete${schemaName}(${schemaVarName}.get${schemaName}Id());
				}
			<#else>
				throw new UnsupportedOperationException("This method needs to be implemented");
			</#if>
		}

		public Set<String> getAvailableCreateStrategies() {
			return SetUtil.fromArray(
				<#if createStrategies?has_content>
					"${createStrategies?join("\", \"")}"
				</#if>
			);
		}

		public Set<String> getAvailableUpdateStrategies() {
			return SetUtil.fromArray(
				<#if updateStrategies?has_content>
					"${updateStrategies?join("\", \"")}"
				</#if>
			);
		}

		@Override
		public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap) throws Exception {
			return getEntityModel(new MultivaluedHashMap<String, Object>(multivaluedMap));
		}

		@Override
		public EntityModel getEntityModel(MultivaluedMap multivaluedMap) throws Exception {
			return null;
		}

		public String getVersion() {
			return "${freeMarkerTool.getVersion(openAPIYAML)}";
		}

		@Override
		public Page<${javaDataType}> read(Filter filter, Pagination pagination, Sort[] sorts, Map<String, Serializable> parameters, String search) throws Exception {
			<#if freeMarkerTool.hasReadVulcanBatchImplementation(javaMethodSignatures)>
				<#assign parentParameterNames = []/>

				<#if getAssetLibraryBatchJavaMethodSignature??>
					<#assign parentParameterNames = parentParameterNames + ["assetLibraryId"]/>

					if (parameters.containsKey("assetLibraryId")) {
						return ${getAssetLibraryBatchJavaMethodSignature.methodName}(
							<@getGETBatchJavaMethodParameters javaMethodParameters=getAssetLibraryBatchJavaMethodSignature.javaMethodParameters />
						);
					}
					else
				</#if>
				<#if getSiteBatchJavaMethodSignature??>
					<#assign parentParameterNames = parentParameterNames + ["siteId"]/>

					if (parameters.containsKey("siteId")) {
						return ${getSiteBatchJavaMethodSignature.methodName}(
							<@getGETBatchJavaMethodParameters javaMethodParameters=getSiteBatchJavaMethodSignature.javaMethodParameters />
						);
					}
					else
				</#if>

				<#if getParentBatchJavaMethodSignatures?has_content>
					<#list getParentBatchJavaMethodSignatures as parentBatchJavaMethodSignature>
						<#assign
							parentParameterNames = parentParameterNames + [parentBatchJavaMethodSignature.parentSchemaName!?uncap_first + "Id"]
						/>

						if (parameters.containsKey("${parentBatchJavaMethodSignature.parentSchemaName!?uncap_first + "Id"}")) {
							return ${parentBatchJavaMethodSignature.methodName}(
								<@getGETBatchJavaMethodParameters javaMethodParameters=parentBatchJavaMethodSignature.javaMethodParameters />
							);
						}
						else
					</#list>
				</#if>

				<#if getBatchJavaMethodSignature??>
					<#if getAssetLibraryBatchJavaMethodSignature?? || getSiteBatchJavaMethodSignature?? || getParentBatchJavaMethodSignatures?has_content>
						{
					</#if>

					return ${getBatchJavaMethodSignature.methodName}(
						<@getGETBatchJavaMethodParameters javaMethodParameters=getBatchJavaMethodSignature.javaMethodParameters />
					);

					<#if getAssetLibraryBatchJavaMethodSignature?? || getSiteBatchJavaMethodSignature?? || getParentBatchJavaMethodSignatures?has_content>
						}
					</#if>
				<#else>
					{
						throw new NotSupportedException("One of the following parameters must be specified: [${parentParameterNames?join(", ")}]");
					}
				</#if>
			<#else>
				throw new UnsupportedOperationException("This method needs to be implemented");
			</#if>
		}

		@Override
		public void setLanguageId(String languageId) {
			this.contextAcceptLanguage = new AcceptLanguage() {

				@Override
				public List<Locale> getLocales() {
					return null;
				}

				@Override
				public String getPreferredLanguageId() {
					return languageId;
				}

				@Override
				public Locale getPreferredLocale() {
					return LocaleUtil.fromLanguageId(languageId);
				}

			};
		}

		@Override
		public void update(Collection<${javaDataType}> ${schemaVarNames}, Map<String, Serializable> parameters) throws Exception {
			<#if updateStrategies?has_content>
				UnsafeConsumer<${javaDataType}, Exception> ${schemaVarName}UnsafeConsumer = null;

				String updateStrategy = (String) parameters.getOrDefault("updateStrategy", "UPDATE");
			</#if>

			<#if updateStrategies?seq_contains("PARTIAL_UPDATE")>
				if ("PARTIAL_UPDATE".equalsIgnoreCase(updateStrategy)) {
					${schemaVarName}UnsafeConsumer = ${schemaVarName} -> patch${schemaName}(

					<#list patchBatchJavaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
							${schemaVarName}
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id") || stringUtil.equals(javaMethodParameter.parameterName, "id")>
							<#if properties?keys?seq_contains("id")>
								${schemaVarName}.getId() != null ? ${schemaVarName}.getId() :
							<#elseif properties?keys?seq_contains(schemaVarName + "Id")>
								(${schemaVarName}.get${schemaName}Id() != null) ? ${schemaVarName}.get${schemaName}Id() :
							</#if>

							<@castParameters
								type=javaMethodParameter.parameterType
								value="${schemaVarName}Id"
							/>
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, "multipartBody")>
							null
						<#else>
							${javaMethodParameter.parameterName}
						</#if>

						<#sep>, </#sep>
					</#list>

					);
				}
			</#if>

			<#if updateStrategies?seq_contains("UPDATE")>
				if ("UPDATE".equalsIgnoreCase(updateStrategy)) {
					${schemaVarName}UnsafeConsumer = ${schemaVarName} -> put${schemaName}(

					<#list putBatchJavaMethodSignature.javaMethodParameters as javaMethodParameter>
						<#if stringUtil.equals(javaMethodParameter.parameterName, "flatten")>
							(Boolean)parameters.get("flatten")
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
							${schemaVarName}
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, schemaVarName + "Id") || stringUtil.equals(javaMethodParameter.parameterName, "id")>
							<#if properties?keys?seq_contains("id")>
								${schemaVarName}.getId() != null ? ${schemaVarName}.getId() :
							<#elseif properties?keys?seq_contains(schemaVarName + "Id")>
								(${schemaVarName}.get${schemaName}Id() != null) ? ${schemaVarName}.get${schemaName}Id() :
							</#if>

							<@castParameters
								type=javaMethodParameter.parameterType
								value="${schemaVarName}Id"
							/>
						<#elseif putBatchJavaMethodSignature.parentSchemaName?? && stringUtil.equals(javaMethodParameter.parameterName, putBatchJavaMethodSignature.parentSchemaName?uncap_first + "Id")>
							<@castParameters
								type=javaMethodParameter.parameterType
								value="${javaMethodSignature.parentSchemaName?uncap_first}Id"
							/>
						<#elseif stringUtil.equals(javaMethodParameter.parameterName, "multipartBody")>
							null
						<#else>
							${javaMethodParameter.parameterName}
						</#if>

						<#sep>, </#sep>
					</#list>

					);
				}
			</#if>

			<#if updateStrategies?has_content>
				if (${schemaVarName}UnsafeConsumer == null) {
					throw new NotSupportedException("Update strategy \"" + updateStrategy + "\" is not supported for ${schemaVarName?cap_first}");
				}

				if (contextBatchUnsafeConsumer != null) {
					contextBatchUnsafeConsumer.accept(${schemaVarNames}, ${schemaVarName}UnsafeConsumer);
				}
				else {
					for (${javaDataType} ${schemaVarName} : ${schemaVarNames}) {
						${schemaVarName}UnsafeConsumer.accept(${schemaVarName});
					}
				}
			<#else>
				throw new UnsupportedOperationException("This method needs to be implemented");
			</#if>
		}
	</#if>

	<#if generateGetPermissionCheckerMethods>
		protected String getPermissionCheckerActionsResourceName(Object id) throws Exception {
			return getPermissionCheckerResourceName(id);
		}

		protected Long getPermissionCheckerGroupId(Object id) throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}

		protected String getPermissionCheckerPortletName(Object id) throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}

		protected Long getPermissionCheckerResourceId(Object id) throws Exception {
			return GetterUtil.getLong(id);
		}

		protected String getPermissionCheckerResourceName(Object id) throws Exception {
			throw new UnsupportedOperationException("This method needs to be implemented");
		}

		protected Page<com.liferay.portal.vulcan.permission.Permission> toPermissionPage(Map<String, Map<String, String>> actions, long id, String resourceName, String roleNames) throws Exception {
			List<ResourceAction> resourceActions = resourceActionLocalService.getResourceActions(resourceName);

			if (Validator.isNotNull(roleNames)) {
				return Page.of(actions, _getPermissions(contextCompany.getCompanyId(), resourceActions, id, resourceName, StringUtil.split(roleNames)));
			}

			return Page.of(actions, _getPermissions(contextCompany.getCompanyId(), resourceActions, id, resourceName, null));
		}

		private Collection<Permission> _getPermissions(long companyId, List<ResourceAction> resourceActions, long resourceId, String resourceName, String[] roleNames) throws Exception {
			Map<String, Permission> permissions = new HashMap<>();

			int count = resourcePermissionLocalService.getResourcePermissionsCount(companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(resourceId));

			if (count == 0) {
				ResourceLocalServiceUtil.addResources(companyId, resourceId, 0, resourceName, String.valueOf(resourceId), false, true, true);
			}

			List<String> actionIds = transform(resourceActions, resourceAction -> resourceAction.getActionId());

			Set<ResourcePermission> resourcePermissions = new HashSet<>();

			resourcePermissions.addAll(resourcePermissionLocalService.getResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId)));
			resourcePermissions.addAll(resourcePermissionLocalService.getResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_GROUP, String.valueOf(GroupThreadLocal.getGroupId())));
			resourcePermissions.addAll(resourcePermissionLocalService.getResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_GROUP_TEMPLATE, "0"));
			resourcePermissions.addAll(resourcePermissionLocalService.getResourcePermissions(companyId, resourceName, ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(resourceId)));

			List<Resource> resources = transform(resourcePermissions, resourcePermission -> ResourceLocalServiceUtil.getResource(resourcePermission.getCompanyId(), resourcePermission.getName(), resourcePermission.getScope(), resourcePermission.getPrimKey()));

			Set<com.liferay.portal.kernel.model.Role> roles = new HashSet<>();

			if (roleNames != null) {
				for (String roleName: roleNames) {
					roles.add(roleLocalService.getRole(companyId, roleName));
				}
			}
			else {
				for (ResourcePermission resourcePermission : resourcePermissions) {
					com.liferay.portal.kernel.model.Role role = roleLocalService.getRole(resourcePermission.getRoleId());

					roles.add(role);
				}
			}

			for (com.liferay.portal.kernel.model.Role role : roles) {
				Set<String> actionsIdsSet = new HashSet<>();

				for (Resource resource : resources) {
					actionsIdsSet.addAll(resourcePermissionLocalService.getAvailableResourcePermissionActionIds(resource.getCompanyId(), resource.getName(), ResourceConstants.SCOPE_COMPANY, String.valueOf(resource.getCompanyId()), role.getRoleId(), actionIds));
					actionsIdsSet.addAll(resourcePermissionLocalService.getAvailableResourcePermissionActionIds(resource.getCompanyId(), resource.getName(), ResourceConstants.SCOPE_GROUP, String.valueOf(GroupThreadLocal.getGroupId()), role.getRoleId(), actionIds));
					actionsIdsSet.addAll(resourcePermissionLocalService.getAvailableResourcePermissionActionIds(resource.getCompanyId(), resource.getName(), ResourceConstants.SCOPE_GROUP_TEMPLATE, "0", role.getRoleId(), actionIds));
					actionsIdsSet.addAll(resourcePermissionLocalService.getAvailableResourcePermissionActionIds(resource.getCompanyId(), resource.getName(), resource.getScope(), resource.getPrimKey(), role.getRoleId(), actionIds));
				}

				if (actionsIdsSet.isEmpty()) {
					continue;
				}

				Permission permission = new Permission() {
					{
						actionIds = actionsIdsSet.toArray(new String[0]);
						roleName = role.getName();
					}
				};

				permissions.put(role.getName(), permission);
			}

			return permissions.values();
		}
	</#if>

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	<#if generateBatch>
		public void setContextBatchUnsafeConsumer(UnsafeBiConsumer<Collection<${javaDataType}>, UnsafeConsumer<${javaDataType}, Exception>, Exception> contextBatchUnsafeConsumer) {
			this.contextBatchUnsafeConsumer = contextBatchUnsafeConsumer;
		}
	</#if>

	public void setContextCompany(com.liferay.portal.kernel.model.Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	public void setContextHttpServletRequest(HttpServletRequest contextHttpServletRequest) {
		this.contextHttpServletRequest = contextHttpServletRequest;
	}

	public void setContextHttpServletResponse(HttpServletResponse contextHttpServletResponse) {
		this.contextHttpServletResponse = contextHttpServletResponse;
	}

	public void setContextUriInfo(UriInfo contextUriInfo) {
		this.contextUriInfo = contextUriInfo;
	}

	public void setContextUser(com.liferay.portal.kernel.model.User contextUser) {
		this.contextUser = contextUser;
	}

	public void setExpressionConvert(ExpressionConvert<Filter> expressionConvert) {
		this.expressionConvert = expressionConvert;
	}

	public void setFilterParserProvider(FilterParserProvider filterParserProvider) {
		this.filterParserProvider = filterParserProvider;
	}

	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	public void setResourceActionLocalService(ResourceActionLocalService resourceActionLocalService) {
		this.resourceActionLocalService = resourceActionLocalService;
	}

	public void setResourcePermissionLocalService(ResourcePermissionLocalService resourcePermissionLocalService) {
		this.resourcePermissionLocalService = resourcePermissionLocalService;
	}

	public void setRoleLocalService(RoleLocalService roleLocalService) {
		this.roleLocalService = roleLocalService;
	}

	public void setSortParserProvider(SortParserProvider sortParserProvider) {
		this.sortParserProvider = sortParserProvider;
	}

	<#if generateBatch>
		<#if freeMarkerTool.isVersionCompatible(configYAML, 2)>
			public void setVulcanBatchEngineExportTaskResource(VulcanBatchEngineExportTaskResource vulcanBatchEngineExportTaskResource) {
				this.vulcanBatchEngineExportTaskResource = vulcanBatchEngineExportTaskResource;
			}
		</#if>

		public void setVulcanBatchEngineImportTaskResource(VulcanBatchEngineImportTaskResource vulcanBatchEngineImportTaskResource) {
			this.vulcanBatchEngineImportTaskResource = vulcanBatchEngineImportTaskResource;
		}

		@Override
		public Filter toFilter(String filterString, Map<String, List<String>> multivaluedMap) {
			try {
				EntityModel entityModel = getEntityModel(multivaluedMap);

				FilterParser filterParser = filterParserProvider.provide(entityModel);

				com.liferay.portal.odata.filter.Filter oDataFilter = new com.liferay.portal.odata.filter.Filter(filterParser.parse(filterString));

				return expressionConvert.convert(oDataFilter.getExpression(), contextAcceptLanguage.getPreferredLocale(), entityModel);
			}
			catch (Exception exception) {
				_log.error("Invalid filter " + filterString, exception);

				return null;
			}
		}

		@Override
		public Sort[] toSorts(String sortString) {
			if (Validator.isNull(sortString)) {
				return null;
			}

			try {
				SortParser sortParser = sortParserProvider.provide(getEntityModel(Collections.emptyMap()));

				if (sortParser == null) {
					return null;
				}

				com.liferay.portal.odata.sort.Sort oDataSort = new com.liferay.portal.odata.sort.Sort(sortParser.parse(sortString));

				List<SortField> sortFields = oDataSort.getSortFields();

				Sort[] sorts = new Sort[sortFields.size()];

				for (int i = 0; i < sortFields.size(); i++) {
					SortField sortField = sortFields.get(i);

					sorts[i] = new Sort(sortField.getSortableFieldName(contextAcceptLanguage.getPreferredLocale()), !sortField.isAscending());
				}

				return sorts;
			}
			catch (Exception exception) {
				_log.error("Invalid sort " + sortString, exception);

				return new Sort[0];
			}
		}
	</#if>

	protected Map<String, String> addAction(String actionName, GroupedModel groupedModel, String methodName) {
		return ActionUtil.addAction(actionName, getClass(), groupedModel, methodName, contextScopeChecker, contextUriInfo);
	}

	protected Map<String, String> addAction(String actionName, Long id, String methodName, Long ownerId, String permissionName, Long siteId) {
		return ActionUtil.addAction(actionName, getClass(), id, methodName, contextScopeChecker, ownerId, permissionName, siteId, contextUriInfo);
	}

	protected Map<String, String> addAction(String actionName, Long id, String methodName, ModelResourcePermission modelResourcePermission) {
		return ActionUtil.addAction(actionName, getClass(), id, methodName, contextScopeChecker, modelResourcePermission, contextUriInfo);
	}

	protected Map<String, String> addAction(String actionName, String methodName, String permissionName, Long siteId) {
		return addAction(actionName, siteId, methodName, null, permissionName, siteId);
	}

	<#if generatePatchMethods>
		protected void preparePatch(${javaDataType} ${schemaVarName}, ${javaDataType} existing${schemaVarName?cap_first}) {
		}
	</#if>

	protected <T, R, E extends Throwable> List<R> transform(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction) {
		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] transform(T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) {
		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] transformToArray(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) {
		return TransformUtil.transformToArray(collection, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> List<R> transformToList(T[] array, UnsafeFunction<T, R, E> unsafeFunction) {
		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected <T, R, E extends Throwable> long[] transformToLongArray(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction) {
		<#if freeMarkerTool.isVersionCompatible(configYAML, 2)>
			return TransformUtil.transformToLongArray(collection, unsafeFunction);
		<#else>
			try {
				return unsafeTransformToLongArray(collection, unsafeFunction);
			}
			catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		</#if>
	}

	protected <T, R, E extends Throwable> List<R> unsafeTransform(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction) throws E {
		return TransformUtil.unsafeTransform(collection, unsafeFunction);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransform(T[] array, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) throws E {
		return TransformUtil.unsafeTransform(array, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> R[] unsafeTransformToArray(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) throws E {
		return TransformUtil.unsafeTransformToArray(collection, unsafeFunction, clazz);
	}

	protected <T, R, E extends Throwable> List<R> unsafeTransformToList(T[] array, UnsafeFunction<T, R, E> unsafeFunction) throws E {
		return TransformUtil.unsafeTransformToList(array, unsafeFunction);
	}

	protected <T, R, E extends Throwable> long[] unsafeTransformToLongArray(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction) throws E {
		<#if freeMarkerTool.isVersionCompatible(configYAML, 2)>
			return TransformUtil.unsafeTransformToLongArray(collection, unsafeFunction);
		<#else>
			return (long[])_unsafeTransformToPrimitiveArray(collection, unsafeFunction, long[].class);
		</#if>
	}

	protected AcceptLanguage contextAcceptLanguage;

	<#if generateBatch>
		protected UnsafeBiConsumer<Collection<${javaDataType}>, UnsafeConsumer<${javaDataType}, Exception>, Exception> contextBatchUnsafeConsumer;
	</#if>

	protected com.liferay.portal.kernel.model.Company contextCompany;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected Object contextScopeChecker;
	protected UriInfo contextUriInfo;
	protected com.liferay.portal.kernel.model.User contextUser;
	protected ExpressionConvert<Filter> expressionConvert;
	protected FilterParserProvider filterParserProvider;
	protected GroupLocalService groupLocalService;
	protected ResourceActionLocalService resourceActionLocalService;
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	protected RoleLocalService roleLocalService;
	protected SortParserProvider sortParserProvider;

	<#if generateBatch>
		<#if freeMarkerTool.isVersionCompatible(configYAML, 2)>
			protected VulcanBatchEngineExportTaskResource vulcanBatchEngineExportTaskResource;
		</#if>

		protected VulcanBatchEngineImportTaskResource vulcanBatchEngineImportTaskResource;
	</#if>

	<#if !freeMarkerTool.isVersionCompatible(configYAML, 2)>
		private <T, R, E extends Throwable> Object _unsafeTransformToPrimitiveArray(Collection<T> collection, UnsafeFunction<T, R, E> unsafeFunction, Class<?> clazz) throws E {
			List<R> list = unsafeTransform(collection, unsafeFunction);

			Object array = clazz.cast(Array.newInstance(clazz.getComponentType(), list.size()));

			for (int i = 0; i < list.size(); i++) {
				Array.set(array, i, list.get(i));
			}

			return array;
		}
	</#if>

	private static final com.liferay.portal.kernel.log.Log _log = LogFactoryUtil.getLog(Base${schemaName}ResourceImpl.class);

}

<#macro castParameters
	type
	value
>
	<#if stringUtil.equals(value, "assetLibraryId") || stringUtil.equals(value, "siteId")>
		(Long)parameters.get("${value}")
	<#elseif stringUtil.startsWith(type, "[L")>
		(

		<#if type?contains("java.lang.Boolean")>
			Boolean[]
		<#elseif type?contains("java.util.Date")>
			java.util.Date[]
		<#elseif type?contains("java.lang.Double")>
			Double[]
		<#elseif type?contains("java.lang.Integer")>
			Integer[]
		<#elseif type?contains("java.lang.Long")>
			Long[]
		<#else>
			String[]
		</#if>

		)parameters.get("${value}")
	<#elseif !stringUtil.startsWith(type, "java")>
		(${type})parameters.get("${value}")
	<#else>
		<#if type?contains("java.lang.Boolean")>
			Boolean.parseBoolean(
		<#elseif type?contains("java.util.Date")>
			new java.util.Date(
		<#elseif type?contains("java.lang.Double")>
			Double.parseDouble(
		<#elseif type?contains("java.lang.Integer")>
			Integer.parseInt(
		<#elseif type?contains("java.lang.Long")>
			Long.parseLong(
		</#if>

		(String)parameters.get("${value}")

		<#if !type?contains("java.lang.String")>
			)
		</#if>
	</#if>
</#macro>

<#macro getActions
	resourceId
	resourceName
	source
>
	HashMapBuilder.put(
		"get", addAction(ActionKeys.PERMISSIONS, "get${source}PermissionsPage", ${resourceName}, ${resourceId})
	).put(
		"replace", addAction(ActionKeys.PERMISSIONS, "put${source}PermissionsPage", ${resourceName}, ${resourceId})
	).build()
</#macro>

<#macro getGETBatchJavaMethodParameters
	javaMethodParameters
>
	<#list javaMethodParameters as javaMethodParameter>
		<#if stringUtil.equals(javaMethodParameter.parameterName, "aggregation")>
			null
		<#elseif stringUtil.equals(javaMethodParameter.parameterName, "filter") || stringUtil.equals(javaMethodParameter.parameterName, "pagination") || stringUtil.equals(javaMethodParameter.parameterName, "search") || stringUtil.equals(javaMethodParameter.parameterName, "sorts") || stringUtil.equals(javaMethodParameter.parameterName, "user")>
			${javaMethodParameter.parameterName}
		<#else>
			<@castParameters
				type=javaMethodParameter.parameterType
				value=javaMethodParameter.parameterName
			/>
		</#if>

		<#sep>, </#sep>
	</#list>
</#macro>

<#macro getPOSTBatchJavaMethodParameters
	javaMethodParameters
	schemaVarName
>
	<#list javaMethodParameters as javaMethodParameter>
		<#if stringUtil.equals(javaMethodParameter.parameterName, schemaVarName)>
			${schemaVarName}
		<#else>
			<@castParameters
				type=javaMethodParameter.parameterType
				value=javaMethodParameter.parameterName
			/>
		</#if>

		<#sep>, </#sep>
	</#list>
</#macro>

<#macro updateResourcePermissions
	groupId
	resourceId
	resourceName
>
	PermissionUtil.checkPermission(ActionKeys.PERMISSIONS, groupLocalService, ${resourceName}, ${resourceId}, ${groupId});

	resourcePermissionLocalService.updateResourcePermissions(contextCompany.getCompanyId(), ${groupId}, ${resourceName}, String.valueOf(${resourceId}), ModelPermissionsUtil.toModelPermissions(contextCompany.getCompanyId(), permissions, ${resourceId}, ${resourceName}, resourceActionLocalService, resourcePermissionLocalService, roleLocalService));

	return toPermissionPage(<#nested>, ${resourceId}, ${resourceName}, null);
</#macro>