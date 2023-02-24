package com.ya.function.net.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

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