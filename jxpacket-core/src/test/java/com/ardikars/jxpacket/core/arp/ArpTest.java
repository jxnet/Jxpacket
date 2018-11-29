package com.ardikars.jxpacket.core.arp;

import com.ardikars.jxpacket.common.UnknownPacket;
import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.arp.Arp;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class ArpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(ETHERNET_II_ARP);

    private Ethernet ethernet;
    private ByteBuf buf = allocator.directBuffer(data.length);

    @Before
    public void before() {
        DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
        NetworkLayer.register(NetworkLayer.ARP, new Arp.Builder());
        buf.setBytes(0, data);
        ethernet = Ethernet.newPacket(buf);
    }

    @Test
    public void payloadType() {
        StreamSupport.stream(ethernet.spliterator(), false)
                .forEach(System.out::println);
    }

    @Test
    public void filter() {
        ethernet.get(Arp.class)
                .forEach(System.out::println);
    }

    @After
    public void after() {
        try {
            buf.release(); // buffer already release to the pool
        } catch (Throwable e) {
            //
        }
    }

}
