package com.ardikars.jxpacket.core.ndp;

import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class NeighborSolicitation extends AbstractPacket {

    private final NeighborSolicitation.Header header;
    private final ByteBuf payload;

    public NeighborSolicitation(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class Header implements Packet.Header {

        public static final int NEIGHBOR_SOLICITATION_HEADER_LENGTH = 16;

        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        private Header(Builder builder) {
            this.targetAddress = builder.targetAddress;
            this.options = builder.options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return NEIGHBOR_SOLICITATION_HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setBytes(0, targetAddress.getAddress());
            buffer.setBytes(16, options.getHeader().getBuffer());
            return buffer;
        }

    }

    public static class Builder implements Packet.Builder {

        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        public Builder targetAddress(Inet6Address targetAddress) {
            this.targetAddress = targetAddress;
            return this;
        }

        public Builder options(NeighborDiscoveryOptions options) {
            this.options = options;
            return this;
        }

        @Override
        public Packet build() {
            return new NeighborSolicitation(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            byte[] ipv6AddrBuffer = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(ipv6AddrBuffer);
            this.targetAddress = Inet6Address.valueOf(ipv6AddrBuffer);
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            return new NeighborSolicitation(this);
        }

    }

}
