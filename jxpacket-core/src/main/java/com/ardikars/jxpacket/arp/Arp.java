package com.ardikars.jxpacket.arp;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.DataLinkType;
import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ProtocolType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class Arp extends AbstractPacket {

	private final Arp.Header header;
	private final Packet payload;

	private Arp(final Builder builder) {
		this.header = new Arp.Header(builder);
		this.payload = super.getPayloadBuilder(this.header)
				.build(builder.payloadBuffer);
	}

	@Override
	public Arp.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final Arp newPacket(final ByteBuf buffer) {
		return new Arp.Builder().build(buffer);
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc826">Arp Header (RFC826)</a>
	 */
	public static final class Header extends PacketHeader {

		public static final int ARP_HEADER_LENGTH = 28;

		private DataLinkType hardwareType;
		private ProtocolType protocolType;
		private byte hardwareAddressLength;
		private byte protocolAddressLength;
		private OperationCode operationCode;
		private MacAddress senderHardwareAddress;
		private Inet4Address senderProtocolAddress;
		private MacAddress targetHardwareAddress;
		private Inet4Address targetProtocolAddress;

		private Header(final Builder builder) {
			this.hardwareType = builder.hardwareType;
			this.protocolType = builder.protocolType;
			this.hardwareAddressLength = builder.hardwareAddressLength;
			this.protocolAddressLength = builder.protocolAddressLength;
			this.operationCode = builder.operationCode;
			this.senderHardwareAddress = builder.senderHardwareAddress;
			this.senderProtocolAddress = builder.senderProtocolAddress;
			this.targetHardwareAddress = builder.targetHardwareAddress;
			this.targetProtocolAddress = builder.targetProtocolAddress;
		}

		public DataLinkType getHardwareType() {
			return hardwareType;
		}

		public ProtocolType getProtocolType() {
			return protocolType;
		}

		public int getHardwareAddressLength() {
			return hardwareAddressLength & 0xff;
		}

		public int getProtocolAddressLength() {
			return protocolAddressLength & 0xff;
		}

		public OperationCode getOperationCode() {
			return operationCode;
		}

		public MacAddress getSenderHardwareAddress() {
			return senderHardwareAddress;
		}

		public Inet4Address getSenderProtocolAddress() {
			return senderProtocolAddress;
		}

		public MacAddress getTargetHardwareAddress() {
			return targetHardwareAddress;
		}

		public Inet4Address getTargetProtocolAddress() {
			return targetProtocolAddress;
		}

		@Override
		public ProtocolType getPayloadType() {
			return ProtocolType.UNKNOWN;
		}

		@Override
		public int getLength() {
			return Arp.Header.ARP_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			int index = 0;
			buffer.setShort(index, hardwareType.getValue());
			index += 2;
			buffer.setShort(index, protocolType.getValue());
			index += 2;
			buffer.setByte(index, hardwareAddressLength);
			index += 1;
			buffer.setByte(index, protocolAddressLength);
			index += 1;
			buffer.setShort(index, operationCode.getValue());
			index += 2;
			buffer.setBytes(index, senderHardwareAddress.toBytes());
			index += MacAddress.MAC_ADDRESS_LENGTH;
			buffer.setBytes(index, senderProtocolAddress.toBytes());
			index += Inet4Address.IPV4_ADDRESS_LENGTH;
			buffer.setBytes(index, targetHardwareAddress.toBytes());
			index += MacAddress.MAC_ADDRESS_LENGTH;
			buffer.setBytes(index, targetProtocolAddress.toBytes());
			return buffer;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("hardwareType=").append(getHardwareType());
			sb.append(", protocolType=").append(getPayloadType());
			sb.append(", hardwareAddressLength=").append(getHardwareAddressLength());
			sb.append(", protocolAddressLength=").append(getProtocolAddressLength());
			sb.append(", operationCode=").append(getOperationCode());
			sb.append(", senderHardwareAddress=").append(getSenderHardwareAddress());
			sb.append(", senderProtocolAddress=").append(getSenderProtocolAddress());
			sb.append(", targetHardwareAddress=").append(getTargetHardwareAddress());
			sb.append(", targetProtocolAddress=").append(getTargetProtocolAddress());
			sb.append('}');
			return sb.toString();
		}
	}

	public static final class Builder extends PacketBuilder {

		private DataLinkType hardwareType;
		private ProtocolType protocolType;
		private byte hardwareAddressLength;
		private byte protocolAddressLength;
		private OperationCode operationCode;
		private MacAddress senderHardwareAddress;
		private Inet4Address senderProtocolAddress;
		private MacAddress targetHardwareAddress;
		private Inet4Address targetProtocolAddress;

		private ByteBuf payloadBuffer;

		public Builder() {

		}

		public Builder hardwareType(final DataLinkType hardwareType) {
			this.hardwareType = hardwareType;
			return this;
		}

		public Builder protocolType(final ProtocolType protocolType) {
			this.protocolType = protocolType;
			return this;
		}

		public Builder hardwareAddressLength(final int hardwareAddressLength) {
			this.hardwareAddressLength = (byte) (hardwareAddressLength & 0xff);
			return this;
		}

		public Builder protocolAddressLength(final int protocolAddressLength) {
			this.protocolAddressLength = (byte) (protocolAddressLength & 0xff);
			return this;
		}

		public Builder operationCode(final OperationCode operationCode) {
			this.operationCode = operationCode;
			return this;
		}

		public Builder senderHardwareAddress(final MacAddress senderHardwareAddress) {
			this.senderHardwareAddress = senderHardwareAddress;
			return this;
		}

		public Builder senderProtocolAddress(final Inet4Address senderProtocolAddress) {
			this.senderProtocolAddress = senderProtocolAddress;
			return this;
		}

		public Builder targetHardwareAddress(final MacAddress targetHardwareAddress) {
			this.targetHardwareAddress = targetHardwareAddress;
			return this;
		}

		public Builder targetProtocolAddress(final Inet4Address targetProtocolAddress) {
			this.targetProtocolAddress = targetProtocolAddress;
			return this;
		}

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Arp build() {
			return new Arp(this);
		}

		@Override
		public Arp build(final ByteBuf buffer) {
			int index = 0;

			byte[] byteBuffer;

			Arp.Builder builder = new Builder();
			builder.hardwareType = DataLinkType.valueOf(buffer.getShort(index));
			index += 2;
			builder.protocolType = ProtocolType.valueOf(buffer.getShort(index));
			index += 2;
			builder.hardwareAddressLength = buffer.getByte(index);
			index += 1;
			builder.protocolAddressLength = buffer.getByte(index);
			index += 1;
			builder.operationCode = OperationCode.valueOf(buffer.getShort(index));
			index += 2;

			int hardwareAddressLength = builder.hardwareAddressLength & 0xff;
			int protocolAddressLength = builder.protocolAddressLength & 0xff;

			byteBuffer = new byte[hardwareAddressLength];
			buffer.getBytes(index, byteBuffer);
			builder.senderHardwareAddress = MacAddress.valueOf(byteBuffer);
			index += hardwareAddressLength;

			byteBuffer = new byte[protocolAddressLength];
			buffer.getBytes(index, byteBuffer);
			builder.senderProtocolAddress = Inet4Address.valueOf(byteBuffer);
			index += protocolAddressLength;

			byteBuffer = new byte[hardwareAddressLength];
			buffer.getBytes(index, byteBuffer);
			builder.targetHardwareAddress = MacAddress.valueOf(byteBuffer);
			index += hardwareAddressLength;

			byteBuffer = new byte[protocolAddressLength];
			buffer.getBytes(index, byteBuffer);
			builder.targetProtocolAddress = Inet4Address.valueOf(byteBuffer);
			index += protocolAddressLength;

			int size = index;
			builder.payloadBuffer = buffer.copy(size, buffer.capacity() - size);
			buffer.release();
			return new Arp(builder);
		}
	}

	public static final class OperationCode extends NamedNumber<Short, OperationCode> {

		public static final OperationCode ARP_REQUEST = new OperationCode((short) 0x01, "ARP Request");

		public static final OperationCode ARP_REPLY = new OperationCode((short) 0x02, "ARP Reply");

		public static final OperationCode UNKNOWN = new OperationCode((short) -1, "Unknown");

		public OperationCode(Short value, String name) {
			super(value, name);
		}

		private static final Map<Short, OperationCode> registry
				= new HashMap<Short, OperationCode>();

		public static OperationCode register(final OperationCode operationCode) {
			return registry.put(operationCode.getValue(), operationCode);
		}

		public static OperationCode valueOf(final Short value) {
			if (registry.containsKey(value)) {
				return registry.get(value);
			} else {
				return UNKNOWN;
			}
		}

		@Override
		public String toString() {
			return super.toString();
		}

		static {
			registry.put(ARP_REQUEST.getValue(), ARP_REQUEST);
			registry.put(ARP_REPLY.getValue(), ARP_REPLY);
		}

	}

}
