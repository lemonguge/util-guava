package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Distributed {

	private Description description;
	// 当前位置
	transient private int point = 0;

	private List<TaskRelate> relates = Lists.newArrayList();

	public Distributed(Description description) {
		this.description = description;
	}

	public TaskRelate register(Executable forktask) {
		return register(forktask, null, null);
	}

	public TaskRelate register(Executable forktask, Executable rollback) {
		return register(forktask, null, rollback);
	}

	public TaskRelate register(Executable forktask, String taskName) {
		return register(forktask, taskName, null);
	}

	public TaskRelate register(Executable forktask, String taskName, Executable rollback) {
		if (forktask == null)
			throw new NullPointerException("任务为空");
		TaskRelate relate = null;
		int times = description.getTimes();
		if (times == 0) {
			relate = new TaskRelate(new TaskInfo());
			relates.add(relate);
		} else {
			relate = relates.get(point);
			point++;
		}

		TaskInfo info = relate.info();
		if (StringUtils.isBlank(taskName))
			taskName = "任务" + relates.size();
		info.setTaskName(taskName);
		info.setForktask(forktask);
		info.setRollback(rollback);
		info.setDescriptionId(description.getId());
		return relate;
	}

	public void execute() throws Exception {
		int times = description.getTimes();
		try {
			description.getTransaction().execute(relates, times);
		} catch (Exception e) {
			description.incTimes();
			throw e;
		}
	}

}
