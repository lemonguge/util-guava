package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskRelate;
import cn.homjie.guava.util.distributed.Transactions;
import cn.homjie.guava.util.retry.BooUtils;

public class DistributedMain {

	public static void main(String[] args) throws Exception {
		rollback();

		// msgRetry();
	}

	static void rollback() throws Exception {
		Description description = new Description(Transactions.rollback());
		Distributed distributed = new Distributed(description);

		distributed.register(() -> {
			System.out.println("调用服务1");
		});

		distributed.register(() -> {
			BooUtils.random();
			System.out.println("调用服务2");
		}, () -> {
			System.out.println("回滚调用服务2");
		});

		distributed.register(() -> {
			BooUtils.random();
			System.out.println("调用服务3");
		}, () -> {
			System.out.println("回滚调用服务3");
		});

		distributed.execute();
	}

	static void msgRetry() throws Exception {
		Description description = new Description(Transactions.msgreply());
		Distributed distributed = new Distributed(description);

		TaskRelate relate1 = distributed.register(() -> {
			System.out.println("调用服务1");
		}, () -> {
			System.out.println("回滚调用服务1");
		});

		TaskRelate relate2 = distributed.register(() -> {
			BooUtils.random();
			System.out.println("调用服务2");
		}, () -> {
			System.out.println("回滚调用服务2");
		});

		distributed.register(() -> {
			BooUtils.random();
			System.out.println("调用服务3");
		}, () -> {
			System.out.println("回滚调用服务3");
		}).depend(relate1, relate2);

		distributed.execute();
	}

}
