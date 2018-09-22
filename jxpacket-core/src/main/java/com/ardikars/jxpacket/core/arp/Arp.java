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

package com.ardikars.jxpacket.core.arp;

import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class Arp extends AbstractPacket {

	private final Arp.Header header;
	private final Packet payload;

	private Arp(final Builder builder) {
		this.header = new Arp.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
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
	 * @see <a href="https://tools.ietf.org/html/rfc826">Arp HeaderAbstract (RFC826)</a>
	 */
	public static final class Header implements Packet.Header {

		public static final int ARP_HEADER_LENGTH = 28;

		private final DataLinkLayer hardwareType;
		private final NetworkLayer protocolType;
		private final byte hardwareAddressLength;
		private final byte protocolAddressLength;
		private final OperationCode operationCode;
		private final MacAddress senderHardwareAddress;
		private final Inet4Address senderProtocolAddress;
		private final MacAddress targetHardwareAddress;
		private final Inet4Address targetProtocolAddress;

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

		public DataLinkLayer getHardwareType() {
			return hardwareType;
		}

		public NetworkLayer getProtocolType() {
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
		public NetworkLayer getPayloadType() {
			return NetworkLayer.UNKNOWN;
		}

		@Override
		public int getLength() {
			return Arp.Header.ARP_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(getLength());
			buffer.setShort(0, hardwareType.getValue());
			buffer.setShort(2, protocolType.getValue());
			buffer.setByte(4, hardwareAddressLength);
			buffer.setByte(5, protocolAddressLength);
			buffer.setShort(6, operationCode.getValue());
			buffer.setBytes(8, senderHardwareAddress.toBytes());
			buffer.setBytes(14, senderProtocolAddress.toBytes());
			buffer.setBytes(18, targetHardwareAddress.toBytes());
			buffer.setBytes(24, targetProtocolAddress.toBytes());
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder("ArpHeader{")
					.append("hardwareType=").append(getHardwareType())
					.append(", protocolType=").append(getPayloadType())
					.append(", hardwareAddressLength=").append(getHardwareAddressLength())
					.append(", protocolAddressLength=").append(getProtocolAddressLength())
					.append(", operationCode=").append(getOperationCode())
					.append(", senderHardwareAddress=").append(getSenderHardwareAddress())
					.append(", senderProtocolAddress=").append(getSenderProtocolAddress())
					.append(", targetHardwareAddress=").append(getTargetHardwareAddress())
					.append(", targetProtocolAddress=").append(getTargetProtocolAddress())
					.append('}').toString();
		}

	}

	public static final class Builder implements Packet.Builder {

		private DataLinkLayer hardwareType;
		private NetworkLayer protocolType;
		private byte hardwareAddressLength;
		private byte protocolAddressLength;
		private OperationCode operationCode;
		private MacAddress senderHardwareAddress;
		private Inet4Address senderProtocolAddress;
		private MacAddress targetHardwareAddress;
		private Inet4Address targetProtocolAddress;

		private ByteBuf payloadBuffer;

		public Builder hardwareType(final DataLinkLayer hardwareType) {
			this.hardwareType = hardwareType;
			return this;
		}

		public Builder protocolType(final NetworkLayer protocolType) {
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
			Arp.Builder builder = new Builder();
			builder.hardwareType = DataLinkLayer.valueOf(buffer.getShort(0));
			builder.protocolType = NetworkLayer.valueOf(buffer.getShort(2));
			builder.hardwareAddressLength = buffer.getByte(4);
			builder.protocolAddressLength = buffer.getByte(5);
			builder.operationCode = OperationCode.valueOf(buffer.getShort(6));
			byte[] byteBuffer;
			int hardwareAddressLength = builder.hardwareAddressLength & 0xff;
			int protocolAddressLength = builder.protocolAddressLength & 0xff;
			byteBuffer = new byte[hardwareAddressLength];
			buffer.getBytes(8, byteBuffer);
			builder.senderHardwareAddress = MacAddress.valueOf(byteBuffer);
			byteBuffer = new byte[protocolAddressLength];
			buffer.getBytes(14, byteBuffer);
			builder.senderProtocolAddress = Inet4Address.valueOf(byteBuffer);
			byteBuffer = new byte[hardwareAddressLength];
			buffer.getBytes(18, byteBuffer);
			builder.targetHardwareAddress = MacAddress.valueOf(byteBuffer);
			byteBuffer = new byte[protocolAddressLength];
			buffer.getBytes(24, byteBuffer);
			builder.targetProtocolAddress = Inet4Address.valueOf(byteBuffer);
			builder.payloadBuffer = buffer.copy(Header.ARP_HEADER_LENGTH, buffer.capacity() - Header.ARP_HEADER_LENGTH);
			buffer.release();
			return new Arp(builder);
		}
	}

	public static final class OperationCode extends NamedNumber<Short, OperationCode> {

		public static final OperationCode ARP_REQUEST = new OperationCode((short) 0x01, "Arp Request");

		public static final OperationCode ARP_REPLY = new OperationCode((short) 0x02, "Arp Reply");

		public static final OperationCode UNKNOWN = new OperationCode((short) -1, "Unknown");

		private static final Map<Short, OperationCode> registry
				= new HashMap<Short, OperationCode>();

		public OperationCode(Short value, String name) {
			super(value, name);
		}

		/**
		 * Add new {@link OperationCode} to registry.
		 * @param operationCode operation code.
		 * @return returns {@link OperationCode}.
		 */
		public static OperationCode register(final OperationCode operationCode) {
			return registry.put(operationCode.getValue(), operationCode);
		}

		/**
		 * Get operation code from value.
		 * @param value value.
		 * @return returns {@link OperationCode}.
		 */
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
