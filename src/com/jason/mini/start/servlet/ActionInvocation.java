package com.jason.mini.start.servlet;

import java.lang.reflect.Method;

public class ActionInvocation {

	Object object;
	
	Method method;
	
	String actionName;
	
	String methodName;

	public ActionInvocation(Object object, Method method) {
		this.object = object;
		this.method = method;
		this.actionName = object.getClass().getSimpleName();
		this.methodName = method.getName();
	}
	
	public Object getObject() {
		return object;
	}


	public void setObject(Object object) {
		this.object = object;
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
