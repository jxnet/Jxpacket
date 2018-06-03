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

package jxpacket.ip.ipv6;

import jxpacket.Packet;
import jxpacket.ip.IPProtocolType;

import java.nio.ByteBuffer;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class Fragment extends IPv6ExtensionHeader {

    public static final int FIXED_FRAGMENT_HEADER_LENGTH = 8;

    private IPProtocolType nextHeader;
    private short fragmentOffset;
    private byte moreFragment;
    private int identification;

    public IPProtocolType getNextHeader() {
        return this.nextHeader;
    }

    public Fragment setNextHeader(final IPProtocolType nextHeader) {
        this.nextHeader = nextHeader;
        return this;
    }

    public short getFragmentOffset() {
        return this.fragmentOffset;
    }

    public Fragment setFragmentOffset(final short fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
        return this;
    }

    public byte getMoreFragment() {
        return this.moreFragment;
    }

    public Fragment setMoreFragment(final byte moreFragment) {
        this.moreFragment = moreFragment;
        return this;
    }

    public int getIdentification() {
        return this.identification;
    }

    public Fragment setIdentification(final int identification) {
        this.identification = identification;
        return this;
    }

    public ByteBuffer getPayload() {
        return this.nextPacket;
    }

    public Fragment setPayload(final ByteBuffer payload) {
        this.nextPacket = payload;
        return this;
    }

    public Fragment setPayload(final byte[] payload) {
        this.nextPacket = ByteBuffer.wrap(payload);
        return this;
    }

    public static Fragment newInstance(final ByteBuffer buffer) {
        Fragment fragment = new Fragment();
        fragment.setNextHeader(IPProtocolType.getInstance(buffer.get()));
        buffer.get(); // reserved
        short sscratch = buffer.getShort();
        fragment.setFragmentOffset((short) (sscratch >> 3 & 0x1fff));
        fragment.setMoreFragment((byte) (sscratch & 0x1));
        fragment.setIdentification(buffer.getInt());
        fragment.nextPacket = buffer.slice();
        return fragment;
    }

    public static Fragment newInstance(final byte[] bytes) {
        return newInstance(bytes, 0, bytes.length);
    }

    public static Fragment newInstance(final byte[] bytes, final int offset, final int length) {
        return newInstance(ByteBuffer.wrap(bytes, offset, length));
    }

    @Override
    public Packet setPacket(Packet packet) {
        return null;
    }

    @Override
    public Packet getPacket() {
        if (this.nextPacket == null || this.nextPacket.capacity() == 0) return null;
        System.out.println(this.nextPacket.capacity());
        return this.nextHeader.decode(this.getPayload());
    }

    @Override
    public byte[] bytes() {
        byte[] data = new byte[FIXED_FRAGMENT_HEADER_LENGTH +
                (this.getPayload() == null ? 0 : this.getPayload().capacity())];
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.put(this.getNextHeader().getValue());
        buffer.put((byte) 0);
        buffer.putShort((short) (
                (this.getFragmentOffset() & 0x1fff) << 3 |
                        this.getMoreFragment() & 0x1
        ));
        buffer.putInt(this.getIdentification());
        if (this.getPayload() != null) {
            buffer.put(this.getPayload());
        }
        return data;
    }

    @Override
    public ByteBuffer buffer() {
        ByteBuffer buffer = ByteBuffer
                .allocateDirect(FIXED_FRAGMENT_HEADER_LENGTH +
                        (this.getPayload() == null ? 0 : this.getPayload().capacity()));
        buffer.put(this.getNextHeader().getValue());
        buffer.put((byte) 0);
        buffer.putShort((short) (
                (this.getFragmentOffset() & 0x1fff) << 3 |
                        this.getMoreFragment() & 0x1
        ));
        buffer.putInt(this.getIdentification());
        if (this.getPayload() != null) {
            buffer.put(this.getPayload());
        }
        return buffer;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append("Next Header: ")
                .append(this.getNextHeader())
                .append(", Fragment Offset: ")
                .append(this.getFragmentOffset())
                .append((this.getMoreFragment() == 0x01) ? ", More Fragment: " : ", Last Fragment: ")
                .append(this.getMoreFragment())
                .append(", Identification: ")
                .append(this.getIdentification())
                .append("]").toString();
    }

}
