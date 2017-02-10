package cn.homjie.guava.util.retry;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * @Class BreakPointTask
 * @Description 断点重试任务
 * @Author JieHong
 * @Date 2017年1月10日 下午4:32:00
 * @param <V>
 */
public class BreakPointTask implements Serializable {

	private static final long serialVersionUID = -1L;

	private Logger _log = LoggerFactory.getLogger("BreakPointTask");

	// 任务信息
	private TaskInfo taskInfo;

	// 当前位置
	private int point = 0;
	// 执行次数
	private int times = 0;
	// 任务参数
	private Param param;
	// 分支任务
	private List<TaskFork> taskList = Lists.newArrayList();
	// 检查索引
	transient private int index = 0;

	public BreakPointTask(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
		this.point = taskInfo.getPoint();
		this.times = taskInfo.getTimes();
		this.param = taskInfo.getParam();
	}

	private boolean checkPoint() {
		index++;
		return index > point;
	}

	private void savePoint() {
		point++;
	}

	public final TaskInfo call() {
		times++;
		index = 0;
		_log.info("尝试第" + times + "次执行");

		try {
			for (TaskFork task : taskList) {
				// 检查执行点
				if (checkPoint()) {

					task.handle(param);

					// 保存执行点
					savePoint();
					_log.info("成功运行TaskFork[" + point + "]");
				}
			}
			taskInfo.setComplete(true);
		} catch (Throwable cause) {
			taskInfo.setComplete(false);
			taskInfo.setEx(new ExecutionException(cause));
		}

		taskInfo.setPoint(point);
		taskInfo.setTimes(times);
		return taskInfo;
	}

	/**
	 * @Title join
	 * @Description 加入分支任务
	 * @Author JieHong
	 * @Date 2017年1月10日 下午6:55:27
	 * @param task
	 * @return
	 */
	public BreakPointTask join(TaskFork task) {
		taskList.add(task);
		return this;
	}

	public List<TaskFork> taskList() {
		return taskList;
	}

}
