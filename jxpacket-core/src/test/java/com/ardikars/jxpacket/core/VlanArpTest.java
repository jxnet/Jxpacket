package com.ardikars.jxpacket.core;

import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.core.arp.Arp;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.ethernet.Vlan;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class VlanArpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(ETHERNET_II_Q_IN_Q_ARP);

    private Ethernet ethernet;
    private ByteBuf buf = allocator.directBuffer(1500);

    @Before
    public void before() {
        DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
        NetworkLayer.register(NetworkLayer.DOT1Q_VLAN_TAGGED_FRAMES, new Vlan.Builder());
        NetworkLayer.register(NetworkLayer.ARP, new Arp.Builder());
        buf.setBytes(0, data);
        ethernet = Ethernet.newPacket(buf);
    }

    @org.junit.Test
    public void payloadType() {
        StreamSupport.stream(ethernet.spliterator(), false)
                .map(packet -> packet.getHeader())
                .forEach(System.out::println);
    }

    @Test
    public void filter() {
        ethernet.get(Arp.class)
                .stream().map(pkt -> pkt.getHeader())
                .forEach(System.out::println);
    }

    @After
    public void after() {
        buf.release(); // buffer already release to the pool
    }

}
