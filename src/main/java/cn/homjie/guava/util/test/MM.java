package cn.homjie.guava.util.test;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Transaction;

public class MM {

	public static void main(String[] args) throws Exception {
		Description description = new Description(Transaction.ROLLBACK);

		new Service().call(description);
	}

}
