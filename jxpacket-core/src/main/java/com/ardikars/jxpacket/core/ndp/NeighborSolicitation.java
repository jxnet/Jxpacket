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

/**
 * NeighborSolicitation
 */
public class NeighborSolicitation extends AbstractPacket {

    private final NeighborSolicitation.Header header;
    private final Packet payload;

    /**
     * Build Neighbor Solicitation packet.
     * @param builder builder.
     */
    public NeighborSolicitation(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
        this.payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header extends AbstractPacket.Header {

        public static final int NEIGHBOR_SOLICITATION_HEADER_LENGTH = 16;

        private final Inet6Address targetAddress;

        private final NeighborDiscoveryOptions options;

        private final Builder builder;

        private Header(Builder builder) {
            this.targetAddress = builder.targetAddress;
            this.options = builder.options;
            this.buffer = builder.buffer.slice(builder.buffer.readerIndex() - getLength(), getLength());
            this.builder = builder;
        }

        public Inet6Address getTargetAddress() {
            return targetAddress;
        }

        public NeighborDiscoveryOptions getOptions() {
            return options;
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
        public Memory getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.allocate(getLength());
                buffer.writeBytes(targetAddress.getAddress());
                buffer.writeBytes(options.getHeader().getBuffer());
            }
            return buffer;
        }

        @Override
        public NeighborSolicitation.Builder getBuilder() {
            return builder;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\ttargetAddress: ").append(targetAddress).append('\n')
                    .append("\toptions: ").append(options).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ NeighborSolicitation Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends AbstractPacket.Builder {

        private Inet6Address targetAddress;

        private NeighborDiscoveryOptions options;

        private Memory buffer;
        private Memory payloadBuffer;

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
        public Packet build(Memory buffer) {
            byte[] ipv6AddrBuffer = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(ipv6AddrBuffer);
            this.targetAddress = Inet6Address.valueOf(ipv6AddrBuffer);
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new NeighborSolicitation(this);
        }

    }

}
