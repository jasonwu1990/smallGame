package com.jason.mini.sayhello.action;

import com.alibaba.fastjson.JSONObject;
import com.jason.mini.annocation.Action;
import com.jason.mini.annocation.Command;
import com.jason.mini.base.action.BaseAction;
import com.jason.mini.sayhello.manager.dto.HelloDto;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Action
public class SayHelloAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1415524559719638847L;
	
	@Command("sayHello")
	public TextWebSocketFrame sayHello() {
		HelloDto dto = new HelloDto();
		return getResult(JSONObject.toJSONString(dto));
	}
	
}
