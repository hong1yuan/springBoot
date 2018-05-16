package com.spring.springboot.util;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author Administrator
 *
 */
public class AES {

	private static final String encoding = "UTF-8";
    /*
     * ����
     */
    public static String encrypt(String sKey,String sSrc) throws Exception {
        if (sKey == null) {
            System.out.print("KeyΪ��null");
            return null;
        }
        // �ж�Key�Ƿ�Ϊ16λ
        if (sKey.length() != 16) {
            System.out.print("Key���Ȳ���16λ");
            return null;
        }
        byte[] raw = sKey.getBytes(encoding);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"�㷨/ģʽ/���뷽ʽ"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encoding));

        return new Base64().encodeToString(encrypted);//�˴�ʹ��BASE64��ת�빦�ܣ�ͬʱ����2�μ��ܵ����á�
    }

    /*
     * ����
     */
    public static String decrypt(String sKey,String sSrc) throws Exception {
        try {
            // �ж�Key�Ƿ���ȷ
            if (sKey == null) {
                System.out.print("KeyΪ��null");
                return null;
            }
            // �ж�Key�Ƿ�Ϊ16λ
            if (sKey.length() != 16) {
                System.out.print("Key���Ȳ���16λ");
                return null;
            }
            byte[] raw = sKey.getBytes(encoding);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//����base64����
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,encoding);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
         String str = "Za6fvRqf4vSyHJk4KV7yNHs0FZm2JWBMuzAPnii5bMsiavrgzHO2bTXfgssvsLv6ncnBnBngkEy3SZxVy4ROTHrRuTsKxsdO0GQQWF2LHrbcumwNbgkA/b0lU1xzy1mo0vVDTKsULcBiZ2nZ5RCO6H7nAKACj7ljNXe6o5qk2YnPwDOwGhzVSzfcSc4dG7jUzUCov7jS7XFQYRTHC95k4izDEznWM5AoVtKQ5jCupI63d1xab+HH6HWt5ox1UXXKmcgYubng6m9V2M03oYSQiG6wkZhMBwIa0C0fTQe7b7UGltvHALk00ZPAFSbBb194jsMv5URDGwgGJBzbjagWQiaDYd9bG/KVHQfL9cD6aVBCS9tKnq42yKgDBNiSEYWFyKy8ipOBfXvby3/x1t5rkQrPKHYAmnxuGwAqaRtmqRbvee7IgztHQMLqxEngkfoVmKGwXU32/H4nHXhPiNyoGHJ7GTUmUo7cRNlB2tHjfanvee7IgztHQMLqxEngkfoVmKGwXU32/H4nHXhPiNyoGB9AWoGcFBlU8YLEdydvo9d2BSffoLZgw8moH0Y+s/8Tc90mwuXKPZkRzDWIiO7zMCJq+uDMc7ZtNd+Cyy+wu/qk5Lu3JGuhvFgQpePH41Qyd5nmWe9FhAz3RKvXDgpvCHY2GAeUgAIfcLfwrdpZkhigxbTTZJPVvWam7eZZcTznnNOm5sfs5N8jeqzCR5Gy3BMv5MVFWW7hPFEkgZxmMhCoxREdu2Es3nRGBxXkfgJNWrIM9QkqsysaGAxCZhSNipiOz+JzqPulL1jhQt4gN0b1LHVgVJQDq5igVeu1C2O6QR8071VodWhFNbH/T+yJxASiVkI59/1MqaGo8jjTNo4Hw6OF0w2bRe7O/saWTE7MOTp+NkPGs1a+gNrilDODDjNhaoyGSkhQV8L64RqRsnYv14zjOA1a5kKMdwG2YzYmVtvuXG9rMPvuGzWCoXgVNBVzTFrKn+DtQH7Q7fu4WnJiLDglGFYT+WAPGaSc5utKZ6kesRujbDs3fuaEjfPC/7vPPjC8D9I+9JONkbN0xuU9IGOTq/0Chp6WxqPwsk7wgvhZqNFq/ZtKs6uZDkMEmg==";
         String key = "40ce9e18ee354320";
//         String miyao = AES.encrypt(key, str);
         String miyao = AES.decrypt(key, str);
         System.out.println(miyao);
         System.out.println(AES.decrypt(key, miyao));
         
    }
    
}
