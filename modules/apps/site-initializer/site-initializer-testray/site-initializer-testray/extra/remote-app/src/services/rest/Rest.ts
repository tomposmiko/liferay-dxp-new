/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import fetcher from '../fetcher';
import {APIResponse} from './types';

type Adapter<T = any> = (data: T) => Partial<T>;
type TransformData<T = any> = (data: T) => T;

export type APIParametersOptions = {
	aggregationTerms?: string;
	fields?: string;
	filter?: string;
	nestedFields?: string;
	nestedFieldsDepth?: number | string;
	page?: number | string;
	pageSize?: number | string;
	sort?: string;
};

const getNestedFieldDepth = (nestedFields: string | undefined) => {
	if (!nestedFields) {
		return 1;
	}

	const nestedFieldsDepthCount = nestedFields
		.split(',')
		.map((item) => item.split('.').length);

	return Math.max(...nestedFieldsDepthCount);
};

interface RestContructor<
	YupModel = any,
	ObjectModel = any,
	NestedObjectOptions = any
> {
	adapter?: Adapter<YupModel>;
	nestedFields?: string;
	nestedObjects?: NestedObjectOptions;
	transformData?: TransformData<ObjectModel>;
	uri: string;
}

class Rest<YupModel = any, ObjectModel = any, NestedObjectOptions = any> {
	private batchMinimumThreshold = 10;
	private nestedFieldsDepth = 1;
	protected adapter: Adapter = (data) => data;
	public fetcher = fetcher;
	public nestedFields: string = '';
	public resource: string = '';
	public transformData: TransformData = (data) => data;
	public uri: string;

	constructor({
		adapter,
		nestedFields = '',
		transformData,
		uri,
	}: RestContructor<YupModel, ObjectModel, NestedObjectOptions>) {
		this.uri = uri;
		this.resource = `/${uri}`;

		if (nestedFields) {
			this.nestedFields = `nestedFields=${nestedFields}`;
			this.nestedFieldsDepth = getNestedFieldDepth(nestedFields);
			this.resource = `/${uri}?${this.nestedFields}&nestedFieldsDepth=${this.nestedFieldsDepth}`;
		}

		if (adapter) {
			this.adapter = adapter;
		}

		if (transformData) {
			this.transformData = transformData;
		}
	}

	static getPageParameter(
		parameters: APIParametersOptions = {},
		baseURL?: string
	) {
		const getBaseSearchParams = (resource?: string) => {
			if (resource && resource.includes('?')) {
				return resource.slice(resource.indexOf('?'));
			}
		};

		const searchParams = new URLSearchParams(getBaseSearchParams(baseURL));

		for (const key in parameters) {
			const value = (parameters as any)[key] as
				| string
				| number
				| undefined;

			if (value) {
				searchParams.set(key, value.toString());
			}
		}

		return searchParams.toString();
	}

	protected async beforeCreate(_data: YupModel) {}
	protected async beforeUpdate(_id: number, _data: YupModel) {}
	protected async beforeRemove(_id: number) {}

	public async create(data: YupModel): Promise<ObjectModel> {
		await this.beforeCreate(data);

		return fetcher.post(`/${this.uri}`, this.adapter(data));
	}

	public async createBatch(data: YupModel[]): Promise<void> {
		if (data.length >= this.batchMinimumThreshold) {
			return fetcher.post(
				`/${this.uri}/batch`,
				data.map((item) => this.adapter(item))
			);
		}

		await Promise.allSettled(data.map((item) => this.create(item)));
	}

	public getAll(
		options: APIParametersOptions = {}
	): Promise<APIResponse<ObjectModel> | undefined> {
		let searchParams = Rest.getPageParameter(options);

		if (searchParams) {
			const operator = this.resource.includes('?') ? '&' : '?';

			searchParams = `${operator}${searchParams}`;
		}

		return this.fetcher(`${this.resource}${searchParams}`);
	}

	public getOne(id: number): Promise<ObjectModel | undefined> {
		return this.fetcher(this.getResource(id));
	}

	public getNestedObject(objectName: NestedObjectOptions, parentId: number) {
		return `/${this.uri}/${parentId}/${objectName}`;
	}

	public getResource(id: number | string) {
		return `/${this.uri}/${id}?${this.nestedFields}&nestedFieldsDepth=${this.nestedFieldsDepth}`;
	}

	public async remove(id: number): Promise<void> {
		await this.beforeRemove(id);

		await fetcher.delete(`/${this.uri}/${id}`);
	}

	public async update(
		id: number,
		data: Partial<YupModel>
	): Promise<ObjectModel> {
		await this.beforeUpdate(id, data as YupModel);

		return fetcher.patch(`/${this.uri}/${id}`, this.adapter(data));
	}

	public async removeBatch(ids: number[]): Promise<void> {
		await Promise.allSettled(ids.map((id) => this.remove(id)));
	}

	public async updateBatch(
		ids: number[],
		data: Partial<YupModel>[]
	): Promise<PromiseSettledResult<ObjectModel>[]> {
		return Promise.allSettled(
			data.map((item, index) => this.update(ids[index], item))
		);
	}

	public transformDataFromList(
		response: APIResponse<ObjectModel>
	): APIResponse<ObjectModel> {
		return {
			...response,
			items: response?.items?.map(this.transformData),
		};
	}
}

export default Rest;
