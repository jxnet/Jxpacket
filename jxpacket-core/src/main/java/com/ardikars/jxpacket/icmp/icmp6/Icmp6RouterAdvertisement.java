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
public class Icmp6RouterAdvertisement extends Icmp.IcmpTypeAndCode {

    public static final Icmp6RouterAdvertisement ROUTER_ADVERTISEMENT =
            new Icmp6RouterAdvertisement((byte) 0, "Router advertisment");

    public Icmp6RouterAdvertisement(Byte code, String name) {
        super((byte) 134, code, name);
    }

    public static Icmp6RouterAdvertisement register(Byte code, String name) {
        Icmp6RouterAdvertisement routerAdvertisement =
                new Icmp6RouterAdvertisement(code, name);
        return routerAdvertisement;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(ROUTER_ADVERTISEMENT);
    }

}
