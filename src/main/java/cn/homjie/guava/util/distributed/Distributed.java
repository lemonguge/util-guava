package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Distributed {

	private Description description;

	private boolean incTimes = true;

	private List<ForkTask<?>> tasks = Lists.newArrayList();

	public Distributed(Description description) {
		this.description = description;
	}

	public void execute(NulExecutable business) throws Exception {
		execute(business, null, null);
	}

	public void execute(NulExecutable business, Executable<Void> rollback) throws Exception {
		execute(business, null, rollback);
	}

	public void execute(NulExecutable business, String taskName) throws Exception {
		execute(business, taskName, null);
	}

	public void execute(NulExecutable business, String taskName, Executable<Void> rollback) throws Exception {
		execute((Executable<Void>) business, taskName, rollback);
	}

	public <T> TaskResult<T> execute(Executable<T> business) throws Exception {
		return execute(business, null, null);
	}

	public <T> TaskResult<T> execute(Executable<T> business, Executable<Void> rollback) throws Exception {
		return execute(business, null, rollback);
	}

	public <T> TaskResult<T> execute(Executable<T> business, String taskName) throws Exception {
		return execute(business, taskName, null);
	}

	public <T> TaskResult<T> execute(Executable<T> business, String taskName, Executable<Void> rollback) throws Exception {
		if (business == null)
			throw new NullPointerException("任务为空");
		if (incTimes) {
			description.incTimes();
			incTimes = false;
		}
		ForkTask<T> task = new ForkTask<T>();
		ForkTaskInfo<T> info = description.info();

		tasks.add(task);

		if (StringUtils.isBlank(taskName))
			taskName = "任务" + tasks.size();
		task.setBusiness(business);
		task.setRollback(rollback);

		Transaction transaction = description.getTransaction();
		transaction.execute(task, info, this);
		return info.getResult();
	}

	Description getDescription() {
		return description;
	}

	List<ForkTask<?>> getTasks() {
		return tasks;
	}

}
