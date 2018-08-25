package jxpacket.common;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.0
 */
public class TwoKeyMap<T, U> {

    private final T firstKey;
    private final U secondKey;

    private TwoKeyMap(T firstKey, U secondKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
    }

    public static <T, U> TwoKeyMap<T, U> newInstance(T firstKey, U secondKey) {
        return new TwoKeyMap<T, U>(firstKey, secondKey);
    }

    public T getFirstKey() {
        return this.firstKey;
    }

    public U getSecondKey() {
        return this.secondKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        if (!(obj instanceof TwoKeyMap)) {
            return false;
        }
        return (this.firstKey.equals(this.getClass().cast(obj).getFirstKey())
                && this.secondKey.equals(this.getClass().cast(obj).getSecondKey()));
    }

    @Override
    public int hashCode() {
        return 17 * 37 + this.getFirstKey().hashCode() + this.getSecondKey().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[First Key: ").append(this.getFirstKey().toString())
                .append(", Second Key: ").append(this.getSecondKey().toString())
                .append("]").toString();
    }

}
