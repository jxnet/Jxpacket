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

import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;

public class DestinationOptions extends Options {

	private final DestinationOptions.Header header;
	private final Packet payload;

	private DestinationOptions(final Builder builder) {
		this.header = new DestinationOptions.Header(builder);
		this.payload = null;
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

		protected Header(Builder builder) {
			super(builder, builder.nextHeader);
		}

		@Override
		public String toString() {
			return super.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("DestinationOptions{")
				.append("header=").append(header)
				.append('}').toString();
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
			Builder builder = new Builder();
			builder.extensionLength = buffer.getInt(1);
			builder.options = new byte[Options.Header.FIXED_OPTIONS_LENGTH
					+ Options.Header.LENGTH_UNIT * builder.extensionLength];
			buffer.getBytes(5, options);
			buffer.release();
			return new DestinationOptions(this);
		}

	}

}
