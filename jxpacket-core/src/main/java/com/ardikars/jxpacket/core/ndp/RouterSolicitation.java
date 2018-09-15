package com.ardikars.jxpacket.core.ndp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class RouterSolicitation extends AbstractPacket {

    private final RouterSolicitation.Header header;
    private final ByteBuf payload;

    public RouterSolicitation(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public RouterSolicitation.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class Header implements Packet.Header {

        public static int ROUTER_SOLICITATION_HEADER_LENGTH = 4;

        private NeighborDiscoveryOptions options;

        public Header(Builder builder) {
            this.options = builder.options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return ROUTER_SOLICITATION_HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setInt(0, 0);
            buffer.setBytes(4, options.getHeader().getBuffer());
            return buffer;
        }

    }

    public static class Builder implements Packet.Builder {

        private NeighborDiscoveryOptions options;

        public Builder options(NeighborDiscoveryOptions options) {
            this.options = options;
            return this;
        }

        @Override
        public Packet build() {
            return new RouterSolicitation(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            buffer.readInt();
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            return new RouterSolicitation(this);
        }

    }

}
