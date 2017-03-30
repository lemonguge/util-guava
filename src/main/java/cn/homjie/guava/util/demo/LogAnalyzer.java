package cn.homjie.guava.util.demo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class LogAnalyzer
 * @Description 日志分析器
 * @Author JieHong
 * @Date 2017年3月30日 下午6:18:56
 */
public class LogAnalyzer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	private File file;
	private boolean isFile;

	public LogAnalyzer(String pathname) {
		this.file = new File(pathname);
		this.isFile = file.exists() && file.isFile();
	}

	public void execute(LineMatcher matcher) throws IOException {
		if (!isFile) {
			log.info("no file to analysis.");
			return;
		}
		if (matcher == null) {
			log.info("no predicate to match.");
			return;
		}
		FileReader fr = new FileReader(file);
		LineNumberReader lnr = new LineNumberReader(fr);
		// 当前行
		String line = null;

		StringBuilder stackTrace = new StringBuilder();
		int stackTraceNum = 0;
		while ((line = lnr.readLine()) != null) {
			// 异常堆栈作为一行处理
			if (isStackTrace(line)) {
				stackTrace.append(line).append("\n");
				stackTraceNum++;
				continue;
			}
			if (stackTrace.length() > 0) {
				String exception = stackTrace.toString();
				if (matcher.predicate(exception))
					log.info(matcher.out(lnr.getLineNumber() - stackTraceNum, exception));
				
				// 清空
				stackTrace.setLength(0);
				stackTraceNum = 0;
			}
			if (matcher.predicate(line)) {
				log.info(matcher.out(lnr.getLineNumber(), line));
			}
		}
		lnr.close();
	}

	private boolean isStackTrace(String line) {
		if(line.length() < 10)
			return true;
		String date = line.substring(0, 10);
		try {
			formatter.parseDateTime(date);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

}