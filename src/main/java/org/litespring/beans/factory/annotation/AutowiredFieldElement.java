package org.litespring.beans.factory.annotation;

import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.AutowireCapableBeanFactory;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.util.ReflectionUtils;

import java.lang.reflect.Field;

public class AutowiredFieldElement extends InjectionElement {
	boolean required;
	
	public AutowiredFieldElement(Field f,boolean required,AutowireCapableBeanFactory factory) {
		super(f,factory);
		this.required = required;
	}
	
	public Field getField(){
		return (Field)this.member;
	}
	@Override
	public void inject(Object target) {
		
		Field field = this.getField();
		try {
			// 该部分参考 测试用例6 部分的代码
			DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
								
			Object value = factory.resolveDependency(desc);
			
			if (value != null) {
				// 将field置为可以访问
				ReflectionUtils.makeAccessible(field);
				field.set(target, value);
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException("Could not autowire field: " + field, ex);
		}
	}

}
