package cn.homjie.guava.util.distributed;

public class TaskMonitor {

	// 当前位置
	private int point = 0;
	// 检查索引
	private int index = 0;

	public boolean checkPoint() {
		index++;
		return index > point;
	}

	public void savePoint() {
		point++;
	}

}
