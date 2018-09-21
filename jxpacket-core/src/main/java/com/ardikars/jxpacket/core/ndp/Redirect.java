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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Redirect extends AbstractPacket {

    private final Redirect.Header header;
    private final ByteBuf payload;

    public Redirect(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Redirect.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return null;
    }

    public static class Header implements Packet.Header {

        public static final byte REDIRECT_HEADER_LENGTH = 36;

        private Inet6Address targetAddress;
        private Inet6Address destinationAddress;

        private NeighborDiscoveryOptions options;

        private Header(Builder builder) {
            this.targetAddress = builder.targetAddress;
            this.destinationAddress = builder.destinationAddress;
            this.options = builder.options;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public int getLength() {
            return REDIRECT_HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setInt(0, 0);
            buffer.setBytes(4, targetAddress.getAddress());
            buffer.setBytes(20, destinationAddress.getAddress());
            buffer.setBytes(36, options.getHeader().getBuffer());
            return buffer;
        }

    }

    public static class Builder implements Packet.Builder {

        private Inet6Address targetAddress;
        private Inet6Address destinationAddress;

        private NeighborDiscoveryOptions options;

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
            return new Redirect(this);
        }

    }

}
