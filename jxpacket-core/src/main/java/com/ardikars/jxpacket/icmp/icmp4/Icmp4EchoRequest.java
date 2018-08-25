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

package com.ardikars.jxpacket.icmp.icmp4;

import com.ardikars.jxpacket.icmp.Icmp;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4EchoRequest extends Icmp.IcmpTypeAndCode {

    public static final Icmp4EchoRequest ECHO_REQUEST =
            new Icmp4EchoRequest((byte) 0, "Echo request (used to ping)");

    protected Icmp4EchoRequest(Byte code, String name) {
        super((byte) 8, code, name);
    }

    public static Icmp4EchoRequest register(Byte code, String name) {
        Icmp4EchoRequest icmPv4EchoRequest =
                new Icmp4EchoRequest(code, name);
        return icmPv4EchoRequest;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
