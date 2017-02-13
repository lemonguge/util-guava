package cn.homjie.guava.util.test;

public class Dao23 {

	public String call(String name) {
		System.out.println("\tDAO调用3["+name+"]");
		return "Hello! "+name;
	}

}
