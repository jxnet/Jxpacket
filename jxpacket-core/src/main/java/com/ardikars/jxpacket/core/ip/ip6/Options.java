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

import com.ardikars.common.memory.Memory;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import com.ardikars.jxpacket.core.ip.Ip6;

import java.util.Arrays;

public abstract class Options extends AbstractPacket {

	protected abstract static class Header extends Ip6.ExtensionHeader {

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
			if (options != null) {
				byte[] data = new byte[options.length];
				System.arraycopy(options, 0, data, 0, data.length);
				return data;
			}
			return new byte[] { };
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
		public Memory getBuffer() {
			if (buffer == null) {
				buffer = ALLOCATOR.allocate(getLength());
				buffer.writeByte(nextHeader.getValue());
				buffer.writeInt(extensionLength);
				if (options != null) {
					buffer.writeBytes(options);
				}
			}
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\t\tnextHeader: ").append(nextHeader).append('\n')
					.append("\t\textensionLength: ").append(extensionLength).append('\n')
					.append("\t\toptions: ").append(Arrays.toString(options)).append('\n')
					.toString();
		}

	}

	protected abstract static class Builder extends AbstractPacket.Builder {

		protected TransportLayer nextHeader;
		protected int extensionLength;
		protected byte[] options;

		protected Memory buffer;
		protected Memory payloadBuffer;

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
