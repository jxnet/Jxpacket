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

import com.ardikars.common.util.NamedNumber;
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
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4 extends AbstractPacket {

    private final Header header;
    private final Packet payload;

    private Icmp4(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Icmp4.Header getHeader() {
        return this.header;
    }

    @Override
    public Packet getPayload() {
        return this.payload;
    }

    public static class Header extends Icmp.IcmpHeader {

        private Header(Builder builder) {
            super.typeAndCode = builder.typeAndCode;
            super.checksum = builder.checksum;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return null;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Icmp4Header{");
            sb.append("typeAndCode=").append(super.typeAndCode);
            sb.append(", checksum=").append(super.checksum);
            sb.append('}');
            return sb.toString();
        }

    }

    public static class Builder extends Icmp.IcmpPacketBuilder {

        @Override
        public Packet build() {
            return new Icmp4(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            byte type = buffer.getByte(0);
            byte code = buffer.getByte(1);
            Optional<Icmp.IcmpTypeAndCode> optional = Icmp4.ICMP4_REGISTRY.stream()
                    .filter(typeAndCode -> typeAndCode.getType() == type && typeAndCode.getCode() == code)
                    .findFirst();
            if (optional.isPresent()) {
                super.typeAndCode = optional.get();
            } else {
                super.typeAndCode = new Icmp.IcmpTypeAndCode(type, code, "Unknown");
            }
            if (super.checksum == 0) {
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
                short checksum = ((short) (~accumulation & 0xffff));
                super.checksum = buffer.getShort(2);
                if (checksum == super.checksum) {
                    // valid checksum
                } else {
                    // invalid checksum
                }
                return new Icmp4(this);
            }
            return new Icmp4(this);
        }

    }

    public static final Collection<Icmp.IcmpTypeAndCode> ICMP4_REGISTRY = new HashSet<>();

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
