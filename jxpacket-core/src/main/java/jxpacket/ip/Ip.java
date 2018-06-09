package jxpacket.ip;

import jxpacket.AbstractPacket;
import jxpacket.Packet;
import jxpacket.UnknownPacket;
import jxpacket.common.NamedNumber;
import jxpacket.ip.ipv6.Authentication;

import java.util.HashMap;
import java.util.Map;

public abstract class Ip extends AbstractPacket {

	private static final Map<IpType, Packet.Builder> registry
			= new HashMap<>();

	public static final Packet.Builder getBuilder(final Header header) {
		Packet.Builder builder = registry.get(header.getPayloadType());
		if (builder == null) {
			return new UnknownPacket.Builder();
		}
		return builder;
	}

	protected static abstract class AbstractIpHeader extends AbstractPacket.PacketHeader {

		protected final byte version;

		protected AbstractIpHeader(final byte version) {
			this.version = version;
		}

		public int getVersion() {
			return this.version & 0xf;
		}

	}

	public static final class IpType extends NamedNumber<Byte, IpType> {

		public static final IpType ICMP = new IpType((byte) 1, "Internet Control Message Protocol Version 4");

		public static final IpType IPV6 = new IpType((byte) 41, "IPv6 Header.");

		public static final IpType IPV6_ICMP = new IpType((byte) 58, "Internet Control Message Protocol Version 6");

		public static final IpType IPV6_ROUTING = new IpType((byte) 43, "Routing Header for IPv6.");

		public static final IpType IPV6_FRAGMENT = new IpType((byte) 44, "Fragment Header for IPv6.");

		public static final IpType IPV6_HOPOPT = new IpType((byte) 0, "IPv6 Hop by Hop NeighborDiscoveryOptions.");

		public static final IpType IPV6_DSTOPT = new IpType((byte) 60, "IPv6 Destination NeighborDiscoveryOptions.");

		public static final IpType IPV6_ESP = new IpType((byte) 50, "IPv6 ESP.");

		public static final IpType IPV6_AH = new IpType((byte) 51, "IPv6 Authentication Header.");

		public static final IpType IGMP = new IpType((byte) 2, "Internet Group Management Protocol");

		public static final IpType TCP = new IpType((byte) 6, "Transmission Control Protocol");

		public static final IpType UDP = new IpType((byte) 17, "User Datagram Protocol");

		public static final IpType UNKNOWN = new IpType((byte) -1, "Unknown");

		private static Map<Byte, IpType> registry = new HashMap<>();

		protected IpType(Byte value, String name) {
			super(value, name);
		}

		public static IpType register(final IpType protocol) {
			return registry.put(protocol.getValue(), protocol);
		}

		public static IpType valueOf(final Byte value) {
			IpType ipType = registry.get(value);
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
		registry.put(IpType.IPV6_AH, new Authentication.Builder());
		registry.put(IpType.UNKNOWN, new UnknownPacket.Builder());
	}

}
