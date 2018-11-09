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

package com.ardikars.jxpacket.common.api;

import com.ardikars.jxpacket.common.Packet;
import io.netty.buffer.ByteBuf;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.5.0
 */
public interface Jxpacket {

    PacketCode inject(Packet packet);

    PacketCode inject(ByteBuf buffer);

    <T, U> PacketCode capture(int count, Listener<T, U> packet, U argument);

}
