package com.ardikars.jxpacket.ip;

import com.ardikars.jxpacket.AbstractPacket;
import com.ardikars.jxpacket.Packet;

public abstract class Ip extends AbstractPacket {

	protected static abstract class IpHeader implements Packet.Header {

		protected final byte version;

		protected IpHeader(final byte version) {
			this.version = version;
		}

		public int getVersion() {
			return this.version & 0xf;
		}

	}

	protected static abstract class IpPaketBuilder implements Packet.Builder {

	}

}
