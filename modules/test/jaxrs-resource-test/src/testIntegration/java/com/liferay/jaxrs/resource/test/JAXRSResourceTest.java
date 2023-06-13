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

package com.liferay.jaxrs.resource.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class JAXRSResourceTest {

	@Test
	public void testInterfaces() throws InvalidSyntaxException {
		Bundle bundle = FrameworkUtil.getBundle(JAXRSResourceTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		StringBundler sb = new StringBundler();

		for (ServiceReference<?> serviceReference :
				bundleContext.getServiceReferences(
					(String)null, "(osgi.jaxrs.resource=true)")) {

			Object service = bundleContext.getService(serviceReference);

			_scanInterfaces(sb, service.getClass());

			bundleContext.ungetService(serviceReference);
		}

		Assert.assertEquals(sb.toString(), 0, sb.index());
	}

	private void _scanInterfaces(StringBundler sb, Class<?> serviceClass) {
		Class<?> superClass = serviceClass.getSuperclass();

		for (Class<?> interfaceClass : serviceClass.getInterfaces()) {
			if (interfaceClass.isAssignableFrom(superClass)) {
				sb.append(serviceClass.getName());
				sb.append(" should not directly implement interface ");
				sb.append(interfaceClass.getName());
				sb.append(
					" because it is already implemented by the super class ");
				sb.append(superClass.getName());
			}
		}
	}

}