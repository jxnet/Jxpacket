/**
 * Copyright (C) 2017-2018  Ardika Rommy Sanjaya <contact@ardikars.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.core.ndp;

import com.ardikars.common.memory.Memory;
import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.UnknownPacket;

/**
 * NeighborAdvertisement
 */
public class NeighborAdvertisement extends AbstractPacket {

    private final NeighborAdvertisement.Header header;
    private final Packet payload;

    /**
     * Build Neighbor Advertisement packet.
     * @param builder build.
     */
    public NeighborAdvertisement(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
        this.payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public NeighborAdvertisement.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static final class Header extends AbstractPacket.Header {

        public static final int HEADER_LENGTH = 20;

        private final boolean routerFlag;
        private final boolean solicitedFlag;
        private final boolean overrideFlag;
        private final Inet6Address targetAddress;

        private final NeighborDiscoveryOptions options;

        private final Builder builder;

        private Header(Builder builder) {
            this.routerFlag = builder.routerFlag;
            this.solicitedFlag = builder.solicitedFlag;
            this.overrideFlag = builder.overrideFlag;
            this.targetAddress = builder.targetAddress;
            this.options = builder.options;
            this.buffer = builder.buffer.slice(builder.buffer.readerIndex() - getLength(), getLength());
            this.builder = builder;
        }

        public boolean isRouterFlag() {
            return routerFlag;
        }

        public boolean isSolicitedFlag() {
            return solicitedFlag;
        }

        public boolean isOverrideFlag() {
            return overrideFlag;
        }

        public Inet6Address getTargetAddress() {
            return targetAddress;
        }

        public NeighborDiscoveryOptions getOptions() {
            return options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return (T) UnknownPacket.UNKNOWN_PAYLOAD_TYPE;
        }

        @Override
        public int getLength() {
            return HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public Memory getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.allocate(getLength());
                buffer.writeInt((routerFlag ? 1 : 0) << 31
                        | (solicitedFlag ? 1 : 0) << 30
                        | (overrideFlag ? 1 : 0) << 29);
                buffer.writeBytes(targetAddress.getAddress());
                buffer.writeBytes(options.getHeader().getBuffer());
            }
            return buffer;
        }

        @Override
        public NeighborAdvertisement.Builder getBuilder() {
            return builder;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\trouterFlag: ").append(routerFlag).append('\n')
                    .append("\tsolicitedFlag: ").append(solicitedFlag).append('\n')
                    .append("\toverrideFlag: ").append(overrideFlag).append('\n')
                    .append("\ttargetAddress: ").append(targetAddress).append('\n')
                    .append("\toptions: ").append(options).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ NeighborAdvertisement Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends AbstractPacket.Builder {

        private boolean routerFlag;
        private boolean solicitedFlag;
        private boolean overrideFlag;
        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        private Memory buffer;
        private Memory payloadBuffer;

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
        public Packet build(Memory buffer) {
            int iscratch = buffer.readInt();
            this.routerFlag = (iscratch >> 31 & 0x1) == 1 ? true : false;
            this.solicitedFlag = (iscratch >> 30 & 0x1) == 1 ? true : false;
            this.overrideFlag = (iscratch >> 29 & 0x1) == 1 ? true : false;
            byte[] ipv6AddrBuffer = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(ipv6AddrBuffer);
            this.targetAddress = Inet6Address.valueOf(ipv6AddrBuffer);
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new NeighborAdvertisement(this);
        }

    }

}
