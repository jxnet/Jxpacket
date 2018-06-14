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

/**
 * This class is a abstraction for IP Address.
 * @see Inet4Address
 * @see java.net.InetAddress
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public abstract class InetAddress {

	/**
	 * Determines the IPv4 or IPv6 address.
	 * @param stringAddress ipv4 or ipv6 string address.
	 * @return an IPv4 or IPv6 address.
	 */
	public static InetAddress valueOf(String stringAddress) {
		if (stringAddress.contains(":")) {
			return Inet6Address.valueOf(stringAddress);
		} else {
			return Inet4Address.valueOf(stringAddress);
		}
	}

	/**
	 * Validate given ip string address.
	 * @param stringAddress ipv4 or ipv6 string address.
	 * @return a {@code boolean} indicating if the stringAddress is a valid ip address;
	 * or false otherwise.
	 */
	public static boolean isValidAddress(String stringAddress) {
		try {
			valueOf(stringAddress);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected abstract boolean isMulticastAddress();

	protected abstract boolean isAnyLocalAddress();

	protected abstract boolean isLoopbackAddress();

	protected abstract boolean isLinkLocalAddress();

	protected abstract boolean isSiteLocalAddress();

	protected abstract boolean isMcGlobal();

	protected abstract boolean isMcNodeLocal();

	protected abstract boolean isMcLinkLocal();

	protected abstract boolean isMcSiteLocal();

	protected abstract boolean isMcOrgLocal();

	protected abstract byte[] toBytes();

}
