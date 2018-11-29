package com.ardikars.jxpacket.core;

import com.ardikars.jxpacket.core.arp.ArpTest;
import com.ardikars.jxpacket.core.ethernet.VlanArpTest;
import com.ardikars.jxpacket.core.ip.Ip4TcpTest;
import com.ardikars.jxpacket.core.ip.Ip6AuthenticationTest;
import com.ardikars.jxpacket.core.ip.Ip6RoutingTest;
import com.ardikars.jxpacket.core.tcp.Ip4UdpTest;
import com.ardikars.jxpacket.core.tcp.Ip6TcpTest;
import com.ardikars.jxpacket.core.udp.Ip6UdpTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArpTest.class,
        Ip4TcpTest.class,
        Ip4UdpTest.class,
        Ip6TcpTest.class,
        Ip6UdpTest.class,
        VlanArpTest.class,
        Ip6RoutingTest.class,
        Ip6AuthenticationTest.class
})
public class SuiteTest {

    @Test
    public void suiteTest() {
        assert true;
    }

}
