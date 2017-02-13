package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskAgent;
import cn.homjie.guava.util.distributed.TaskResult;

public class Server3 {

	public TaskResult<Void> call(boolean local,String msg, Description description) throws Exception {
		Distributed distributed = new Distributed(description);
		TaskAgent<Void> agent = distributed.register(() -> {
			if (local)
				System.out.println("本地调用服务3["+msg+"]");
			else
				System.out.println("远程调用服务3["+msg+"]");
			return null;
		});
		return agent.run();
	}

}
