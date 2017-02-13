package cn.homjie.guava.util.test;

import cn.homjie.guava.util.retry.BooUtils;

public class Dao22 {

	public void call(String name) {
		BooUtils.random();
		System.out.println("\tDAO调用2["+name+"]");
	}

}
