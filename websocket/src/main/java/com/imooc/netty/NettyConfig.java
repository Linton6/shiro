package com.imooc.netty;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author Linton
 * @Date 2019/8/11 20:06
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description   存储整个工程的全局变量
 */

public class NettyConfig {

    /**
     * 存储每一个客户端介入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}

