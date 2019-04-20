package com.ardikars.jxpacket.core.tcp;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.common.memory.Memory;
 import com.ardikars.common.util.Hexs;
import org.junit.After;

public class Ip6TcpTest extends BaseTest {

    private byte[] data = Hexs.parseHex(IPV6_TCP_SYN);

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
