package com.ardikars.jxpacket.iso8583;

import com.ardikars.common.util.Hexs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MessageVersionTest {

    byte[] datas = Hexs.parseHex("3031323839");

    @Test
    public void test() {
        for (byte data : datas) {
            System.out.println(MessageVersion.valueOf(data));
        }
    }

}
