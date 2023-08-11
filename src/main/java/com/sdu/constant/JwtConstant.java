package com.sdu.constant;

public class JwtConstant {

    /**
     * token
     */
    public static final int JWT_ERRCODE_NULL = 4000;			//Token不存在
    public static final int JWT_ERRCODE_EXPIRE = 4001;			//Token过期
    public static final int JWT_ERRCODE_FAIL = 4002;			//验证不通过

    /**
     * JWT
     */
    public static final String JWT_SECERT = "7a1bb3adb24534e12aea5941466a351a";			//密匙
    public static final long JWT_TTL = 24 * 60 * 60 * 1000;									//token有效时间
}
