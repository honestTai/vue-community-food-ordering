package com.takeOut.food.util.encryption;

import org.springframework.util.DigestUtils;

public class MD5Util {

    //盐，用于混交md5
    private static final String slat = "houseRentApi";

    /**
     * 生成md5
     *
     * @param string 需要签名的字符串
     * @return
     */
    public static String getMD5(String string) {
        String base = string + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

}
