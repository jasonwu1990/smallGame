package com.jason.mini.start.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.jason.mini.start.consts.GameConstants;

public class ConfigManager {

	private static ConfigManager instance = new ConfigManager();
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	Map<String, String> configMap = new HashMap<String, String> ();
	
	long configModifyTime = 0l;
	
	
	public void initConfig() {
		ConfigManager.getInstance().checkFile();
		Thread t = new Thread(new FileCheckRunnable());
		t.start();
	}
	
	public String getPropertyString(String key) {
		return configMap.get(key);
	}
	
	public void checkFile() {
		// 读取配置文件获取action加载路径
		String path = GameConstants.path;
		String fileName = GameConstants.configFileName;
		File file = new File(path+fileName);
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			if(configModifyTime == 0 || 
					(configModifyTime > 0 && configModifyTime < file.lastModified())) {
				prop.load(input);
				Set<Entry<Object, Object>> entrySet = prop.entrySet();
				for(Entry<Object, Object> e : entrySet) {
					configMap.put((String)e.getKey(), (String)e.getValue());
				}
				configModifyTime = System.currentTimeMillis();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class FileCheckRunnable implements Runnable{

		long interval = 1000l;
		
		private FileCheckRunnable(long interval) {
			this.interval = interval;
		}
		
		private FileCheckRunnable() {
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					ConfigManager.getInstance().checkFile();
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
}
