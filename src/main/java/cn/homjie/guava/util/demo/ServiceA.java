package cn.homjie.guava.util.demo;

public class ServiceA {

	private ServiceB b = new ServiceB();

	private ServiceC c = new ServiceC();

	private ServiceD d = new ServiceD();

	public void service() {
		query();
		b.update();
		delete();
		c.query();
		d.save();
	}

	private void delete() {
		System.out.println("A delete");

	}

	private void query() {
		System.out.println("A query");
	}

}
