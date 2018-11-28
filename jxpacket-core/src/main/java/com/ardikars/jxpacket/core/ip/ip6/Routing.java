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
import com.ardikars.jxpacket.core.ip.Ip6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Routing extends AbstractPacket {

	private final Header header;
	private final Packet payload;

	private Routing(final Builder builder) {
		this.header = new Routing.Header(builder);
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

	public static final class Header extends Ip6.ExtensionHeader {

		public static final int FIXED_ROUTING_HEADER_LENGTH = 4;
		public static final int FIXED_ROUTING_DATA_LENGTH = 4;

		private final TransportLayer nextHeader;
		private final byte extensionLength;
		private final Type routingType;
		private final byte segmentLeft;

		private final byte[] routingData;

		private Header(final Builder builder) {
			this.nextHeader = builder.nextHeader;
			this.extensionLength = builder.extensionLength;
			this.routingType = builder.routingType;
			this.segmentLeft = builder.segmentLeft;
			this.routingData = builder.routingData;
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getExtensionLength() {
			return extensionLength & 0xff;
		}

		public Type getRoutingType() {
			return routingType;
		}

		public int getSegmentLeft() {
			return segmentLeft & 0xff;
		}

		/**
		 * Get routing data.
		 * @return returns routing data.
		 */
		public byte[] getRoutingData() {
			byte[] routingData = new byte[this.routingData.length];
			System.arraycopy(this.routingData, 0, routingData, 0, routingData.length);
			return routingData;
		}

		@Override
		public TransportLayer getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return FIXED_ROUTING_HEADER_LENGTH + (routingData == null ? 0 : routingData.length);
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setByte(0, nextHeader.getValue());
			buffer.setByte(1, extensionLength);
			buffer.setByte(2, routingType.getValue());
			buffer.setByte(3, segmentLeft);
			buffer.setBytes(4, routingData);
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\tnextHeader: ").append(nextHeader).append('\n')
					.append("\textensionLength: ").append(extensionLength).append('\n')
					.append("\troutingType: ").append(routingType).append('\n')
					.append("\tsegmentLeft: ").append(segmentLeft).append('\n')
					.append("\troutingData: ").append(Arrays.toString(routingData)).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ Routing Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder implements Packet.Builder {

		private TransportLayer nextHeader;
		private byte extensionLength;
		private Type routingType;
		private byte segmentLeft;

		private byte[] routingData;

		public Builder nextHeader(final TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder extensionLength(final int extensionLength) {
			this.extensionLength = (byte) (extensionLength & 0xff);
			return this;
		}

		public Builder routingType(final Type routingType) {
			this.routingType = routingType;
			return this;
		}

		public Builder segmentLeft(final int segmentLeft) {
			this.segmentLeft = (byte) (segmentLeft & 0xff);
			return this;
		}

		/**
		 * Add routing data.
		 * @param routingData routing data.
		 * @return returns this {@link Builder} object.
		 */
		public Builder routingData(final byte[] routingData) {
			this.routingData = new byte[routingData.length];
			System.arraycopy(routingData, 0, this.routingData, 0, this.routingData.length);
			return this;
		}

		@Override
		public Routing build() {
			return new Routing(this);
		}

		@Override
		public Routing build(final ByteBuf buffer) {
			int index = 0;
			Builder builder = new Builder();
			builder.nextHeader = TransportLayer.valueOf(buffer.getByte(index));
			index += 1;
			builder.extensionLength = buffer.getByte(index);
			index += 1;
			builder.routingType = Routing.Type.valueOf(buffer.getByte(index));
			index += 1;
			builder.segmentLeft = buffer.getByte(index);
			index += 1;
			builder.routingData = new byte[Header.FIXED_ROUTING_DATA_LENGTH + 8 * builder.extensionLength];
			buffer.getBytes(index, builder.routingData);
			release(buffer);
			return new Routing(builder);
		}

	}

	public static final class Type extends NamedNumber<Byte, Type> {

		public static final Type UNKNOWN
				= new Type((byte) -1, "UNKNOWN.");

		public static final Type DEPRECATED_01
				= new Type((byte) 0,
				"Due to the fact that with Routing HeaderAbstract type 0 a simple but effective[15]"
						+ " denial-of-service attack could be launched, this header is deprecated since 2007[16]"
						+ " and host and routers are required to ignore these headers.");

		public static final Type DEPRECATED_02
				= new Type((byte) 1,
				"Used for the Nimrod[17] project funded by DARPA. It is deprecated since 2009.");

		public static final Type ALLOWED_01
				= new Type((byte) 2,
				"A limited version of type 0 and is used for Mobile IPv6, where it can hold the Home Address of the Mobile Node.");

		public static final Type ALLOWED_02
				= new Type((byte) 3,
				"RPL Source Route HeaderAbstract[18] for Low-Power and Lossy Networks.");

		public static final Type PRIVATE_USE_01
				= new Type((byte) 253,
				"May be used for testing, not for actual implementations. RFC3692-style Experiment 1.[13]");

		public static final Type PRIVATE_USE_02
				= new Type((byte) 254,
				"May be used for testing, not for actual implementations. RFC3692-style Experiment 2.[13]");

		private static Map<Byte, Type> registry =
				new HashMap<>();

		protected Type(Byte value, String name) {
			super(value, name);
		}

		/**
		 * Get routing type from value.
		 * @param value value.
		 * @return returns {@link Type}.
		 */
		public static Type valueOf(final byte value) {
			Type type = registry.get(value);
			if (type == null) {
				return UNKNOWN;
			}
			return type;
		}

		/**
		 * Add new routing type to registry.
		 * @param type routing type.
		 * @return returns {@link Type}.
		 */
		public static Type register(final Type type) {
			registry.put(type.getValue(), type);
			return type;
		}

		static {
			registry.put(DEPRECATED_01.getValue(), DEPRECATED_01);
			registry.put(DEPRECATED_02.getValue(), DEPRECATED_02);
			registry.put(ALLOWED_01.getValue(), ALLOWED_01);
			registry.put(ALLOWED_02.getValue(), ALLOWED_02);
			registry.put(PRIVATE_USE_01.getValue(), PRIVATE_USE_01);
			registry.put(PRIVATE_USE_02.getValue(), PRIVATE_USE_02);
		}

	}

}
