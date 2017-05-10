package cn.homjie.guava.util.demo;

import org.apache.commons.lang3.StringUtils;

public class FundsMatcher {

	private String mcId;

	private String thread;
	private int lineNumber;

	public FundsMatcher(String mcId) {
		this.mcId = mcId;
	}

	public LineMatcher prevMatcher() {
		return new LineAndThreadMatcher();
	}

	public LineMatcher suffMatcher() {
		return new ReqNoMatcher();
	}

	private class LineAndThreadMatcher implements LineMatcher {
		@Override
		public boolean predicate(int lineNumber, String line) {
			if (thread == null) {
				boolean ok = StringUtils.containsIgnoreCase(line, mcId);
				if (ok) {
					FundsMatcher.this.lineNumber = lineNumber;
					int start = line.indexOf("[");
					int end = line.indexOf("]");
					thread = line.substring(start + 1, end);
				}
				return ok;
			}
			return false;
		}

		@Override
		public String out(int lineNumber, String line) {
			return null;
		}
	}

	private class ReqNoMatcher implements LineMatcher {

		@Override
		public boolean predicate(int lineNumber, String line) {
			return lineNumber >= FundsMatcher.this.lineNumber && lineNumber <= FundsMatcher.this.lineNumber + 200
					&& (StringUtils.containsIgnoreCase(line, thread) || StringUtils.containsIgnoreCase(line, "exception"));
		}

		@Override
		public String out(int lineNumber, String line) {
			if (line.contains("reqNo=")) {
				int start = line.indexOf("reqNo=");
				int end = line.indexOf("&sign=");
				return "[" + mcId + "][" + line.substring(start + 6, end) + "]";
			}
			return null;
		}

	}

}
