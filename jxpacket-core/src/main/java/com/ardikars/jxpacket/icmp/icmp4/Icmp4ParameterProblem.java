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

package com.ardikars.jxpacket.icmp.icmp4;

import com.ardikars.jxpacket.icmp.Icmp;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Icmp4ParameterProblem extends Icmp.IcmpTypeAndCode {

    public static final Icmp4ParameterProblem POINTER_INDICATES_THE_ERROR =
            new Icmp4ParameterProblem((byte) 0, "Pointer indicates the error");

    public static final Icmp4ParameterProblem MISSING_REQUIRED_OPTION =
            new Icmp4ParameterProblem((byte) 1, "Missing a required option");

    public static final Icmp4ParameterProblem BAD_LENGTH =
            new Icmp4ParameterProblem((byte) 2, "Bad length");

    protected Icmp4ParameterProblem(Byte code, String name) {
        super((byte) 12, code, name);
    }

    public static Icmp4ParameterProblem register(Byte code, String name) {
        Icmp4ParameterProblem parameterProblem =
                new Icmp4ParameterProblem(code, name);
        return parameterProblem;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
