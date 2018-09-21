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
public class Icmp6NodeInformationQuery extends Icmp.IcmpTypeAndCode {

    public static final Icmp6NodeInformationQuery DATA_FIELD_CONTAINS_IPV6_ADDRESS =
            new Icmp6NodeInformationQuery((byte) 0, "Data field contains an IPv6 address");

    public static final Icmp6NodeInformationQuery DATA_FIELD_CONTAIONS_NAME =
            new Icmp6NodeInformationQuery((byte) 1, "Data field contains a name which is the Subject of this Query");

    public static final Icmp6NodeInformationQuery DATA_FIELD_CONTAINS_IPV4_ADDRESS =
            new Icmp6NodeInformationQuery((byte) 2, "Data field contains an IPv4 address");

    public Icmp6NodeInformationQuery(Byte code, String name) {
        super((byte) 139, code, name);
    }

    /**
     * Add new {@link Icmp6NodeInformationQuery} to registry.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6NodeInformationQuery}.
     */
    public static Icmp6NodeInformationQuery register(Byte code, String name) {
        Icmp6NodeInformationQuery nodeInformationQuery =
                new Icmp6NodeInformationQuery(code, name);
        return nodeInformationQuery;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(DATA_FIELD_CONTAINS_IPV6_ADDRESS);
        Icmp6.ICMP6_REGISTRY.add(DATA_FIELD_CONTAIONS_NAME);
        Icmp6.ICMP6_REGISTRY.add(DATA_FIELD_CONTAINS_IPV4_ADDRESS);
    }

}
