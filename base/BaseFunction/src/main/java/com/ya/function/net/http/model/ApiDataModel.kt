package com.ya.function.net.http.model

import androidx.annotation.Keep


/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
@Keep
class ApiDataModel<T>(var code: Int = 0,
                      var message: String? = "",
                      var info: String? = "",
                      var data: T? = null)