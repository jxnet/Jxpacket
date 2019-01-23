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

/**
 * Unknown packet.
 * @author Ardika Rommy Sanjaya
 * @since 1.5.0
 */
public class UnknownPacket extends AbstractPacket {

	public static final NamedNumber<Integer, ?> UNKNOWN_PAYLOAD_TYPE
			= null;/*new NamedNumber<Integer, NamedNumber>(-1, "UNKNOWN PAYLOAD TYPE") {

		@Override
		public Integer getValue() {
			return super.getValue();
		}

	};*/

	private final UnknownPacket.Header header;
	private final Packet payload;

	private UnknownPacket(final Builder builder) {
		this.header = new UnknownPacket.Header(builder);
		this.payload = null;
		payloadBuffer = builder.payloadBuffer;
	}

	public static UnknownPacket newPacket(final ByteBuf buffer) {
		return new UnknownPacket.Builder().build(buffer);
	}

	@Override
	public UnknownPacket.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final class Header extends AbstractPacket.Header {

		private final ByteBuf buffer;

		private final Builder builder;

		public Header(final Builder builder) {
			this.buffer = builder.payloadBuffer;
			this.builder = builder;
		}

		@Override
		public int getLength() {
			return buffer.capacity();
		}

		@Override
		public ByteBuf getBuffer() {
			return buffer;
		}

		@Override
		public <T extends NamedNumber> T getPayloadType() {
			return (T) UNKNOWN_PAYLOAD_TYPE;
		}

		@Override
		public UnknownPacket.Builder getBuilder() {
			return builder;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\tbuffer: ").append(buffer).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ UnknownPacket Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).toString();
	}

	public static final class Builder extends AbstractPacket.Builder {

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
			return new UnknownPacket(builder);
		}

		@Override
		public void reset() {
			// nothing to do
		}

		@Override
		public void reset(int offset, int length) {
			// do nothing
		}

	}

}
