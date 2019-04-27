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

package com.ardikars.jxpacket.core.ethernet;

import com.ardikars.common.memory.Memory;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.NetworkLayer;

import java.util.HashMap;
import java.util.Map;

public class Vlan extends AbstractPacket {

	private final Vlan.Header header;
	private final Packet payload;

	private Vlan(final Builder builder) {
		this.header = new Vlan.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getType().getValue())
				.newInstance(builder.payloadBuffer);
		payloadBuffer = builder.payloadBuffer;
	}

	public static Vlan newPacket(final Memory buffer) {
		return new Vlan.Builder().build(buffer);
	}

	@Override
	public Vlan.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/IEEE_802.1ad">IEEE 802.1ad</a>
	 * @see <a href="https://en.wikipedia.org/wiki/IEEE_802.1Q">IEEE 802.1Q</a>
	 */
	public static final class Header extends AbstractPacket.Header {

		public static final int VLAN_HEADER_LENGTH = 4;

		private final PriorityCodePoint priorityCodePoint; // 3 bit
		private final byte canonicalFormatIndicator; // 1 bit
		private final short vlanIdentifier; // 12 bit
		private final NetworkLayer type;

		private final Builder builder;

		private Header(final Builder builder) {
			this.priorityCodePoint = builder.priorityCodePoint;
			this.canonicalFormatIndicator = builder.canonicalFormatIndicator;
			this.vlanIdentifier = builder.vlanIdentifier;
			this.type = builder.type;
			this.buffer = builder.buffer.slice(builder.buffer.readerIndex(), getLength());
			this.builder = builder;
		}

		public PriorityCodePoint getPriorityCodePoint() {
			return priorityCodePoint;
		}

		public int getCanonicalFormatIndicator() {
			return canonicalFormatIndicator & 0x01;
		}

		public int getVlanIdentifier() {
			return vlanIdentifier & 0x0fff;
		}

		public NetworkLayer getType() {
			return type;
		}

		@Override
		public NetworkLayer getPayloadType() {
			return type;
		}

		@Override
		public int getLength() {
			return Vlan.Header.VLAN_HEADER_LENGTH;
		}

		@Override
		public Memory getBuffer() {
			if (buffer == null) {
				buffer = ALLOCATOR.allocate(getLength());
				buffer.setShort(0, NetworkLayer.DOT1Q_VLAN_TAGGED_FRAMES.getValue());
				buffer.setShort(2, ((priorityCodePoint.getValue() << 13) & 0x07)
						| ((canonicalFormatIndicator << 14) & 0x01) | (vlanIdentifier & 0x0fff));
			}
			return buffer;
		}

		@Override
		public Vlan.Builder getBuilder() {
			return builder;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\tpriorityCodePoint: ").append(priorityCodePoint).append('\n')
					.append("\tcanonicalFormatIndicator: ").append(canonicalFormatIndicator).append('\n')
					.append("\tvlanIdentifier: ").append(vlanIdentifier).append('\n')
					.append("\ttype: ").append(type).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ Vlan Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder extends AbstractPacket.Builder {

		private PriorityCodePoint priorityCodePoint; // 3 bit
		private byte canonicalFormatIndicator; // 1 bit
		private short vlanIdentifier; // 12 bit
		private NetworkLayer type;

		private Memory buffer;
		private Memory payloadBuffer;

		public Builder priorityCodePoint(final PriorityCodePoint priorityCodePoint) {
			this.priorityCodePoint = priorityCodePoint;
			return this;
		}

		public Builder canonicalFormatIndicator(final int canonicalFormatIndicator) {
			this.canonicalFormatIndicator = (byte) (canonicalFormatIndicator & 0x01);
			return this;
		}

		public Builder vlanIdentifier(final int vlanIdentifier) {
			this.vlanIdentifier = (short) (vlanIdentifier & 0x0fff);
			return this;
		}

		public Builder type(final NetworkLayer type) {
			this.type = type;
			return this;
		}

		public Builder payloadBuffer(final Memory buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Vlan build() {
			return new Vlan(this);
		}

		@Override
		public Vlan build(final Memory buffer) {
			short tci = buffer.readShort();
			short type = buffer.readShort();
			this.priorityCodePoint = PriorityCodePoint.valueOf((byte) (tci >> 13 & 0x07));
			this.canonicalFormatIndicator = (byte) (tci >> 14 & 0x01);
			this.vlanIdentifier = (short) (tci & 0x0fff);
			this.type = NetworkLayer.valueOf(type);
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new Vlan(this);
		}

		@Override
		public void reset() {
			if (buffer != null) {
				reset(buffer.readerIndex(), Header.VLAN_HEADER_LENGTH);
			}
		}

		@Override
		public void reset(int offset, int length) {
			if (buffer != null) {
				Validate.notIllegalArgument(offset + length <= buffer.capacity());
				Validate.notIllegalArgument(priorityCodePoint != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(canonicalFormatIndicator >= 0, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(vlanIdentifier >= 0, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(type != null, ILLEGAL_HEADER_EXCEPTION);
				int index = offset;
				int tci = ((priorityCodePoint.getValue() << 13) & 0x07)
						| ((canonicalFormatIndicator << 14) & 0x01) | (vlanIdentifier & 0x0fff);
				buffer.setShort(index, tci);
				index += 2;
				buffer.setShort(index, type.getValue());
			}
		}

	}

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/IEEE_P802.1p">IEEE P802.1p</a>
	 */
	public static final class PriorityCodePoint extends NamedNumber<Byte, PriorityCodePoint> {

		public static final PriorityCodePoint BK
				= new PriorityCodePoint((byte) 1, "Background (priority=0)");
		public static final PriorityCodePoint BE
				= new PriorityCodePoint((byte) 0, "Best effort (default)/(priority=1)");
		public static final PriorityCodePoint EE
				= new PriorityCodePoint((byte) 2, "Excellent effort (priority=2)");
		public static final PriorityCodePoint CA
				= new PriorityCodePoint((byte) 3, "Critical applications (priority=3)");
		public static final PriorityCodePoint VI
				= new PriorityCodePoint((byte) 4, "Video, < 100 ms latency and jitter (priority=4)");
		public static final PriorityCodePoint VO
				= new PriorityCodePoint((byte) 5, "Voice, < 10 ms latency and jitter (priority=5)");
		public static final PriorityCodePoint IC
				= new PriorityCodePoint((byte) 6, "Internetwork control (priority=6)");
		public static final PriorityCodePoint NC
				= new PriorityCodePoint((byte) 7, "Network control (priority=7)");

		private static final Map<Byte, PriorityCodePoint> registry
				= new HashMap<Byte, PriorityCodePoint>();

		protected PriorityCodePoint(Byte value, String name) {
			super(value, name);
		}

		/**
		 * Get priority code point from value.
		 * @param value value.
		 * @return returns {@link PriorityCodePoint}.
		 */
		public static PriorityCodePoint valueOf(final byte value) {
			PriorityCodePoint priorityCodePoint = registry.get(value);
			if (priorityCodePoint == null) {
				return new PriorityCodePoint((byte) -1, "UNKONWN");
			}
			return priorityCodePoint;
		}

		/**
		 * Add new {@link PriorityCodePoint} to registry.
		 * @param priorityCodePoint priority code point.
		 * @return returns {@link PriorityCodePoint}.
		 */
		public static PriorityCodePoint register(final PriorityCodePoint priorityCodePoint) {
			registry.put(priorityCodePoint.getValue(), priorityCodePoint);
			return priorityCodePoint;
		}

		static {
			registry.put(BK.getValue(), BK);
			registry.put(BE.getValue(), BE);
			registry.put(EE.getValue(), EE);
			registry.put(CA.getValue(), CA);
			registry.put(VI.getValue(), VI);
			registry.put(VO.getValue(), VO);
			registry.put(IC.getValue(), IC);
			registry.put(NC.getValue(), NC);
		}

	}

}
