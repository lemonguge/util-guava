package cn.homjie.guava.util.demo;

/**
 * @Class LineMatcher
 * @Description 行匹配器
 * @Author JieHong
 * @Date 2017年3月30日 下午6:19:11
 */
public interface LineMatcher {

	boolean predicate(String line);

	String out(int lineNumber, String line);

}
