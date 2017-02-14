package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskResult;

public class ServerB {

	public TaskResult<String> call(boolean local, Description description) throws Exception {
		Distributed distributed = new Distributed(description);

		distributed.execute(() -> {
			if (local)
				System.out.println("本地调用服务B");
			else
				System.out.println("远程调用服务B");
		}, "ServiceB task");

		TaskResult<String> tr1 = distributed.execute(() -> {
			return new Dao21().call();
		}, "ServiceB taskD-1");

		distributed.execute(() -> {
			String result = tr1.get();
			new Dao22().call(result);
		}, "ServiceB taskD-2");

		TaskResult<String> tr2 = distributed.execute(() -> {
			String result = tr1.get();
			return new Dao23().call(result);
		}, "ServiceB taskD-3");

		return tr2;

	}

}
