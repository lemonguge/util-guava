package cn.homjie.guava.util.demo;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class RepeatLogMain {

	public static void main(String[] args) throws IOException {
		LogAnalyzer la = new LogAnalyzer("D:\\Documents\\Company\\日志\\20170502\\funds_20170502.out");
		la.execute(new RepeatMatcher("ce4330993a46492d9eb38b7b0da4ced7"));
	}

	private static class RepeatMatcher implements LineMatcher {
		private String mcId;

		private Set<String> threads = Sets.newHashSet();

		private String thread;

		public RepeatMatcher(String mcId) {
			this.mcId = mcId;
		}

		@Override
		public boolean predicate(int lineNumber, String line) {
			boolean ok = StringUtils.containsIgnoreCase(line, mcId);
			if (ok) {
				int start = line.indexOf("[");
				int end = line.indexOf("]");
				String thread = line.substring(start + 1, end);
				if (!threads.contains(thread)) {
					threads.add(thread);
					this.thread = thread;
				} else {
					return false;
				}
			}
			return ok;
		}

		@Override
		public String out(int lineNumber, String line) {
			return "[" + thread + "][" + lineNumber + "]";
		}
	}

}
