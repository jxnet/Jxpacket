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
import com.ardikars.jxpacket.icmp.Icmp6;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class Icmp6TimeExceeded extends Icmp.IcmpTypeAndCode {

    public static Icmp6TimeExceeded HOP_LIMIT_EXCEEDED_IN_TRANSIT =
            new Icmp6TimeExceeded((byte) 0, "Hop limit exceeded in transit");

    public static Icmp6TimeExceeded FRAGMENT_REASSEMBLY_TIME_EXCEEDED =
            new Icmp6TimeExceeded((byte) 1, "Fragment reassembly time exceeded");

    public Icmp6TimeExceeded(Byte code, String name) {
        super((byte) 3, code, name);
    }

    public static Icmp6TimeExceeded register(Byte code, String name) {
        Icmp6TimeExceeded timeExceeded =
                new Icmp6TimeExceeded(code, name);
        return timeExceeded;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(HOP_LIMIT_EXCEEDED_IN_TRANSIT);
        Icmp6.ICMP6_REGISTRY.add(FRAGMENT_REASSEMBLY_TIME_EXCEEDED);
    }

}