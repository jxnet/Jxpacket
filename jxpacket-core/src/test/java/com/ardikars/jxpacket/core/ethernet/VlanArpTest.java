package com.ardikars.jxpacket.core.ethernet;

import com.ardikars.jxpacket.core.BaseTest;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;

public class VlanArpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(ETHERNET_II_Q_IN_Q_ARP);

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
