package cn.homjie.guava.util.demo;

import org.apache.commons.lang3.StringUtils;

public class ProductzLogMain {

	public static void main(String[] args) throws Exception {
		LogAnalyzer la = new LogAnalyzer("D:\\Documents\\Company\\日志\\20170627\\product.log");
		ExceptionMatcher matcher = new ExceptionMatcher();
		la.execute(matcher);
	}

	private static class ExceptionMatcher implements LineMatcher {
		
		@Override
		public boolean predicate(int lineNumber, String line) {
			return StringUtils.containsIgnoreCase(line, "F882B6CBBF844BFD9528448EAC60BE7E") && StringUtils.containsIgnoreCase(line, "信用卡账单更新数据");
		}

		@Override
		public String out(int lineNumber, String line) {
			return line;
		}

	}

}
