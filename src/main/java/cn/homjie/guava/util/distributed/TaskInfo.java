package cn.homjie.guava.util.distributed;

import java.io.Serializable;
import java.util.UUID;

public class TaskInfo implements Serializable {

	private static final long serialVersionUID = 4231823135297119988L;

	transient private String taskName;
	transient private Executable forktask;
	transient private Executable rollback;

	private String taskLog;
	transient private String exceptionLog;

	private String id;
	private String descriptionId;

	public TaskInfo() {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Executable getForktask() {
		return forktask;
	}

	public void setForktask(Executable forktask) {
		this.forktask = forktask;
	}

	public Executable getRollback() {
		return rollback;
	}

	public void setRollback(Executable rollback) {
		this.rollback = rollback;
	}

	public String getTaskLog() {
		return taskLog;
	}

	public void setTaskLog(String taskLog) {
		this.taskLog = taskLog;
	}

	public String getExceptionLog() {
		return exceptionLog;
	}

	public void setExceptionLog(String exceptionLog) {
		this.exceptionLog = exceptionLog;
	}

	public String getId() {
		return id;
	}

	public String getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(String descriptionId) {
		this.descriptionId = descriptionId;
	}

	@Override
	public String toString() {
		return "TaskInfo [taskName=" + taskName + ", taskLog=" + taskLog + "]" + " exceptionLog=" + exceptionLog;
	}

}
