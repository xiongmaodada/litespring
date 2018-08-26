package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
	// 根据一个类型寻找一个对象
	public Object resolveDependency(DependencyDescriptor descriptor);
}
