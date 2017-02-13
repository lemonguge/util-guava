package cn.homjie.guava.util.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

public enum Transaction {

	ROLLBACK {

		@Override
		<T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			Throwable ex = null;
			try {
				T result = task.getBusiness().handle();

				TaskResult<T> taskResult = new TaskResult<T>(result);
				info.setResult(taskResult);

				info.setTaskStatus(TaskStatus.SUCCESS.name());
			} catch (RollbackFailureException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_FAILURE.name());

				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			}catch (RollbackSuccessException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());

				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			}catch (Exception e) {
				info.setTaskStatus(TaskStatus.EXCEPTION.name());

				info.setStackTrace(ExceptionUtils.getStackTrace(e));
				ex = e;
			}

			if (ex != null) {
				List<TaskAgent<?>> agents = distributed.getAgents();
				// 回滚是否出现异常
				boolean failure = false;
				for (TaskAgent<?> taskAgent : agents) {
					if (task == taskAgent.getTask())
						break;
					Executable<Void> rollback = taskAgent.getTask().getRollback();
					if (rollback != null) {
						try {
							rollback.handle();
							info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());
						} catch (Exception e) {
							failure = true;
							info.setTaskStatus(TaskStatus.ROLLBACK_FAILURE.name());
							info.setStackTrace(ExceptionUtils.getStackTrace(e));
						}
					} else {
						info.setTaskStatus(TaskStatus.ROLLBACK_NOTFIND.name());
					}
				}
				
				if(ex instanceof RollbackFailureException){
					throw (RollbackFailureException) ex;
				}else{
					if(ex instanceof RollbackSuccessException){
						ex = ex.getCause();
					}
					if(failure)
						throw new RollbackFailureException(ex);
					else
						throw new RollbackSuccessException(ex);
				}
				
				
			}
		}

		@Override
		<T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
			if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
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
			if(!TaskStatus.SUCCESS.name().equals(info.getTaskStatus())){
				exec(task, info);
			}
		}

		private <T> void exec(ForkTask<T> task, ForkTaskInfo<T> info) {
			try {
				T result = task.getBusiness().handle();

				TaskResult<T> taskResult = new TaskResult<T>(result);
				info.setResult(taskResult);

				info.setTaskStatus(TaskStatus.SUCCESS.name());
			} catch (DistributedException e) {
				info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());

				TaskResult<T> taskResult = new TaskResult<T>(e.getCause());
				info.setResult(taskResult);
			} catch (Exception e) {
				info.setTaskStatus(TaskStatus.EVENTUAL_FAILURE.name());

				TaskResult<T> taskResult = new TaskResult<T>(e);
				info.setResult(taskResult);
				info.setStackTrace(ExceptionUtils.getStackTrace(e));
			}
		}

	};

	public <T> void execute(TaskAgent<T> agent) throws Exception {
		ForkTask<T> task = agent.getTask();
		ForkTaskInfo<T> info = agent.getInfo();
		Distributed distributed = agent.getDistributed();
		if (distributed.getDescription().firstTime()){
			if(info.getResult() != null)
				return;
			firstExec(task, info, distributed);
		}
		else
			retryExec(task, info, distributed);

	}

	abstract <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

	abstract <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

}
