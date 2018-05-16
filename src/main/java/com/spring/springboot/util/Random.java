package com.spring.springboot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

//import org.apache.log4j.Logger;


public class Random {
	//private static final Logger log = Logger.getLogger(Random.class);
	
	//args[0]���ݴ��·��   args[1]�߳��� args  [2]ÿ���̶߳�������
	//d:/md5/20180506.txt 100 1000
	public static void main(String[] args) {
		int threadCount = Integer.parseInt(args[1]);
		for(int i=0;i<threadCount;i++){
//		for(int i=0;i<4;i++){
			final String file  = args[0];
			final int count = Integer.parseInt(args[2]);
			new Thread(){
				public void run(){
					t(file,count);
				}
			}.start();
		}
	}
	
	public static void t(String file,int count){
        FileWriter fw = null;
        //String file = "/home/data/pushcenter/test";
        //String file = "D:";
        File fileName = new File(file);// Ҫд����ļ�·��
		File file1 = new File(fileName.getParent());//�����ļ���
		File file2 = new File(fileName.getPath());	//�ļ�ȫ·��
		try {
			if (file1.mkdirs()) {//�ļ��в����ھʹ���
				 
		    }

			if (file2.createNewFile()) {//�ļ������ھʹ���
				
			}
			//fw = new FileWriter("D:\\md5\\phone.txt", true);
			fw = new FileWriter(file, true);
			for (int i = 0; i < count; i++) {
//				for (int i = 0; i < 10; i++) {
				long random = ThreadLocalRandom.current().nextLong(10000000000L,99999999999L);
				String rdm = String.valueOf(random);
				fw.write(rdm);
                fw.write("\r\n");
            }
            fw.close();
			
		} catch (Exception e) {
			//log.error("�ļ�������", e);
		}finally{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
	}


}

