package cn.homjie.guava.util.distributed;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class Transactions {

	public static Transaction msgreply() {
		return new Msgreply();
	}

	public static Transaction rollback() {
		return new Rollback();
	}

	private static class Msgreply implements Transaction {

		private static final long serialVersionUID = 5351458749934462716L;

		@Override
		public void execute(List<TaskRelate> relates, int times) throws Exception {
			if (times == 0) {
				firstExec(relates);
			} else {
				retryExec(relates);
			}
		}

		private void firstExec(List<TaskRelate> relates) throws Exception {
			// 被异常所忽略的
			Set<TaskRelate> ignores = Sets.newHashSet();
			for (TaskRelate relate : relates) {
				TaskInfo info = relate.info();
				if (ignores.contains(relate)) {
					info.setTaskLog(TaskLog.MSGREPLY_IGNORE.name());
					continue;
				}
				try {
					info.getForktask().handle();
					info.setTaskLog(TaskLog.SUCCESS.name());
				} catch (Exception e) {
					info.setTaskLog(TaskLog.MSGREPLY_FAILURE.name());
					info.setExceptionLog(ExceptionUtils.getStackTrace(e));

					Set<TaskRelate> needIgnore = relate.children();
					ignores.addAll(needIgnore);
				}
			}

		}

		private void retryExec(List<TaskRelate> relates) throws Exception {
			// 被异常所忽略的
			Set<TaskRelate> ignores = Sets.newHashSet();
			for (TaskRelate relate : relates) {
				TaskInfo info = relate.info();
				if (TaskLog.SUCCESS.name().equals(info.getTaskLog()))
					continue;
				if (ignores.contains(relate)) {
					info.setTaskLog(TaskLog.MSGREPLY_IGNORE.name());
					continue;
				}
				try {
					info.getForktask().handle();
					info.setTaskLog(TaskLog.SUCCESS.name());
				} catch (Exception e) {
					info.setTaskLog(TaskLog.MSGREPLY_FAILURE.name());
					info.setExceptionLog(ExceptionUtils.getStackTrace(e));

					Set<TaskRelate> needIgnore = relate.children();
					ignores.addAll(needIgnore);
				}
			}

		}

	}

	private static class Rollback implements Transaction {

		private static final long serialVersionUID = 2936520186742923987L;

		@Override
		public void execute(List<TaskRelate> relates, int times) throws Exception {
			if (times == 0) {
				firstExec(relates);
			} else {
				retryExec(relates);
			}
		}

		private void firstExec(List<TaskRelate> relates) throws Exception {
			List<TaskInfo> success = Lists.newArrayList();
			Exception ex = null;
			for (TaskRelate relate : relates) {
				TaskInfo info = relate.info();
				try {
					info.getForktask().handle();
					// 执行成功记录
					success.add(info);
					info.setTaskLog(TaskLog.SUCCESS.name());
				} catch (Exception e) {
					info.setTaskLog(TaskLog.FAILURE.name());
					info.setExceptionLog(ExceptionUtils.getStackTrace(e));
					ex = e;
					break;
				}
			}
			if (ex != null) {
				for (TaskInfo info : success) {
					Executable rollback = info.getRollback();
					if (rollback != null) {
						try {
							rollback.handle();
							info.setTaskLog(TaskLog.ROLLBACK_SUCCESS.name());
						} catch (Exception e) {
							info.setTaskLog(TaskLog.ROLLBACK_FAILURE.name());
							info.setExceptionLog(ExceptionUtils.getStackTrace(e));
						}
					} else {
						info.setTaskLog(TaskLog.ROLLBACK_NOTFIND.name());
					}
				}
				throw ex;
			}
		}

		private void retryExec(List<TaskRelate> relates) throws Exception {

		}

	}
}
