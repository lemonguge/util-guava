package cn.homjie.guava.util.demo;

public class ServiceB {

	private ServiceE e = new ServiceE();

	public void update() {
		query();
		e.delete();
		System.out.println("B update");
	}

	public void query() {
		System.out.println("B query");
	}

}
