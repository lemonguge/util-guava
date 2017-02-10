package cn.homjie.guava.util.distributed;

@FunctionalInterface
public interface Executable {

	void handle() throws Exception;

}