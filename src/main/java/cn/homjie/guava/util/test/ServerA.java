package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;

public class ServerA {

	public void call(boolean local, Description description) throws Exception {
		Distributed distributed = new Distributed(description);
		distributed.execute(() -> {
			if (local)
				System.out.println("本地调用服务A");
			else
				System.out.println("远程调用服务A");
		}, "ServiceA task");
	}

}
