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

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class Vlan extends AbstractPacket {

	private final Vlan.Header header;
	private final Packet payload;

	private Vlan(final Builder builder) {
		this.header = new Vlan.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getType().getValue())
				.newInstance(builder.payloadBuffer);
	}

	public static Vlan newPacket(final ByteBuf buffer) {
		return new Vlan.Builder().build(buffer);
	}

	@Override
	public Vlan.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return this.payload;
	}

	/**
	 * @see <a href="https://en.wikipedia.org/wiki/IEEE_802.1ad">IEEE 802.1ad</a>
	 * @see <a href="https://en.wikipedia.org/wiki/IEEE_802.1Q">IEEE 802.1Q</a>
	 */
	public static final class Header implements Packet.Header {

		public static final int VLAN_HEADER_LENGTH = 4;

		private final PriorityCodePoint priorityCodePoint; // 3 bit
		private final byte canonicalFormatIndicator; // 1 bit
		private final short vlanIdentifier; // 12 bit
		private final NetworkLayer type;

		private Header(final Builder builder) {
			this.priorityCodePoint = builder.priorityCodePoint;
			this.canonicalFormatIndicator = builder.canonicalFormatIndicator;
			this.vlanIdentifier = builder.vlanIdentifier;
			this.type = builder.type;
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
			return this.type;
		}

		@Override
		public int getLength() {
			return Vlan.Header.VLAN_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setShort(0, NetworkLayer.DOT1Q_VLAN_TAGGED_FRAMES.getValue());
			buffer.setShort(2, ((priorityCodePoint.getValue() << 13) & 0x07)
					| ((canonicalFormatIndicator << 14) & 0x01) | (vlanIdentifier & 0x0fff));
			return buffer;
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

	public static final class Builder implements Packet.Builder {

		private PriorityCodePoint priorityCodePoint; // 3 bit
		private byte canonicalFormatIndicator; // 1 bit
		private short vlanIdentifier; // 12 bit
		private NetworkLayer type;

		private ByteBuf payloadBuffer;

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

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Vlan build() {
			return new Vlan(this);
		}

		@Override
		public Vlan build(final ByteBuf buffer) {
			short tci = buffer.getShort(0);
			short type = buffer.getShort(2);
			Vlan.Builder builder = new Builder();
			builder.priorityCodePoint = PriorityCodePoint.valueOf((byte) (tci >> 13 & 0x07));
			builder.canonicalFormatIndicator = (byte) (tci >> 14 & 0x01);
			builder.vlanIdentifier = (short) (tci & 0x0fff);
			builder.type = NetworkLayer.valueOf(type);
			builder.payloadBuffer = buffer.copy(Header.VLAN_HEADER_LENGTH, buffer.capacity() - Header.VLAN_HEADER_LENGTH);
			release(buffer);
			return new Vlan(builder);
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
				= new HashMap<>();

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
