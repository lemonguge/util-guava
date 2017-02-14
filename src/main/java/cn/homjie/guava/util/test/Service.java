package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Distributed;
import cn.homjie.guava.util.distributed.TaskResult;

public class Service {

	public void call(Description description) throws Exception {
		Distributed distributed = new Distributed(description);

		distributed.execute(() -> new ServerA().call(false, description.child()), "Service taskS-A");

		TaskResult<String> tr2 = distributed.execute(() -> {
			return new ServerB().call(true, description.child()).get();
		}, "Service taskS-B");

		distributed.execute(() -> new ServerC().call(false, tr2.get(), description.child()), "Service taskS-C");

	}

}
