package cn.homjie.guava.util.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

/**
 * @Class Description
 * @Description 信息
 * @Author JieHong
 * @Date 2017年1月11日 下午4:05:10
 */
public class Description implements Serializable {

	private static final long serialVersionUID = -3941038773069693258L;

	// 执行次数
	private int times = 0;
	// 当前位置
	transient private int point = 0;

	// 当前主键
	private String id;
	// 父级主键
	private String pid;

	// 事务处理
	private Transaction transaction;

	// 子info集合
	private List<Description> children = Lists.newArrayList();

	public Description(Transaction transaction) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Description child() {
		if (times == 0) {
			Description child = instance();
			return child;
		} else {
			Description child = children.get(point);
			if (child == null) {
				child = instance();
			}
			point++;
			return child;
		}
	}

	private Description instance() {
		Description child = new Description(transaction);
		child.pid = id;
		children.add(child);
		return child;
	}

	public int getTimes() {
		return times;
	}

	public void incTimes() {
		times++;
	}

	public String getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

}
