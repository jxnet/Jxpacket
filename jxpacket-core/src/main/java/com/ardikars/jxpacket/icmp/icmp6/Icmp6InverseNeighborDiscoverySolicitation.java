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
public class Icmp6InverseNeighborDiscoverySolicitation extends Icmp.IcmpTypeAndCode {

    public static final Icmp6InverseNeighborDiscoverySolicitation INVERSE_NEIGHBOR_DISCOVERY_SOLICITATION =
            new Icmp6InverseNeighborDiscoverySolicitation((byte) 0, "Inverse neighbor discovery solicitation message");

    protected Icmp6InverseNeighborDiscoverySolicitation(Byte code, String name) {
        super((byte) 141, code, name);
    }

    public static Icmp6InverseNeighborDiscoverySolicitation register(Byte code, String name) {
        Icmp6InverseNeighborDiscoverySolicitation inverseNeighborDiscoverySolicitation =
                new Icmp6InverseNeighborDiscoverySolicitation(code, name);
        return inverseNeighborDiscoverySolicitation;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
