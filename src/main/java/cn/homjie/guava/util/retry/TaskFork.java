package cn.homjie.guava.util.retry;

/**
 * @Class TaskFork
 * @Description 断点分支任务
 * @Author JieHong
 * @Date 2017年1月10日 下午6:52:39
 */
@FunctionalInterface
public interface TaskFork {

	void handle(Param param) throws Exception;

}