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

package jxpacket.common.net;

import jxpacket.common.util.Validate;

import java.util.Arrays;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public final class Inet4Address extends InetAddress {

	/**
	 * IPv4 Address (0.0.0.0).
	 */
	public static final Inet4Address ZERO = valueOf(0);

	/**
	 * IPv4 Loopback address (127.0.0.1).
	 */
	public static final Inet4Address LOCALHOST = valueOf("127.0.0.1");

	/**
	 * IPv4 Address Length.
	 */
	public static final int IPV4_ADDRESS_LENGTH = 4;
	
	private byte[] address = new byte[Inet4Address.IPV4_ADDRESS_LENGTH];

	private Inet4Address() {

	}

	private Inet4Address(final byte[] address) {
		Validate.nullPointer(address);
		Validate.notIllegalArgument(address.length == IPV4_ADDRESS_LENGTH);
		this.address = address;
	}

	/**
	 * Create Inet4Address instance.
	 * @param inet4Address ipv4 string address.
	 * @return Inet4Address instance.
	 */
	public static Inet4Address valueOf(String inet4Address) {
		inet4Address = Validate.nullPointer(inet4Address, "0.0.0.0");
		String[] parts = inet4Address.split("\\.");
		byte[] result = new byte[parts.length];
		Validate.notIllegalArgument(result.length == IPV4_ADDRESS_LENGTH);
		for (int i = 0; i < result.length; i++) {
			Validate.notIllegalArgument(parts[i] != null || parts[i].length() != 0);
			Validate.notIllegalArgument(!(parts[i].length() > 1 && parts[i].startsWith("0")));
			result[i] = Integer.valueOf(parts[i]).byteValue();
			Validate.notIllegalArgument((result[i] & 0xff) <= 0xff);
		}
		return Inet4Address.valueOf(result);
	}

	/**
	 * Create IPv4Address instance.
	 * @param address ipv4 bytes address.
	 * @return IPv4Address instance.
	 */
	public static Inet4Address valueOf(final byte[] address) {
		return new Inet4Address(address);
	}

	/**
	 * Create Inet4Address instance.
	 * @param address ipv4 int address.
	 * @return Inet4Address instance.
	 */
	public static Inet4Address valueOf(final int address) {
		return new Inet4Address(new byte[]{(byte) (address >>> 24),
				(byte) (address >>> 16), (byte) (address >>> 8),
				(byte) address});
	}

	@Override
	public boolean isMulticastAddress() {
		return ((toInt() & 0xf0000000) == 0xe0000000);
	}

	@Override
	public boolean isAnyLocalAddress() {
		return toInt() == 0;
	}

	@Override
	public boolean isLoopbackAddress() {
		/* 127.x.x.x */
		return address[0] == 127;
	}

	@Override
	public boolean isLinkLocalAddress() {
		// link-local unicast in IPv4 (169.254.0.0/16)
		// defined in "Documenting Special Use IPv4 Address Blocks
		// that have been Registered with IANA" by Bill Manning
		// draft-manning-dsua-06.txt
		int address = toInt();
		return (((address >>> 24) & 0xFF) == 169)
				&& (((address >>> 16) & 0xFF) == 254);
	}

	@Override
	public boolean isSiteLocalAddress() {
		// refer to RFC 1918
		// 10/8 prefix
		// 172.16/12 prefix
		// 192.168/16 prefix
		int address = toInt();
		return (((address >>> 24) & 0xFF) == 10)
				|| ((((address >>> 24) & 0xFF) == 172)
				&& (((address >>> 16) & 0xF0) == 16))
				|| ((((address >>> 24) & 0xFF) == 192)
				&& (((address >>> 16) & 0xFF) == 168));
	}

	@Override
	public boolean isMcGlobal() {
		// 224.0.1.0 to 238.255.255.255
		return ((address[0] & 0xff) >= 224 && (address[0] & 0xff) <= 238 ) &&
				!((address[0] & 0xff) == 224 && address[1] == 0 &&
						address[2] == 0);
	}

	@Override
	public boolean isMcNodeLocal() {
		// unless ttl == 0
		return false;
	}

	@Override
	public boolean isMcLinkLocal() {
		// 224.0.0/24 prefix and ttl == 1
		int address = toInt();
		return (((address >>> 24) & 0xFF) == 224)
				&& (((address >>> 16) & 0xFF) == 0)
				&& (((address >>> 8) & 0xFF) == 0);
	}

	@Override
	public boolean isMcSiteLocal() {
		// 239.255/16 prefix or ttl < 32
		int address = toInt();
		return (((address >>> 24) & 0xFF) == 239)
				&& (((address >>> 16) & 0xFF) == 255);
	}

	@Override
	public boolean isMcOrgLocal() {
		// 239.192 - 239.195
		int address = toInt();
		return (((address >>> 24) & 0xFF) == 239)
				&& (((address >>> 16) & 0xFF) >= 192)
				&& (((address >>> 16) & 0xFF) <= 195);
	}

	/**
	 * Returning int address of Inet4Address.
	 * @return int ipv4 address.
	 */
	public int toInt() {
		int ip = 0;
		for (int i = 0; i < Inet4Address.IPV4_ADDRESS_LENGTH; i++) {
			final int t = (this.address[i] & 0xff) << (3 - i) * 8;
			ip |= t;
		}
		return ip;
	}

	/**
	 * Returning bytes address of Inet4Address.
	 * @return bytes ipv4 address.
	 */
	public byte[] toBytes() {
		return Arrays.copyOf(this.address, this.address.length);
	}

	/**
	 * Change value of Inet4address.
	 * @param inet4address Inet4Address.
	 */
	public void update(final Inet4Address inet4address) {
		Validate.nullPointer(inet4address);
		this.address = inet4address.toBytes();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Inet4Address that = (Inet4Address) o;

		return Arrays.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(address);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.address[0] & 0xff).append(".");
		sb.append(this.address[1] & 0xff).append(".");
		sb.append(this.address[2] & 0xff).append(".");
		sb.append(this.address[3] & 0xff);
		return sb.toString();
	}

}
