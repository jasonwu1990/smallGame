package com.jason.mini.start.result;

public interface Result<T> {

	/**
	 * 获取视图名称
	 * @return
	 */
	String getViewName();
	
	/**
	 * 返回视图结果
	 * @return
	 */
	T getResult();
	
	/**
	 * 获得字节长度
	 * @return
	 */
	int getBytesLength();
}
