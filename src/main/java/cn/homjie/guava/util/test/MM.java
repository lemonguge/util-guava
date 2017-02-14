package cn.homjie.guava.util.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.homjie.guava.util.distributed.Description;
import cn.homjie.guava.util.distributed.Transaction;

public class MM {

	public static void main(String[] args) throws Exception {

		// first();
		again();
	}

	static void first() throws Exception {
		Description description = new Description(Transaction.ROLLBACK);
		run(description);
	}

	static void again() throws FileNotFoundException, IOException, Exception {
		FileInputStream fin = new FileInputStream("description.object");
		ObjectInputStream in = new ObjectInputStream(fin);
		Description description = null;
		try {
			description = (Description) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		in.close();
		if (description != null) {
			run(description);
		}
	}

	private static void run(Description description) throws Exception {
		try {
			new Service().call(description);
		} catch (Exception e) {
			e.printStackTrace();
			FileOutputStream fos = new FileOutputStream("description.object");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(description);
			oos.flush();
			oos.close();
		}
	}

}
