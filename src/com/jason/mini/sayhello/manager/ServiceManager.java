package com.jason.mini.sayhello.manager;

public class ServiceManager {

	private static ServiceManager instance = new ServiceManager();
	
	public static ServiceManager getInstance() {
		return instance;
	}
	
	
}
