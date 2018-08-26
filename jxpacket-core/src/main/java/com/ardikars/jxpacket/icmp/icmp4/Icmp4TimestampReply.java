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
import com.ardikars.jxpacket.icmp.Icmp4;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4TimestampReply extends Icmp.IcmpTypeAndCode {

    public static final Icmp4TimestampReply TIMESTAMP_REPLY =
            new Icmp4TimestampReply((byte) 0, "Timestamp reply");

    public Icmp4TimestampReply(Byte code, String name) {
        super((byte) 14, code, name);
    }

    public static Icmp4TimestampReply register(Byte code, String name) {
        Icmp4TimestampReply timestampReply =
                new Icmp4TimestampReply(code, name);
        return timestampReply;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp4.ICMP4_REGISTRY.add(TIMESTAMP_REPLY);
    }

}
