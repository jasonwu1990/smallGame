package com.jason.mini.start.servlet;

import java.lang.reflect.Method;

public class ActionInvocation {

	Class<?> clazz;
	
	Method method;
	
	String actionName;
	
	String methodName;


	public ActionInvocation(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
		this.actionName = clazz.getSimpleName();
		this.methodName = method.getName();
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}
