package cn.homjie.guava.util.demo;

public class Rest {

	public void callA() {
		ServiceA a = new ServiceA();
		a.service();
	}

	public void callB() {
		ServiceB b = new ServiceB();
		b.update();
	}

	public static void main(String[] args) {
		Rest rest = new Rest();
		rest.callA();
		System.out.println("-------------");
		rest.callB();
	}

}
