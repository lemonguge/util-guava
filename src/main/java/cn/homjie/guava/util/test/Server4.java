package cn.homjie.guava.util.test;

public class Server4 {

	public void call(boolean local) {
		if (local)
			System.out.println("本地调用服务1");
		else
			System.out.println("远程调用服务1");
	}

}
