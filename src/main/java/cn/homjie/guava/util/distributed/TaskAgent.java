package cn.homjie.guava.util.distributed;

public class TaskAgent<T> {

	private ForkTask<T> task;
	private ForkTaskInfo<T> info;

	private Distributed distributed;

	public TaskAgent(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) {
		this.task = task;
		this.info = info;
		this.distributed = distributed;
	}

	public TaskResult<T> run() throws Exception {
		Transaction transaction = distributed.getDescription().getTransaction();
		transaction.execute(this);
		return info.getResult();
	}

	ForkTask<T> getTask() {
		return task;
	}

	ForkTaskInfo<T> getInfo() {
		return info;
	}

	Distributed getDistributed() {
		return distributed;
	}

}
