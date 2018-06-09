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

import jxpacket.common.NamedNumber;
import jxpacket.common.util.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public final class MacAddress {

	/**
	 * MAC Address Length.
	 */
	public static final int MAC_ADDRESS_LENGTH = 6;

	/**
	 * Zero MAC Address (00:00:00:00:00:00).
	 */
	public static final MacAddress ZERO = valueOf("00:00:00:00:00:00");

	/**
	 * Dummy MAC Address (de:ad:be:ef:c0:fe).
	 */
	public static final MacAddress DUMMY = valueOf("de:ad:be:ef:c0:fe");

	/**
	 * Broadcast MAC Address (ff:ff:ff:ff:ff:ff).
	 */
	public static final MacAddress BROADCAST = valueOf("ff:ff:ff:ff:ff:ff");

	/**
	 * Multicast Address.
	 */
	public static final MacAddress IPV4_MULTICAST = valueOf("01:00:5e:00:00:00");

	public static final MacAddress IPV4_MULTICAST_MASK = valueOf("ff:ff:ff:80:00:00");
	
	private byte[] address = new byte[MAC_ADDRESS_LENGTH];
	
	private MacAddress(byte[] address) {
		Validate.nullPointer(address);
		Validate.notIllegalArgument(address.length == MAC_ADDRESS_LENGTH);
		this.address = Arrays.copyOf(address, MacAddress.MAC_ADDRESS_LENGTH);
	}

	/**
	 * Create MacAddress instance.
	 * @param address string MAC Address.
	 * @return MacAddress instance.
	 */
	public static MacAddress valueOf(String address) {
		address = Validate.nullPointer(address, "00:00:00:00:00:00");
		final String[] elements = address.split(":|-");
		Validate.notIllegalArgument(elements.length == MAC_ADDRESS_LENGTH);
		final byte[] b = new byte[MAC_ADDRESS_LENGTH];
		for (int i = 0; i < MAC_ADDRESS_LENGTH; i++) {
			final String element = elements[i];
			b[i] = (byte) Integer.parseInt(element, 16);
		}
		return new MacAddress(b);
	}

	/**
	 * Create MacAddress instance.
	 * @param address bytes MAC Address.
	 * @return MacAddress instance.
	 */
	public static MacAddress valueOf(final byte[] address) {
		return new MacAddress(address);
	}

	/**
	 * Create MacAddress instance.
	 * @param address long MAC Address.
	 * @return MacAddress instance.
	 */
	public static MacAddress valueOf(final long address) {
		final byte[] bytes = new byte[] {
				(byte) (address >> 40 & 0xff),
				(byte) (address >> 32 & 0xff),
				(byte) (address >> 24 & 0xff),
				(byte) (address >> 16 & 0xff),
				(byte) (address >> 8 & 0xff),
				(byte) (address >> 0 & 0xff)};
		return new MacAddress(bytes);
	}

	/**
	 * Validate Mac Address.
	 * @param address string address.
	 * @return true is valid, false otherwise.
	 */
	public static boolean isValidAddress(final String address) {
		Validate.nullPointer(address);
		return Pattern.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", address);
	}

	public void update(final MacAddress macAddress) {
		Validate.nullPointer(macAddress);
		this.address = macAddress.toBytes();
	}

	public Oui getOui() {
		return Oui.valueOf(this);
	}

	/**
	 * Returns length of MAC Address.
	 * @return MAC Address length.
	 */
	public int length() {
		return this.address.length;
	}

	/**
	 * Returns bytes MAC Address.
	 * @return bytes MAC Address.
	 */
	public byte[] toBytes() {
		return Arrays.copyOf(this.address, this.address.length);
	}

	/**
	 * Returning long MAC Address.
	 * @return long MAC Address.
	 */
	public long toLong() {
		long addr = 0;
		for (int i = 0; i < MAC_ADDRESS_LENGTH; i++) {
			long tmp = (this.address[i] & 0xffL) << (5 - i) * 8;
			addr |= tmp;
		}
		return addr;
	}

	/**
	 * Return true if Broadcast MAC Address.
	 * @return true if Broadcast MAC Address, false otherwise.
	 */
	public boolean isBroadcast() {
		for (final byte b : this.address) {
			if (b != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return true if Multicast MAC Address.
	 * @return true if Multicast MAC Address, false otherwise.
	 */
	public boolean isMulticast() {
		if (this.isBroadcast()) {
			return false;
		}
		return (this.address[0] & 0x01) != 0;
	}

	/**
	 *
	 * @return returns true if the MAC address represented by this object is
	 *         a globally unique address; otherwise false.
	 */
	public boolean isGloballyUnique() {
		return (address[0] & 2) == 0;
	}

	/**
	 *
	 * @return true if the MAC address represented by this object is
	 *         a unicast address; otherwise false.
	 */
	public boolean isUnicast() {
		return (address[0] & 1) == 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MacAddress that = (MacAddress) o;

		return Arrays.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(address);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final byte b : this.address) {
			if (sb.length() > 0) {
				sb.append(":");
			}
			String hex = Integer.toHexString(b & 0xff);
			sb.append(hex.length() == 1 ? "0" + hex : hex);
		}
		return sb.toString();
	}

	public static final class Oui extends NamedNumber<Integer, Oui> {

		/**
		 * Cisco: 0x00000C
		 */
		public static final Oui CISCO_00000C
				= new Oui(0x00000C, "Cisco");

		/**
		 * Fujitsu: 0x00000E
		 */
		public static final Oui FUJITSU_00000E
				= new Oui(0x00000E, "Fujitsu");

		/**
		 * Hewlett-Packard: 0x080009
		 */
		public static final Oui HEWLETT_PACKARD_080009
				= new Oui(0x080009, "Hewlett-Packard");

		/**
		 * Fuji-Xerox: 0x080037
		 */
		public static final Oui FUJI_XEROX_080037
				= new Oui(0x080037, "Fuji-Xerox");

		/**
		 * IBM: 0x08005A
		 */
		public static final Oui IBM_08005A
				= new Oui(0x08005A, "IBM");

		/**
		 * Cisco: 0x000142
		 */
		public static final Oui CISCO_000142
				= new Oui(0x000142, "Cisco");

		/**
		 * Cisco: 0x000143
		 */
		public static final Oui CISCO_000143
				= new Oui(0x000143, "Cisco");

		/**
		 * AlaxalA: 0x0012E2
		 */
		public static final Oui ALAXALA_0012E2
				= new Oui(0x0012E2, "AlaxalA");

		/**
		 * Hitachi: 0x001F67
		 */
		public static final Oui Hitachi_001F67
				= new Oui(0x001F67, "Hitachi");

		/**
		 * Hitachi Cable: 0x004066
		 */
		public static final Oui HITACHI_CABLE_004066
				= new Oui(0x004066, "Hitachi Cable");

		/**
		 * Microsoft Corporation: 0x485073
		 */
		public static final Oui MICROSOFT_CORPORATION
				= new Oui(0x485073, "Microsoft Corporation");

		private static final Map<Integer, Oui> registry
				= new HashMap<Integer, Oui>();

		/**
		 *
		 * @param value value
		 * @param name name
		 */
		public Oui(Integer value, String name) {
			super(value, name);
			if ((value & 0xFF000000) != 0) {
				throw new IllegalArgumentException(
						value + " is invalid value. "
								+"value must be between 0 and 0x00FFFFFF"
				);
			}
		}

		/**
		 *
		 * @param macAddress value
		 * @return a Oui object.
		 */
		public static Oui valueOf(final MacAddress macAddress) {
			int offset = 0;
			byte[] array = new byte[] { 0, macAddress.address[0], macAddress.address[1], macAddress.address[2] };
			int value = ((array[offset]) << (24))
					| ((0xFF & array[offset + 1]) << (16))
					| ((0xFF & array[offset + 2]) << (8))
					| ((0xFF & array[offset + 3]));
			Oui oui = registry.get(value);
			if (oui == null) {
				return new Oui(1, "UNKNOWN");
			}
			return oui;
		}

		/**
		 *
		 * @param version version
		 * @return a Oui object.
		 */
		public static Oui register(Oui version) {
			return registry.put(version.getValue(), version);
		}

		static {
			registry.put(CISCO_00000C.getValue(), CISCO_00000C);
			registry.put(FUJITSU_00000E.getValue(), FUJITSU_00000E);
			registry.put(HEWLETT_PACKARD_080009.getValue(), HEWLETT_PACKARD_080009);
			registry.put(FUJI_XEROX_080037.getValue(), FUJI_XEROX_080037);
			registry.put(IBM_08005A.getValue(), IBM_08005A);
			registry.put(CISCO_000142.getValue(), CISCO_000142);
			registry.put(CISCO_000143.getValue(), CISCO_000143);
			registry.put(ALAXALA_0012E2.getValue(), ALAXALA_0012E2);
			registry.put(Hitachi_001F67.getValue(), Hitachi_001F67);
			registry.put(HITACHI_CABLE_004066.getValue(), HITACHI_CABLE_004066);
			registry.put(MICROSOFT_CORPORATION.getValue(), MICROSOFT_CORPORATION);
		}

	}

}
