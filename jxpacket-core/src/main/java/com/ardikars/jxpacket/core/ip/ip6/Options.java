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

import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.ip.Ip6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;

public abstract class Options extends AbstractPacket {

	abstract static class Header extends Ip6.ExtensionHeader {

		public static final int FIXED_OPTIONS_LENGTH = 6;
		public static final int LENGTH_UNIT = 8;

		protected final TransportLayer nextHeader;
		protected final int extensionLength;
		protected final byte[] options;

		protected Header(final Builder builder, TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			this.extensionLength = builder.extensionLength;
			this.options = builder.options;
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getExtensionLength() {
			return extensionLength;
		}

		public byte[] getOptions() {
			return options;
		}

		@Override
		public TransportLayer getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_OPTIONS_LENGTH + LENGTH_UNIT * extensionLength;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setByte(0, nextHeader.getValue());
			buffer.setInt(1, extensionLength);
			if (options != null) {
				buffer.setBytes(5, options);
			}
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder("Header{")
					.append("nextHeader=").append(nextHeader)
					.append(", extensionLength=").append(extensionLength)
					.append(", options=").append(Arrays.toString(options))
					.append('}').toString();
		}

	}

	abstract static class Builder implements Packet.Builder {

		protected TransportLayer nextHeader;
		protected int extensionLength;
		protected byte[] options;

		public Builder(final TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
		}

		public Builder extensionLength(final int extensionLength) {
			this.extensionLength = extensionLength;
			return this;
		}

		public Builder options(final byte[] options) {
			this.options = new byte[options.length];
			System.arraycopy(options, 0, this.options, 0, this.options.length);
			return this;
		}

	}

}
