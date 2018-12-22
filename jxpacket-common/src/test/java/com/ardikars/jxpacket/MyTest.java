package com.ardikars.jxpacket;

import io.netty.util.internal.PlatformDependent;
import org.junit.Test;

public class MyTest extends BaseTest {

    @Test
    public void getUnsafe() {
        PlatformDependent.hasUnsafe();
    }

}
