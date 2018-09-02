package org.litespring.test.v5;


import org.junit.Test;
import org.litespring.service.v5.PetStoreService;
import org.litespring.tx.TransactionManager;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * 4. 利用 cglib实现方法拦截
 */
public class CGLibTest {
	
	@Test
	public void testCallBack(){
		
		Enhancer enhancer = new Enhancer();
		
        enhancer.setSuperclass(PetStoreService.class);
        // 设置拦截器
        enhancer.setCallback( new TransactionInterceptor() );
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        
        
	}
	
	
	public static class TransactionInterceptor implements MethodInterceptor {
		TransactionManager txManager = new TransactionManager();
	    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
	        
	        txManager.start();
	        Object result = proxy.invokeSuper(obj, args);
	        txManager.commit();
	       
	        return result;
	    }
	}
	
	// 通过filter 过滤toString方法的拦截
	@Test 
	public void  testFilter(){
		
		Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);
        
        enhancer.setInterceptDuringConstruction(false);
        
        Callback[] callbacks = new Callback[]{new TransactionInterceptor(),NoOp.INSTANCE};
        
        Class<?>[] types = new Class<?>[callbacks.length];
		for (int x = 0; x < types.length; x++) {
			types[x] = callbacks[x].getClass();
		}
		
		
		
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);
        
        
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());
        
	}
	private static class ProxyCallbackFilter implements CallbackFilter {	

		public ProxyCallbackFilter() {			
			
		}
		// 选择用第几个拦截器
		public int accept(Method method) {
			if(method.getName().startsWith("place")){
				return 0;
			} else{
				return 1;
			}
			
		}

	}
}
