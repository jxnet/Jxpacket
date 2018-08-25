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

package com.ardikars.jxpacket.icmp.icmpv6;

import jxpacket.common.TwoKeyMap;
import com.ardikars.jxpacket.icmp.ICMPTypeAndCode;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class ICMPv6RouterRenumbering extends ICMPTypeAndCode {

    public static final ICMPv6RouterRenumbering ROUTER_RENUMBERING_COMMAND =
            new ICMPv6RouterRenumbering((byte) 0, "Router renumbering command");

    public static final ICMPv6RouterRenumbering ROUTER_RENUMBERING_RESULT =
            new ICMPv6RouterRenumbering((byte) 1, "Router renumbering result");

    public static final ICMPv6RouterRenumbering SEQUENCE_NUMBER_RESET =
            new ICMPv6RouterRenumbering((byte) 255, "Sequence number reset");

    protected ICMPv6RouterRenumbering(Byte code, String name) {
        super((byte) 138, code, name);
    }

    public static ICMPv6RouterRenumbering register(Byte code, String name) {
        TwoKeyMap<Byte, Byte> key = TwoKeyMap.newInstance((byte) 138, code);
        ICMPv6RouterRenumbering routerRenumbering =
                new ICMPv6RouterRenumbering(key.getSecondKey(), name);
        return (ICMPv6RouterRenumbering) ICMPTypeAndCode.registry.put(key, routerRenumbering);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
