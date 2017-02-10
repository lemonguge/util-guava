package cn.homjie.guava.util.distributed;

import java.io.Serializable;
import java.util.List;

@FunctionalInterface
public interface Transaction extends Serializable {

	public void execute(List<TaskRelate> monitors, int times) throws Exception;

}
