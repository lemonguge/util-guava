package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskAgent;
import cn.homjie.guava.util.distributed.TaskResult;

public class Server2 {

	public TaskResult<String> call(boolean local, Description description) throws Exception {
		Distributed distributed = new Distributed(description);
		
		distributed.register(() -> {
			if (local)
				System.out.println("本地调用服务3");
			else
				System.out.println("远程调用服务3");
			return null;
		}).run();
		
		TaskAgent<String> agent1 = distributed.register(() -> new Dao21().call());
		
		TaskAgent<Void> agent2 = distributed.register(() -> {
			TaskResult<String> result = agent1.run();
			new Dao22().call(result.get());
			return null;
		});
		agent2.run();
		
		TaskAgent<String> agent3 = distributed.register(() -> {
			TaskResult<String> result = agent1.run();
			return new Dao23().call(result.get());
		});
		
		return agent3.run();
		
	}

}
