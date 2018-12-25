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

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class Fragment extends AbstractPacket {

	private final Fragment.Header header;
	private final Packet payload;

	private Fragment(final Builder builder) {
		this.header = new Header(builder);
		this.payload = TransportLayer.valueOf(header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
		payloadBuffer = builder.payloadBuffer;
	}

	@Override
	public Packet.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final class Header extends AbstractPacket.Header {

		public static final int FIXED_FRAGMENT_HEADER_LENGTH = 8;

		private final TransportLayer nextHeader;
		private final short fragmentOffset;
		private final FlagType flagType;
		private final int identification;

		private Header(final Builder builder) {
			this.nextHeader = builder.nextHeader;
			this.fragmentOffset = builder.fragmentOffset;
			this.flagType = builder.flagType;
			this.identification = builder.identification;
			this.buffer = builder.buffer.slice(0, getLength());
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getFragmentOffset() {
			return fragmentOffset & 0x1fff;
		}

		public FlagType getFlagType() {
			return flagType;
		}

		public int getIdentification() {
			return identification;
		}

		@Override
		public TransportLayer getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_FRAGMENT_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			if (buffer == null) {
				buffer = ALLOCATOR.directBuffer(getLength());
				buffer.writeByte(nextHeader.getValue());
				buffer.writeByte(0); // reserved
				buffer.writeShort((fragmentOffset & 0x1fff) << 3
						| flagType.getValue() & 0x1);
				buffer.writeInt(identification);
			}
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\t\tnextHeader: ").append(nextHeader).append('\n')
					.append("\t\tfragmentOffset: ").append(fragmentOffset).append('\n')
					.append("\t\tflagType: ").append(flagType).append('\n')
					.append("\t\tidentification: ").append(identification).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("\t[ Fragment Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\t\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder extends AbstractPacket.Builder {

		private TransportLayer nextHeader;
		private short fragmentOffset;
		private FlagType flagType;
		private int identification;

		private ByteBuf buffer;
		private ByteBuf payloadBuffer;

		public Builder nextHeader(TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder fragmentOffset(int fragmentOffset) {
			this.fragmentOffset = (short) (fragmentOffset & 0x1fff);
			return this;
		}

		public Builder flagType(FlagType flagType) {
			this.flagType = flagType;
			return this;
		}

		public Builder identification(int identification) {
			this.identification = identification;
			return this;
		}

		@Override
		public Fragment build() {
			return new Fragment(this);
		}

		@Override
		public Fragment build(final ByteBuf buffer) {
			this.nextHeader = TransportLayer.valueOf(buffer.readByte());
			short sscratch = buffer.readShort();
			this.fragmentOffset = (short) (sscratch >> 3 & 0x1fff);
			this.flagType = FlagType.valueOf((byte) (sscratch & 0x1));
			this.identification = buffer.readInt();
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new Fragment(this);
		}

	}

	public static final class FlagType extends NamedNumber<Byte, FlagType> {

		public static final FlagType LAST_FRAGMENT = new FlagType((byte) 0, "Last fragment.");

		public static final FlagType MORE_FRAGMENT = new FlagType((byte) 1, "More fragment.");

		public static final FlagType UNKNOWN = new FlagType((byte) -1, "UNKNOWN.");

		private static final Map<Byte, FlagType> registry
				= new HashMap<>();

		protected FlagType(Byte value, String name) {
			super(value, name);
		}

		/**
		 * Add new flag type to registry.
		 * @param flagType new fragment type.
		 * @return returns {@link FlagType}.
		 */
		public static FlagType register(final FlagType flagType) {
			registry.put(flagType.getValue(), flagType);
			return flagType;
		}

		/**
		 * Get flag type from value.
		 * @param flag value.
		 * @return returns {@link FlagType}.
		 */
		public static FlagType valueOf(final byte flag) {
			FlagType flagType = registry.get(flag);
			if (flagType == null) {
				return UNKNOWN;
			}
			return flagType;
		}

	}


}
