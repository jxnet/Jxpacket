package com.ardikars.jxpacket.core.udp;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;

public class Ip4UdpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(ETHERNET_IPV4_UDP);

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
