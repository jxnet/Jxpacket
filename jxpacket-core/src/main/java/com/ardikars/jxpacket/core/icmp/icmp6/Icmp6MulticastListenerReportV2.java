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
public class Icmp6MulticastListenerReportV2 extends Icmp.IcmpTypeAndCode {

    public static final Icmp6MulticastListenerReportV2 MULTICAST_LISTENER_REPORT =
            new Icmp6MulticastListenerReportV2((byte) 0, "Multicast listener report");

    public Icmp6MulticastListenerReportV2(Byte code, String name) {
        super((byte) 143, code, name);
    }

    public static Icmp6MulticastListenerReportV2 register(Byte code, String name) {
        Icmp6MulticastListenerReportV2 multicastListenerReport =
                new Icmp6MulticastListenerReportV2(code, name);
        return multicastListenerReport;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    static {
        Icmp6.ICMP6_REGISTRY.add(MULTICAST_LISTENER_REPORT);
    }

}
