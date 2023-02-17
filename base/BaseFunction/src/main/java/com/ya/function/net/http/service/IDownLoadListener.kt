package com.ya.function.net.http.service

import java.io.File

/**
 *author: Bai
 *Date:2019/12/19
 *Description
 */
interface IDownLoadListener {

    fun onFail(e: Throwable)

    fun onSuccess(file: File)

    fun onProgress(progress: Int)
}