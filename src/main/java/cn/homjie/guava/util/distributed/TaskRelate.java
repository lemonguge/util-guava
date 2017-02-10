package cn.homjie.guava.util.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class TaskRelate implements Serializable {

	private static final long serialVersionUID = -7951335101711731019L;

	private TaskInfo info;

	transient private List<TaskRelate> children = Lists.newArrayList();

	TaskRelate(TaskInfo info) {
		this.info = info;
	}

	public void depend(TaskRelate... parents) {
		for (TaskRelate parent : parents) {
			depend(parent);
		}
	}

	private void depend(TaskRelate parent) {
		// 不允许互相依赖
		if (children.stream().anyMatch(tr -> tr == parent)) {
			throw new RuntimeException("[" + info.getTaskName() + "]与[" + parent.info.getTaskName() + "]互相依赖");
		}
		parent.children.add(this);
	}

	TaskInfo info() {
		return info;
	}

	Set<TaskRelate> children() {
		Set<TaskRelate> allChildren = Sets.newHashSet();
		// 所有的被影响的后代
		children(this, allChildren);
		return allChildren;
	}

	private void children(TaskRelate parent, Set<TaskRelate> set) {
		if (!parent.children.isEmpty()) {
			set.addAll(parent.children);
			parent.children.forEach(child -> children(child, set));
		}
	}

}
