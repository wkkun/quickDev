package com.ya.function.net.http.service

import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

/**
 *author: Bai
 *Date:2019/12/19
 *Description
 */
class DownloadService(private val okHttpClient: OkHttpClient,private val request: Request) {

    fun newCall(): Call{
        return okHttpClient.newCall(request)
    }

    fun newCall(cacheFile: File,downloadListener: IDownLoadListener){

        newCall().enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                downloadListener.onFail(e)
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val body = response.body()

                    body?.let {

                        cacheFile.deleteOnExit()
                        cacheFile.createNewFile()

                        val inputStream = it.byteStream()

                        val outputStream = FileOutputStream(cacheFile)

                        val buf = ByteArray(2048)

                        var len: Int

                        var sum: Long = 0

                        val total = it.contentLength()

                        while (inputStream.read(buf).apply { len = this } != -1) {
                            outputStream.write(buf, 0, len)
                            sum += len
                            val progress = ((sum * 1.0f / total * 1.0f) * 100).toInt()
                            downloadListener.onProgress(progress)
                        }

                        outputStream.flush()

                        inputStream.close()

                        outputStream.close()

                        downloadListener.onSuccess(cacheFile)

                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    downloadListener.onFail(e)
                }
            }

        })
    }
}