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
public class Icmp6RouterRenumbering extends Icmp.IcmpTypeAndCode {

    public static final Icmp6RouterRenumbering ROUTER_RENUMBERING_COMMAND =
            new Icmp6RouterRenumbering((byte) 0, "Router renumbering command");

    public static final Icmp6RouterRenumbering ROUTER_RENUMBERING_RESULT =
            new Icmp6RouterRenumbering((byte) 1, "Router renumbering result");

    public static final Icmp6RouterRenumbering SEQUENCE_NUMBER_RESET =
            new Icmp6RouterRenumbering((byte) 255, "Sequence number reset");

    public Icmp6RouterRenumbering(Byte code, String name) {
        super((byte) 138, code, name);
    }

    /**
     * Add new {@link Icmp6RouterRenumbering} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6RouterRenumbering}.
     */
    public static Icmp6RouterRenumbering register(Byte code, String name) {
        Icmp6RouterRenumbering routerRenumbering =
                new Icmp6RouterRenumbering(code, name);
        return routerRenumbering;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(ROUTER_RENUMBERING_COMMAND);
        Icmp6.ICMP6_REGISTRY.add(ROUTER_RENUMBERING_RESULT);
        Icmp6.ICMP6_REGISTRY.add(SEQUENCE_NUMBER_RESET);
    }

}
