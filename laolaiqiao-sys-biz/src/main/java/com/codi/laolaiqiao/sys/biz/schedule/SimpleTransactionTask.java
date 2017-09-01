package com.codi.laolaiqiao.sys.biz.schedule;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * 以事务的方式执行Task
 * 
 */
public abstract class SimpleTransactionTask<T> extends AbstractTask {

	@Override
	protected void doBiz() {
		transactionTemplate.execute(new TransactionCallback<T>() {

			@Override
			public T doInTransaction(TransactionStatus status) {
				return doTransaction(status);
			}
		});
	}

	public abstract T doTransaction(TransactionStatus status);

}
