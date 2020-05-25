package com.example.android.dwms

import java.text.DecimalFormat

/*  숫자변환 또는 기타 함수 모음
*   */

fun makeCommaNumber(input:Int): String{
    val formatter = DecimalFormat("###,###")
    return formatter.format(input)
}