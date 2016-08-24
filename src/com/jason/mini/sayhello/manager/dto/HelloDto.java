package com.jason.mini.sayhello.manager.dto;

public class HelloDto {

	String helloInfo;
	
	public HelloDto(String helloInfo) {
		if(helloInfo != null) {
			this.helloInfo = helloInfo;
			return;
		}
		this.helloInfo = String.valueOf(Math.random() * 100);
	}
	
	public HelloDto() {
		this.helloInfo = String.valueOf(Math.random() * 100);
	}

	public String getHelloInfo() {
		return helloInfo;
	}

	public void setHelloInfo(String helloInfo) {
		this.helloInfo = helloInfo;
	}
}
