package com.ardikars.jxpacket.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArpTest.class,
        Ip4TcpTest.class,
        Ip4UdpTest.class
})
public class SuiteTest {

    @Test
    public void suiteTest() {
        assert true;
    }

}