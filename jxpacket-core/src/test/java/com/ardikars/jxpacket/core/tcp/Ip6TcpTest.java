package com.ardikars.jxpacket.core.tcp;

import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.ip.Ip6;
import com.ardikars.jxpacket.core.tcp.Tcp;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class Ip6TcpTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(IPV6_TCP_SYN);

    private Ethernet ethernet;
    private ByteBuf buf = allocator.directBuffer(data.length);

    @Before
    public void before() {
        DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
        NetworkLayer.register(NetworkLayer.IPV6, new Ip6.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
        buf.writeBytes(data);
        ethernet = Ethernet.newPacket(buf);
    }

    @Test
    public void payloadType() {
        StreamSupport.stream(ethernet.spliterator(), false)
                .forEach(System.out::println);
    }

    @Test
    public void filter() {
        ethernet.get(Tcp.class)
                .forEach(System.out::println);
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
