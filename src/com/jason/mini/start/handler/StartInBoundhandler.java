package com.jason.mini.start.handler;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jason.mini.start.module.ModuleManager;
import com.jason.mini.start.servlet.ActionInvocation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class StartInBoundhandler extends SimpleChannelInboundHandler<Object>{
	
	private static Log logger = LogFactory.getLog(StartInBoundhandler.class);

    private static final String WEBSOCKET_PATH = "/websocket";

    private WebSocketServerHandshaker handshaker;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest) {
        	handleHttpRequest(ctx, (FullHttpRequest)msg);
        }
        
        if(msg instanceof WebSocketFrame) {
        	handleWebSocketRequest(ctx, (WebSocketFrame)msg);
        }
	}
	
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 如果解码失败了
		if(req.decoderResult().isFailure()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}
		if(req.method() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}
		
		WebSocketServerHandshakerFactory wsfactory = new WebSocketServerHandshakerFactory(
				getWebSocketLocation(req), null, true);
		handshaker = wsfactory.newHandshaker(req);
		if(handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		}else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

    private void handleWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if(frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
			return;
		}
		
		if(frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		
		if(!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format("%s frame types not supported.", frame.getClass().getName()));
		}
		
		String request = ((TextWebSocketFrame)frame).text();
		
		// 心跳包
		if(request.equalsIgnoreCase("heartBeat")) {
			ctx.writeAndFlush(new TextWebSocketFrame(request));
			return;
		}
		
		JSONObject jsonObject = null;
		try{
			jsonObject = (JSONObject) JSON.parse(request);
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
		String cmdValue = jsonObject.getString("cmd");
		
		ActionInvocation ai = ModuleManager.getInstance().getActionInvocation(cmdValue);
		
		if(ai != null) {
			try {
				Object action = ai.getObject();
				TextWebSocketFrame frame1 = (TextWebSocketFrame)(ai.getMethod().invoke(action));
				ctx.writeAndFlush(frame1);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}else{
			ctx.writeAndFlush(new TextWebSocketFrame("不存在的cmd"));
		}
	}
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("远程连接关闭");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    
	private String getWebSocketLocation(FullHttpRequest req) {
		String location =  req.headers().get(HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
	}

	/**
	 * 返回http应答
	 */
	public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request,
			FullHttpResponse response) {
		// 如果是200
		if(response.status().equals(HttpResponseStatus.OK)) {
			ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(byteBuf);
			byteBuf.release();
			HttpUtil.setContentLength(response, response.content().readableBytes());
		}
		
		ChannelFuture future = ctx.channel().writeAndFlush(response);
		if(!HttpUtil.isKeepAlive(request) || !response.status().equals(HttpResponseStatus.OK)) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	cause.printStackTrace();
    	ctx.close().sync();
    }
	
}