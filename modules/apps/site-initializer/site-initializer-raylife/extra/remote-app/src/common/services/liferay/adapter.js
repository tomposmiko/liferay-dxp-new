import '../../../types';
import {allowedProductQuote} from '../../../routes/get-a-quote/utils/webContents';
import {toSlug} from '../../utils';

const _formatCommerceProductPrice = (price) => parseInt(price, 10);

/**
 * @param {AssetCategoryResponse[]}  data Array of matched categories
 * @returns {BusinessType[]} Array of business types
 */
const adaptToBusinessType = (data) =>
	data.map(({description, id, name, taxonomyCategoryProperties}) => ({
		description,
		id,
		taxonomyCategoryProperties,
		title: name,
	}));

/**
 * @param {DataForm}  data Basics form object
 * @returns {BasicsFormApplicationRequest} Basics Form ready for application request
 */
const adaptToRaylifeApplicationToForm = (data) => {
	const basics = {
		applicationId: data.id,
		businessCategoryId: data.businessCategoryId,
		businessInformation: {
			business: {
				email: data.email,
				location: {
					address: data.address,
					addressApt: data.addressApt,
					city: data.city,
					state: data.state,
					zip: data.zip,
				},
				phone: data.phone,
				website: data.website,
			},
			firstName: data.firstName,
			lastName: data.lastName,
		},
		businessSearch: data.businessSearch,
		product: data.product,
		productQuote: Number(data.productQuote),
		properties: {
			naics: data.naics,
			segment: data.segment,
		},
	};

	const business = {
		hasAutoPolicy: data.hasAutoPolicy,
		hasSellProductsUnderOwnBrand: data.hasSellProductsUnderOwnBrand,
		hasStoredCustomerInformation: data.hasStoredCustomerInformation,
		legalEntity: data.legalEntity,
		overallSales: data.overallSales,
		salesMerchandise: data.salesMerchandise,
		yearsOfExperience: data.yearsOfExperience,
	};

	const employees = {
		annualPayrollForEmployees: data.annualPayrollForEmployees,
		annualPayrollForOwner: data.annualPayrollForOwner,
		businessOperatesYearRound: data.businessOperatesYearRound,
		estimatedAnnualGrossRevenue: data.estimatedAnnualGrossRevenue,
		fein: data.fein,
		hasFein: data.hasFein,
		partTimeEmployees: data.partTimeEmployees,
		startBusinessAtYear: data.startBusinessAtYear,
	};

	const property = {
		buildingSquareFeetOccupied: data.buildingSquareFeetOccupied,
		doOwnBuildingAtAddress: data.doOwnBuildingAtAddress,
		isPrimaryBusinessLocation: data.isPrimaryBusinessLocation,
		isThereDivingBoards: data.isThereDivingBoards,
		isThereSwimming: data.isThereSwimming,
		stories: data.stories,
		totalBuildingSquareFeet: data.totalBuildingSquareFeet,
		yearBuilding: data.yearBuilding,
	};

	const formState = {
		basics,
		business,
		employees,
		property,
	};

	for (const form in formState) {
		const formKeyHasAnyValue = Object.values(formState[form]).some(Boolean);

		if (!formKeyHasAnyValue) {
			delete formState[form];
		}
	}

	return formState;
};

/**
 * @param {DataForm}  data Basics form object
 * @returns {BasicsFormApplicationRequest} Basics Form ready for application request
 */
const adaptToFormApplicationRequest = (form) => ({
	address: form?.basics?.businessInformation?.business?.location?.address,
	addressApt:
		form?.basics?.businessInformation?.business?.location?.addressApt,
	annualPayrollForEmployees: form?.employees?.annualPayrollForEmployees,
	annualPayrollForOwner: form?.employees?.annualPayrollForOwner,
	buildingSquareFeetOccupied: form?.property?.buildingSquareFeetOccupied,
	businessCategoryId: form?.basics?.businessCategoryId,
	businessOperatesYearRound: form?.employees?.businessOperatesYearRound,
	businessSearch: form?.basics?.businessSearch,
	city: form?.basics?.businessInformation?.business?.location?.city,
	doOwnBuildingAtAddress: form?.property?.doOwnBuildingAtAddress,
	email: form?.basics?.businessInformation?.business?.email,
	estimatedAnnualGrossRevenue: form?.employees?.estimatedAnnualGrossRevenue,
	fein: form?.employees?.fein,
	firstName: form?.basics?.businessInformation?.firstName,
	hasAutoPolicy: form?.business?.hasAutoPolicy,
	hasFein: form?.employees?.hasFein,
	hasSellProductsUnderOwnBrand: form?.business?.hasSellProductsUnderOwnBrand,
	hasStoredCustomerInformation: form?.business?.hasStoredCustomerInformation,
	isPrimaryBusinessLocation: form?.property?.isPrimaryBusinessLocation,
	isThereDivingBoards: form?.property?.isThereDivingBoards,
	isThereSwimming: form?.property?.isThereSwimming,
	lastName: form?.basics?.businessInformation?.lastName,
	legalEntity: form?.business?.legalEntity,
	naics: form?.basics?.properties?.naics,
	overallSales: form?.business?.overallSales,
	partTimeEmployees: form?.employees?.partTimeEmployees,
	phone: form?.basics?.businessInformation?.business?.phone,
	policySent: false,
	product: form?.basics?.product,
	productQuote: `${form?.basics?.productQuote}`,
	salesMerchandise: form?.business?.salesMerchandise,
	segment: form?.basics?.properties?.segment,
	startBusinessAtYear: form?.employees?.startBusinessAtYear,
	state: form?.basics?.businessInformation?.business?.location?.state,
	stories: form?.property?.stories,
	totalBuildingSquareFeet: form?.property?.totalBuildingSquareFeet,
	website: form?.basics?.businessInformation?.business?.website,
	yearBuilding: form?.property?.yearBuilding,
	yearsOfExperience: form?.business?.yearsOfExperience,
	zip: form?.basics?.businessInformation?.business?.location?.zip,
});

/**
 * @param {{
 * 	  description: {
 *      en_US: string
 *    }
 *    name: {
 *      en_US: string
 *    }
 *    skus: {
 *      price: number
 *      promoPrice: number
 *    }[]
 * }[]}  data Array of products
 * @returns {ProductQuote[]} Array of business types
 */
const adaptToProductQuote = (data = []) =>
	data.map(({description, name, productId, skus}) => ({
		description: description.en_US,
		id: productId,
		period: `($${_formatCommerceProductPrice(
			skus[0].promoPrice
		)}-${_formatCommerceProductPrice(skus[0].price)}/mo)`,
		template: {
			allowed: allowedProductQuote(name.en_US),
			name: toSlug(name.en_US),
		},
		title: name.en_US,
	}));

export const LiferayAdapt = {
	adaptToBusinessType,
	adaptToFormApplicationRequest,
	adaptToProductQuote,
	adaptToRaylifeApplicationToForm,
};
