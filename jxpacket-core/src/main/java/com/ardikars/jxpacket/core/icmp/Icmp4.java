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

package com.ardikars.jxpacket.core.icmp;

import com.ardikars.common.memory.Memory;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4DestinationUnreachable;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4EchoReply;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4EchoRequest;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4ParameterProblem;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4RedirectMessage;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4RouterAdvertisement;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4RouterSolicitation;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4TimeExceeded;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4Timestamp;
import com.ardikars.jxpacket.core.icmp.icmp4.Icmp4TimestampReply;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4 extends AbstractPacket {

    public static final Collection<Icmp.IcmpTypeAndCode> ICMP4_REGISTRY = new HashSet<Icmp.IcmpTypeAndCode>();

    private final Icmp4.Header header;
    private final Packet payload;

    private Icmp4(Builder builder) {
        this.header = new Header(builder);
        this.payload = Icmp.IcmpTypeAndCode.valueOf(this.header.getPayloadType().getValue().byteValue())
                .newInstance(builder.payloadBuffer);
        payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public Icmp4.Header getHeader() {
        return header;
    }

    @Override
    public Packet getPayload() {
        return payload;
    }

    public static class Header extends Icmp.AbstractPacketHeader {

        private final Builder builder;

        private Header(Builder builder) {
            typeAndCode = builder.typeAndCode;
            checksum = builder.checksum;
            buffer = builder.buffer.slice(builder.buffer.readerIndex(), getLength());
            this.builder = builder;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return (T) typeAndCode;
        }

        @Override
        public Builder getBuilder() {
            return builder;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\ttypeAndCode: ").append(typeAndCode).append('\n')
                    .append("\tchecksum: ").append(checksum).append('\n')
                    .toString();
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ Icmp4 Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends Icmp.AbstractPacketBuilder {

        private Memory buffer;
        private Memory payloadBuffer;

        private boolean calculateChecksum;

        private Builder calculateChecksum(boolean caculateChecksum) {
            this.calculateChecksum = caculateChecksum;
            return this;
        }

        @Override
        public Packet build() {
            return new Icmp4(this);
        }

        @Override
        public Packet build(Memory buffer) {
            byte type = buffer.readByte();
            byte code = buffer.readByte();
            super.typeAndCode = Icmp.findIcmpTypeAndCode(type, code, Icmp4.ICMP4_REGISTRY);
            super.checksum = buffer.readShort();
            if (calculateChecksum) {
                int index = 0;
                int accumulation = 0;
                for (int i = 0; i < buffer.capacity() / 2; ++i) {
                    accumulation += 0xffff & buffer.getShort(index);
                    index += 2;
                }
                // pad to an even number of shorts
                if (buffer.capacity() % 2 > 0) {
                    accumulation += (buffer.getByte(index) & 0xff) << 8;
                    index++;
                }
                accumulation = (accumulation >> 16 & 0xffff)
                        + (accumulation & 0xffff);
                short checksum = (short) (~accumulation & 0xffff);
                super.checksum = buffer.readShort();
                if (checksum != super.checksum) {
                    // invalid checksum
                    this.checksum = 0;
                }
            }
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new Icmp4(this);
        }

        @Override
        public void reset() {
            if (buffer != null) {
                reset(buffer.readerIndex(), Header.ICMP_HEADER_LENGTH);
            }
        }

        @Override
        public void reset(int offset, int length) {
            if (buffer != null) {
                Validate.notIllegalArgument(offset + length <= buffer.capacity());
                Validate.notIllegalArgument(typeAndCode != null, ILLEGAL_HEADER_EXCEPTION);
                Validate.notIllegalArgument(checksum >= 0, ILLEGAL_HEADER_EXCEPTION);
                int index = offset;
                buffer.setByte(index, typeAndCode.getType());
                index += 1;
                buffer.setByte(index, typeAndCode.getCode());
                index += 1;
                buffer.setShort(index, checksum);
            }
        }

    }

    static {
        try {
            Class.forName(Icmp4DestinationUnreachable.class.getName());
            Class.forName(Icmp4EchoReply.class.getName());
            Class.forName(Icmp4EchoRequest.class.getName());
            Class.forName(Icmp4ParameterProblem.class.getName());
            Class.forName(Icmp4RedirectMessage.class.getName());
            Class.forName(Icmp4RouterAdvertisement.class.getName());
            Class.forName(Icmp4RouterSolicitation.class.getName());
            Class.forName(Icmp4TimeExceeded.class.getName());
            Class.forName(Icmp4Timestamp.class.getName());
            Class.forName(Icmp4TimestampReply.class.getName());
        } catch (ClassNotFoundException e) {
            //
        }
    }

}
