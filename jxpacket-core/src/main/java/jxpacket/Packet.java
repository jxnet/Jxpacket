package jxpacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import jxpacket.common.Builder;
import jxpacket.common.NamedNumber;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Packet extends Iterable<Packet>, Serializable {

	Packet.Header getHeader();

	Packet getPayload();

	<T extends Packet> List<T> get(Class<T> clazz);

	<T extends Packet> List<T> get(Class<T> clazz, Predicate<Packet> predicate);

	<T extends Packet> Stream<T> getAsStream(Class<T> clazz);

	<T extends Packet> Stream<T> getAsStream(Class<T> clazz, Predicate<Packet> predicate);

	<T extends Packet> T getFirst(Class<T> clazz);

	<T extends Packet> T getFirst(Class<T> clazz, Predicate<Packet> predicate);

	<T extends Packet> boolean contains(Class<T> clazz);

	interface Header extends Serializable {

		<T extends NamedNumber> T getPayloadType();

		int getLength();

		ByteBuf getBuffer();

	}

	interface Builder extends jxpacket.common.Builder<Packet, ByteBuf> {

	}

	class PacketIterator implements Iterator<Packet> {

		private Packet next;

		private Packet previous;

		public PacketIterator(final Packet packet) {
			this.next = packet;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Packet next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			previous = next;
			next = next.getPayload();
			return previous;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}