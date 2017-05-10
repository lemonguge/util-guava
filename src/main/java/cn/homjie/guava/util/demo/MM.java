package cn.homjie.guava.util.demo;

public class MM {

	public static void main(String[] args) {
		String line = "2017-05-08 16:18:13.910 [catalina-exec-54] INFO  c.t.m.f.r.FundsAccountResource - orderId: 25f231ebdea74470a482b7fb73c8132a";
		int start = line.indexOf("[");
		int end = line.indexOf("]");
		System.out.println(line.substring(start + 1, end));

	}

}
