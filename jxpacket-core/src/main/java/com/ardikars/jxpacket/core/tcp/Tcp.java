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

package com.ardikars.jxpacket.core.tcp;

import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public class Tcp extends AbstractPacket {

    private final Tcp.Header header;
    private final Packet payload;

    private Tcp(final Builder builder) {
        this.header = new Tcp.Header(builder);
        this.payload = TransportLayer.valueOf(this.header.getPayloadType().getValue())
                .newInstance(builder.payloadBuffer);
    }

    @Override
    public Tcp.Header getHeader() {
        return this.header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    /**
     * @see <a href="https://tools.ietf.org/html/rfc793">TCP</a>
     */
    public static final class Header implements Packet.Header {

        public static final int TCP_HEADER_LENGTH = 20;

        private final short sourcePort;
        private final short destinationPort;
        private final int sequence;
        private final int acknowledge;
        private final byte dataOffset;
        private final TcpFlags flags;
        private final short windowSize;
        private final short checksum;
        private final short urgentPointer;
        private final byte[] options;

        private Header(final Builder builder) {
            this.sourcePort = builder.sourcePort;
            this.destinationPort = builder.destinationPort;
            this.sequence = builder.sequence;
            this.acknowledge = builder.acknowledge;
            this.dataOffset = builder.dataOffset;
            this.flags = builder.flags;
            this.windowSize = builder.windowSize;
            this.checksum = builder.checksum;
            this.urgentPointer = builder.urgentPointer;
            this.options = builder.options;
        }

        public int getSourcePort() {
            return this.sourcePort & 0xffff;
        }

        public int getDestinationPort() {
            return this.destinationPort & 0xffff;
        }

        public int getSequence() {
            return this.sequence;
        }

        public int getAcknowledge() {
            return this.acknowledge;
        }

        public int getDataOffset() {
            return this.dataOffset & 0xf;
        }

        public TcpFlags getFlags() {
            return flags;
        }

        public int getWindowSize() {
            return this.windowSize & 0xffff;
        }

        public int getChecksum() {
            return this.checksum & 0xffff;
        }

        public int getUrgentPointer() {
            return this.urgentPointer & 0xffff;
        }

        /**
         * Get options.
         * @return returns options.
         */
        public byte[] getOptions() {
            if (options == null) {
                return new byte[0];
            }
            byte[] buffer = new byte[this.options.length];
            System.arraycopy(options, 0, buffer, 0, buffer.length);
            return buffer;
        }

        @Override
        public TransportLayer getPayloadType() {
            return TransportLayer.UNKNOWN;
        }

        @Override
        public int getLength() {
            int length = TCP_HEADER_LENGTH;
            if (this.options != null) {
                length += this.options.length;
            }
            return length;
        }

        @Override
        public ByteBuf getBuffer() {
            ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
            buffer.setShort(0, this.sourcePort);
            buffer.setShort(2, this.destinationPort);
            buffer.setIndex(4, this.sequence);
            buffer.setIndex(8, this.acknowledge);
            buffer.setShort(12, (short) ((this.flags.getShortValue() & 0x1ff) | (this.dataOffset & 0xf) << 12));
            buffer.setShort(14, this.windowSize);
            buffer.setShort(16, this.checksum);
            buffer.setShort(18, this.urgentPointer);
            if (this.options != null) {
                buffer.setBytes(20, this.options);
            }
            return buffer;
        }

        @Override
        public String toString() {
            return new StringBuilder("Header{")
                    .append("sourcePort=").append(getSourcePort())
                    .append(", destinationPort=").append(getDestinationPort())
                    .append(", sequence=").append(getSequence())
                    .append(", acknowledge=").append(getAcknowledge())
                    .append(", dataOffset=").append(getDataOffset())
                    .append(", flags=").append(getFlags())
                    .append(", windowSize=").append(getWindowSize())
                    .append(", checksum=").append(getChecksum())
                    .append(", urgentPointer=").append(getUrgentPointer())
                    .append(", options=").append(Arrays.toString(getOptions()))
                    .append('}').toString();
        }

    }

    public static class Builder implements Packet.Builder {

        private short sourcePort;
        private short destinationPort;
        private int sequence;
        private int acknowledge;
        private byte dataOffset;
        private TcpFlags flags;
        private short windowSize;
        private short checksum;
        private short urgentPointer;
        private byte[] options;

        private ByteBuf payloadBuffer;

        public Builder sourcePort(int sourcePort) {
            this.sourcePort = (short) (sourcePort & 0xffff);
            return this;
        }

        public Builder destinationPort(int destinationPort) {
            this.destinationPort = (short) (destinationPort & 0xffff);
            return this;
        }

        public Builder sequence(int sequence) {
            this.sequence = sequence;
            return this;
        }

        public Builder acknowledge(int acknowledge) {
            this.acknowledge = acknowledge;
            return this;
        }

        public Builder dataOffset(int dataOffset) {
            this.dataOffset = (byte) (dataOffset & 0xf);
            return this;
        }

        public Builder flags(TcpFlags flags) {
            this.flags = flags;
            return this;
        }

        public Builder windowsSize(int windowSize) {
            this.windowSize = (short) (windowSize & 0xffff);
            return this;
        }

        public Builder checksum(int checksum) {
            this.checksum = (short) (checksum & 0xffff);
            return this;
        }

        public Builder urgentPointer(int urgentPointer) {
            this.urgentPointer = (short) (urgentPointer & 0xffff);
            return this;
        }

        public Builder options(byte[] options) {
            this.options = Validate.nullPointer(options, new byte[0]);
            return this;
        }

        public Builder payloadBuffer(ByteBuf buffer) {
            this.payloadBuffer = buffer;
            return this;
        }

        @Override
        public Packet build() {
            return new Tcp(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            this.sourcePort = buffer.getShort(0);
            this.destinationPort = buffer.getShort(2);
            this.sequence = buffer.getInt(4);
            this.acknowledge = buffer.getInt(8);
            short flags = buffer.getShort(12);
            this.dataOffset = (byte) (flags >> 12 & 0xf);
            this.flags = new TcpFlags.Builder().build((short) (flags & 0x1ff));
            this.windowSize = buffer.getShort(14);
            this.checksum = buffer.getShort(16);
            this.urgentPointer = buffer.getShort(18);
            if (this.dataOffset > 5) {
                int optionLength = (this.dataOffset << 2) - Header.TCP_HEADER_LENGTH;
                if (buffer.capacity() < Header.TCP_HEADER_LENGTH + optionLength) {
                    optionLength = buffer.capacity() - Header.TCP_HEADER_LENGTH;
                }
                this.options = new byte[optionLength];
                buffer.getBytes(20, options);
                int length = 20 + optionLength;
                this.payloadBuffer = buffer.copy(length, buffer.capacity() - length);
            }
            this.payloadBuffer = buffer.copy(20, buffer.capacity() - 20);
            return new Tcp(this);
        }

    }

}
