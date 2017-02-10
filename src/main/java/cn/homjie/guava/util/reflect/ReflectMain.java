package cn.homjie.guava.util.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

public class ReflectMain {

	public static void main(String[] args) throws Exception {

		// reflect1();

		reflect2();

	}

	static void reflect2() throws Exception {
		Class<?> clazz = Math.class;
		Object[] params = { 2, 3 };
		int result = (int) MethodUtils.invokeMethod(clazz.newInstance(), "add", params);
		System.out.println(result);
	}

	static void reflect1() throws Exception {
		Class<?> clazz = Duck.class;
		// Duck所有声明的方法，不包括继承的
		Method[] methods = clazz.getDeclaredMethods();
		System.out.println(Arrays.toString(methods));

		// Duck所有的public的方法，包括继承的
		methods = clazz.getMethods();
		System.out.println(Arrays.toString(methods));

		// private方法
		Method method = clazz.getDeclaredMethod("name", ArrayUtils.EMPTY_CLASS_ARRAY);
		method.setAccessible(true);
		System.out.println(method.invoke(clazz.newInstance(), ArrayUtils.EMPTY_OBJECT_ARRAY));

	}

}
