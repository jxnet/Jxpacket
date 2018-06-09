package jxpacket.common.net;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class NetworkInterface  {

	private String name;
	private String displayName;
	private int index;
	private List<Address> addresses;
	private MacAddress hardwareAddress;
	private boolean virtual;
	private boolean loopback;
	private boolean pointToPoint;
	private boolean up;
	private int mtu;

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getIndex() {
		return index;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public MacAddress getHardwareAddress() {
		return hardwareAddress;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public boolean isLoopback() {
		return loopback;
	}

	public boolean isPointToPoint() {
		return pointToPoint;
	}

	public boolean isUp() {
		return up;
	}

	public int getMtu() {
		return mtu;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("NetworkInterface{");
		sb.append("name='").append(name).append('\'');
		sb.append(", displayName='").append(displayName).append('\'');
		sb.append(", index=").append(index);
		sb.append(", addresses=").append(addresses);
		sb.append(", hardwareAddress=").append(hardwareAddress);
		sb.append(", virtual=").append(virtual);
		sb.append(", loopback=").append(loopback);
		sb.append(", pointToPoint=").append(pointToPoint);
		sb.append(", up=").append(up);
		sb.append(", mtu=").append(mtu);
		sb.append('}');
		return sb.toString();
	}

	public static final class Address {

		private InetAddress address = null;
		private InetAddress broadcast = null;
		private short maskLength = 0;

		public InetAddress getAddress() {
			return address;
		}

		public InetAddress getBroadcast() {
			return broadcast;
		}

		public short getMaskLength() {
			return maskLength;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Address{");
			sb.append("address=").append(address);
			sb.append(", broadcast=").append(broadcast);
			sb.append(", maskLength=").append(maskLength);
			sb.append('}');
			return sb.toString();
		}

	}

	public static Stream<NetworkInterface> getNetworkInterfacesAsStream() {
		return getNetworkInterfaces().stream();
	}

	public static List<NetworkInterface> getNetworkInterfaces() {
		List<NetworkInterface> networkInterfaces = new ArrayList<>();
		try {
			java.net.NetworkInterface.networkInterfaces()
					.forEach(value -> {
						NetworkInterface networkInterface = new NetworkInterface();
						networkInterface.name = value.getName();
						networkInterface.displayName = value.getDisplayName();
						networkInterface.index = value.getIndex();
						networkInterface.virtual = value.isVirtual();
						try {
							networkInterface.loopback = value.isLoopback();
							networkInterface.pointToPoint = value.isPointToPoint();
							networkInterface.up = value.isUp();
							networkInterface.mtu = value.getMTU();
							byte[] hardwareAddress = value.getHardwareAddress();
							if (hardwareAddress != null && hardwareAddress.length == MacAddress.MAC_ADDRESS_LENGTH) {
								networkInterface.hardwareAddress = MacAddress.valueOf(hardwareAddress);
							}
							networkInterface.addresses = new ArrayList<>();
							value.getInterfaceAddresses().stream()
									.forEach(interfaceAddress -> {
										Address address = new Address();
										java.net.InetAddress inetAddress = interfaceAddress.getAddress();
										if (inetAddress != null) {
											if (inetAddress instanceof java.net.Inet4Address) {
												address.address = Inet4Address.valueOf(inetAddress.getAddress());
											} else if (inetAddress instanceof java.net.Inet6Address) {
												address.address = Inet6Address.valueOf(inetAddress.getAddress());
											}
										}
										java.net.InetAddress inetBroadcaseAddress = interfaceAddress.getBroadcast();
										if (inetBroadcaseAddress != null) {
											if (inetBroadcaseAddress instanceof java.net.Inet4Address) {
												address.broadcast = Inet4Address.valueOf(inetBroadcaseAddress.getAddress());
											} else if (inetBroadcaseAddress instanceof java.net.Inet6Address) {
												address.broadcast = Inet6Address.valueOf(inetBroadcaseAddress.getAddress());
											}
										}
										address.maskLength = interfaceAddress.getNetworkPrefixLength();
										networkInterface.addresses.add(address);
									});
						} catch (SocketException e) {
							e.printStackTrace();
						}
						networkInterfaces.add(networkInterface);
					});
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return networkInterfaces;
	}

}
