package com.ardikars.jxpacket.ip.ip6;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import com.ardikars.jxpacket.ip.Ip;
import com.ardikars.jxpacket.ip.Ip6;

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

		private Ip.Type nextHeader;
		private byte extensionLength;
		private Type routingType;
		private byte segmentLeft;

		private byte[] routingData;

		private Header(final Builder builder) {
			this.nextHeader = builder.nextHeader;
			this.extensionLength = builder.extensionLength;
			this.routingType = builder.routingType;
			this.segmentLeft = builder.segmentLeft;
			this.routingData = builder.routingData;
		}

		public Ip.Type getNextHeader() {
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

		public byte[] getRoutingData() {
			return routingData;
		}

		@Override
		public Ip.Type getPayloadType() {
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
			final StringBuilder sb = new StringBuilder("HeaderAbstract{");
			sb.append("nextHeader=").append(getNextHeader());
			sb.append(", extensionLength=").append(getExtensionLength());
			sb.append(", routingType=").append(getRoutingType());
			sb.append(", segmentLeft=").append(getSegmentLeft());
			sb.append(", routingData=").append(Arrays.toString(getRoutingData()));
			sb.append('}');
			return sb.toString();
		}

	}

	public static final class Builder extends PacketBuilder {

		private Ip.Type nextHeader;
		private byte extensionLength;
		private Type routingType;
		private byte segmentLeft;

		private byte[] routingData;

		public Builder nextHeader(final Ip.Type nextHeader) {
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

		public Builder routingData(final byte[] routingData) {
			this.routingData = routingData;
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
			builder.nextHeader = Ip.Type.valueOf(buffer.getByte(index));
			index += 1;
			builder.extensionLength = buffer.getByte(index);
			index += 1;
			builder.routingType = Routing.Type.valueOf(buffer.getByte(index));
			index += 1;
			builder.segmentLeft = buffer.getByte(index);
			index += 1;
			builder.routingData = new byte[Header.FIXED_ROUTING_DATA_LENGTH + 8 * builder.extensionLength];
			buffer.getBytes(index, builder.routingData);
			buffer.release();
			return new Routing(builder);
		}

	}

	public static final class Type extends NamedNumber<Byte, Type> {

		public static final Type UNKNOWN
				= new Type((byte) -1, "UNKNOWN.");

		public static final Type DEPRECATED_01
				= new Type((byte) 0, "Due to the fact that with Routing HeaderAbstract type 0 a simple but effective[15] denial-of-service attack could be launched, this header is deprecated since 2007[16] and host and routers are required to ignore these headers.");

		public static final Type DEPRECATED_02
				= new Type((byte) 1, "Used for the Nimrod[17] project funded by DARPA. It is deprecated since 2009.");

		public static final Type ALLOWED_01
				= new Type((byte) 2, "A limited version of type 0 and is used for Mobile IPv6, where it can hold the Home Address of the Mobile Node.");

		public static final Type ALLOWED_02
				= new Type((byte) 3, "RPL Source Route HeaderAbstract[18] for Low-Power and Lossy Networks.");

		public static final Type PRIVATE_USE_01
				= new Type((byte) 253, "May be used for testing, not for actual implementations. RFC3692-style Experiment 1.[13]");

		public static final Type PRIVATE_USE_02
				= new Type((byte) 254, "May be used for testing, not for actual implementations. RFC3692-style Experiment 2.[13]");

		private static Map<Byte, Type> registry =
				new HashMap<>();

		protected Type(Byte value, String name) {
			super(value, name);
		}

		public static Type valueOf(final byte value) {
			Type type = registry.get(value);
			if (type == null) {
				return UNKNOWN;
			}
			return type;
		}

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