package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Distributed {

	private Description description;

	private boolean incTimes = true;

	private List<TaskAgent<?>> agents = Lists.newArrayList();

	public Distributed(Description description) {
		this.description = description;
	}

	public <T> TaskAgent<T> register(Executable<T> business) {
		return register(business, null, null);
	}

	public <T> TaskAgent<T> register(Executable<T> business, Executable<Void> rollback) {
		return register(business, null, rollback);
	}

	public <T> TaskAgent<T> register(Executable<T> business, String taskName) {
		return register(business, taskName, null);
	}

	public <T> TaskAgent<T> register(Executable<T> business, String taskName, Executable<Void> rollback) {
		if (business == null)
			throw new NullPointerException("任务为空");
		if (incTimes) {
			description.incTimes();
			incTimes = false;
		}
		ForkTask<T> task = new ForkTask<T>();
		ForkTaskInfo<T> info = description.info();

		TaskAgent<T> agent = new TaskAgent<T>(task, info, this);
		agents.add(agent);

		if (StringUtils.isBlank(taskName))
			taskName = "任务" + agents.size();
		task.setBusiness(business);
		task.setRollback(rollback);

		return agent;
	}

	Description getDescription() {
		return description;
	}

	List<TaskAgent<?>> getAgents() {
		return agents;
	}

}
