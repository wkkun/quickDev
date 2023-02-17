package com.ya.function.net.http.exception

import java.io.IOException

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
class ApiException(private var errorCode:Int = 0,
                   private var errorMsg: String? = ""): IOException(errorMsg){


    fun getErrorCode(): Int{
        return errorCode
    }

    fun getErrorMsg(): String?{
        return errorMsg
    }
}