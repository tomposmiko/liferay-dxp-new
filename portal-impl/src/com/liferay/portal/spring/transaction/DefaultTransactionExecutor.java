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

package com.liferay.portal.spring.transaction;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.kernel.transaction.TransactionLifecycleManager;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Michael C. Han
 * @author Shuyang Zhou
 */
public class DefaultTransactionExecutor
	implements TransactionExecutor, TransactionHandler {

	public DefaultTransactionExecutor(
		PlatformTransactionManager platformTransactionManager) {

		_platformTransactionManager = platformTransactionManager;
	}

	@Override
	public void commit(
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter) {

		_commit(
			_platformTransactionManager, transactionAttributeAdapter,
			transactionStatusAdapter, null);
	}

	@Override
	public <T> T execute(
			TransactionAttributeAdapter transactionAttributeAdapter,
			UnsafeSupplier<T, Throwable> unsafeSupplier)
		throws Throwable {

		return _execute(
			_platformTransactionManager, transactionAttributeAdapter,
			unsafeSupplier);
	}

	@Override
	public PlatformTransactionManager getPlatformTransactionManager() {
		return _platformTransactionManager;
	}

	@Override
	public void rollback(
			Throwable throwable,
			TransactionAttributeAdapter transactionAttributeAdapter,
			TransactionStatusAdapter transactionStatusAdapter)
		throws Throwable {

		throw _rollback(
			_platformTransactionManager, throwable, transactionAttributeAdapter,
			transactionStatusAdapter);
	}

	@Override
	public TransactionStatusAdapter start(
		TransactionAttributeAdapter transactionAttributeAdapter) {

		return _start(_platformTransactionManager, transactionAttributeAdapter);
	}

	private void _commit(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter,
		Throwable applicationThrowable) {

		Throwable throwable = null;

		try {
			platformTransactionManager.commit(
				transactionStatusAdapter.getTransactionStatus());
		}
		catch (Throwable t) {
			if (applicationThrowable != null) {
				t.addSuppressed(applicationThrowable);
			}

			throwable = t;

			throw t;
		}
		finally {
			if (throwable != null) {
				TransactionLifecycleManager.fireTransactionRollbackedEvent(
					transactionAttributeAdapter, transactionStatusAdapter,
					throwable);
			}
			else {
				TransactionLifecycleManager.fireTransactionCommittedEvent(
					transactionAttributeAdapter, transactionStatusAdapter);
			}

			TransactionExecutorThreadLocal.popTransactionExecutor();
		}
	}

	private <T> T _execute(
			PlatformTransactionManager platformTransactionManager,
			TransactionAttributeAdapter transactionAttributeAdapter,
			UnsafeSupplier<T, Throwable> unsafeSupplier)
		throws Throwable {

		TransactionStatusAdapter transactionStatusAdapter = _start(
			platformTransactionManager, transactionAttributeAdapter);

		T returnValue = null;

		try {
			returnValue = unsafeSupplier.get();
		}
		catch (Throwable throwable) {
			throw _rollback(
				platformTransactionManager, throwable,
				transactionAttributeAdapter, transactionStatusAdapter);
		}

		_commit(
			platformTransactionManager, transactionAttributeAdapter,
			transactionStatusAdapter, null);

		return returnValue;
	}

	private Throwable _rollback(
		PlatformTransactionManager platformTransactionManager,
		Throwable throwable,
		TransactionAttributeAdapter transactionAttributeAdapter,
		TransactionStatusAdapter transactionStatusAdapter) {

		if (transactionAttributeAdapter.rollbackOn(throwable)) {
			try {
				platformTransactionManager.rollback(
					transactionStatusAdapter.getTransactionStatus());
			}
			catch (Throwable t) {
				t.addSuppressed(throwable);

				throw t;
			}
			finally {
				TransactionLifecycleManager.fireTransactionRollbackedEvent(
					transactionAttributeAdapter, transactionStatusAdapter,
					throwable);

				TransactionExecutorThreadLocal.popTransactionExecutor();
			}
		}
		else {
			_commit(
				platformTransactionManager, transactionAttributeAdapter,
				transactionStatusAdapter, throwable);
		}

		return throwable;
	}

	private TransactionStatusAdapter _start(
		PlatformTransactionManager platformTransactionManager,
		TransactionAttributeAdapter transactionAttributeAdapter) {

		TransactionStatusAdapter transactionStatusAdapter =
			new TransactionStatusAdapter(
				platformTransactionManager.getTransaction(
					transactionAttributeAdapter));

		TransactionExecutorThreadLocal.pushTransactionExecutor(this);

		TransactionLifecycleManager.fireTransactionCreatedEvent(
			transactionAttributeAdapter, transactionStatusAdapter);

		return transactionStatusAdapter;
	}

	private final PlatformTransactionManager _platformTransactionManager;

}