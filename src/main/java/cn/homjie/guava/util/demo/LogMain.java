package cn.homjie.guava.util.demo;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class LogMain {

	public static void main(String[] args) throws IOException {
		LogAnalyzer la = new LogAnalyzer("D:\\Documents\\Company\\日志\\productweb.out");
		la.execute(new ExceptionMatcher());
		
	}

	private static class ExceptionMatcher implements LineMatcher {

		@Override
		public boolean predicate(String line) {
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
			return StringUtils.containsIgnoreCase(line, "exception") && 
					!StringUtils.containsIgnoreCase(line, "invalid token") && 
					!StringUtils.containsIgnoreCase(line, "令牌已失效") && 
					!StringUtils.containsIgnoreCase(line, "--resource层捕获异常--") && 
					!StringUtils.containsIgnoreCase(line, "javax.ws.rs.NotFoundException: HTTP 404 Not Found") && 
					!StringUtils.containsIgnoreCase(line, "redis.clients.jedis.exceptions.JedisConnectionException: Could not get a resource from the pool");
		}

		@Override
		public String out(int lineNumber, String line) {
			return "[" + lineNumber + "]\n[" + line + "]\n";
		}

	}

}
