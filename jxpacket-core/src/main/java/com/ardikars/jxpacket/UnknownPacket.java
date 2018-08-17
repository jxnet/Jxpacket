package com.ardikars.jxpacket;

import io.netty.buffer.ByteBuf;

public class UnknownPacket extends AbstractPacket {

	private final UnknownPacket.Header header;

	private UnknownPacket(final Builder builder) {
		this.header = new UnknownPacket.Header(builder);
	}

	public static UnknownPacket newPacket(final ByteBuf buffer) {
		return new UnknownPacket.Builder().build(buffer);
	}

	@Override
	public UnknownPacket.Header getHeader() {
		return this.header;
	}

	@Override
	public Packet getPayload() {
		return null;
	}

	public static final class Header extends PacketHeader {

		private ByteBuf buffer;

		public Header(final Builder builder) {
			this.buffer = builder.payloadBuffer;
		}

		@Override
		public int getLength() {
			return buffer.capacity();
		}

		@Override
		public ByteBuf getBuffer() {
			return this.buffer;
		}

		@Override
		public ProtocolType getPayloadType() {
			return ProtocolType.UNKNOWN;
		}
	}

	public static final class Builder extends PacketBuilder {

		private ByteBuf payloadBuffer;

		public Builder() { }

		public Builder payloadBuffer(final ByteBuf buffer) {
			this.payloadBuffer = buffer;
			return this;
		}

		@Override
		public UnknownPacket build() {
			return new UnknownPacket(this);
		}

		@Override
		public UnknownPacket build(ByteBuf buffer) {
			Builder builder = new Builder()
					.payloadBuffer(buffer);
			buffer.release();
			return new UnknownPacket(builder);
		}

	}

}
