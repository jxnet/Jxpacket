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
public class Icmp6InverseNeighborDiscoveryAdvertisement extends Icmp.IcmpTypeAndCode {

    public static final Icmp6InverseNeighborDiscoveryAdvertisement INVERSE_NEIGHBOR_DISCOVERY_ADVERTISEMENT =
            new Icmp6InverseNeighborDiscoveryAdvertisement((byte) 0, "Inverse Neighbor Discovery Advertisement Message");

    protected Icmp6InverseNeighborDiscoveryAdvertisement(Byte code, String name) {
        super((byte) 142, code, name);
    }

    public static Icmp6InverseNeighborDiscoveryAdvertisement register(Byte code, String name) {
        Icmp6InverseNeighborDiscoveryAdvertisement neighborDiscoveryAdvertisement =
                new Icmp6InverseNeighborDiscoveryAdvertisement(code, name);
        return neighborDiscoveryAdvertisement;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
