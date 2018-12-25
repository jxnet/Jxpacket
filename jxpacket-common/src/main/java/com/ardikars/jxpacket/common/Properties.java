package com.ardikars.jxpacket.common;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

final class Properties {

    static final ByteBufAllocator BYTE_BUF_ALLOCATOR;

    static {
        BYTE_BUF_ALLOCATOR = PooledByteBufAllocator.DEFAULT;
    }

}
