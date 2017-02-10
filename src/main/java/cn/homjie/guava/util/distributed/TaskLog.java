package cn.homjie.guava.util.distributed;

public enum TaskLog {

	SUCCESS("执行成功"),

	FAILURE("执行失败"),

	ROLLBACK_SUCCESS("执行成功，之后出现异常，调用回滚成功"),

	ROLLBACK_NOTFIND("执行成功，之后出现异常，无需进行回滚"),

	ROLLBACK_FAILURE("执行成功，之后出现异常，调用回滚异常"),

	MSGREPLY_FAILURE("执行失败，即将进行消息重试补偿"),

	MSGREPLY_IGNORE("执行忽略，即将进行消息重试补偿");

	private String log;

	private TaskLog(String log) {
		this.log = log;
	}

	public String log() {
		return log;
	}
}
