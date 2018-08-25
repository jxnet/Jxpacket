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
public class Icmp4Timestamp extends Icmp.IcmpTypeAndCode {

    public static final Icmp4Timestamp TIMESTAMP =
            new Icmp4Timestamp((byte) 0, "Timestamp");

    protected Icmp4Timestamp(Byte code, String name) {
        super((byte) 13, code, name);
    }

    public static Icmp4Timestamp register(Byte code, String name) {
        Icmp4Timestamp timestamp =
                new Icmp4Timestamp(code, name);
        return timestamp;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
