package cn.homjie.guava.util.retry;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

public class BreakPointTaskDemo {

	private static final Logger log = LoggerFactory.getLogger("Main");

	public static void main(String[] args) {

		TaskInfo info = new TaskInfo();
		info.setPoint(0);

		BreakPointTask<Void> task = task(info);

		Retryer<Void> retryer = RetryerBuilder.<Void> newBuilder().withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
				.withStopStrategy(StopStrategies.stopAfterAttempt(3)).retryIfRuntimeException().build();

		TaskInfo i = null;
		try {
			retryer.call(task);
		} catch (Exception e) {
			log.error("执行异常", e);
			i = task.taskInfo();
		}
		if (i == null)
			return;
		BreakPointTask<Void> f = task(i);
		Retryer<Void> fr = RetryerBuilder.<Void> newBuilder().withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS)).retryIfRuntimeException().build();
		try {
			fr.call(f);
		} catch (Exception e) {
			log.error("执行异常", e);
		}

	}

	static BreakPointTask<Void> task(TaskInfo info) {
		return new BreakPointTask<Void>(info) {

			private static final long serialVersionUID = 4214874170834732473L;

			int p1 = 1;

			@Override
			public Void run() throws Exception {

				int p2 = 10;

				if (checkPoint()) {
					BooUtils.random();
					p1 = 2;
					p2 = 20;
					log.info("do service 1[" + p1 + "][" + p2 + "]");

					savePoint();
				}
				if (checkPoint()) {
					BooUtils.random();
					p1 = 4;
					p2 = 40;
					log.info("do service 2[" + p1 + "][" + p2 + "]");

					savePoint();
				}
				if (checkPoint()) {
					BooUtils.random();
					p1 = 6;
					p2 = 60;
					log.info("do service 3[" + p1 + "][" + p2 + "]");
					savePoint();
				}
				return null;
			}

		};
	}

}
