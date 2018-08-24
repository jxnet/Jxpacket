package com.ardikars.jxpacket.ip.ipv6;

import com.ardikars.jxpacket.Packet;
import com.ardikars.jxpacket.ip.Ip;
import io.netty.buffer.ByteBuf;

public class HopByHopOptions extends Options {

	private final HopByHopOptions.Header header;
	private final Packet payload;

	private HopByHopOptions(final Builder builder) {
		this.header = new Header(builder);
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

		protected Header(final HopByHopOptions.Builder builder) {
			super(builder, builder.nextHeader);
		}

	}

	public static final class Builder extends Options.Builder {

		public Builder() {
			super(Ip.Type.IPV6_HOPOPT);
		}

		@Override
		public HopByHopOptions build() {
			return new HopByHopOptions(this);
		}

		@Override
		public HopByHopOptions build(final ByteBuf buffer) {
			Builder builder = new Builder();
			builder.extensionLength = buffer.getInt(1);
			builder.options = new byte[Options.Header.FIXED_OPTIONS_LENGTH
					+ Options.Header.LENGTH_UNIT * builder.extensionLength];
			buffer.getBytes(5, options);
			buffer.release();
			return new HopByHopOptions(this);
		}

	}

}
