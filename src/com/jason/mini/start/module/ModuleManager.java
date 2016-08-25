package com.jason.mini.start.module;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.jason.mini.annocation.Action;

public class ModuleManager {

	private static ModuleManager instance = new ModuleManager();
	
	public static ModuleManager getInstance() {
		return instance;
	}
	
	Map<String, Class<?>> action2Class = new HashMap<String, Class<?>> ();
	
	public void initModule() {
		// 读取配置文件获取action加载路径
		String pack = "";
		Set<Class<?>> classes = ModuleManager.getClasses(pack);
		for(Class<?> clazz : classes) {
			initHandleAction(clazz);
		}
	}
	
	private void initHandleAction(Class<?> clazz) {
		if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
			return ;
		}
		Action action = ModuleManager.getAnnocation(clazz, Action.class);
	}
	
	public static <T extends Annotation> T getAnnocation(Class<?> clazz, Class<T> anClass) {
		Class<?> cc = clazz;
		T annotation = null;
		while(null != cc && cc != Object.class) {
			annotation = cc.getAnnotation(anClass);
			if(null != null) {
				return annotation;
			}
			cc = cc.getSuperclass();
		}
		
		return null;
	}
	

	/**
	 * 获取扫描目录下的所有类
	 * @param dir
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packDirName = pack.replace(".", "/"); 
		Enumeration<URL> dirs;
		
		try{
			dirs = Thread.currentThread().getContextClassLoader().getResources(packDirName);
			while(dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if("file".equalsIgnoreCase(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassInPackageByFile(pack, filePath, true, classes);
				}else if("jar".equalsIgnoreCase(protocol)) {
					
				}else if("class".equalsIgnoreCase(protocol)) {
					try {
						Thread.currentThread().getContextClassLoader().loadClass(url.getFile());
					}catch(Throwable e) {
						e.printStackTrace();
					}
					
				}
			}
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return classes;
	}

	private static void findAndAddClassInPackageByFile(String pack, String filePath, final boolean b, Set<Class<?>> classes) {
		File dir = new File(filePath);
		if(!dir.exists() || !dir.isDirectory()) {
			return;
		}
		
		File[] dirFiles = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return ((b && file.isDirectory()) || (file.getName().endsWith(".class")));
			}
			
		});
		
		
		for(File file : dirFiles) {
			if(file.isDirectory()) {
				findAndAddClassInPackageByFile(pack+"."+file.getName(), file.getAbsolutePath(), b, classes);
			}else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try{
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(pack+"."+className));
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
