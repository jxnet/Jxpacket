package com.ardikars.jxpacket.core;

import com.ardikars.jxpacket.core.arp.ArpTest;
import com.ardikars.jxpacket.core.ethernet.VlanArpTest;
import com.ardikars.jxpacket.core.ip.*;
import com.ardikars.jxpacket.core.ndp.*;
import com.ardikars.jxpacket.core.udp.Ip4UdpTest;
import com.ardikars.jxpacket.core.tcp.Ip6TcpTest;
import com.ardikars.jxpacket.core.udp.Ip6UdpTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArpTest.class,
        VlanArpTest.class,
        Ip4TcpTest.class,
        Ip6AuthenticationTest.class,
        Ip6FragmentationTest.class,
        Ip6HopByHopOptionTest.class,
        Ip6OverIp4Test.class,
        Ip6RoutingTest.class,
        NeighborAdvertisementTest.class,
        NeighborSolicitationTest.class,
        RouterAdvertisementTest.class,
        RouterSolicitationTest.class,
        Ip6TcpTest.class,
        Ip4UdpTest.class,
        Ip6UdpTest.class,
})
public class SuiteTest {

    @Test
    public void suiteTest() {
        assert true;
    }

}
