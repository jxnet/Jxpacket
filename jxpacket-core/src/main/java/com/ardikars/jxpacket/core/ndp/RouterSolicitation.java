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
import com.ardikars.jxpacket.common.UnknownPacket;
import io.netty.buffer.ByteBuf;

/**
 * RouterSolicitation
 */
public class RouterSolicitation extends AbstractPacket {

    private final RouterSolicitation.Header header;
    private final Packet payload;

    /**
     * Builde Router Solicitation packet.
     * @param builder builder.
     */
    public RouterSolicitation(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
        this.payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public RouterSolicitation.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header extends AbstractPacket.Header {

        public static final int ROUTER_SOLICITATION_HEADER_LENGTH = 4;

        private final NeighborDiscoveryOptions options;

        /**
         * Builde Router Solicitation packet.
         * @param builder builder.
         */
        public Header(Builder builder) {
            this.options = builder.options;
            this.buffer = builder.buffer.slice(0,
                    ROUTER_SOLICITATION_HEADER_LENGTH + options.getHeader().getLength());
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
            return ROUTER_SOLICITATION_HEADER_LENGTH + options.getHeader().getLength();
        }

        @Override
        public ByteBuf getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.directBuffer(getLength());
                buffer.writeInt(0);
                buffer.writeBytes(options.getHeader().getBuffer());
            }
            return buffer;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\toptions: ").append(options).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ RouterSolicitation Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends AbstractPacket.Builder {

        private NeighborDiscoveryOptions options;

        private ByteBuf buffer;
        private ByteBuf payloadBuffer;

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
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new RouterSolicitation(this);
        }

    }

}
