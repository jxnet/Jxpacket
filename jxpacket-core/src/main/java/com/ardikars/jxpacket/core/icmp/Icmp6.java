/**
 * Copyright (C) 2017  Ardika Rommy Sanjaya
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
import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp6 extends AbstractPacket {

    private final Header header;
    private final Packet payload;

    public Icmp6(Builder builder) {
        this.header = new Header(builder);
        this.payload = null;
    }

    @Override
    public Header getHeader() {
        return null;
    }

    @Override
    public Packet getPayload() {
        return null;
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

    }

    public static class Builder extends Icmp.IcmpPacketBuilder {

        @Override
        public Packet build() {
            return new Icmp6(this);
        }

        @Override
        public Packet build(ByteBuf buffer) {
            byte type = buffer.getByte(0);
            byte code = buffer.getByte(1);
            Optional<Icmp.IcmpTypeAndCode> optional = Icmp6.ICMP6_REGISTRY.stream()
                    .filter(typeAndCode -> typeAndCode.getType() == type && typeAndCode.getCode() == code)
                    .findFirst();
            if (optional.isPresent()) {
                super.typeAndCode = optional.get();
            } else {
                super.typeAndCode = new Icmp.IcmpTypeAndCode(type, code, "Unknown");
            }
            return new Icmp6(this);
        }

    }

    public static final Collection<Icmp.IcmpTypeAndCode> ICMP6_REGISTRY = new HashSet<>();

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
