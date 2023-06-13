<#list dataFactory.assetEntryModels as assetEntryModel>
	${dataFactory.toInsertSQL(assetEntryModel)}
</#list>

${dataFactory.toInsertSQL(dataFactory.commerceCatalogModel)}

${dataFactory.toInsertSQL(dataFactory.commerceCatalogResourcePermission())}

${dataFactory.toInsertSQL(dataFactory.commerceChannelModel)}

${dataFactory.toInsertSQL(dataFactory.commerceCurrencyModel)}

<#list dataFactory.CPDefinitionLocalizationModels as cpDefinitionLocalizationModel>
	${dataFactory.toInsertSQL(cpDefinitionLocalizationModel)}
</#list>

<#list dataFactory.CPDefinitionModels as cpDefinitionModel>
	${dataFactory.toInsertSQL(cpDefinitionModel)}
</#list>

<#list dataFactory.CPFriendlyURLEntryModels as cpFriendlyURLEntryModel>
	${dataFactory.toInsertSQL(cpFriendlyURLEntryModel)}

	${dataFactory.getCSVWriter("cpFriendlyURLEntry").write(cpFriendlyURLEntryModel.urlTitle + "\n")}
</#list>

<#list dataFactory.CPInstanceModels as cpInstanceModel>
	${dataFactory.toInsertSQL(cpInstanceModel)}
</#list>

<#list dataFactory.CProductModels as cProductModel>
	${dataFactory.toInsertSQL(cProductModel)}
</#list>

<#list dataFactory.CPTaxCategoryModels as cpTaxCategoryModel>
	${dataFactory.toInsertSQL(cpTaxCategoryModel)}
</#list>