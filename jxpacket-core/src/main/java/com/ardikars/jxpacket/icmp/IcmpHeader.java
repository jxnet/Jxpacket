/**
 * Copyright (C) 2017  Ardika Rommy Sanjaya
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ardikars.jxpacket.icmp;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import io.netty.buffer.ByteBuf;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
abstract class IcmpHeader extends AbstractPacket.PacketHeader {

    public static int ICMP_HEADER_LENGTH = 4;

    private ICMPTypeAndCode typeAndCode;
    private short checksum;

    @Override
    public abstract <T extends NamedNumber> T getPayloadType();

    @Override
    public int getLength() {
        return ICMP_HEADER_LENGTH;
    }

    @Override
    public abstract ByteBuf getBuffer();

}
