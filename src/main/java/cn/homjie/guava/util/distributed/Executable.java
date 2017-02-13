package cn.homjie.guava.util.distributed;

@FunctionalInterface
public interface Executable<T> {

	T handle() throws Exception;

}