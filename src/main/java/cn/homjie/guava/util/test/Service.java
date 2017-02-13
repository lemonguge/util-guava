package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskAgent;
import cn.homjie.guava.util.distributed.TaskResult;

public class Service {

	public TaskResult<Void> call(Description description) throws Exception {
		Distributed distributed = new Distributed(description);
		
		distributed.register(() -> new Server1().call(false, description.child())).run();
		
		TaskAgent<String> agent2 = distributed.register(() -> new Server2().call(true, description.child()).get());
		
		distributed.register(() -> {
			new Server3().call(false, agent2.run().get(), description.child());
			return null;
		}).run();
		
		return null;
		
	}

}
