package com.zwh.test.title_bar

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.log

fun lock(p1: String, p2: String, method: String): String {
    return method
}

@ExperimentalContracts
fun main(args: Array<String>) {

    //    val test = KotlinTest()
//    val viewModel= ViewModel by viewmodes
//    TitleBar2Activity::javaClass
//    fun b(param: Int): String {
//        return param.toString()
//    }
//        login("name", "word", "")


//    fun lock(p1: String, p2: String, method: String): String {
//        return method
//    }


//    println(::lock.invoke("param1", "param2"))
//    println(test.lock("param1", "param2", test::f.get().invoke("1", "2")))
//    val titleBar2Activity = TitleBar2Activity()
//    val intent = Intent(titleBar2Activity.applicationContext, KotlinTest::javaClass.invoke(test))
//    val intent = Intent(null, KotlinTest::abs)

//    val a = ""
//    val b = ""
//
//    var name:String? = null
//    if (!name.isNullOrEmptyWithoutContract()) {
//        //name.length报错，自定义扩展函数中的判空逻辑未同步到编译器 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type String?
////        Lo.d(TAG, )
//        println("name:$name,length:${name.length}")
//        print("a")
//    }
//    print("b")

//print(a in 1..1000)
//    val a = 4
//    val b = 4
//    when {
//        a in 0..10 -> println("1..10")
//        b is Int -> println("int")
//    }

    val str: String? = null
    val length: Int = str?.length ?: 3
    println(length)



}

@ExperimentalContracts
fun String?.isNullOrEmptyWithoutContract(): Boolean {

    contract {
        returns(false) implies (this@isNullOrEmptyWithoutContract != null)
    }
    return this == null || this.isEmpty()


}


inline val <T : Any> T.abs: String
    get() = (this as java.lang.Object).toString()

@ExperimentalContracts
public inline fun <T> T.le(block: (T) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}

//class KotlinTest {
/**
 * @param str1 参数1
 * @param str2 参数2
 */
fun getResult(str1: String, str2: String): String = "result is {$str1 , $str2}"

val f = fun(str1: String, str2: String): String = "result is {$str1 , $str2}"

/**
 * @param p1 参数1
 * @param p2 参数2
 * @param method 方法名称
 */
fun lock(p1: String, p2: String, method: (str1: String, str2: String) -> String): String {
    return method(p1, p2)
}


fun lock() {
}

class Producer<out T> {
//    fun produce(): T {
//        return T;
//    }

}

fun login(user: String, password: String, illegalStr: String) {
    fun validate(value: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(illegalStr)
        }
    }
    validate(user)
    validate(password)
    val a = require(user.isNotEmpty()) {
        "ssss"
    }
    println(a)

}

val producer: Producer<TextView> = Producer<Button>() // 👈 这里不写 out 也不会报错
val producer1: Producer<out TextView> = Producer<Button>() //

//}