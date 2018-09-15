package com.ardikars.jxpacket.core.ndp;

import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class NeighborAdvertisement extends AbstractPacket {

    private final NeighborAdvertisement.Header header;
    private final ByteBuf payload;

    public NeighborAdvertisement(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public NeighborAdvertisement.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static final class Header implements Packet.Header {

        public static int HEADER_LENGTH = 20;

        private boolean routerFlag;
        private boolean solicitedFlag;
        private boolean overrideFlag;
        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        private Header(Builder builder) {
            this.routerFlag = builder.routerFlag;
            this.solicitedFlag = builder.solicitedFlag;
            this.overrideFlag = builder.overrideFlag;
            this.targetAddress = builder.targetAddress;
            this.options = builder.options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setInt(0, (this.routerFlag ? 1 : 0) << 31 |
                    (this.solicitedFlag ? 1 : 0) << 30 |
                    (this.overrideFlag ? 1 : 0) << 29);
            buffer.setBytes(4, this.targetAddress.getAddress());
            buffer.setBytes(20, options.getHeader().getBuffer());
            return buffer;
        }

    }

    public static class Builder implements Packet.Builder {

        private boolean routerFlag;
        private boolean solicitedFlag;
        private boolean overrideFlag;
        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        public Builder routerFlag(boolean routerFlag) {
            this.routerFlag = routerFlag;
            return this;
        }

        public Builder solicitedFlag(boolean solicitedFlag) {
            this.solicitedFlag = solicitedFlag;
            return this;
        }

        public Builder overrideFlag(boolean overrideFlag) {
            this.overrideFlag = overrideFlag;
            return this;
        }

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
            return new NeighborAdvertisement(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            int iscratch = buffer.readInt();
            this.routerFlag = (iscratch >> 31 & 0x1) == 1 ? true : false;
            this.solicitedFlag = (iscratch >> 30 & 0x1) == 1 ? true : false;
            this.overrideFlag = (iscratch >> 29 & 0x1) == 1 ? true : false;
            byte[] ipv6AddrBuffer = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(ipv6AddrBuffer);
            this.targetAddress = Inet6Address.valueOf(ipv6AddrBuffer);
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            return new NeighborAdvertisement(this);
        }

    }

}
