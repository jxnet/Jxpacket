package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.common.memory.Memory;
 import com.ardikars.common.util.Hexs;
import org.junit.After;

/**
 * @author jxpacket 2018/11/29
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Ip6RoutingTest extends BaseTest {

    private byte[] data = Hexs.parseHex(IPV6_ROUTING_UDP);

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
