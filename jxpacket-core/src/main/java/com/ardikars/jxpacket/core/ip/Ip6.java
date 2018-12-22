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

package com.ardikars.jxpacket.core.ip;

import com.ardikars.common.net.Inet6Address;
import com.ardikars.jxpacket.common.AbstractPacket;
import com.ardikars.jxpacket.common.Packet;
import com.ardikars.jxpacket.common.layer.TransportLayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class Ip6 extends Ip {

	private final Ip6.Header header;
	private final Packet payload;

	private Ip6(final Builder builder) {
		this.header = new Ip6.Header(builder);
		this.payload = TransportLayer.valueOf(this.header.getPayloadType().getValue())
				.newInstance(builder.payloadBuffer);
	}

	@Override
	public Ip6.Header getHeader() {
		return header;
	}

	@Override
	public Packet getPayload() {
		return payload;
	}

	/**
	 * @see <a href="https://tools.ietf.org/html/rfc8200">IPv6 HeaderAbstract</a>
	 */
	public static final class Header extends AbstractPacketHeader {

		public static final int IPV6_HEADER_LENGTH = 40;

		private final byte trafficClass;
		private final int flowLabel;
		private final short payloadLength;
		private final TransportLayer nextHeader;
		private final byte hopLimit;
		private final Inet6Address sourceAddress;
		private final Inet6Address destinationAddress;

		protected Header(final Builder builder) {
			super((byte) 0x06);
			this.trafficClass = builder.trafficClass;
			this.flowLabel = builder.flowLabel;
			this.payloadLength = builder.payloadLength;
			this.nextHeader = builder.nextHeader;
			this.hopLimit = builder.hopLimit;
			this.sourceAddress = builder.sourceAddress;
			this.destinationAddress = builder.destinationAddress;
			this.buffer = builder.buffer.slice(0, getLength());
		}

		public int getTrafficClass() {
			return trafficClass & 0xff;
		}

		public int getFlowLabel() {
			return flowLabel & 0xfffff;
		}

		public int getPayloadLength() {
			return payloadLength & 0xffff;
		}

		public TransportLayer getNextHeader() {
			return nextHeader;
		}

		public int getHopLimit() {
			return hopLimit & 0xff;
		}

		public Inet6Address getSourceAddress() {
			return sourceAddress;
		}

		public Inet6Address getDestinationAddress() {
			return destinationAddress;
		}

		@Override
		public TransportLayer getPayloadType() {
			return nextHeader;
		}

		@Override
		public int getLength() {
			return IPV6_HEADER_LENGTH;
		}

		@Override
		public ByteBuf getBuffer() {
			if (buffer == null) {
				buffer = ALLOCATOR.directBuffer(getLength());
				buffer.writeInt((super.version & 0xf) << 28 | (trafficClass & 0xff) << 20 | flowLabel & 0xfffff);
				buffer.writeShort(payloadLength);
				buffer.writeByte(nextHeader.getValue());
				buffer.writeByte(hopLimit);
				buffer.writeBytes(sourceAddress.toBytes());
				buffer.writeBytes(destinationAddress.toBytes());
			}
			return buffer;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append("\tversion: ").append(version).append('\n')
					.append("\ttrafficClass: ").append(trafficClass).append('\n')
					.append("\tflowLabel: ").append(flowLabel).append('\n')
					.append("\tpayloadLength: ").append(payloadLength).append('\n')
					.append("\tnextHeader: ").append(nextHeader).append('\n')
					.append("\thopLimit: ").append(hopLimit).append('\n')
					.append("\tsourceAddress: ").append(sourceAddress).append('\n')
					.append("\tdestinationAddress: ").append(destinationAddress).append('\n')
					.toString();
		}

	}

	@Override
	public String toString() {
		return new StringBuilder("[ Ip6 Header (").append(getHeader().getLength()).append(" bytes) ]")
				.append('\n').append(header).append("\tpayload: ").append(payload != null ? payload.getClass().getSimpleName() : "")
				.toString();
	}

	public static final class Builder extends AbstractPaketBuilder {

		private byte trafficClass;
		private int flowLabel;
		private short payloadLength;
		private TransportLayer nextHeader;
		private byte hopLimit;
		private Inet6Address sourceAddress;
		private Inet6Address destinationAddress;

		private ByteBuf buffer;
		private ByteBuf payloadBuffer;

		public Builder trafficClass(final int trafficClass) {
			this.trafficClass = (byte) (trafficClass & 0xff);
			return this;
		}

		public Builder flowLabel(final int flowLabel) {
			this.flowLabel = flowLabel & 0xfffff;
			return this;
		}

		public Builder payloadLength(final int payloadLength) {
			this.payloadLength = (short) (payloadLength & 0xffff);
			return this;
		}

		public Builder nextHeader(final TransportLayer nextHeader) {
			this.nextHeader = nextHeader;
			return this;
		}

		public Builder hopLimit(final int hopLimit) {
			this.hopLimit = (byte) (hopLimit & 0xff);
			return this;
		}

		public Builder sourceAddress(final Inet6Address sourceAddress) {
			this.sourceAddress = sourceAddress;
			return this;
		}

		public Builder destinationAddress(final Inet6Address destinationAddress) {
			this.destinationAddress = destinationAddress;
			return this;
		}

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public Packet build() {
			return new Ip6(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			int iscratch = buffer.readInt();
			this.trafficClass = (byte) (iscratch >> 20 & 0xff);
			this.flowLabel = iscratch & 0xfffff;
			this.payloadLength = buffer.readShort();
			this.nextHeader = TransportLayer.valueOf(buffer.readByte());
			this.hopLimit = buffer.readByte();
			byte[] addrBuf = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
			buffer.readBytes(addrBuf);
			this.sourceAddress = Inet6Address.valueOf(addrBuf);
			addrBuf = new byte[Inet6Address.IPV6_ADDRESS_LENGTH];
			buffer.readBytes(addrBuf);
			this.destinationAddress = Inet6Address.valueOf(addrBuf);
			this.buffer = buffer;
			this.payloadBuffer = buffer.slice();
			return new Ip6(this);
		}

	}

	public abstract static class ExtensionHeader extends AbstractPacket.Header {

	}

}
