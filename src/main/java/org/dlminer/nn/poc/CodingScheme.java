package org.dlminer.nn.poc;

/**
 * Created by slava on 20/09/17.
 */
public enum CodingScheme {

    ONEVSALL;


    public static double[] integerToCode(double number, int digits,
                                         CodingScheme codingScheme) {
        if (codingScheme.equals(ONEVSALL)) {
            return integerToOneVsAll(number, digits);
        }
        throw new IllegalArgumentException("Unknown coding scheme!");
    }

    private static double[] integerToOneVsAll(double number, int digits) {
        int intNumber = (int) number;
        if (intNumber >= digits) {
            throw new IllegalArgumentException("digits must exceed number!");
        }
        double[] bits = new double[digits];
        bits[intNumber] = 1;
        return bits;
    }


    /*private static double[] integerToBinary(double number, int digits) {
        int intNumber = (int) number;
        char[] bitChars = Integer.toBinaryString(intNumber).toCharArray();
        double[] bits;
        if (digits <= 0) {
            bits = new double[bitChars.length];
        } else {
            bits = new double[digits];
        }
        int shift = bits.length - bitChars.length;
        for (int i=0; i<bitChars.length; i++) {
            char ch = bitChars[i];
            if (ch == '0') {
                bits[shift + i] = 0;
            } else {
                bits[shift + i] = 1;
            }
        }
        return bits;
    }*/


    public static double codeToInteger(double[] bits, CodingScheme codingScheme) {
        if (codingScheme.equals(ONEVSALL)) {
            return oneVsAllToInteger(bits);
        }
        throw new IllegalArgumentException("Unknown coding scheme!");
    }

    private static double oneVsAllToInteger(double[] bits) {
        for (int i=0; i<bits.length; i++) {
            if (bits[i] == 1) {
                return i;
            }
        }
        throw new IllegalArgumentException("bits are not in the one-vs-all format!");
    }


    /*private static double binaryToInteger(double[] bits) {
        if (bits.length == 1) {
            return bits[0];
        }
        String bitChars = "";
        for (int i=0; i<bits.length; i++) {
            bitChars += (int) bits[i];
        }
        return Integer.parseInt(bitChars, 2);
    }*/

}
