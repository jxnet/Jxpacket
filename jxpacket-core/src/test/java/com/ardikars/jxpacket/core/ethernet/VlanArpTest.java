package com.ardikars.jxpacket.core.ethernet;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.common.memory.Memory;
 import com.ardikars.common.util.Hexs;
import org.junit.After;

public class VlanArpTest extends BaseTest {

    private byte[] data = Hexs.parseHex(ETHERNET_II_Q_IN_Q_ARP);

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
