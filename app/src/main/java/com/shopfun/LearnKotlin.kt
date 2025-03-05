package com.shopfun

fun main()
{
    val imapof = mapOf("Cat1" to 1, "Cat2" to 2, "Cat1" to 3)
    imapof.forEach(){
        println("${it.key} key value is ${it.value}")
    }
    println(imapof)
    println(imapof.values)
    println(imapof.keys)

    var imutablelist = mutableListOf(0)
    imutablelist.removeAt(0)
    imutablelist.add(1)
    imutablelist.add(2)
    imutablelist.add(0,3)
//    imutablelist.reverse()
    println(imutablelist)

    //mutableScatterMapOf()
    // mutableScatterSetOf()
    // mutableVectorOf()

    //Lambda
    val addnum: (Int, Int) -> Int = {a, b ->
        val total = a + b
        println("Total of $a and $b is $total")
        total
    }

//    val addnum: (Int, Int) -> Int = faddnum()
    println("Total is " + addnum(1, 2))

    //Trailing Lambda

}

/*
private fun faddnum(): (Int, Int) -> Int {
    val addnum: (Int, Int) -> Int = { a, b ->
        val total = a + b
        println("Total of $a and $b is $total")
        total
    }
    return addnum
}
*/
