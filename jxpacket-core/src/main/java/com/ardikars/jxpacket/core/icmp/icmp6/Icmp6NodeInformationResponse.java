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
public class Icmp6NodeInformationResponse extends Icmp.IcmpTypeAndCode {

    public static final Icmp6NodeInformationResponse SUCCESSFULL_REPLY =
            new Icmp6NodeInformationResponse((byte) 0, "A successfull reply");

    public static final Icmp6NodeInformationResponse RESPONDER_REFUSES_TO_SUPPLY_THE_ASWER =
            new Icmp6NodeInformationResponse((byte) 1, "The Responder refuses to supply the answer");

    public static final Icmp6NodeInformationResponse QTYPE_OF_THE_QUERY_IS_UNKNOWN_TO_THE_RESPONDER =
            new Icmp6NodeInformationResponse((byte) 2, "The Qtype of the Query is unknown to the Responder");

    public Icmp6NodeInformationResponse(Byte code, String name) {
        super((byte) 140, code, name);
    }

    public static Icmp6NodeInformationResponse register(Byte code, String name) {
        Icmp6NodeInformationResponse informationResponse =
                new Icmp6NodeInformationResponse(code, name);
        return informationResponse;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(SUCCESSFULL_REPLY);
        Icmp6.ICMP6_REGISTRY.add(RESPONDER_REFUSES_TO_SUPPLY_THE_ASWER);
        Icmp6.ICMP6_REGISTRY.add(QTYPE_OF_THE_QUERY_IS_UNKNOWN_TO_THE_RESPONDER);
    }

}
