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
 * This class represents an Internet Protocol version 4 (IPv4) address.
 * Defined by <a href="https://tools.ietf.org/html/rfc790">IPv4 Address</a>
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
	 * Determines the IPv4 address.
	 * @param stringAddress ipv4 string address.
	 * @return an IPv4 address.
	 */
	public static Inet4Address valueOf(String stringAddress) {
		stringAddress = Validate.nullPointer(stringAddress, "0.0.0.0");
		String[] parts = stringAddress.split("\\.");
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
	 * Determines the IPv4 address.
	 * @param bytesAddress ipv4 bytes address.
	 * @return an IPv4 address.
	 */
	public static Inet4Address valueOf(final byte[] bytesAddress) {
		return new Inet4Address(bytesAddress);
	}

	/**
	 * Determines the IPv4 address.
	 * @param intAddress ipv4 int address.
	 * @return an IPv4 address.
	 */
	public static Inet4Address valueOf(final int intAddress) {
		return new Inet4Address(new byte[]{(byte) (intAddress >>> 24),
				(byte) (intAddress >>> 16), (byte) (intAddress >>> 8),
				(byte) intAddress});
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
		return address[0] == 127; /* 127.x.x.x */
	}

	@Override
	public boolean isLinkLocalAddress() {
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
	 * Returns the int IPv4 address of this {@code Inet4Address}
	 * object.
	 * @return  the int IPv4 address of this object.
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
	 * Returns the raw IPv4 address of this {@code Inet4Address}
	 * object. The result is in network byte order: the highest order
	 * byte of the address is in {@code toBytes()[0]}.
	 *
	 * @return  the raw IPv4 address of this object.
	 */
	public byte[] toBytes() {
		return Arrays.copyOf(this.address, this.address.length);
	}

	/**
	 * Change address of this {@code Inet4Address} object.
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
