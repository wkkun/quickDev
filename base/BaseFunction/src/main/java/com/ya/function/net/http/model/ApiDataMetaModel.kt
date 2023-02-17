package com.ya.function.net.http.model

import android.util.ArrayMap
import androidx.annotation.Keep
import java.io.Serializable

/**
 *author: Bai
 *Date:2019/12/27
 *Description
 */
@Keep
class ApiDataMetaModel<T>(var data: T? = null,
                          var meta: Meta = Meta()
) {

    @Keep
    class Meta(
        var code: Int = -1,
        var header: ArrayMap<Any, Any> = ArrayMap(),
        var message: String = "",
        var info: String = ""
    ) : Serializable
}