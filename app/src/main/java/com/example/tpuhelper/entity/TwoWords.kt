package com.example.tpuhelper.entity

class TwoWords() {
    var iconNumber:Int = 0
    var name: String = ""
    var description: String = ""

    constructor(name: String, description: String) : this() {
        this.name = name;
        this.description = description;
    }

    constructor(name: String, description: String, iconNumber:Int) : this() {
        this.name = name;
        this.description = description;
        this.iconNumber = iconNumber;

    }
}