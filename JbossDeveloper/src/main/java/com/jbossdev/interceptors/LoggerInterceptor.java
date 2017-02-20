package com.jbossdev.interceptors;

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;

@Logged
@Interceptor
public class LoggerInterceptor {

	private static final Logger logger = Logger.getLogger(LoggerInterceptor.class.getName());

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		Method method = ic.getMethod();
		String simpleName = ic.getTarget().getClass().getSimpleName();
		int endIndex = simpleName.indexOf('$');
		if (endIndex > 0) {
			simpleName = simpleName.substring(0, endIndex);
		}
		String parameters = "";
		parameters = extractParameters(ic, parameters);
		 
		logger.info("Executing method: " + simpleName + "." + method.getName() + parameters);
		return ic.proceed();
	}

	private String extractParameters(InvocationContext ic, String parameters) {
		if (ic.getParameters().length > 0) {
			parameters = " - with params: ";
			for (int i = 0; i < ic.getParameters().length; i++) {
				if (i > 0) {
					parameters += ", ";
				}
				parameters += ic.getParameters()[i].toString();
			}
		}
		return parameters;
	}

}
