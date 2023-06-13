<#assign
	commerceCurrencyModel = dataFactory.newCommerceCurrencyModel()

	commerceCatalogModel = dataFactory.newCommerceCatalogModel(commerceCurrencyModel)

	commerceCatalogGroupModel = dataFactory.newCommerceCatalogGroupModel(commerceCatalogModel)
	commerceChannelModel = dataFactory.newCommerceChannelModel(commerceCurrencyModel)
	cpTaxCategoryModel = dataFactory.newCPTaxCategoryModel()
/>

${dataFactory.toInsertSQL(commerceCatalogModel)}

${dataFactory.toInsertSQL(dataFactory.newCommerceCatalogResourcePermissionModel(commerceCatalogModel))}

${dataFactory.toInsertSQL(commerceChannelModel)}

${dataFactory.toInsertSQL(commerceCurrencyModel)}

<#list dataFactory.getSequence(dataFactory.maxCommerceProductCount) as commerceProductCount>
	<#assign cProductModel = dataFactory.newCProductModel(commerceCatalogGroupModel) />

	${dataFactory.toInsertSQL(cProductModel)}

	<#list dataFactory.getSequence(dataFactory.maxCommerceProductDefinitionCount) as commerceProductDefinitionCount>
		<#assign
			cpDefinitionModel = dataFactory.newCPDefinitionModel(cpTaxCategoryModel, cProductModel, commerceCatalogGroupModel, commerceProductDefinitionCount)
		/>

		${dataFactory.toInsertSQL(cpDefinitionModel)}

		${dataFactory.toInsertSQL(dataFactory.newCPDefinitionModelAssetEntryModel(cpDefinitionModel, commerceCatalogGroupModel))}

		${dataFactory.toInsertSQL(dataFactory.newCPDefinitionLocalizationModel(cpDefinitionModel))}

		<#list dataFactory.getSequence(dataFactory.maxCommerceProductInstanceCount) as commerceProductInstanceCount>
			${dataFactory.toInsertSQL(dataFactory.newCPInstanceModel(cpDefinitionModel, commerceCatalogGroupModel, commerceProductInstanceCount))}
		</#list>
	</#list>
</#list>

${dataFactory.toInsertSQL(cpTaxCategoryModel)}

<@insertGroup _groupModel=commerceCatalogGroupModel />

<@insertGroup _groupModel=dataFactory.newCommerceChannelGroupModel(commerceChannelModel) />