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

import com.ardikars.common.memory.Memory;
import com.ardikars.common.net.Inet4Address;
import com.ardikars.common.net.MacAddress;
import com.ardikars.common.util.NamedNumber;
import com.ardikars.common.util.Validate;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.DataLinkLayer;
import com.ardikars.jxpacket.common.layer.NetworkLayer;

import java.util.HashMap;
import java.util.Map;

public class Arp extends AbstractPacket {

	private final Arp.Header header;
	private final Packet payload;

	private Arp(final Builder builder) {
		this.header = new Arp.Header(builder);
		this.payload = NetworkLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
		payloadBuffer = builder.payloadBuffer;
	}

	@Override
	public Arp.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	public static final Arp newPacket(final Memory buffer) {
		return new Arp.Builder().build(buffer);
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc826">Arp HeaderAbstract (RFC826)</a>
	 */
	public static final class Header extends AbstractPacket.Header {

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

		private final Builder builder;

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
			this.buffer = builder.buffer.slice(builder.buffer.readerIndex() - getLength(), getLength());
			this.builder = builder;
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
		public Memory getBuffer() {
			if (buffer == null) {
				buffer = ALLOCATOR.allocate(getLength());
				buffer.writeShort(hardwareType.getValue());
				buffer.writeShort(protocolType.getValue());
				buffer.writeByte(hardwareAddressLength);
				buffer.writeByte(protocolAddressLength);
				buffer.writeShort(operationCode.getValue());
				buffer.writeBytes(senderHardwareAddress.toBytes());
				buffer.writeBytes(senderProtocolAddress.toBytes());
				buffer.writeBytes(targetHardwareAddress.toBytes());
				buffer.writeBytes(targetProtocolAddress.toBytes());
			}
			return buffer;
		}

        @Override
        public Arp.Builder getBuilder() {
            return builder;
        }

        @Override
		public String toString() {
			return new StringBuilder()
					.append("\thardwareType: ").append(hardwareType).append('\n')
					.append("\tprotocolType: ").append(protocolType).append('\n')
					.append("\thardwareAddressLength: ").append(hardwareAddressLength).append('\n')
					.append("\tprotocolAddressLength: ").append(protocolAddressLength).append('\n')
					.append("\toperationCode: ").append(operationCode).append('\n')
					.append("\tsenderHardwareAddress: ").append(senderHardwareAddress).append('\n')
					.append("\tsenderProtocolAddress: ").append(senderProtocolAddress).append('\n')
					.append("\ttargetHardwareAddress: ").append(targetHardwareAddress).append('\n')
					.append("\ttargetProtocolAddress: ").append(targetProtocolAddress).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ Arp Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder extends AbstractPacket.Builder {

		private DataLinkLayer hardwareType;
		private NetworkLayer protocolType;
		private byte hardwareAddressLength;
		private byte protocolAddressLength;
		private OperationCode operationCode;
		private MacAddress senderHardwareAddress;
		private Inet4Address senderProtocolAddress;
		private MacAddress targetHardwareAddress;
		private Inet4Address targetProtocolAddress;

		private Memory buffer;
		private Memory payloadBuffer;

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

		public Builder payloadBuffer(final Memory buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Arp build() {
			return new Arp(this);
		}

		@Override
		public Arp build(final Memory buffer) {
			this.hardwareType = DataLinkLayer.valueOf(buffer.readShort());
			this.protocolType = NetworkLayer.valueOf(buffer.readShort());
			this.hardwareAddressLength = buffer.readByte();
			this.protocolAddressLength = buffer.readByte();
			this.operationCode = OperationCode.valueOf(buffer.readShort());
			byte[] byteBuffer;
			int hardwareAddressLength = this.hardwareAddressLength & 0xff;
			int protocolAddressLength = this.protocolAddressLength & 0xff;
			byteBuffer = new byte[hardwareAddressLength];
			buffer.readBytes(byteBuffer);
			this.senderHardwareAddress = MacAddress.valueOf(byteBuffer);
			byteBuffer = new byte[protocolAddressLength];
			buffer.readBytes(byteBuffer);
			this.senderProtocolAddress = Inet4Address.valueOf(byteBuffer);
			byteBuffer = new byte[hardwareAddressLength];
			buffer.readBytes(byteBuffer);
			this.targetHardwareAddress = MacAddress.valueOf(byteBuffer);
			byteBuffer = new byte[protocolAddressLength];
			buffer.readBytes(byteBuffer);
			this.targetProtocolAddress = Inet4Address.valueOf(byteBuffer);
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new Arp(this);
		}

		@Override
		public void reset() {
			if (buffer != null) {
				reset(buffer.readerIndex(), Header.ARP_HEADER_LENGTH);
			}
		}

		@Override
		public void reset(int offset, int length) {
			if (buffer != null) {
				Validate.notIllegalArgument(offset + length <= buffer.capacity());
				Validate.notIllegalArgument(hardwareType != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(protocolType != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(hardwareAddressLength != 0, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(protocolAddressLength != 0, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(senderHardwareAddress != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(senderProtocolAddress != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(targetHardwareAddress != null, ILLEGAL_HEADER_EXCEPTION);
				Validate.notIllegalArgument(targetProtocolAddress != null, ILLEGAL_HEADER_EXCEPTION);
				int index = offset;
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
			}
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
