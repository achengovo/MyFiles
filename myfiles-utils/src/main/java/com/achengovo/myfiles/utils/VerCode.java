package com.achengovo.myfiles.utils;

import java.util.Random;

public class VerCode {
    /**
     * 生成验证码
     * @param length
     * @return
     */
    public static String getRandomVerCode(int length){
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
