package com.zwh.test.kotlin_test

class Student public constructor(val name: String) {

    constructor(name: String, sex: Int) : this(name) {

    }

    constructor(name: String, sex: Int, age: Int) : this(name, sex) {

    }
}