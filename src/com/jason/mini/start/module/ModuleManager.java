package com.jason.mini.start.module;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {

	private static ModuleManager instance = new ModuleManager();
	
	public static ModuleManager getInstance() {
		return instance;
	}
	
	Map<String, Class<?>> action2Class = new HashMap<String, Class<?>> ();
	
	public void initModule() {
		
	}
	
}
