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

import com.ardikars.jxpacket.TwoKeyMap;
import com.ardikars.jxpacket.icmp.ICMPTypeAndCode;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class ICMPv6RedirectMessage extends ICMPTypeAndCode {

    public static final ICMPv6RedirectMessage REDIRECT_MESSAGE =
            new ICMPv6RedirectMessage((byte) 0, "Redirect message");

    protected ICMPv6RedirectMessage(Byte code, String name) {
        super((byte) 137, code, name);
    }

    public static ICMPv6RedirectMessage register(Byte code, String name) {
        TwoKeyMap<Byte, Byte> key = TwoKeyMap.newInstance((byte) 137, code);
        ICMPv6RedirectMessage redirectMessage =
                new ICMPv6RedirectMessage(key.getSecondKey(), name);
        return (ICMPv6RedirectMessage) ICMPTypeAndCode.registry.put(key, redirectMessage);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
