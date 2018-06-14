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

package jxpacket.common;

import java.io.Serializable;

/**
 * Common base class for dynamic named number (enum like).
 * @see Enum
 * @see Number
 * Example:
 * <pre>
 * public final class HttpStatusCode extends NamedNumber<Integer, HttpStatusCode> {
 *
 *      public static final HttpStatusCode NOT_FOUND =
 *          new HttpStatusCode(404, "Not found.");
 *
 *      public static final HttpStatusCode OK =
 *          new HttpStatusCode(20, "Ok.");
 *
 *      public static final HttpStatusCode UNKNOWN =
 *          new HttpStatusCode(0, "Unknown Http Status Code.");
 *
 *      public HttpStatusCode(Integer value, String name) {
 *          super(value, name);
 *      }
 *
 *      private static final Map<Integer, HttpStatusCode> registry
 *          = new HashMap<>();
 *
 *      public static final HttpStatusCode register(final HttpStatusCode httpStatusCode) {
 *          registry.put(httpStatusCode.getValue(), httpStatusCode);
 *          return httpStatusCode;
 *      }
 *
 *      public static final HttpStatusCode valueOf(final int rawValue) {
 *          HttpStatusCode httpStatusCode = registry.get(rawValue);
 *          if (httpStatusCode == null) {
 *              return UNKNOWN;
 *          }
 *          return httpStatusCode;
 *      }
 *
 *      static {
 *          registry.put(NOT_FOUND.getValue(), NOT_FOUND);
 *          registry.put(OK.getValue(), OK);
 *          registry.put(UNKNOWN.getValue(), UNKNOWN);
 *      }
 *
 *  }
 *  </pre>
 * @author Ardika Rommy Sanjaya
 * @param <T> number.
 * @param <U> named number.
 */
public abstract class NamedNumber<T extends Number, U extends NamedNumber<T, ?>> implements Serializable {

    private static final long serialVersionUID = -7754849362562086047L;

    private final T value;
    private final String name;

    protected NamedNumber(T value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Returns the number of this {@code NamedNumber} object.
     * @return returns the number of this {@code NamedNumber} object.
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Returns the name of this {@code NamedNumber} object.
     * @return returns the name of this {@code NamedNumber} object.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        if (!(obj instanceof NamedNumber)) {
            return false;
        }
        return this.value.equals(this.getClass().cast(obj).getValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("[Value: ")
                .append(this.value)
                .append(", Name: ")
                .append(this.name)
                .append("]").toString();
    }

}
