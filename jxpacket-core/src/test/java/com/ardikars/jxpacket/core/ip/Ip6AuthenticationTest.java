package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.core.BaseTest;
import com.ardikars.jxpacket.core.ethernet.Ethernet;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.After;

/**
 * @author jxpacket 2018/11/29
 * @author <a href="mailto:contact@ardikars.com">Langkuy</a>
 */
public class Ip6AuthenticationTest extends BaseTest {

    private byte[] data = StringUtil.decodeHexDump(IPV6_AUTHENTICATION);

    private ByteBuf buf = allocator.directBuffer(data.length);

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
