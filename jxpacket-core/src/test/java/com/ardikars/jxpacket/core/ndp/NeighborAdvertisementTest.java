package com.ardikars.jxpacket.core.ndp;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.common.memory.Memory;
 import com.ardikars.common.util.Hexs;
import org.junit.After;

public class NeighborAdvertisementTest extends BaseTest {

    private byte[] data = Hexs.parseHex(NDP_NEIGHBOR_ADVERTISEMENT);

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
