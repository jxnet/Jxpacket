package com.ardikars.jxpacket.core.ip;

import com.ardikars.jxpacket.common.AbstractPacket;

public abstract class Ip extends AbstractPacket {

	protected static abstract class IpHeader implements Header {

		protected final byte version;

		protected IpHeader(final byte version) {
			this.version = version;
		}

		public int getVersion() {
			return this.version & 0xf;
		}

	}

	protected static abstract class IpPaketBuilder implements Builder {

	}

}
