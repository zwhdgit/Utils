package com.zwh.utils.helper

import android.app.Activity
import android.text.TextUtils
import android.widget.EditText
import com.zwh.utils.listener.SoftKeyBoardListener
import java.math.BigDecimal

class MoneyUtil {
    companion object {
        // 两行也要懒
        @JvmStatic
        fun getTwoDecimal(money: String): BigDecimal {
            val decimal = BigDecimal(money)
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP)

        }

//        @JvmStatic
//        fun main(args: Array<String>) {
//            val money = "12345678.99"
//            val formatAmount = StringUtils.formatAmount(money)
//            val df = DecimalFormat("#,##0.00")
//            isValidMoney(df.toString())
//            val parse = df.parse(formatAmount)
//            println(parse)
//        }

        @JvmStatic
        fun isValidMoney(money: String): Boolean {
            val reverseAmount = StringUtils.reverseAmount(money)
            return !TextUtils.isEmpty(reverseAmount) && BigDecimal(reverseAmount) > BigDecimal.ZERO
        }

        @JvmStatic
        fun isValidMoney(money: String?, min: Int): Boolean {
            val reverseAmount = StringUtils.reverseAmount(money)
            return !TextUtils.isEmpty(reverseAmount) && BigDecimal(reverseAmount).compareTo(
                BigDecimal(min)) > -1
        }

        fun getStandardAmount(amount: String?, newScale: Int, roundingMode: Int): String {
            val bigDecimal = BigDecimal(amount)
            return bigDecimal.setScale(newScale, roundingMode).toString()
        }

        fun setInputEndListener(activity: Activity, ed: EditText) {
            //键盘的监听
            SoftKeyBoardListener.setListener(activity,
                object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
                    override fun keyBoardShow(height: Int) {
                    }

                    override fun keyBoardHide(height: Int) {
                        //键盘隐藏的时候格式金额
                        val str = ed.text.toString()
                        if (StringUtils.isValidString(str)) {
                            val amountStr = getStandardAmount(str,
                                2,
                                BigDecimal.ROUND_HALF_UP)
                            ed.setText(amountStr)
                            ed.setSelection(amountStr.length)
                        }
                    }
                })
        }
    }

}