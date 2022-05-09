//package com.kdyzm.socks5.netty.inbound;
//
//import com.kdyzm.socks5.netty.properties.ConfigProperties;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.socksx.v5.*;
//import io.netty.util.ReferenceCountUtil;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Set;
//
///**
// * @author kdyzm
// * @date 2021-04-23
// */
//@Slf4j
//@AllArgsConstructor
//public class Socks5CommandRequestInboundHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
//
//    private EventLoopGroup eventExecutors;
//
//    private Set<String> blackList;
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5CommandRequest msg) throws Exception {
//        Socks5AddressType socks5AddressType = msg.dstAddrType();
//        if (!msg.type().equals(Socks5CommandType.CONNECT)) {
//            log.debug("receive commandRequest type={}", msg.type());
//            ReferenceCountUtil.retain(msg);
//            ctx.fireChannelRead(msg);
//            return;
//        }
//        //检查黑名单
////        if (inBlackList(msg.dstAddr())) {
////            log.info("{} 地址在黑名单中，拒绝连接", msg.dstAddr());
////            //假装连接成功
////            DefaultSocks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, socks5AddressType);
////            ctx.writeAndFlush(commandResponse);
////            ctx.pipeline().addLast("HttpServerCodec",new HttpServerCodec());
////            ctx.pipeline().addLast(new BlackListInboundHandler());
////            ctx.pipeline().remove(Socks5CommandRequestInboundHandler.class);
////            ctx.pipeline().remove(Socks5CommandRequestDecoder.class);
////            return;
////        }
//        log.debug("准备连接目标服务器，ip={},port={}", msg.dstAddr(), msg.dstPort());
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(eventExecutors)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        //添加服务端写客户端的Handler
//                        ch.pipeline().addLast(new Dest2ClientInboundHandler(ctx));
//                    }
//                });
//        ChannelFuture future = bootstrap.connect(msg.dstAddr(), msg.dstPort());
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    log.debug("目标服务器连接成功");
//                    //添加客户端转发请求到服务端的Handler
//                    ctx.pipeline().addLast(new Client2DestInboundHandler(future));
//                    DefaultSocks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, socks5AddressType);
//                    ctx.writeAndFlush(commandResponse);
//                    ctx.pipeline().remove(Socks5CommandRequestInboundHandler.class);
//                    ctx.pipeline().remove(Socks5CommandRequestDecoder.class);
//                } else {
//                    log.error("连接目标服务器失败,address={},port={}", msg.dstAddr(), msg.dstPort());
//                    DefaultSocks5CommandResponse commandResponse = new DefaultSocks5CommandResponse(Socks5CommandStatus.FAILURE, socks5AddressType);
//                    ctx.writeAndFlush(commandResponse);
//                    future.channel().close();
//                }
//            }
//        });
//    }
//
//    private boolean inBlackList(String dstAddr) {
//        for (String black : blackList) {
//            if (dstAddr.toLowerCase().endsWith(black)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
