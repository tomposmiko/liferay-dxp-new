package ${packagePath}.uad.anonymizer.test;

import ${apiPackagePath}.model.${entity.name};
import ${apiPackagePath}.service.${entity.name}LocalService;
import ${packagePath}.uad.constants.${portletShortName}UADConstants;
import ${packagePath}.uad.test.${entity.name}UADTestHelper;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.test.util.BaseUADAnonymizerTestCase;

<#if entity.hasEntityColumn("statusByUserId")>
	import com.liferay.user.associated.data.test.util.WhenHasStatusByUserIdField;
</#if>

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * @author ${author}
 * @generated
 */
@RunWith(Arquillian.class)
public class ${entity.name}UADAnonymizerTest extends BaseUADAnonymizerTestCase<${entity.name}> <#if entity.hasEntityColumn("statusByUserId")>implements WhenHasStatusByUserIdField </#if>{

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new LiferayIntegrationTestRule();

	<#if entity.hasEntityColumn("statusByUserId")>
		@Override
		public ${entity.name} addBaseModelWithStatusByUserId(long userId, long statusByUserId) throws Exception {
			${entity.name} ${entity.varName} = _${entity.varName}UADTestHelper.add${entity.name}WithStatusByUserId(userId, statusByUserId);

			_${entity.varNames}.add(${entity.varName});

			return ${entity.varName};
		}
	</#if>

	@After
	public void tearDown() throws Exception {
		_${entity.varName}UADTestHelper.cleanUpDependencies(_${entity.varNames});
	}

	@Override
	protected ${entity.name} addBaseModel(long userId) throws Exception {
		return addBaseModel(userId, true);
	}

	@Override
	protected ${entity.name} addBaseModel(long userId, boolean deleteAfterTestRun) throws Exception {
		${entity.name} ${entity.varName} = _${entity.varName}UADTestHelper.add${entity.name}(userId);

		if (deleteAfterTestRun) {
			_${entity.varNames}.add(${entity.varName});
		}

		return ${entity.varName};
	}

	@Override
	protected void deleteBaseModels(List<${entity.name}> baseModels) throws Exception {
		_${entity.varName}UADTestHelper.cleanUpDependencies(baseModels);
	}

	@Override
	protected UADAnonymizer getUADAnonymizer() {
		return _uadAnonymizer;
	}

	@Override
	protected boolean isBaseModelAutoAnonymized(long baseModelPK, User user) throws Exception {
		${entity.name} ${entity.varName} = _${entity.varName}LocalService.get${entity.name}(baseModelPK);

		<#list entity.UADUserIdColumnNames as uadUserIdColumnName>
			<#list entity.UADAnonymizableEntityColumnsMap[uadUserIdColumnName] as uadAnonymizableEntityColumn>
				<#if !uadAnonymizableEntityColumn.isPrimitiveType()>
					${uadAnonymizableEntityColumn.type} ${uadAnonymizableEntityColumn.name} = ${entity.varName}.get${uadAnonymizableEntityColumn.methodName}();
				</#if>
			</#list>
		</#list>

		if (<#list entity.UADUserIdColumnNames as uadUserIdColumnName>
				<#list entity.UADAnonymizableEntityColumnsMap[uadUserIdColumnName] as uadAnonymizableEntityColumn>
					<#if uadAnonymizableEntityColumn.isPrimitiveType()>
						(${entity.varName}.get${uadAnonymizableEntityColumn.methodName}() !=
					<#else>
						!${uadAnonymizableEntityColumn.name}.equals(
					</#if>

					user.get${textFormatter.format(uadAnonymizableEntityColumn.UADAnonymizeFieldName, 6)}())

					<#sep> && </#sep>
				</#list>

				<#sep> && </#sep>
			</#list>) {

			return true;
		}

		return false;
	}

	@Override
	protected boolean isBaseModelDeleted(long baseModelPK) {
		if (_${entity.varName}LocalService.fetch${entity.name}(baseModelPK) == null) {
			return true;
		}

		return false;
	}

	@DeleteAfterTestRun
	private final List<${entity.name}> _${entity.varNames} = new ArrayList<${entity.name}>();

	@Inject
	private ${entity.name}LocalService _${entity.varName}LocalService;

	@Inject
	private ${entity.name}UADTestHelper _${entity.varName}UADTestHelper;

	@Inject(
		filter = "model.class.name=" + ${portletShortName}UADConstants.CLASS_NAME_${entity.constantName}
	)
	private UADAnonymizer _uadAnonymizer;

}