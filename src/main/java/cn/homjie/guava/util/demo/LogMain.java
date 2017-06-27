package cn.homjie.guava.util.demo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;

public class LogMain {

	public static void main(String[] args) throws IOException {
		LogAnalyzer la = new LogAnalyzer("D:\\Documents\\Company\\日志\\20170508\\funds_20170508.out");
		
		String[] ids = {"004a8327b0854807bb9b5df0742de5a3",
				"c22032abd77444a1af9fa0f944d29bd3",
				"49b1ceb2e403456b9ca02e8dee88bfb0",
				"11e186aefa5541358291b6e4a01dbdcd",
				"5cd9935fbf694cb1b33cd9e202c7f5bb",
				"3cb504de48724200bc95a671c6e6fff5",
				"328e043f39da44a0a92f41775822cc01",
				"a80fb1895a1746479b620597915298ea",
				"f0a640106ff74f0aa4706abad8cbfd5e",
				"f37935d3d77f48f197492c16748780a1",
				"cdd37066ed20428f896369f9554dc1d4",
				"d752bdefda654a26a3719d45eddbc33f",
				"02a6696985aa43c88bb5c40573978b31",
				"063ca95a27ca405e8ba412a118c02379",
				"9055e614920043d498866c5fe759c9e0",
				"b4527d4efd274919983c84e42685611a",
				"b90703815431437585cc9e9b224c7f3f",
				"bac6b58a96174ee5816ec161b6f617c8",
				"67f483fd03e844d598bae2836e50bd37",
				"1ca61faeac3a46dab2696e48433e0b65",
				"85e1996d6489444d8b0066b1ed15d866",
				"749952d8fa1849bfb2eee9f6193d5293",
				"d67b9c558cb448938de95916424310a8",
				"39a24ad64df045c580e8550b2d762118",
				"d07a5991d81b4b10ab0c848e4b93699f",
				"9b9b9c41cdbf47a0a598e97c89a0db1b",
				"2b3ec6fc72d244ca99fba21d74c37d92",
				"7c9c33c7615743e89a624b568e807d8c",
				"866d376767b34b788fa7d8cbebced585",
				"a9eff057cc994c888bce6db547ecfc27",
				"978c06b264d74dc5a2accbaedb07c90a",
				"832a79dc28e44d94ad443e3d5c94408d",
				"e1a85d02f33f4e4ea31e3dab737adbb7",
				"30b3ad94cfaf4412b9d9cad695e120a1",
				"94f4a8affd644e53904a83a6c36ddfc7",
				"c14b6dba828c4f9d9636a09ecc27acf0",
				"39df876ca63f41f1b0d1f1a1662e8d76",
				"383951d9eb4d4c31abee4866cc058b5f",
				"876c7c0a21ba4dd59adf5d473574a3b5",
				"9f6518c617bb46d5b84bc434cb391081",
				"f1ac505d607449c485a3b76d57b50030",
				"a9bc122c6c7042dab914cdc8596dbaee",
				"66243e8a179947b7b3c3f4f17b4f1b10",
				"874809f633194106a771e034a0f351f9",
				"89a7b44539fd4252aa485da4e27751a5",
				"8092dce6ac914e95894b141fff7da098",
				"4f361e5400824a308dbe53a5cb196d68",
				"256f1856df994ce986d018c38012768d",
				"2b2f395b62634ae59d2d69d001d14498"};
		
		System.out.println(ids.length);
		
		ExecutorService executors = Executors.newCachedThreadPool();
		for (String id : ids) {
			executors.execute(() -> {
				FundsMatcher fundsMatcher = new FundsMatcher(id);
				try {
					la.execute(fundsMatcher.prevMatcher());
					la.execute(fundsMatcher.suffMatcher());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
		executors.shutdown();

	}

	private static class ExceptionMatcher implements LineMatcher {

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
			
			return StringUtils.containsIgnoreCase(line, "c22032abd77444a1af9fa0f944d29bd3");
//			return lineNumber >= 129508 && lineNumber <= 129728 && (
//					StringUtils.containsIgnoreCase(line, "catalina-exec-324") || StringUtils.containsIgnoreCase(line, "exception"));
		}

		@Override
		public String out(int lineNumber, String line) {
			return "[" + lineNumber + "]\n[" + line + "]\n";
		}

	}

}
