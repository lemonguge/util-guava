package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.collect.Lists;

public enum Transaction {

	ROLLBACK {

		@Override
		public <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			// 只有成功才有结果，否则抛出异常
			if (info.getResult() != null)
				return;
			Throwable ex = null;
			try {
				T result = task.getBusiness().handle();
				info.setResult(new TaskResult<T>(result));
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
			for (ForkTask<?> forktask : tasks) {
				if (task == forktask)
					break;
				Executable<Void> rollback = forktask.getRollback();
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
		public <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
				task.getBusiness().handle();
			} else if (TaskStatus.ROLLBACK_EXCEPTION.name().equals(info.getTaskStatus())) {
				task.getRollback().handle();
			}
		}
	},

	EVENTUAL {

		@Override
		public <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			// 只有状态不为空才有结果
			if (info.getTaskStatus() != null)
				return;
			exec(task, info);
		}

		@Override
		public <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			String taskStatus = info.getTaskStatus();
			if (RETRY_STATUS.contains(taskStatus)) {
				exec(task, info);
			}
		}

		private <T> void exec(ForkTask<T> task, ForkTaskInfo<T> info) {
			try {
				T result = task.getBusiness().handle();
				info.setResult(new TaskResult<T>(result));
				info.setTaskStatus(TaskStatus.SUCCESS.name());
			} catch (DistributedException e) {
				info.setResult(new TaskResult<T>(e.getCause()));
				info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());
			} catch (Exception e) {
				info.setResult(new TaskResult<T>(e));
				info.setTaskStatus(TaskStatus.EVENTUAL_EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));
			}
		}

	};

	private static final List<String> RETRY_STATUS = Lists.newArrayList(TaskStatus.EVENTUAL_FAILURE.name(), TaskStatus.EVENTUAL_EXCEPTION.name(),
			TaskStatus.EVENTUAL_IGNORE.name());

	public abstract <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception;

	public abstract <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception;

}
