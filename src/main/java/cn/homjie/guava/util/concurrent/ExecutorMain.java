package cn.homjie.guava.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorMain {

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		int num = 10;
		for (int i = 0; i < num; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e) {
					}
					System.out.println("Asynchronous task");
				}
			});
		}
		System.out.println("done");
		executorService.shutdown();

	}

}
