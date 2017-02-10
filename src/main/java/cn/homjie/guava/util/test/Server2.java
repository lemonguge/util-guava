package cn.homjie.guava.util.test;

public class Server2 {

	public void call(boolean local) {
		q2(false);
		u2(true);
	}

	public void q2(boolean local) {
		if (local)
			System.out.println("本地查询2Q");
		else
			System.out.println("远程查询2Q");
	}

	public void u2(boolean local) {
		if (local)
			System.out.println("本地更新2U");
		else
			System.out.println("远程更新2U");
	}

}
