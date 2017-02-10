package cn.homjie.guava.util.retry;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * @Class TaskInfo
 * @Description 任务信息
 * @Author JieHong
 * @Date 2017年1月11日 下午4:05:10
 */
public class TaskInfo implements Serializable {

	private static final long serialVersionUID = -3941038773069693258L;

	// 当前位置
	private int point = 0;
	// 执行次数
	private int times = 0;
	// 任务参数
	private Param param;
	// 任务是否完成
	private boolean complete;
	// 执行异常
	private ExecutionException ex;

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public ExecutionException getEx() {
		return ex;
	}

	public void setEx(ExecutionException ex) {
		this.ex = ex;
	}

}
