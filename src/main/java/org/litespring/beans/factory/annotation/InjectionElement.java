package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

public abstract class InjectionElement {
	// member可以代表字段、方法、构造方法
	protected Member member;
	protected AutowireCapableBeanFactory factory; 
	InjectionElement(Member member,AutowireCapableBeanFactory factory){
		this.member = member;
		this.factory = factory;		
	}
	
	public abstract void inject(Object target);
}
