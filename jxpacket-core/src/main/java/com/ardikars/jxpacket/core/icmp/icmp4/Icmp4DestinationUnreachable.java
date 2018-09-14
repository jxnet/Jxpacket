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

package com.ardikars.jxpacket.core.icmp.icmp4;

import com.ardikars.jxpacket.core.icmp.Icmp;
import com.ardikars.jxpacket.core.icmp.Icmp4;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4DestinationUnreachable extends Icmp.IcmpTypeAndCode {

    public static final Icmp4DestinationUnreachable DESTINATION_NETWORK_UNREACHABLE =
            new Icmp4DestinationUnreachable((byte) 0, "Destination network unreachable");

    public static final Icmp4DestinationUnreachable DESTINATION_HOST_UNREACHABLE =
            new Icmp4DestinationUnreachable((byte) 1, "Destination host unreachable");

    public static final Icmp4DestinationUnreachable DESTINATION_PROTOCOL_UNREACHABLE =
            new Icmp4DestinationUnreachable((byte) 2, "Destination protocol unreachable");

    public static final Icmp4DestinationUnreachable DESTINATION_PORT_UNREACHABLE =
            new Icmp4DestinationUnreachable((byte) 3, "Destination port unreachable");

    public static final Icmp4DestinationUnreachable FRAGMENTATION_REQUIRED =
            new Icmp4DestinationUnreachable((byte) 4, "Fragmentation required, and DF flag set");

    public static final Icmp4DestinationUnreachable SOURCE_ROUTE_FAILED =
            new Icmp4DestinationUnreachable((byte) 5, "Source route failed");

    public static final Icmp4DestinationUnreachable DESTINATION_NETWORK_UNKNOWN =
            new Icmp4DestinationUnreachable((byte) 6, "Destination network unknown");

    public static final Icmp4DestinationUnreachable DESTINATION_HOST_UNKOWN =
            new Icmp4DestinationUnreachable((byte) 7, "Destinatin host unknown");

    public static final Icmp4DestinationUnreachable SOURCE_HOST_ISOLATED =
            new Icmp4DestinationUnreachable((byte) 8, "Destination host isolated");

    public static final Icmp4DestinationUnreachable NETWORK_ADMINISTRATIVELY_PROHIBITED =
            new Icmp4DestinationUnreachable((byte) 9, "Network administratively prohibited");

    public static final Icmp4DestinationUnreachable HOST_ADMINISTRATIVELY_PROHIBITED =
            new Icmp4DestinationUnreachable((byte) 10, "Host administratively prohibited");

    public static final Icmp4DestinationUnreachable NETWORK_UNREACHABLE_FOR_TOS =
            new Icmp4DestinationUnreachable((byte) 11, "Network unreachable for ToS");

    public static final Icmp4DestinationUnreachable HOST_UNREACHABLE_FOR_TOS =
            new Icmp4DestinationUnreachable((byte) 12, "Host unreachable for ToS");

    public static final Icmp4DestinationUnreachable COMMUNICATION_ADMINISTRATIVELY_PROHIBITED =
            new Icmp4DestinationUnreachable((byte) 13, "Communication administratively prohibited");

    public static final Icmp4DestinationUnreachable HOST_PRECEDENCE_VIOLATION =
            new Icmp4DestinationUnreachable((byte) 14, "Host Precedence Violation");

    public static final Icmp4DestinationUnreachable PRECEDENCE_CUTOFF_IN_EFFECT =
            new Icmp4DestinationUnreachable((byte) 15, "Precedence cutoff in effect");

    public Icmp4DestinationUnreachable(Byte code, String name) {
        super((byte) 3, code, name);
    }

    public static Icmp4DestinationUnreachable register(Byte code, String name) {
        Icmp4DestinationUnreachable destinationUnreachable =
                new Icmp4DestinationUnreachable(code, name);
        return destinationUnreachable;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp4.ICMP4_REGISTRY.add(DESTINATION_NETWORK_UNREACHABLE);
        Icmp4.ICMP4_REGISTRY.add(DESTINATION_HOST_UNREACHABLE);
        Icmp4.ICMP4_REGISTRY.add(DESTINATION_PROTOCOL_UNREACHABLE);
        Icmp4.ICMP4_REGISTRY.add( DESTINATION_PORT_UNREACHABLE);
        Icmp4.ICMP4_REGISTRY.add(FRAGMENTATION_REQUIRED);
        Icmp4.ICMP4_REGISTRY.add(SOURCE_ROUTE_FAILED);
        Icmp4.ICMP4_REGISTRY.add(DESTINATION_NETWORK_UNKNOWN);
        Icmp4.ICMP4_REGISTRY.add(DESTINATION_HOST_UNKOWN);
        Icmp4.ICMP4_REGISTRY.add(SOURCE_HOST_ISOLATED);
        Icmp4.ICMP4_REGISTRY.add(NETWORK_ADMINISTRATIVELY_PROHIBITED);
        Icmp4.ICMP4_REGISTRY.add(HOST_ADMINISTRATIVELY_PROHIBITED);
        Icmp4.ICMP4_REGISTRY.add(NETWORK_UNREACHABLE_FOR_TOS);
        Icmp4.ICMP4_REGISTRY.add(HOST_UNREACHABLE_FOR_TOS);
        Icmp4.ICMP4_REGISTRY.add(COMMUNICATION_ADMINISTRATIVELY_PROHIBITED);
        Icmp4.ICMP4_REGISTRY.add(HOST_PRECEDENCE_VIOLATION);
        Icmp4.ICMP4_REGISTRY.add(PRECEDENCE_CUTOFF_IN_EFFECT);
    }

}
