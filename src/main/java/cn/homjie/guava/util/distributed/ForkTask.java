package cn.homjie.guava.util.distributed;

public class ForkTask<T> {

	private String taskName;
	private Executable<T> business;
	private Executable<Void> rollback;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Executable<T> getBusiness() {
		return business;
	}

	public void setBusiness(Executable<T> business) {
		this.business = business;
	}

	public Executable<Void> getRollback() {
		return rollback;
	}

	public void setRollback(Executable<Void> rollback) {
		this.rollback = rollback;
	}

}
