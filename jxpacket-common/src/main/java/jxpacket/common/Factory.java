package jxpacket.common;

public interface Factory<T, V> {

	T newInstance();

	T newInstance(V value);

}
