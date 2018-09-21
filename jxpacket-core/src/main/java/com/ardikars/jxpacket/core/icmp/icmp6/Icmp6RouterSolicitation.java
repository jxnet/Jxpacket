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
public class Icmp6RouterSolicitation extends Icmp.IcmpTypeAndCode {

    public static final Icmp6RouterSolicitation ROUTER_SOLICITATION =
            new Icmp6RouterSolicitation((byte) 0, "Router solicitation");

    protected Icmp6RouterSolicitation(Byte code, String name) {
        super((byte) 133, code, name);
    }

    /**
     * Add new {@link Icmp6RouterSolicitation} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6RouterSolicitation}.
     */
    public static Icmp6RouterSolicitation register(Byte code, String name) {
        Icmp6RouterSolicitation routerSolicitation =
                new Icmp6RouterSolicitation(code, name);
        return routerSolicitation;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(ROUTER_SOLICITATION);
    }

}
