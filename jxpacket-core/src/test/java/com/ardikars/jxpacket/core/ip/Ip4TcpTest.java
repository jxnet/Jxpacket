package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;

public class Ip4TcpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(ETHERNET_IPV4_TCP_SYN);

    private ByteBuf buf = allocator.directBuffer(data.length);

    @Override
    public void before() {
        buf.writeBytes(data);
        ethernet = Ethernet.newPacket(buf);
    }

    @After
    public void after() {
        try {
            buf.release();
        } catch (Throwable e) {
            //
        }
    }

}
