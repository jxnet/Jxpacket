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

import com.ardikars.common.net.Inet6Address;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.UnknownPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Redirect extends AbstractPacket {

    private final Redirect.Header header;
    private final Packet payload;

    public Redirect(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
        this.payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public Redirect.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header extends AbstractPacket.Header {

        public static final byte REDIRECT_HEADER_LENGTH = 36;

        private final Inet6Address targetAddress;
        private final Inet6Address destinationAddress;

        private final NeighborDiscoveryOptions options;

        private Header(Builder builder) {
            this.targetAddress = builder.targetAddress;
            this.destinationAddress = builder.destinationAddress;
            this.options = builder.options;
            this.buffer = builder.buffer.slice(0, getLength());
        }

        public Inet6Address getTargetAddress() {
            return targetAddress;
        }

        public Inet6Address getDestinationAddress() {
            return destinationAddress;
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
            return REDIRECT_HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.directBuffer(getLength());
                buffer.writeInt(0);
                buffer.writeBytes(targetAddress.getAddress());
                buffer.writeBytes(destinationAddress.getAddress());
                buffer.writeBytes(options.getHeader().getBuffer());
            }
            return buffer;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\ttargetAddress: ").append(targetAddress).append('\n')
                    .append("\tdestinationAddress: ").append(destinationAddress).append('\n')
                    .append("\toptions: ").append(options).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ Redirect Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends AbstractPacket.Builder {

        private Inet6Address targetAddress;
        private Inet6Address destinationAddress;

        private NeighborDiscoveryOptions options;

        private ByteBuf buffer;
        private ByteBuf payloadBuffer;

        public Builder targetAddrss(Inet6Address targetAddress) {
            this.targetAddress = targetAddress;
            return this;
        }

        public Builder destinationAddress(Inet6Address destinationAddress) {
            this.destinationAddress = destinationAddress;
            return this;
        }

        @Override
        public Packet build() {
            return new Redirect(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            buffer.readInt();
            byte[] target = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(target);
            this.targetAddress = Inet6Address.valueOf(target);
            byte[] destination = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
            buffer.readBytes(destination);
            this.destinationAddress = Inet6Address.valueOf(destination);
            this.options = (NeighborDiscoveryOptions) new NeighborDiscoveryOptions.Builder()
                    .build(buffer);
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new Redirect(this);
        }

    }

}
