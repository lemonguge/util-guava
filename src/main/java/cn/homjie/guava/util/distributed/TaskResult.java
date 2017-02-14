package cn.homjie.guava.util.distributed;

import java.io.Serializable;

public class TaskResult<T> implements Serializable {

	private static final long serialVersionUID = -3144849695440150966L;

	public static final TaskResult<Void> VOID = new TaskResult<Void>();

	private T result;

	private Throwable ex;

	private boolean success;

	private TaskResult() {
		this.result = null;
		this.success = true;
	}

	TaskResult(T result) {
		this.result = result;
		this.success = true;
	}

	TaskResult(Throwable ex) {
		this.ex = ex;
		this.success = false;
	}

	public T get() {
		if (success)
			return result;
		else
			throw new DistributedException(ex);
	}

}
