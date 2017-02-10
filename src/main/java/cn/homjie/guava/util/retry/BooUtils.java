package cn.homjie.guava.util.retry;

import java.util.Random;

public class BooUtils {

	private static final Random R = new Random();

	public static void random() {
		// 随机获得[0, 1]
		if (R.nextInt(3) != 1)
			throw new RuntimeException("运行异常");
	}

	public static void main(String[] args) {
		int times = 50;
		for (int i = 0; i < times; i++) {
			random();
		}
	}

}
