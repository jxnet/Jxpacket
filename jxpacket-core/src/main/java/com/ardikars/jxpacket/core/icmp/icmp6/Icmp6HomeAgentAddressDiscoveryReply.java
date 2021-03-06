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

package com.ardikars.jxpacket.core.icmp.icmp6;

import com.ardikars.jxpacket.core.icmp.Icmp;
import com.ardikars.jxpacket.core.icmp.Icmp6;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class Icmp6HomeAgentAddressDiscoveryReply extends Icmp.IcmpTypeAndCode {

    public static final Icmp6HomeAgentAddressDiscoveryReply HOME_AGENT_ADDRESS_DISCOVERY_REPLY =
            new Icmp6HomeAgentAddressDiscoveryReply((byte) 0, "Home Agent Address Discovery Reply Message");

    public Icmp6HomeAgentAddressDiscoveryReply(Byte code, String name) {
        super((byte) 145, code, name);
    }

    /**
     * Add new {@link Icmp6HomeAgentAddressDiscoveryReply} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6HomeAgentAddressDiscoveryReply}.
     */
    public static Icmp6HomeAgentAddressDiscoveryReply register(Byte code, String name) {
        return new Icmp6HomeAgentAddressDiscoveryReply(code, name);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(HOME_AGENT_ADDRESS_DISCOVERY_REPLY);
    }

}
