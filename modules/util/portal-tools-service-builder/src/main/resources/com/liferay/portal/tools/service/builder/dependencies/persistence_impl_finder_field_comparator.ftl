<#if entityColumn.comparator == "=">
	<#if entityColumn.isPrimitiveType(false)>
		<#if stringUtil.equals(entityColumn.type, "boolean")>
			(${entityColumn.name} != ${entity.varName}.is${entityColumn.methodName}())
		<#else>
			(${entityColumn.name} != ${entity.varName}.get${entityColumn.methodName}())
		</#if>
	<#else>
		!Objects.equals(${entityColumn.name}, ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif entityColumn.comparator == "!=">
	<#if entityColumn.isPrimitiveType(false)>
		<#if stringUtil.equals(entityColumn.type, "boolean")>
			(${entityColumn.name} == ${entity.varName}.is${entityColumn.methodName}())
		<#else>
			(${entityColumn.name} == ${entity.varName}.get${entityColumn.methodName}())
		</#if>
	<#else>
		Objects.equals(${entityColumn.name}, ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif entityColumn.comparator == ">">
	<#if stringUtil.equals(entityColumn.type, "BigDecimal")>
		(${entityColumn.name}.compareTo(${entity.varName}.get${entityColumn.methodName}()) >= 0)
	<#elseif stringUtil.equals(entityColumn.type, "Date")>
		(${entityColumn.name}.getTime() >= ${entity.varName}.get${entityColumn.methodName}().getTime())
	<#else>
		(${entityColumn.name} >= ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif entityColumn.comparator == ">=">
	<#if stringUtil.equals(entityColumn.type, "BigDecimal")>
		(${entityColumn.name}.compareTo(${entity.varName}.get${entityColumn.methodName}()) > 0)
	<#elseif stringUtil.equals(entityColumn.type, "Date")>
		(${entityColumn.name}.getTime() > ${entity.varName}.get${entityColumn.methodName}().getTime())
	<#else>
		(${entityColumn.name} > ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif entityColumn.comparator == "<">
	<#if stringUtil.equals(entityColumn.type, "BigDecimal")>
		(${entityColumn.name}.compareTo(${entity.varName}.get${entityColumn.methodName}()) <= 0)
	<#elseif stringUtil.equals(entityColumn.type, "Date")>
		(${entityColumn.name}.getTime() <= ${entity.varName}.get${entityColumn.methodName}().getTime())
	<#else>
		(${entityColumn.name} <= ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif entityColumn.comparator == "<=">
	<#if stringUtil.equals(entityColumn.type, "BigDecimal")>
		(${entityColumn.name}.compareTo(${entity.varName}.get${entityColumn.methodName}()) < 0)
	<#elseif stringUtil.equals(entityColumn.type, "Date")>
		(${entityColumn.name}.getTime() < ${entity.varName}.get${entityColumn.methodName}().getTime())
	<#else>
		(${entityColumn.name} < ${entity.varName}.get${entityColumn.methodName}())
	</#if>
<#elseif stringUtil.equals(entityColumn.comparator, "LIKE")>
	!StringUtil.wildcardMatches(${entity.varName}.get${entityColumn.methodName}(), ${entityColumn.name}, '_', '%', '\\',
	<#if entityColumn.isCaseSensitive()>
		true
	<#else>
		false
	</#if>
	)
</#if>