package com.jason.mini;

import com.jason.mini.start.MiniServerBootStrap;

/**
 * 启动类
 * @author wufan
 *
 */
public class StartUp {

	public static void main(String[] args) {
		MiniServerBootStrap server = new MiniServerBootStrap();
		try{
			server.startUp();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
