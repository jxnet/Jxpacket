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
public class Icmp6HomeAgentAddressDiscoveryRequest extends Icmp.IcmpTypeAndCode {

    public static final Icmp6HomeAgentAddressDiscoveryRequest HOME_AGENT_ADDRESS_DISCOVERY_REQUEST =
            new Icmp6HomeAgentAddressDiscoveryRequest((byte) 0, "Home Agent Address Discovery Request Message");

    public Icmp6HomeAgentAddressDiscoveryRequest(Byte code, String name) {
        super((byte) 144, code, name);
    }

    /**
     * Add new {@link Icmp6HomeAgentAddressDiscoveryRequest} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6HomeAgentAddressDiscoveryRequest}.
     */
    public static Icmp6HomeAgentAddressDiscoveryRequest register(Byte code, String name) {
        Icmp6HomeAgentAddressDiscoveryRequest homeAgentAddressDiscoveryRequest =
                new Icmp6HomeAgentAddressDiscoveryRequest(code, name);
        return homeAgentAddressDiscoveryRequest;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(HOME_AGENT_ADDRESS_DISCOVERY_REQUEST);
    }

}
