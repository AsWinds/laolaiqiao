package com.codi.laolaiqiao.common.util;

import java.nio.ByteBuffer;


public class LongUtils {
	
	/**
	 * combine longs byte order use default order
	 * 
	 * */
	public static final byte[] combine(long ...longs){
		int size = longs.length * (Long.SIZE / Byte.SIZE);
		ByteBuffer buffer = ByteBuffer.allocate(size);
		for (int i = 0; i < longs.length; i++) {
	        buffer.putLong(longs[i]);
        }
		return buffer.array();
	}
}
