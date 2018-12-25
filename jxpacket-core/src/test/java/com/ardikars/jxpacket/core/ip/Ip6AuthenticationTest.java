package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import com.ardikars.jxpacket.core.icmp.Icmp6;
import com.ardikars.jxpacket.core.ip.ip6.*;
import com.ardikars.jxpacket.core.tcp.Tcp;
import com.ardikars.jxpacket.core.udp.Udp;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

/**
 * @author jxpacket 2018/11/29
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Ip6AuthenticationTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(IPV6_AUTHENTICATION);

    private Ethernet ethernet;
    private ByteBuf buf = allocator.directBuffer(data.length);

    @Before
    public void before() {
        DataLinkLayer.register(DataLinkLayer.EN10MB, new Ethernet.Builder());
        NetworkLayer.register(NetworkLayer.IPV6, new Ip6.Builder());
        TransportLayer.register(TransportLayer.TCP, new Tcp.Builder());
        TransportLayer.register(TransportLayer.UDP, new Udp.Builder());
        TransportLayer.register(TransportLayer.IPV6, new Ip6.Builder());
        TransportLayer.register(TransportLayer.IPV6_ICMP, new Icmp6.Builder());
        TransportLayer.register(TransportLayer.IPV6_AH, new Authentication.Builder());
        TransportLayer.register(TransportLayer.IPV6_DSTOPT, new DestinationOptions.Builder());
        TransportLayer.register(TransportLayer.IPV6_ROUTING, new Routing.Builder());
        TransportLayer.register(TransportLayer.IPV6_FRAGMENT, new Fragment.Builder());
        TransportLayer.register(TransportLayer.IPV6_HOPOPT, new HopByHopOptions.Builder());
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
        ethernet.get(Authentication.class)
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
