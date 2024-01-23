package com.zwh.utils.helper;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * String 掩码
 */
public class StringUtils {
    /**
     * 格式化掩码手机号
     *
     * @param maskStr        掩码字符串
     * @param mobileAreaCode 区号
     * @return 掩码后字符串 +263 71****16
     *
     */
    public static String formatMobile(String mobileAreaCode, String maskStr) {
        if (TextUtils.isEmpty(mobileAreaCode)) {
            //区号为空
            if (TextUtils.isEmpty(maskStr)) {
                return "";
            } else {
                //区号为空,但是手机号不为空
                if (maskStr.length() <= 6) {
                    return maskStr;
                }

                //号码前3位、后4位明文显示
                StringBuilder sb = new StringBuilder(maskStr);
                StringBuilder showPhone = sb.replace(2, maskStr.length() - 2, "*****");
                return showPhone.toString();
            }
        } else {
            //区号不为空
            if (TextUtils.isEmpty(maskStr)) {
                return "";
            }

            //区号不为空,手机号也不为空
            //替换掉相应的区号

            if (maskStr.length() <= 6) {
                return maskStr;
            }

            StringBuilder sb = new StringBuilder(maskStr);
            int i = maskStr.indexOf(mobileAreaCode);
            if (i == -1) {
                //号码前3位、后4位明文显示
                StringBuilder showPhone = sb.replace(2, sb.length() - 2, "*****");
                return "+" + mobileAreaCode + " " + showPhone;
            } else {
                //区号的位置 开始位置i 结束位置length
                int length = i + mobileAreaCode.length();

                if (maskStr.length() < length + 7) {
                    return maskStr;
                } else {
                    //替换相应的手机号
                    sb.replace(0, length, "+" + mobileAreaCode + " ");
                    StringBuilder showPhone = sb.replace(length + 4, sb.length() - 2, "*****");
                    return showPhone.toString();
                }
            }
        }
    }

    /**
     * 银行卡掩码
     *
     * @param maskStr 掩码字符串
     * @return 掩码后字符串
     */
    public static String formatBankCode(String maskStr) {
        if (TextUtils.isEmpty(maskStr)) {
            return "";
        }

        if (maskStr.length() < 4) {
            return maskStr;
        }

        StringBuilder sb = new StringBuilder(maskStr);
        StringBuilder showPhone = sb.replace(0, sb.length() - 4, "**** **** **** ");
        return showPhone.toString();
    }



    /**
     * 格式化金额
     */
    public static String formatAmount(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return "";
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(amount);
            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
            DecimalFormat df = new DecimalFormat("#,##0.00", decimalFormatSymbols);
            return df.format(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
        } catch (Exception e) {
            return "";
        }
    }

    public static String reverseAmount(String money) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        try {
            Number parse = df.parse(money);
            return parse.toString();
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 条形码的掩码
     *
     * @param qrCode 二维码
     * @return
     */
    public static String barCodeMask(String qrCode) {
        if (TextUtils.isEmpty(qrCode)) {
            return "";
        }
        if (qrCode.length() >= 8) {
            StringBuilder sb = new StringBuilder(qrCode);
            StringBuilder showPhone = sb.replace(4, sb.length() - 4, " **** **** ");
            return showPhone.toString();
        }
        return qrCode;
    }


    /**
     * 有时kotlin为避免空指针会将null转为“null”
     * 所有 “null” string对象当无效字符串处理
     */
    public static Boolean isValidString(String s) {
        return s != null && !s.trim().equals("") && !s.trim().equals("null");
    }

    public static Boolean isValidMobile(String s) {
        if (!isValidString(s)) return false;
        return s.length() == 9 && s.startsWith("71");
    }

}
