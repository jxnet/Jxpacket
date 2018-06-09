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

package jxpacket.common.util;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class Validate {

    /**
     * Check null pointer.
     * @param reference reference.
     * @param exception NullPointerException.
     * @param <T> type of reference.
     */
    public static <T> void nullPointer(T reference, NullPointerException exception) {
        if (reference == null) {
            if (exception == null) {
                throw new NullPointerException();
            } else {
                throw exception;
            }
        }
    }

    /**
     * Check null pointer.
     * @param reference reference.
     * @param <T> type of reference.
     */
    public static <T> void nullPointer(T reference) {
        nullPointer(reference, null);
    }

    /**
     * Check null pointer.
     * @param reference reference.
     * @param newVal new value.
     * @param <T> type of reference and new value.
     * @return reference if not null, newVal otherwise.
     */
    public static <T> T nullPointer(T reference, T newVal) {
        if (reference == null) {
            return newVal;
        } else {
            return reference;
        }
    }

    /**
     * Check illegal argument.
     * @param expression expression.
     * @param exception IllegalArgumentException.
     */
    public static void notIllegalArgument(boolean expression, IllegalArgumentException exception) {
        if (!expression) {
            if (exception == null) {
                throw new IllegalArgumentException();
            } else {
                throw exception;
            }
        }
    }

    /**
     * Check illegal argument.
     * @param expression expression.
     */
    public static void notIllegalArgument(boolean expression) {
        notIllegalArgument(expression, null);
    }

    /**
     * Check illegal argument.
     * @param expression expression.
     * @param reference reference.
     * @param newVal new value.
     * @param <T> type of reference and new value.
     * @return reference if not null, newVal otherwise.
     */
    public static <T> T notIllegalArgument(boolean expression, T reference, T newVal) {
        if (!expression) {
            return newVal;
        } else {
            return reference;
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(byte[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(char[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(short[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(int[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(float[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(long[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     */
    public static void notInBounds(double[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Validate notInBounds.
     * @param array array.
     * @param offset offset.
     * @param length length.
     * @param <T> type of array.
     */
    public static <T> void notInBounds(T[] array, int offset, int length) {
        nullPointer(array, new NullPointerException("array is null."));
        notIllegalArgument(array.length > 0, new IllegalArgumentException("array is empty."));
        notIllegalArgument(length > 0, new IllegalArgumentException("length is zero."));
        if (offset < 0 || length < 0 || length > array.length || offset > array.length - 1 || offset + length > array.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

}
