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

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class RouterSolicitation extends AbstractPacket {

    private final RouterSolicitation.Header header;
    private final Packet payload;

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
        return payload;
    }

    public static class Header implements Packet.Header {

        public static final int ROUTER_SOLICITATION_HEADER_LENGTH = 4;

        private final NeighborDiscoveryOptions options;

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

        @Override
        public String toString() {
            return new StringBuilder("Header{")
                    .append("options=").append(options)
                    .append('}').toString();
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
