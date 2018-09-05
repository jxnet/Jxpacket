package com.ardikars.jxpacket.ip;

import com.ardikars.common.util.NamedNumber;
import com.ardikars.jxnet.packet.AbstractPacket;
import com.ardikars.jxnet.packet.Packet;
import com.ardikars.jxpacket.UnknownPacket;
import com.ardikars.jxpacket.ip.ip6.Authentication;
import com.ardikars.jxpacket.ip.ip6.DestinationOptions;
import com.ardikars.jxpacket.ip.ip6.HopByHopOptions;

import java.util.HashMap;
import java.util.Map;

public abstract class Ip extends AbstractPacket {

	private static final Map<Type, Builder> registry
			= new HashMap<>();

	public static final Builder getBuilder(final Header header) {
		Builder builder = registry.get(header.getPayloadType());
		if (builder == null) {
			return new UnknownPacket.Builder();
		}
		return builder;
	}

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

	public static final class Type extends NamedNumber<Byte, Type> {

		public static final Type ICMP = new Type((byte) 1, "Internet Control Message Protocol Version 4");

		public static final Type IPV6 = new Type((byte) 41, "IPv6 HeaderAbstract.");

		public static final Type IPV6_ICMP = new Type((byte) 58, "Internet Control Message Protocol Version 6");

		public static final Type IPV6_ROUTING = new Type((byte) 43, "Routing HeaderAbstract for IPv6.");

		public static final Type IPV6_FRAGMENT = new Type((byte) 44, "Fragment HeaderAbstract for IPv6.");

		public static final Type IPV6_HOPOPT = new Type((byte) 0, "IPv6 Hop by Hop NeighborDiscoveryOptions.");

		public static final Type IPV6_DSTOPT = new Type((byte) 60, "IPv6 Destination NeighborDiscoveryOptions.");

		public static final Type IPV6_ESP = new Type((byte) 50, "IPv6 ESP.");

		public static final Type IPV6_AH = new Type((byte) 51, "IPv6 Authentication HeaderAbstract.");

		public static final Type IGMP = new Type((byte) 2, "Internet Group Management Protocol");

		public static final Type TCP = new Type((byte) 6, "Transmission Control Protocol");

		public static final Type UDP = new Type((byte) 17, "User Datagram Protocol");

		public static final Type UNKNOWN = new Type((byte) -1, "Unknown");

		private static Map<Byte, Type> registry = new HashMap<>();

		protected Type(Byte value, String name) {
			super(value, name);
		}

		public static Type register(final Type protocol) {
			return registry.put(protocol.getValue(), protocol);
		}

		public static Type valueOf(final Byte value) {
			Type ipType = registry.get(value);
			if (ipType == null) {
				return UNKNOWN;
			} else {
				return ipType;
			}
		}

		static {
			registry.put(ICMP.getValue(), ICMP);
			registry.put(IPV6.getValue(), IPV6);
			registry.put(IPV6_ICMP.getValue(), IPV6_ICMP);
			registry.put(IPV6_ROUTING.getValue(), IPV6_ROUTING);
			registry.put(IPV6_FRAGMENT.getValue(), IPV6_FRAGMENT);
			registry.put(IPV6_HOPOPT.getValue(), IPV6_HOPOPT);
			registry.put(IPV6_DSTOPT.getValue(), IPV6_DSTOPT);
			registry.put(IPV6_ESP.getValue(), IPV6_ESP);
			registry.put(IPV6_AH.getValue(), IPV6_AH);
			registry.put(IGMP.getValue(), IGMP);
			registry.put(TCP.getValue(), TCP);
			registry.put(UDP.getValue(), UDP);
		}

	}

	static {
		registry.put(Type.IPV6_AH, new Authentication.Builder());
		registry.put(Type.IPV6_DSTOPT, new DestinationOptions.Builder());
		registry.put(Type.IPV6_HOPOPT, new HopByHopOptions.Builder());
		registry.put(Type.UNKNOWN, new UnknownPacket.Builder());
	}

}
