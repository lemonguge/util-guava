package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Distributed {

	private Description description;
	// 触发一次，则增加执行次数
	private boolean once = true;

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
		acquire((Executable<Void>) business, taskName, rollback);
	}

	public <T> TaskResult<T> acquire(Executable<T> business) throws Exception {
		return acquire(business, null, null);
	}

	public <T> TaskResult<T> acquire(Executable<T> business, Executable<Void> rollback) throws Exception {
		return acquire(business, null, rollback);
	}

	public <T> TaskResult<T> acquire(Executable<T> business, String taskName) throws Exception {
		return acquire(business, taskName, null);
	}

	public <T> TaskResult<T> acquire(Executable<T> business, String taskName, Executable<Void> rollback) throws Exception {
		if (business == null)
			throw new NullPointerException("任务为空");
		if (once) {
			description.incTimes();
			once = false;
		}
		ForkTask<T> task = new ForkTask<T>();
		ForkTaskInfo<T> info = description.info();

		tasks.add(task);

		if (StringUtils.isBlank(taskName))
			taskName = "任务" + tasks.size();
		task.setBusiness(business);
		task.setRollback(rollback);

		Transaction transaction = description.getTransaction();

		if (description.firstTime()) {
			transaction.firstExec(task, info, tasks);
		} else
			transaction.retryExec(task, info, tasks);

		return info.getResult();
	}

	Description getDescription() {
		return description;
	}

	List<ForkTask<?>> getTasks() {
		return tasks;
	}

}
