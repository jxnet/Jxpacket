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

package com.ardikars.jxpacket.core.icmp.icmp6;

import com.ardikars.jxpacket.core.icmp.Icmp;
import com.ardikars.jxpacket.core.icmp.Icmp6;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class Icmp6MobilePrefixSolicitation extends Icmp.IcmpTypeAndCode {

    public static final Icmp6MobilePrefixSolicitation MOBILE_PREFIX_SOLICITATION =
            new Icmp6MobilePrefixSolicitation((byte) 0, "Mobile Prefix Solicitation");

    public Icmp6MobilePrefixSolicitation(Byte code, String name) {
        super((byte) 146, code, name);
    }

    public static Icmp6MobilePrefixSolicitation register(Byte code, String name) {
        Icmp6MobilePrefixSolicitation mobilePrefixSolicitation =
                new Icmp6MobilePrefixSolicitation(code, name);
        return mobilePrefixSolicitation;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(MOBILE_PREFIX_SOLICITATION);
    }

}
