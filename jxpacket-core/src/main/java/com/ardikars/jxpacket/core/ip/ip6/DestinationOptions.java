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

package com.ardikars.jxpacket.core.ip.ip6;

import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;

public class DestinationOptions extends Options {

	private final DestinationOptions.Header header;
	private final Packet payload;

	private DestinationOptions(final Builder builder) {
		this.header = new DestinationOptions.Header(builder);
		this.payload = TransportLayer.valueOf(header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
		payloadBuffer = builder.payloadBuffer;
	}

	@Override
	public Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final class Header extends Options.Header {

		private final Builder builder;

		protected Header(Builder builder) {
			super(builder, builder.nextHeader);
			this.buffer = builder.buffer.slice(0, getLength());
			this.builder = builder;
		}

		@Override
		public String toString() {
			return super.toString();
		}

		@Override
		public DestinationOptions.Builder getBuilder() {
			return builder;
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("\t[ DestinationOptions Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\t\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder extends Options.Builder {

		public Builder() {
			super(TransportLayer.IPV6_AH);
		}

		@Override
		public DestinationOptions build() {
			return new DestinationOptions(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			nextHeader = TransportLayer.valueOf(buffer.readByte());
			extensionLength = buffer.readInt();
			options = new byte[Options.Header.FIXED_OPTIONS_LENGTH
					+ Options.Header.LENGTH_UNIT * extensionLength];
			buffer.readBytes(options);
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new DestinationOptions(this);
		}

		@Override
		public void reset() {
			if (buffer != null) {
				reset(buffer.readerIndex(), Header.FIXED_OPTIONS_LENGTH);
			}
		}

		@Override
		public void reset(int offset, int length) {
			if (buffer != null) {
				Validate.notIllegalArgument(offset + length <= buffer.capacity());
				Validate.notIllegalArgument(nextHeader != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(extensionLength >= 0, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(options != null, ILLEGAL_HEADER_EXCEPTION);
				int index = offset;
				buffer.setByte(index, nextHeader.getValue());
				index += 1;
				buffer.setInt(index, extensionLength);
				index += 4;
				buffer.setBytes(index, options);
			}
		}

	}

}
