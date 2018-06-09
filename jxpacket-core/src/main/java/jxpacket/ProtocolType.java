/**
 * Copyright (C) 2017  Ardika Rommy Sanjaya
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

package jxpacket;

import jxpacket.common.NamedNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public final class ProtocolType extends NamedNumber<Short, ProtocolType> {

	/**
	 * MTU Size (1500)
	 */
	public static final int IEEE802_3_MAX_LENGTH = 1500;

	/**
	 * IPv4: 0x0800
	 */
	public static final ProtocolType IPV4
			= new ProtocolType((short) 0x0800, "IPv4");

	/**
	 * ARP: 0x0806
	 */
	public static final ProtocolType ARP
			= new ProtocolType((short) 0x0806, "ARP");

	/**
	 * IEEE 802.1Q VLAN-tagged frames: 0x8100
	 */
	public static final ProtocolType DOT1Q_VLAN_TAGGED_FRAMES
			= new ProtocolType((short) 0x8100, "IEEE 802.1Q VLAN-tagged frames");

	/**
	 * QinQ: 0x88a8
	 */
	public static final ProtocolType IEEE_802_1_AD
			= new ProtocolType((short) 0x88a8, "QinQ");
	/**
	 * RARP: 0x8035
	 */
	public static final ProtocolType RARP
			= new ProtocolType((short) 0x8035, "RARP");

	/**
	 * Appletalk: 0x809b
	 */
	public static final ProtocolType APPLETALK
			= new ProtocolType((short) 0x809b, "Appletalk");

	/**
	 * IPv6: 0x86dd
	 */
	public static final ProtocolType IPV6
			= new ProtocolType((short) 0x86dd, "IPv6");

	/**
	 * PPP: 0x880b
	 */
	public static final ProtocolType PPP
			= new ProtocolType((short) 0x880b, "PPP");

	/**
	 * MPLS: 0x8847
	 */
	public static final ProtocolType MPLS
			= new ProtocolType((short) 0x8847, "MPLS");

	/**
	 * PPPoED Discovery Stage: 0x8863
	 */
	public static final ProtocolType PPPOE_DISCOVERY_STAGE
			= new ProtocolType((short) 0x8863, "PPPoED Discovery Stage");

	/**
	 * PPPoED Session Stage: 0x8864
	 */
	public static final ProtocolType PPPOE_SESSION_STAGE
			= new ProtocolType((short) 0x8864, "PPPoED Session Stage");

	public static final ProtocolType UNKNOWN
			= new ProtocolType((short) -1, "Unknown");

	private static final Map<Short, ProtocolType> registry
			= new HashMap<Short, ProtocolType>();

	/**
	 * @param value value
	 * @param name  name
	 */
	public ProtocolType(Short value, String name) {
		super(value, name);
	}

	/**
	 * @param value value
	 * @return a ProtocolType object.
	 */
	public static ProtocolType valueOf(final Short value) {
		if ((value & 0xFFFF) <= IEEE802_3_MAX_LENGTH) {
			return UNKNOWN;
		}
		ProtocolType protocolType = registry.get(value);
		if (protocolType == null) {
			return UNKNOWN;
		}
		return protocolType;
	}

	/**
	 * @param type type
	 * @return a ProtocolType object.
	 */
	public static ProtocolType register(ProtocolType type) {
		return registry.put(type.getValue(), type);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	static {
		registry.put(IPV4.getValue(), IPV4);
		registry.put(ARP.getValue(), ARP);
		registry.put(DOT1Q_VLAN_TAGGED_FRAMES.getValue(), DOT1Q_VLAN_TAGGED_FRAMES);
		registry.put(RARP.getValue(), RARP);
		registry.put(APPLETALK.getValue(), APPLETALK);
		registry.put(IPV6.getValue(), IPV6);
		registry.put(PPP.getValue(), PPP);
		registry.put(MPLS.getValue(), MPLS);
		registry.put(PPPOE_DISCOVERY_STAGE.getValue(), PPPOE_DISCOVERY_STAGE);
		registry.put(PPPOE_SESSION_STAGE.getValue(), PPPOE_SESSION_STAGE);
		registry.put(IEEE_802_1_AD.getValue(), IEEE_802_1_AD);
	}

}