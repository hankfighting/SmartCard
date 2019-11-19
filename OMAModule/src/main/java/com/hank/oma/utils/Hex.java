package com.hank.oma.utils;

/**
 * @author hank
 */
public final class Hex {
	private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	/**
	 * convert byte[] to hexString
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}


	public static final String long2HexString(long n, int byteNum) {
		byteNum <<= 1;
		char[] buf = new char[byteNum];

		for(int i = byteNum - 1; i >= 0; --i) {
			buf[i] = HEX_DIGITS[(int)(n & 15L)];
			n >>>= 4;
		}

		return new String(buf);
	}


	public static final byte[] padding(byte[] data, byte firstPadByte, byte nextPadByte, boolean padding) {
		int padLen = data.length % 8;
		byte[] temp = (byte[])null;
		int i;
		if(padLen == 0) {
			if(!padding) {
				return data;
			} else {
				temp = new byte[data.length + 8];
				System.arraycopy(data, 0, temp, 0, data.length);
				temp[data.length] = firstPadByte;

				for(i = 1; i < 8; ++i) {
					temp[data.length + i] = nextPadByte;
				}

				return temp;
			}
		} else {
			padLen = 8 - padLen;
			temp = new byte[data.length + padLen];
			System.arraycopy(data, 0, temp, 0, data.length);
			temp[data.length] = firstPadByte;

			for(i = 1; i < padLen; ++i) {
				temp[data.length + i] = nextPadByte;
			}

			return temp;
		}
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
