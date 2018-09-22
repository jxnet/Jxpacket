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
public class Icmp6ParameterProblem extends Icmp.IcmpTypeAndCode {

    public static final Icmp6ParameterProblem ERRORNEOUS_HEADER_FIELD_ENCOUTERED =
            new Icmp6ParameterProblem((byte) 0, "Erroneous header field encountered");

    public static final Icmp6ParameterProblem UNRECOGNIZED_NEXT_HEADER_TYPE_ENCOUNTERED =
            new Icmp6ParameterProblem((byte) 1, "Unrecognized Next HeaderAbstract type encountered");

    public static final Icmp6ParameterProblem UNRECOGNIZED_IPV6_OPTION_ENCOUNTERED =
            new Icmp6ParameterProblem((byte) 2, "Unrecognized IPv6 option encountered");

    public Icmp6ParameterProblem(Byte code, String name) {
        super((byte) 4, code, name);
    }

    /**
     * Add new {@link Icmp6ParameterProblem}.
     * @param code icmp type code.
     * @param name icmp type name.
     * @return returns {@link Icmp6ParameterProblem}.
     */
    public static Icmp6ParameterProblem register(Byte code, String name) {
        return new Icmp6ParameterProblem(code, name);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(ERRORNEOUS_HEADER_FIELD_ENCOUTERED);
        Icmp6.ICMP6_REGISTRY.add(UNRECOGNIZED_NEXT_HEADER_TYPE_ENCOUNTERED);
        Icmp6.ICMP6_REGISTRY.add(UNRECOGNIZED_IPV6_OPTION_ENCOUNTERED);
    }

}
