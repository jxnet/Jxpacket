package jxpacket.common;

/**
 * Factory interface.
 * @param <T> target type.
 * @param <V> source type.
 */
public interface Factory<T, V> {

	/**
	 * Build object.
	 * @param value param.
	 * @return object.
	 */
	T newInstance(V value);

}
