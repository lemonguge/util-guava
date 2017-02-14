package cn.homjie.guava.util.distributed;

import java.io.Serializable;

public class TaskResult<T> implements Serializable {

	private static final long serialVersionUID = -3144849695440150966L;
	
	static final TaskResult<Void> VOID_SUCCESS = new TaskResult<Void>(true);
	static final TaskResult<Void> VOID_FAILURE = new TaskResult<Void>(false);

	private T result;

	private Throwable ex;

	private boolean success;
	
	private TaskResult(boolean success) {
		this.result = null;
		this.success = success;
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
