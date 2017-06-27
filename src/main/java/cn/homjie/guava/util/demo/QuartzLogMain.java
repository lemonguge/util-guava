package cn.homjie.guava.util.demo;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class QuartzLogMain {

	public static void main(String[] args) throws Exception {
		LogAnalyzer la = new LogAnalyzer("D:\\Documents\\Company\\日志\\20170627\\quartz.log");
		ExceptionMatcher matcher = new ExceptionMatcher();
		la.execute(matcher);
		System.out.println(matcher.getErrors());
	}

	private static class ExceptionMatcher implements LineMatcher {
		
		private Set<String> errors = Sets.newHashSet();

		@Override
		public boolean predicate(int lineNumber, String line) {
			/*
			 * productweb
			 * 
			 * StringUtils.containsIgnoreCase(line, "exception") && 
					!StringUtils.containsIgnoreCase(line, "invalid token") && 
					!StringUtils.containsIgnoreCase(line, "令牌已失效") && 
					!StringUtils.containsIgnoreCase(line, "--resource层捕获异常--") && 
					!StringUtils.containsIgnoreCase(line, "javax.ws.rs.NotFoundException: HTTP 404 Not Found") && 
					!StringUtils.containsIgnoreCase(line, "redis.clients.jedis.exceptions.JedisConnectionException: Could not get a resource from the pool")
			 */
			
			/*
			 * product
			 * 
			 * StringUtils.containsIgnoreCase(line, "exception") && 
					!StringUtils.containsIgnoreCase(line, "invalid token") && 
					!StringUtils.containsIgnoreCase(line, "令牌已失效") && 
					!StringUtils.containsIgnoreCase(line, "--resource层捕获异常--") && 
					!StringUtils.containsIgnoreCase(line, "com.alibaba.dubbo.rpc.RpcException: Failed to invoke the method getSysAccountById in the service com.fengdai.user.service.SysAccountService.")
			 */
			// BatchTransferBillReqNoC84E9747AE8F405AA9FAA5DCA7006C6C
			
			return StringUtils.containsIgnoreCase(line, "数据不完整，无法构建");
		}

		@Override
		public String out(int lineNumber, String line) {
			int start = line.indexOf("[");
			int end = line.indexOf("]");
			errors.add(line.substring(start + 1, end));
			return null;
		}

		public Set<String> getErrors() {
			return errors;
		}

	}

}
