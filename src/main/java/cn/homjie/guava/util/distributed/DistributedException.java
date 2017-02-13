package cn.homjie.guava.util.distributed;

public class DistributedException extends RuntimeException {

	private static final long serialVersionUID = 3026319152089291579L;

	public DistributedException(Throwable cause) {
		super(cause);
	}

}
