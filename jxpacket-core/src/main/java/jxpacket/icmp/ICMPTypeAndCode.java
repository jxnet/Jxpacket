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

package jxpacket.icmp;

import jxpacket.common.Decoder;
import jxpacket.common.NamedTwoKeyMap;
import jxpacket.common.TwoKeyMap;
import jxpacket.icmp.icmpv4.ICMPv4DestinationUnreachable;
import jxpacket.icmp.icmpv4.ICMPv4EchoReply;
import jxpacket.icmp.icmpv4.ICMPv4EchoRequest;
import jxpacket.icmp.icmpv4.ICMPv4ParameterProblem;
import jxpacket.icmp.icmpv4.ICMPv4RedirectMessage;
import jxpacket.icmp.icmpv4.ICMPv4RouterAdvertisement;
import jxpacket.icmp.icmpv4.ICMPv4RouterSolicitation;
import jxpacket.icmp.icmpv4.ICMPv4TimeExceeded;
import jxpacket.icmp.icmpv4.ICMPv4Timestamp;
import jxpacket.icmp.icmpv4.ICMPv4TimestampReply;
import jxpacket.icmp.icmpv6.ICMPv6DestinationUnreachable;
import jxpacket.icmp.icmpv6.ICMPv6EchoReply;
import jxpacket.icmp.icmpv6.ICMPv6EchoRequest;
import jxpacket.icmp.icmpv6.ICMPv6HomeAgentAddressDiscoveryReply;
import jxpacket.icmp.icmpv6.ICMPv6HomeAgentAddressDiscoveryRequest;
import jxpacket.icmp.icmpv6.ICMPv6InverseNeighborDiscoveryAdvertisement;
import jxpacket.icmp.icmpv6.ICMPv6InverseNeighborDiscoverySolicitation;
import jxpacket.icmp.icmpv6.ICMPv6MobilePrefixAdvertisement;
import jxpacket.icmp.icmpv6.ICMPv6MobilePrefixSolicitation;
import jxpacket.icmp.icmpv6.ICMPv6MulticastListenerDone;
import jxpacket.icmp.icmpv6.ICMPv6MulticastListenerQuery;
import jxpacket.icmp.icmpv6.ICMPv6MulticastListenerReportV1;
import jxpacket.icmp.icmpv6.ICMPv6MulticastListenerReportV2;
import jxpacket.icmp.icmpv6.ICMPv6NeighborAdvertisement;
import jxpacket.icmp.icmpv6.ICMPv6NeighborSolicitation;
import jxpacket.icmp.icmpv6.ICMPv6NodeInformationQuery;
import jxpacket.icmp.icmpv6.ICMPv6NodeInformationResponse;
import jxpacket.icmp.icmpv6.ICMPv6PacketTooBigMessage;
import jxpacket.icmp.icmpv6.ICMPv6ParameterProblem;
import jxpacket.icmp.icmpv6.ICMPv6RedirectMessage;
import jxpacket.icmp.icmpv6.ICMPv6RouterAdvertisement;
import jxpacket.icmp.icmpv6.ICMPv6RouterRenumbering;
import jxpacket.icmp.icmpv6.ICMPv6RouterSolicitation;
import jxpacket.icmp.icmpv6.ICMPv6TimeExceeded;
import jxpacket.ndp.NeighborAdvertisement;
import jxpacket.ndp.NeighborSolicitation;
import jxpacket.ndp.Redirect;
import jxpacket.ndp.RouterAdvertisement;
import jxpacket.ndp.RouterSolicitation;
import jxpacket.Packet;
import jxpacket.mld.MulticastListenerReportV2;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public abstract class ICMPTypeAndCode extends NamedTwoKeyMap<Byte, Byte, ICMPTypeAndCode> implements Decoder<Packet, ByteBuffer> {

    protected static Map<TwoKeyMap<Byte, Byte>, ICMPTypeAndCode> registry =
            new HashMap<TwoKeyMap<Byte, Byte>, ICMPTypeAndCode>();

    protected ICMPTypeAndCode(Byte firstKey, Byte secondKey, String name) {
        super(firstKey, secondKey, name);
    }

    public static ICMPTypeAndCode register(ICMPTypeAndCode typeAndCode) {
        return registry.put(typeAndCode.getKey(), typeAndCode);
    }

    public static ICMPTypeAndCode unregister(ICMPTypeAndCode typeAndCode) {
        return registry.remove(typeAndCode.getKey());
    }

    public static ICMPTypeAndCode getTypeAndCode(Byte type, Byte code) {
        TwoKeyMap<Byte, Byte> key = TwoKeyMap.newInstance(type, code);
        ICMPTypeAndCode typeAndCode = registry.get(key);
        if (typeAndCode == null) {
            return ICMPUnknownTypeAndCode.UNKNOWN;
        }
        return registry.get(key);
    }

    public byte getType() {
        return super.getKey().getFirstKey();
    }
    public byte getCode() {
        return super.getKey().getSecondKey();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[Type: " + (this.getType() & 0xff))
                .append(", Code: " + (this.getCode() & 0xff))
                .append("]").toString();
    }

    //@Override
    public Packet decode(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        buffer.rewind();
        int type = getType() & 0xff;
        int code = getCode() & 0xff;
        switch (type) {
            case 133:
                switch (code) {
                    case 0: return RouterSolicitation.newInstance(buffer);
                    default: return null;

                }
            case 134:
                switch (code) {
                    case 0: return RouterAdvertisement.newInstance(buffer);
                    default: return null;
                }
            case 135:
                switch (code) {
                    case 0: return NeighborSolicitation.newInstance(buffer);
                    default: return null;
                }
            case 136:
                switch (code) {
                    case 0: return NeighborAdvertisement.newInstance(buffer);
                    default: return null;
                }
            case 137:
                switch (code) {
                    case 0: return Redirect.newInstance(buffer);
                    default: return null;
                }
            case 143:
                switch (code) {
                    case 0: return MulticastListenerReportV2.newInstance(buffer);
                    default: return null;
                }
            default: return null;
        }
    }

    static {
        if (registry.size() == 0) {
            register(ICMPv4DestinationUnreachable.DESTINATION_NETWORK_UNREACHABLE);
            register(ICMPv4DestinationUnreachable.DESTINATION_HOST_UNREACHABLE);
            register(ICMPv4DestinationUnreachable.DESTINATION_PROTOCOL_UNREACHABLE);
            register(ICMPv4DestinationUnreachable.DESTINATION_PORT_UNREACHABLE);
            register(ICMPv4DestinationUnreachable.FRAGMENTATION_REQUIRED);
            register(ICMPv4DestinationUnreachable.SOURCE_ROUTE_FAILED);
            register(ICMPv4DestinationUnreachable.DESTINATION_NETWORK_UNKNOWN);
            register(ICMPv4DestinationUnreachable.DESTINATION_HOST_UNKOWN);
            register(ICMPv4DestinationUnreachable.SOURCE_HOST_ISOLATED);
            register(ICMPv4DestinationUnreachable.NETWORK_ADMINISTRATIVELY_PROHIBITED);
            register(ICMPv4DestinationUnreachable.HOST_ADMINISTRATIVELY_PROHIBITED);
            register(ICMPv4DestinationUnreachable.NETWORK_UNREACHABLE_FOR_TOS);
            register(ICMPv4DestinationUnreachable.HOST_UNREACHABLE_FOR_TOS);
            register(ICMPv4DestinationUnreachable.COMMUNICATION_ADMINISTRATIVELY_PROHIBITED);
            register(ICMPv4DestinationUnreachable.HOST_PRECEDENCE_VIOLATION);
            register(ICMPv4DestinationUnreachable.PRECEDENCE_CUTOFF_IN_EFFECT);

            register(ICMPv4EchoReply.ECHO_REPLY);

            register(ICMPv4EchoRequest.ECHO_REQUEST);

            register(ICMPv4ParameterProblem.POINTER_INDICATES_THE_ERROR);
            register(ICMPv4ParameterProblem.MISSING_REQUIRED_OPTION);
            register(ICMPv4ParameterProblem.BAD_LENGTH);

            register(ICMPv4RedirectMessage.REDIRECT_DATAGRAM_FOR_NETWORK);

            register(ICMPv4RouterAdvertisement.ROUTER_ADVERTISEMENT);

            register(ICMPv4RouterSolicitation.ROUTER_DISCOVERY_SELECTION_SOLICITATION);

            register(ICMPv4TimeExceeded.TTL_EXPIRED_IN_TRANSIT);
            register(ICMPv4TimeExceeded.FRAGMENT_REASSEMBLY_TIME_EXEEDED);

            register(ICMPv4Timestamp.TIMESTAMP);

            register(ICMPv4TimestampReply.TIMESTAMP_REPLY);

            // ICMPv6InverseNeighborDiscoverySolicitation

            register(ICMPv6EchoRequest.ECHO_REQUEST);

            register(ICMPv6EchoReply.ECHO_REPLY);

            register(ICMPv6DestinationUnreachable.NO_ROUTE_TO_DESTINATION);
            register(ICMPv6DestinationUnreachable.COMMUNICATION_WITH_DESTINATION_ADMINIS_TRATIVELY_PROHIBITED);
            register(ICMPv6DestinationUnreachable.BEYOND_SCOPE_OF_SOURCE_ADDRESS);
            register(ICMPv6DestinationUnreachable.ADDRESS_UNREACHABLE);
            register(ICMPv6DestinationUnreachable.PORT_UNREACHABLE);
            register(ICMPv6DestinationUnreachable.SOURCE_ADDRESS_FAILED);
            register(ICMPv6DestinationUnreachable.REJECT_ROUTE_TO_DESTINATION);
            register(ICMPv6DestinationUnreachable.ERROR_IN_SOURCE_ROUTING_HEADER);

            register(ICMPv6PacketTooBigMessage.PACKET_TOO_BIG_MESSAGE);

            register(ICMPv6ParameterProblem.ERRORNEOUS_HEADER_FIELD_ENCOUTERED);
            register(ICMPv6ParameterProblem.UNRECOGNIZED_NEXT_HEADER_TYPE_ENCOUNTERED);
            register(ICMPv6ParameterProblem.UNRECOGNIZED_IPV6_OPTION_ENCOUNTERED);

            register(ICMPv6TimeExceeded.HOP_LIMIT_EXCEEDED_IN_TRANSIT);
            register(ICMPv6TimeExceeded.FRAGMENT_REASSEMBLY_TIME_EXCEEDED);

            register(ICMPv6MulticastListenerQuery.MULTICAST_LISTENER_QUERY);

            register(ICMPv6MulticastListenerReportV1.MULTICAST_LISTENER_REPORT);
            register(ICMPv6MulticastListenerReportV2.MULTICAST_LISTENER_REPORT);

            register(ICMPv6MulticastListenerDone.MULTICAST_LISTENER_DONE);

            register(ICMPv6RouterSolicitation.ROUTER_SOLICITATION);

            register(ICMPv6RouterAdvertisement.ROUTER_ADVERTISEMENT);

            register(ICMPv6NeighborSolicitation.NEIGHBOR_SOLICITATION);

            register(ICMPv6NeighborAdvertisement.NEIGHBOR_ADVERTISEMENT);

            register(ICMPv6RedirectMessage.REDIRECT_MESSAGE);

            register(ICMPv6RouterRenumbering.ROUTER_RENUMBERING_COMMAND);
            register(ICMPv6RouterRenumbering.ROUTER_RENUMBERING_RESULT);
            register(ICMPv6RouterRenumbering.SEQUENCE_NUMBER_RESET);

            register(ICMPv6NodeInformationQuery.DATA_FIELD_CONTAINS_IPV6_ADDRESS);
            register(ICMPv6NodeInformationQuery.DATA_FIELD_CONTAIONS_NAME);
            register(ICMPv6NodeInformationQuery.DATA_FIELD_CONTAINS_IPV4_ADDRESS);

            register(ICMPv6NodeInformationResponse.SUCCESSFULL_REPLY);
            register(ICMPv6NodeInformationResponse.RESPONDER_REFUSES_TO_SUPPLY_THE_ASWER);
            register(ICMPv6NodeInformationResponse.QTYPE_OF_THE_QUERY_IS_UNKNOWN_TO_THE_RESPONDER);

            register(ICMPv6InverseNeighborDiscoverySolicitation.INVERSE_NEIGHBOR_DISCOVERY_SOLICITATION);

            register(ICMPv6InverseNeighborDiscoveryAdvertisement.INVERSE_NEIGHBOR_DISCOVERY_ADVERTISEMENT);

            register(ICMPv6HomeAgentAddressDiscoveryRequest.HOME_AGENT_ADDRESS_DISCOVERY_REQUEST);

            register(ICMPv6HomeAgentAddressDiscoveryReply.HOME_AGENT_ADDRESS_DISCOVERY_REPLY);

            register(ICMPv6MobilePrefixSolicitation.MOBILE_PREFIX_SOLICITATION);

            register(ICMPv6MobilePrefixAdvertisement.MOBILE_PREFIX_ADVERTISEMENT);

        }
    }

}
