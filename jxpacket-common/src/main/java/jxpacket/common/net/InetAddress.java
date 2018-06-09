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
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public abstract class InetAddress {

	public abstract byte[] toBytes();

	/**
	 * Create instance of Inet4Address or Inet6Address/
	 * @param ipString ipv4 or ipv6 string address.
	 * @return Inet4Address or Inet6Address.
	 */
	public static InetAddress valueOf(String ipString) {
		if (ipString.contains(":")) {
			return Inet6Address.valueOf(ipString);
		} else {
			return Inet4Address.valueOf(ipString);
		}
	}

	/**
	 * Validate ipv4 or ipv6 address.
	 * @param ipString ipv4 or ipv6 string address.
	 * @return true is valid, false otherwise.
	 */
	public static boolean isValidAddress(String ipString) {
		try {
			valueOf(ipString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//
	/**
	 * Utility routine to check if the InetAddress is an
	 * IP multicast address.
	 * @return a {@code boolean} indicating if the InetAddress is
	 * an IP multicast address
	 * @since   1.1
	 */
	public boolean isMulticastAddress() {
		return false;
	}

	/**
	 * Utility routine to check if the InetAddress is a wildcard address.
	 * @return a {@code boolean} indicating if the Inetaddress is
	 *         a wildcard address.
	 * @since 1.4
	 */
	public boolean isAnyLocalAddress() {
		return false;
	}

	/**
	 * Utility routine to check if the InetAddress is a loopback address.
	 *
	 * @return a {@code boolean} indicating if the InetAddress is
	 * a loopback address; or false otherwise.
	 * @since 1.4
	 */
	public boolean isLoopbackAddress() {
		return false;
	}

	/**
	 * Utility routine to check if the InetAddress is an link local address.
	 *
	 * @return a {@code boolean} indicating if the InetAddress is
	 * a link local address; or false if address is not a link local unicast address.
	 * @since 1.4
	 */
	public boolean isLinkLocalAddress() {
		return false;
	}

	/**
	 * Utility routine to check if the InetAddress is a site local address.
	 *
	 * @return a {@code boolean} indicating if the InetAddress is
	 * a site local address; or false if address is not a site local unicast address.
	 * @since 1.4
	 */
	public boolean isSiteLocalAddress() {
		return false;
	}

	/**
	 * Utility routine to check if the multicast address has global scope.
	 *
	 * @return a {@code boolean} indicating if the address has
	 *         is a multicast address of global scope, false if it is not
	 *         of global scope or it is not a multicast address
	 * @since 1.4
	 */
	public boolean isMcGlobal() {
		return false;
	}

	/**
	 * Utility routine to check if the multicast address has node scope.
	 *
	 * @return a {@code boolean} indicating if the address has
	 *         is a multicast address of node-local scope, false if it is not
	 *         of node-local scope or it is not a multicast address
	 * @since 1.4
	 */
	public boolean isMcNodeLocal() {
		return false;
	}

	/**
	 * Utility routine to check if the multicast address has link scope.
	 *
	 * @return a {@code boolean} indicating if the address has
	 *         is a multicast address of link-local scope, false if it is not
	 *         of link-local scope or it is not a multicast address
	 * @since 1.4
	 */
	public boolean isMcLinkLocal() {
		return false;
	}

	/**
	 * Utility routine to check if the multicast address has site scope.
	 *
	 * @return a {@code boolean} indicating if the address has
	 *         is a multicast address of site-local scope, false if it is not
	 *         of site-local scope or it is not a multicast address
	 * @since 1.4
	 */
	public boolean isMcSiteLocal() {
		return false;
	}

	/**
	 * Utility routine to check if the multicast address has organization scope.
	 *
	 * @return a {@code boolean} indicating if the address has
	 *         is a multicast address of organization-local scope,
	 *         false if it is not of organization-local scope
	 *         or it is not a multicast address
	 * @since 1.4
	 */
	public boolean isMcOrgLocal() {
		return false;
	}

}
