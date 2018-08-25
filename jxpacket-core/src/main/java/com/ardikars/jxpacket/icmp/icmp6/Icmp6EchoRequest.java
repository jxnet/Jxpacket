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

package com.ardikars.jxpacket.icmp.icmp6;

import com.ardikars.jxpacket.icmp.Icmp;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class Icmp6EchoRequest extends Icmp.IcmpTypeAndCode {

    public static final Icmp6EchoRequest ECHO_REQUEST =
            new Icmp6EchoRequest((byte) 0, "Echo request");

    protected Icmp6EchoRequest(Byte code, String name) {
        super((byte) 128, code, name);
    }

    public static Icmp6EchoRequest register(Byte code, String name) {
        Icmp6EchoRequest echoRequest =
                new Icmp6EchoRequest(code, name);
        return echoRequest;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
