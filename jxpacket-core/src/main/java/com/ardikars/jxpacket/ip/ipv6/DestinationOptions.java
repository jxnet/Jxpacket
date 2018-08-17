package com.ardikars.jxpacket.ip.ipv6;

import com.ardikars.jxpacket.Packet;
import io.netty.buffer.ByteBuf;
import com.ardikars.jxpacket.ip.Ip;

public class DestinationOptions extends Options {

	private final DestinationOptions.Header header;
	private final Packet payload;

	private DestinationOptions(final Builder builder) {
		this.header = new DestinationOptions.Header(builder);
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

	public static final class Header extends Options.Header {

		protected Header(Builder builder) {
			super(builder, builder.nextHeader);
		}

	}

	public static final class Builder extends Options.Builder {

		public Builder() {
			super(Ip.Type.IPV6_AH);
		}

		@Override
		public DestinationOptions build() {
			return new DestinationOptions(this);
		}

		@Override
		public Packet build(final ByteBuf buffer) {
			int index = 0;
			Builder builder = new Builder();
			index += 1;
			builder.extensionLength = buffer.getInt(index);
			index += 4;
			builder.options = new byte[Options.Header.FIXED_OPTIONS_LENGTH
					+ Options.Header.LENGTH_UNIT * builder.extensionLength];
			buffer.getBytes(index, options);
			buffer.release();
			return new DestinationOptions(this);
		}

	}

}
