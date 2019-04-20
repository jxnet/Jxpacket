package com.ardikars.jxpacket.core;

import com.ardikars.common.memory.Memories;
import com.ardikars.common.net.MacAddress;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.common.memory.Memory;
import org.junit.Test;

public class MyTest {

    @Test
    public void test() throws InterruptedException {
        Memory buf = Memories.allocator().allocate(1200);
        for (int i = 0; i < buf.capacity() / 4; i ++) {
            buf.writeInt(i);
        }
        Packet packet = Ethernet.newPacket(buf);
        Ethernet ethernet = packet.get(Ethernet.class).get(0);
        System.out.println(ethernet);
        System.out.println("Addr: " + ethernet.getHeader().getBuffer().memoryAddress());

        Thread.sleep(1000);
        Ethernet eth = ethernet.getHeader().getBuilder().sourceMacAddress(MacAddress.DUMMY)
                .build();

        System.out.println(eth );
        System.out.println("Addr: " + eth.getHeader().getBuffer().memoryAddress());


    }
}
