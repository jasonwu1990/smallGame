package com.jason.mini.start;

import com.jason.mini.start.Const.GameConstants;
import com.jason.mini.start.config.ConfigManager;
import com.jason.mini.start.handler.StartInBoundhandler;
import com.jason.mini.start.module.ModuleManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class MiniServerBootStrap {
	
	public void startUp() throws InterruptedException {
		
		// 初始化配置文件
		ConfigManager.getInstance().initConfig();
		
		initModule();
		
		String portStr = ConfigManager.getInstance().getPropertyString(GameConstants.KEY_TCPPORT);
		if(portStr == null) {
			throw new InterruptedException();
		}
		int port = Integer.valueOf(portStr);
		
		EventLoopGroup boseGroup = new NioEventLoopGroup();
		EventLoopGroup chileGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap s = new ServerBootstrap();
			s.group(boseGroup, chileGroup).channel(NioServerSocketChannel.class)
			.localAddress(port).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-codec", new HttpServerCodec());
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("readTimeOutHandler",
                            new ReadTimeoutHandler(10));
					ch.pipeline().addLast("ServerHandler", new StartInBoundhandler());
				}
				
			});
			
			ChannelFuture f = s.bind().sync();
			System.out.println("Server started at port:" + port + "......");
			f.channel().closeFuture().sync();
		}finally {
			boseGroup.shutdownGracefully().sync();
			chileGroup.shutdownGracefully().sync();
		}
		
	}

	private void initModule() {
		ModuleManager.getInstance().initModule();
	}
	
}
