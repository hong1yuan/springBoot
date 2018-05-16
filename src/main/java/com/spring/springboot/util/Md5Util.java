package com.spring.springboot.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class Md5Util {

	public static String getMd5ByFile(File file) throws Exception {
        String value = null;
        FileInputStream in = null;
        in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {}
            }
        }
        return value;
    }
	
	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		File file = new File(args[0]);
        String value = Md5Util.getMd5ByFile(file);
        System.out.println((System.currentTimeMillis() - time));
        System.out.println(value);
	}
}
