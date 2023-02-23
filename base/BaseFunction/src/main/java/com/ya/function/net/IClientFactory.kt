package com.ya.function.net

/**
 *author: Bai
 *Date:2019/11/5
 *  T : client 对象
 *  P ：创建参数
 */
interface IClientFactory<T: IClient,P> {

    fun get(param: P): T

}