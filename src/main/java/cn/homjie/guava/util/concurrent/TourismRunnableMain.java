package cn.homjie.guava.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class TourismRunnableMain {

	public static void main(String[] args) {
		String name = "明刚红丽黑白";
		Phaser phaser = new Phaser(name.length());
		List<Thread> tourismThread = new ArrayList<Thread>();
		for (char ch : name.toCharArray()) {
			tourismThread.add(new Thread(new TourismRunnable(phaser), "小" + ch));
		}
		for (Thread thread : tourismThread) {
			thread.start();
		}
	}
}
