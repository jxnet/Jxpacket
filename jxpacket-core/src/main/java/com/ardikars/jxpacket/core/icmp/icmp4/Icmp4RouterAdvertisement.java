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

package com.ardikars.jxpacket.core.icmp.icmp4;

import com.ardikars.jxpacket.core.icmp.Icmp;
import com.ardikars.jxpacket.core.icmp.Icmp4;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4RouterAdvertisement extends Icmp.IcmpTypeAndCode {

    public static final Icmp4RouterAdvertisement ROUTER_ADVERTISEMENT =
            new Icmp4RouterAdvertisement((byte) 0, "Router Advertisement");

    public Icmp4RouterAdvertisement(Byte code, String name) {
        super((byte) 9, code, name);
    }

    /**
     * Add new {@link Icmp4RouterAdvertisement} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp4RouterAdvertisement}.
     */
    public static Icmp4RouterAdvertisement register(Byte code, String name) {
        Icmp4RouterAdvertisement routerAdvertisement =
                new Icmp4RouterAdvertisement(code, name);
        return routerAdvertisement;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp4.ICMP4_REGISTRY.add(ROUTER_ADVERTISEMENT);
    }

}
