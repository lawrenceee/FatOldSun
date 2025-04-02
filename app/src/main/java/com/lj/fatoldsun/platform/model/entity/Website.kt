package com.lj.fatoldsun.platform.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * {
 *     "data": [
 *         {
 *             "category": "源码",
 *             "icon": "",
 *             "id": 22,
 *             "link": "https://www.androidos.net.cn/sourcecode",
 *             "name": "androidos",
 *             "order": 11,
 *             "visible": 1
 *         },
 *         ...
 *     ],
 *     "errorCode": 0,
 *     "errorMsg": ""
 * }
 * ttps://www.wanandroid.com/friend/json 的 data 字段
 */
@Entity(tableName = "website")
data class Website(
    val category: String,
    val icon: String,
    @PrimaryKey val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int

)
