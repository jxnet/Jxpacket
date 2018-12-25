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

package com.ardikars.jxpacket.core.udp;

import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.ApplicationLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Udp extends AbstractPacket {

    private final Udp.Header header;
    private final Packet payload;

    private Udp(final Builder builder) {
        this.header = new Header(builder);
        this.payload = ApplicationLayer.valueOf(this.header.getPayloadType().getValue())
                .newInstance(builder.payloadBuffer);
        payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public Udp.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header extends AbstractPacket.Header {

        public static final int UDP_HEADER_LENGTH = 8;

        private final short sourcePort;
        private final short destinationPort;
        private final short length;
        private final short checksum;

        private Header(final Builder builder) {
            this.sourcePort = builder.sourcePort;
            this.destinationPort = builder.destinationPort;
            this.length = builder.length;
            this.checksum = builder.checksum;
            this.buffer = builder.buffer.slice(0, getLength());
        }

        public int getSourcePort() {
            return sourcePort & 0xffff;
        }

        public int getDestinationPort() {
            return destinationPort & 0xffff;
        }

        public int getChecksum() {
            return checksum & 0xffff;
        }

        @Override
        public ApplicationLayer getPayloadType() {
            return ApplicationLayer.valueOf(destinationPort);
        }

        @Override
        public int getLength() {
            return UDP_HEADER_LENGTH;
        }

        @Override
        public ByteBuf getBuffer() {
            if (buffer == null) {
                buffer = ALLOCATOR.directBuffer(getLength());
                buffer.writeShort(this.sourcePort);
                buffer.writeShort(this.destinationPort);
                buffer.writeShort(this.length);
                buffer.writeShort(this.checksum);
            }
            return buffer;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\tsourcePort: ").append(sourcePort).append('\n')
                    .append("\tdestinationPort: ").append(destinationPort).append('\n')
                    .append("\tlength: ").append(length).append('\n')
                    .append("\tchecksum: ").append(checksum).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ Udp Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends AbstractPacket.Builder {

        private short sourcePort;
        private short destinationPort;
        private short length;
        private short checksum;

        private ByteBuf buffer;
        private ByteBuf payloadBuffer;

        public Builder sourcePort(int sourcePort) {
            this.sourcePort = (short) (sourcePort & 0xffff);
            return this;
        }

        public Builder destinationPort(int destinationPort) {
            this.destinationPort = (short) (destinationPort & 0xffff);
            return this;
        }

        public Builder length(int length) {
            this.length = (short) (length & 0xffff);
            return this;
        }

        public Builder checksum(int checksum) {
            this.checksum = (short) (checksum & 0xffff);
            return this;
        }

        public Builder payloadBuffer(ByteBuf payloadBuffer) {
            this.payloadBuffer = payloadBuffer;
            return this;
        }

        @Override
        public Packet build() {
            return new Udp(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            this.sourcePort = buffer.getShort(0);
            this.destinationPort = buffer.getShort(2);
            this.length = buffer.getShort(4);
            this.checksum = buffer.getShort(6);
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new Udp(this);
        }

    }

}
