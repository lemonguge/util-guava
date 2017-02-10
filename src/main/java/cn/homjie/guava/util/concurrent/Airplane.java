package cn.homjie.guava.util.concurrent;

import java.util.Random;
import java.util.concurrent.Phaser;

public class Airplane {
	private Phaser phaser;
	private Random random;

	public Airplane(int peopleNum) {
		phaser = new Phaser(peopleNum);
		random = new Random();
	}

	/**
	 * 下机
	 */
	public void getOffPlane() {
		try {
			String name = Thread.currentThread().getName();
			Thread.sleep(random.nextInt(500));
			System.out.println(name + " 在飞机在休息着....");
			Thread.sleep(random.nextInt(500));
			System.out.println(name + " 下飞机了");
			System.out.println(name + "phase[" + phaser.getPhase() + "]");
			System.out.println(name + "arrive[" + phaser.arrive() + "]");
			System.out.println(name + "phase[" + phaser.getPhase() + "]");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void doWork() {

		String name = Thread.currentThread().getName();
		System.out.println(name + "准备做 清理 工作，当前阶段[" + phaser.getPhase() + "]");
		// 等待当前阶段完成
		phaser.awaitAdvance(phaser.getPhase());
		System.out.println("当前阶段[" + phaser.getPhase() + "]，飞机的乘客都下机," + name + "可以开始做 清理 工作");

	}

}
