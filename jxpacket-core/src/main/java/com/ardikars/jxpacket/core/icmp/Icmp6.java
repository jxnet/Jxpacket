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
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6DestinationUnreachable;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6EchoReply;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6EchoRequest;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6HomeAgentAddressDiscoveryReply;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6HomeAgentAddressDiscoveryRequest;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6InverseNeighborDiscoveryAdvertisement;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6InverseNeighborDiscoverySolicitation;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MobilePrefixAdvertisement;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MobilePrefixSolicitation;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MulticastListenerDone;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MulticastListenerQuery;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MulticastListenerReportV1;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6MulticastListenerReportV2;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6NeighborAdvertisement;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6NeighborSolicitation;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6NodeInformationQuery;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6NodeInformationResponse;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6PacketTooBigMessage;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6ParameterProblem;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6RedirectMessage;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6RouterAdvertisement;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6RouterRenumbering;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6RouterSolicitation;
import com.ardikars.jxpacket.core.icmp.icmp6.Icmp6TimeExceeded;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp6 extends AbstractPacket {

    public static final Collection<Icmp.IcmpTypeAndCode> ICMP6_REGISTRY = new HashSet<Icmp.IcmpTypeAndCode>();

    private final Icmp6.Header header;
    private final Packet payload;

    /**
     * Build icmpv6 packet.
     * @param builder builder.
     */
    public Icmp6(Builder builder) {
        this.header = new Header(builder);
        this.payload = Icmp.IcmpTypeAndCode.valueOf(this.header.getPayloadType().getValue().byteValue())
                .newInstance(builder.payloadBuffer);
        payloadBuffer = builder.payloadBuffer;
    }

    @Override
    public Icmp6.Header getHeader() {
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
            buffer = builder.buffer.slice(0, getLength());
            this.builder = builder;
        }

        @Override
        public <T extends NamedNumber> T getPayloadType() {
            return (T) typeAndCode;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("\ttypeAndCode: ").append(typeAndCode).append('\n')
                    .append("\tchecksum: ").append(checksum).append('\n')
                    .toString();
        }

        @Override
        public AbstractPacket.Builder getBuilder() {
            return builder;
        }

    }

    @Override
    public String toString() {
        return new StringBuilder("[ Icmp6 Header (").append(getHeader().getLength()).append(" bytes) ]")
                .append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
                .toString();
    }

    public static class Builder extends Icmp.AbstractPacketBuilder {

        private Memory buffer;
        private Memory payloadBuffer;

        @Override
        public Packet build() {
            return new Icmp6(this);
        }

        @Override
        public Packet build(Memory buffer) {
            byte type = buffer.readByte();
            byte code = buffer.readByte();
            super.typeAndCode = Icmp.findIcmpTypeAndCode(type, code, Icmp6.ICMP6_REGISTRY);
            super.checksum = buffer.readShort();
            this.buffer = buffer;
            this.payloadBuffer = buffer.slice();
            return new Icmp6(this);
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
            Class.forName(Icmp6DestinationUnreachable.class.getName());
            Class.forName(Icmp6EchoReply.class.getName());
            Class.forName(Icmp6EchoRequest.class.getName());
            Class.forName(Icmp6HomeAgentAddressDiscoveryReply.class.getName());
            Class.forName(Icmp6HomeAgentAddressDiscoveryRequest.class.getName());
            Class.forName(Icmp6InverseNeighborDiscoveryAdvertisement.class.getName());
            Class.forName(Icmp6InverseNeighborDiscoverySolicitation.class.getName());
            Class.forName(Icmp6MobilePrefixAdvertisement.class.getName());
            Class.forName(Icmp6MobilePrefixSolicitation.class.getName());
            Class.forName(Icmp6MulticastListenerDone.class.getName());
            Class.forName(Icmp6MulticastListenerQuery.class.getName());
            Class.forName(Icmp6MulticastListenerReportV1.class.getName());
            Class.forName(Icmp6MulticastListenerReportV2.class.getName());
            Class.forName(Icmp6NeighborAdvertisement.class.getName());
            Class.forName(Icmp6NeighborSolicitation.class.getName());
            Class.forName(Icmp6NodeInformationQuery.class.getName());
            Class.forName(Icmp6NodeInformationResponse.class.getName());
            Class.forName(Icmp6PacketTooBigMessage.class.getName());
            Class.forName(Icmp6ParameterProblem.class.getName());
            Class.forName(Icmp6RedirectMessage.class.getName());
            Class.forName(Icmp6RouterAdvertisement.class.getName());
            Class.forName(Icmp6RouterRenumbering.class.getName());
            Class.forName(Icmp6RouterSolicitation.class.getName());
            Class.forName(Icmp6TimeExceeded.class.getName());
        } catch (ClassNotFoundException e) {
            //
        }
    }

}
