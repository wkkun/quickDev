package com.ya.function.net.http.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 *
 * fix Empty Body
 *
 * @see https://github.com/square/retrofit/issues/1554
 * @author  Edwin.Wu edwin.wu05@gmail.com
 * @version  2019/3/15 10:39 AM
 * @since  JDK1.8
 */
class NullOnEmptyConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): Converter.Factory {
            return NullOnEmptyConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any>? {
        val delegate = retrofit
            .nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter { body ->
            if (body.contentLength() == 0L) {
                null
            } else delegate.convert(body)
        }
    }
}