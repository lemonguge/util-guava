package cn.homjie.guava.util.retry;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;

public class RetryConditionTest {

	private static Callable<Void> nullPointerExceptionTask = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			System.out.println("runtime ex was called.");
			throw new NullPointerException("runtime");
		}
	};

	private static Callable<Void> ioExceptionTask = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			System.out.println("ex was called.");
			throw new IOException("ex");
		}
	};

	private static Callable<Void> errorTask = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			System.out.println("error was called.");
			throw new ThreadDeath();
		}
	};

	public static void main(String[] args) {
		// t1();

		t2();
	}

	static void t1() {
		Retryer<Void> retryer = RetryerBuilder.<Void> newBuilder().retryIfException() // 抛出异常时重试
				.withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 重试3次后停止
				.build();

		try {
			retryer.call(ioExceptionTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
		System.out.println("---1---");

		try {
			retryer.call(nullPointerExceptionTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
		System.out.println("---2---");

		try {
			retryer.call(errorTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
	}

	static void t2() {
		Retryer<Void> retryer = RetryerBuilder.<Void> newBuilder().retryIfRuntimeException() // 抛出runtime异常时重试
				.withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 重试3次后停止
				.build();

		try {
			retryer.call(ioExceptionTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
		System.out.println("---1---");

		try {
			retryer.call(nullPointerExceptionTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
		System.out.println("---2---");

		try {
			retryer.call(errorTask);
		} catch (ExecutionException e) {
			System.out.println("ExecutionException[" + e.getMessage() + "]");
		} catch (RetryException e) {
			System.out.println("RetryException[" + e.getMessage() + "]");
		}
		System.out.println("---3---");
	}
}
