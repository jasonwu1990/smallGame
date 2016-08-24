package com.jason.mini.base.action;

import java.io.Serializable;

import com.jason.mini.annocation.Action;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Action
public class BaseAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8215159952587238873L;
	
	/**
	 * 通用socket格式返回
	 * @param result
	 * @return
	 */
	public TextWebSocketFrame getResult(String result) {
		return new TextWebSocketFrame(result);
	}

}
