package ${packagePath}.service.impl;

import ${apiPackagePath}.model.${entity.name};
import ${apiPackagePath}.service.persistence.${entity.name}Persistence;

import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.service.change.tracking.CTService;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author ${author}
<#if entity.isDeprecated()>
 * @deprecated ${classDeprecatedComment}
</#if>
 * @generated
 */
<#if dependencyInjectorDS>
	@Component(service = AopService.class)
</#if>
public class ${entity.name}CTServiceImpl implements
	<#if dependencyInjectorDS>
		AopService,
	</#if>

	CTService<${entity.name}> {

	@Override
	public CTPersistence<${entity.name}> getCTPersistence() {
		return _${entity.varName}Persistence;
	}

	@Override
	public Class<${entity.name}> getModelClass() {
		return ${entity.name}.class;
	}

	@Override
	public <R, E extends Throwable> R updateWithUnsafeFunction(UnsafeFunction<CTPersistence<${entity.name}>, R, E> updateUnsafeFunction) throws E {
		return updateUnsafeFunction.apply(_${entity.varName}Persistence);
	}

	<#if dependencyInjectorDS>
		@Reference
	<#else>
		@BeanReference(type = ${entity.name}Persistence.class)
	</#if>
	private ${entity.name}Persistence _${entity.varName}Persistence;

}