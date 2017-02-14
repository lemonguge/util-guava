package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

public enum Transaction {

	ROLLBACK {

		@Override
		<T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			// 二次调用
			if (info.getResult() != null)
				return;
			Throwable ex = null;
			try {
				T result = task.getBusiness().handle();
				TaskResult<T> taskResult = new TaskResult<T>(result);
				info.setResult(taskResult);
				info.setTaskStatus(TaskStatus.SUCCESS.name());
				return;
			} catch (RollbackFailureException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_FAILURE.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			} catch (RollbackSuccessException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			} catch (Exception e) { // 原始异常
				info.setTaskStatus(TaskStatus.EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));
				ex = e;
			}

			// 回滚是否出现异常
			boolean exception = false;
			List<TaskAgent<?>> agents = distributed.getAgents();
			for (TaskAgent<?> taskAgent : agents) {
				if (task == taskAgent.getTask())
					break;
				Executable<Void> rollback = taskAgent.getTask().getRollback();
				if (rollback != null) {
					try {
						rollback.handle();
						info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());
					} catch (Exception e) {
						exception = true;
						info.setTaskStatus(TaskStatus.ROLLBACK_EXCEPTION.name());
						info.setStackTrace(ExceptionUtils.getStackTrace(e));
					}
				} else {
					info.setTaskStatus(TaskStatus.ROLLBACK_NOTFIND.name());
				}
			}
			// 回滚失败的异常，仍然抛出回滚失败
			if (ex instanceof RollbackFailureException) {
				// TODO 持久化
				throw (RollbackFailureException) ex;
			} else {
				if (ex instanceof RollbackSuccessException) {
					ex = ex.getCause();
				}
				if (exception) {
					// TODO 持久化
					throw new RollbackFailureException(ex);
				} else
					throw new RollbackSuccessException(ex);
			}

		}

		@Override
		<T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
				task.getBusiness().handle();
			} else if (TaskStatus.ROLLBACK_EXCEPTION.name().equals(info.getTaskStatus())) {
				task.getRollback().handle();
			}
		}
	},

	EVENTUAL {

		@Override
		<T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			exec(task, info);
		}

		@Override
		<T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			exec(task, info);
		}

		private <T> void exec(ForkTask<T> task, ForkTaskInfo<T> info) {
			// 二次调用
			if (info.getTaskStatus() != null)
				return;
			try {
				T result = task.getBusiness().handle();
				TaskResult<T> taskResult = new TaskResult<T>(result);
				info.setResult(taskResult);
				info.setTaskStatus(TaskStatus.SUCCESS.name());
			} catch (DistributedException e) {
				TaskResult<T> taskResult = new TaskResult<T>(e.getCause());
				info.setResult(taskResult);
				info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());
				// TODO 持久化
			} catch (Exception e) {
				TaskResult<T> taskResult = new TaskResult<T>(e);
				info.setResult(taskResult);
				info.setTaskStatus(TaskStatus.EVENTUAL_EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));
				// TODO 持久化
			}
		}

	};

	public <T> void execute(TaskAgent<T> agent) throws Exception {
		ForkTask<T> task = agent.getTask();
		ForkTaskInfo<T> info = agent.getInfo();
		Distributed distributed = agent.getDistributed();
		if (distributed.getDescription().firstTime()) {
			firstExec(task, info, distributed);
		} else
			retryExec(task, info, distributed);

	}

	abstract <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

	abstract <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

}
