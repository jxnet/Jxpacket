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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Ardika Rommy Sanjaya
 * @since 1.1.5
 */
public class ArrayUtils {

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static byte[] reverse(byte[] value) {
        Validate.nullPointer(value);
        byte[] array = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static char[] reverse(char[] value) {
        Validate.nullPointer(value);
        char[] array = new char[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static short[] reverse(short[] value) {
        Validate.nullPointer(value);
        short[] array = new short[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static int[] reverse(int[] value) {
        Validate.nullPointer(value);
        int[] array = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static float[] reverse(float[] value) {
        Validate.nullPointer(value);
        float[] array = new float[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static long[] reverse(long[] value) {
        Validate.nullPointer(value);
        long[] array = new long[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @return array in reverse order.
     */
    public static double[] reverse(double[] value) {
        Validate.nullPointer(value);
        double[] array = new double[value.length];
        for (int i = 0; i < value.length; i++) {
            array[i] = value[value.length - i - 1];
        }
        return array;
    }

    /**
     * Reverse order.
     * @param value value.
     * @param <T> type of value.
     * @return array in reverse order.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] reverse(T[] value) {
        Validate.nullPointer(value);
        List<T> collections = Arrays.asList(value);
        Collections.reverse(collections);
        return (T[]) collections.toArray();
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static byte[] concatenate(byte[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (byte[] arr: arrays) {
            totalLen += arr.length;
        }
        byte[] all = new byte[totalLen];
        int copied = 0;
        for (byte[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static char[] concatenate(char[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (char[] arr: arrays) {
            totalLen += arr.length;
        }
        char[] all = new char[totalLen];
        int copied = 0;
        for (char[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static short[] concatenate(short[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (short[] arr: arrays) {
            totalLen += arr.length;
        }
        short[] all = new short[totalLen];
        int copied = 0;
        for (short[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static int[] concatenate(int[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (int[] arr: arrays) {
            totalLen += arr.length;
        }
        int[] all = new int[totalLen];
        int copied = 0;
        for (int[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static float[] concatenate(float[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (float[] arr: arrays) {
            totalLen += arr.length;
        }
        float[] all = new float[totalLen];
        int copied = 0;
        for (float[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static long[] concatenate(long[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (long[] arr: arrays) {
            totalLen += arr.length;
        }
        long[] all = new long[totalLen];
        int copied = 0;
        for (long[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @return array.
     */
    public static double[] concatenate(double[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (double[] arr: arrays) {
            totalLen += arr.length;
        }
        double[] all = new double[totalLen];
        int copied = 0;
        for (double[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

    /**
     * Concatenate array.
     * @param arrays arrays.
     * @param <T> type of array.
     * @return array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] concatenate(T[]... arrays) {
        Validate.nullPointer(arrays);
        int totalLen = 0;
        for (T[] arr: arrays) {
            totalLen += arr.length;
        }
        T[] all = (T[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), totalLen);
        int copied = 0;
        for (T[] arr: arrays) {
            System.arraycopy(arr, 0, all, copied, arr.length);
            copied += arr.length;
        }
        return all;
    }

}
