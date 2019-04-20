package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.common.memory.Memory;
 import com.ardikars.common.util.Hexs;
import org.junit.After;

public class Ip4TcpTest extends BaseTest {

    private byte[] data = Hexs.parseHex(ETHERNET_IPV4_TCP_SYN);

    private Memory buf = allocator.allocate(data.length);

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
