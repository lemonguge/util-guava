package cn.homjie.guava.util.retry;

/**
 * @Class Holder
 * @Description 内部类变量容器
 * @Author JieHong
 * @Date 2016年7月7日 下午5:08:27
 * @param <T>
 */
public class Holder<T> {
	private T t;

	public Holder() {
	}

	public Holder(T t) {
		this.t = t;
	}

	public void set(T t) {
		this.t = t;
	}

	/**
	 * @Title get
	 * @Description 获取变量
	 * @Author JieHong
	 * @Date 2016年7月7日 下午5:09:45
	 * @return
	 */
	public T get() {
		return t;
	}

	@Override
	public String toString() {
		return t == null ? "null" : t.toString();
	}
}