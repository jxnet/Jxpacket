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
public class Icmp6NeighborSolicitation extends Icmp.IcmpTypeAndCode {

    public static final Icmp6NeighborSolicitation NEIGHBOR_SOLICITATION =
            new Icmp6NeighborSolicitation((byte) 0, "Neighbor solicitation");

    public Icmp6NeighborSolicitation(Byte code, String name) {
        super((byte) 135, code, name);
    }

    public static Icmp6NeighborSolicitation register(Byte code, String name) {
        Icmp6NeighborSolicitation neighborSolicitation =
                new Icmp6NeighborSolicitation(code, name);
        return neighborSolicitation;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(NEIGHBOR_SOLICITATION);
    }

}
