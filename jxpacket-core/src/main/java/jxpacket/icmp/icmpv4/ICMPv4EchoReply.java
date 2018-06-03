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

package jxpacket.icmp.icmpv4;

import jxpacket.common.TwoKeyMap;
import jxpacket.icmp.ICMPTypeAndCode;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class ICMPv4EchoReply extends ICMPTypeAndCode {

    public static final ICMPv4EchoReply ECHO_REPLY =
            new ICMPv4EchoReply((byte) 0, "Echo reply (used to ping)");

    protected ICMPv4EchoReply(Byte code, String name) {
        super((byte) 0, code, name);
    }

    public static ICMPv4EchoReply register(Byte code, String name) {
        TwoKeyMap<Byte, Byte> key = TwoKeyMap.newInstance((byte) 0, code);
        ICMPv4EchoReply icmPv4EchoReply =
                new ICMPv4EchoReply(key.getSecondKey(), name);
        return (ICMPv4EchoReply) ICMPTypeAndCode.registry.put(key, icmPv4EchoReply);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
