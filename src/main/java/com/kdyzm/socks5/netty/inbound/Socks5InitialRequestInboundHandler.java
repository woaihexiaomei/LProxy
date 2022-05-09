package com.kdyzm.socks5.netty.inbound;

import com.kdyzm.socks5.netty.properties.ConfigProperties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.*;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kdyzm
 * @date 2021-04-23
 */
@Slf4j
public class Socks5InitialRequestInboundHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {

    public Socks5InitialRequestInboundHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) throws Exception {
        log.debug("初始化socks5链接");
        boolean failure = msg.decoderResult().isFailure();
        if (failure) {
            log.error("初始化socks5失败，请检查是否是socks5协议");
            ReferenceCountUtil.retain(msg);
            ctx.fireChannelRead(msg);
            return;
        }



//        if(configProperties.isAuthentication()){
//        log.info("需要登录");
//            Socks5InitialResponse socks5InitialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.PASSWORD);
//            ctx.writeAndFlush(socks5InitialResponse);
////        }else{
            Socks5InitialResponse socks5InitialResponse = new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH);
            ctx.writeAndFlush(socks5InitialResponse);
        //}
        ctx.pipeline().remove(this);
        ctx.pipeline().remove(Socks5InitialRequestDecoder.class);
    }
}
