package cn.homjie.guava.util.reflect;

public class Duck extends TwoLeg {

	public void swim() {
		System.out.println(name() + "游泳");
	}

	private String name() {
		return "鸭子";
	}
}
