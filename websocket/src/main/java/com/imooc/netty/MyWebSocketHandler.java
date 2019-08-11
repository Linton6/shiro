package com.imooc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * @Author Linton
 * @Date 2019/8/11 20:11
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description  接受/处理/响应客户端websocket请求的核心业务处理
 */

public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    // 客户端与服务端创建连接时调用
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务端连接开启...");
    }

    // 客户端与服务端断开连接的时候调用
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务端连接关闭...");
    }

    // 服务端接受客户端发送过来的数据结束之后调用
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    // 工程出现异常的时候调用
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private WebSocketServerHandshaker handshaker;
    private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";


// 服务端处理客户端websocket请求的核心方法
    protected void messageReceived(ChannelHandlerContext context, Object msg) throws Exception {
        // 处理客户端向服务端发起HTTP握手请求的业务
        if (msg instanceof FullHttpRequest) {
            handHttpRequest(context, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) { // 处理websocket连接业务

        }


    }

    /**
     * 处理客户端向服务端发起HTTP握手请求的业务
     */
    private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // getDecoderResult  替换为 decoderResult
        if (!req.decoderResult().isSuccess() || !("websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST ));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL,null,false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
    /**
     * 服务端向客户顿发送响应的方法
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res ) {
        if (res.status().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 服务端向客户端发送数据
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

    }
}


