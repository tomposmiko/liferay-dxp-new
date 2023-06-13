/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

export default async function liferayFetcher<T>(
	url: string,
	token: string,
	options?: RequestInit
): Promise<T> {
	// eslint-disable-next-line @liferay/portal/no-global-fetch
	const response = await fetch(url, {
		...options,
		headers: {
			...options?.headers,
			'Accept': 'application/json',
			'x-csrf-token': token,
		},
	});

	if (!response.ok) {
		throw new Error(String(response.status));
	}

	if (response.status !== 204) {
		return response.json();
	}

	return response as any;
}

liferayFetcher.post = <T>(
	url: string,
	token: string,
	data: T,
	options?: RequestInit
) => {
	return liferayFetcher<T>(url, token, {
		body: JSON.stringify(data),
		headers: {
			'Content-Type': 'application/json',
		},
		method: 'POST',
		...options,
	});
};

liferayFetcher.put = <T>(
	url: string,
	token: string,
	data: Partial<T>,
	options?: RequestInit
) =>
	liferayFetcher<T>(url, token, {
		...options,
		body: JSON.stringify(data),
		headers: {
			...options?.headers,
			'Content-Type': 'application/json',
		},
		method: 'PUT',
	});

liferayFetcher.patch = <T>(
	url: string,
	token: string,
	data: Partial<T>,
	options?: RequestInit
) =>
	liferayFetcher<T>(url, token, {
		...options,
		body: JSON.stringify(data),
		headers: {
			...options?.headers,
			'Content-Type': 'application/json',
		},
		method: 'PATCH',
	});

liferayFetcher.delete = <T>(
	url: string,
	token: string,
	options?: RequestInit
) =>
	liferayFetcher<T>(url, token, {
		...options,
		headers: {
			...options?.headers,
		},
		method: 'DELETE',
	});
