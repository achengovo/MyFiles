package com.achengovo.myfiles.utils;

import java.security.MessageDigest;

public class Md5Util {
    public static void main(String[] args) {
        System.out.println(remd5("admin"));
    }

    // 盐，用于混淆md5
    public static String remd5(String dataStr) {
        String slat = "hf@asi(hfewjokoekw%#$032-135935(34'slfkwqeefsoadjfsa`";
        try {
            dataStr = dataStr + slat;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
