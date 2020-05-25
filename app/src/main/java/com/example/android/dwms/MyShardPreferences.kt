package com.example.android.dwms

import android.content.Context
import android.content.SharedPreferences

class MyShardPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val PREF_KEY_LOGID = "g_loginid"    //// 로그인 id
    val PREF_KEY_WAREHOUSE = "g_warehouse"  ///// 창고정보
    val PREF_KEY_OWNER = "g_owner"   ///// owner 정보
    val PREF_KEY_RFID = "g_rfid"     //// rfid 정보

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    /* 파일 이름과 EdtiText를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */
    // key값을 이용한 저장
    fun setV(key:String, value:String? ){
        prefs.edit().putString(key,value).apply()
    }
    // key값을 이용한 로드
    fun getV(key:String):String?{
        return prefs.getString(key,"")
    }
    var g_loginid:String?
        get() = prefs.getString(PREF_KEY_LOGID,"")
        set(value) = prefs.edit().putString(PREF_KEY_LOGID, value).apply()
    /* get/set 함수 임의 설정, get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장    */

    var g_warehouse:String?
        get() = prefs.getString(PREF_KEY_WAREHOUSE,"")
        set(value) = prefs.edit().putString(PREF_KEY_WAREHOUSE, value).apply()
    /* get/set 함수 임의 설정, get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장    */

    var g_owner:String?
        get() = prefs.getString(PREF_KEY_OWNER,"")
        set(value) = prefs.edit().putString(PREF_KEY_OWNER, value).apply()
    /* get/set 함수 임의 설정, get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장    */

    var g_rfid:String?
        get() = prefs.getString(PREF_KEY_RFID,"")
        set(value) = prefs.edit().putString(PREF_KEY_RFID, value).apply()
    /* get/set 함수 임의 설정, get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장    */
}
