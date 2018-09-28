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

package com.ardikars.jxpacket.common;

import com.ardikars.common.util.NamedNumber;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class UnknownPacket extends AbstractPacket {

	private final UnknownPacket.Header header;

	private UnknownPacket(final Builder builder) {
		this.header = new UnknownPacket.Header(builder);
	}

	public static UnknownPacket newPacket(final ByteBuf buffer) {
		return new UnknownPacket.Builder().build(buffer);
	}

	@Override
	public UnknownPacket.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return null;
	}

	public static final class Header implements Packet.Header {

		final private ByteBuf buffer;

		public Header(final Builder builder) {
			this.buffer = builder.payloadBuffer;
		}

		@Override
		public int getLength() {
			return buffer.capacity();
		}

		@Override
		public ByteBuf getBuffer() {
			return this.buffer;
		}

		@Override
		public <T extends NamedNumber> T getPayloadType() {
			return null;
		}

		@Override
		public String toString() {
			return new StringBuilder("Header{")
					.append("buffer=").append(buffer)
//					.append("hexDump=").append(ByteBufUtil.hexDump(buffer))
					.append('}').toString();
		}

	}

	public static final class Builder implements Packet.Builder {

		private ByteBuf payloadBuffer;

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public UnknownPacket build() {
			return new UnknownPacket(this);
		}

		@Override
		public UnknownPacket build(ByteBuf buffer) {
			Builder builder = new Builder()
					.payloadBuffer(buffer);
			buffer.release();
			return new UnknownPacket(builder);
		}

	}

}
