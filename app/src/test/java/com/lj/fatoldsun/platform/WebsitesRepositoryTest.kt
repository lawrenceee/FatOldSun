package com.lj.fatoldsun.platform

import com.lj.fatoldsun.core.network.Response
import com.lj.fatoldsun.platform.db.WebsitesDao
import com.lj.fatoldsun.platform.model.entity.Website
import com.lj.fatoldsun.platform.network.ApiService
import com.lj.fatoldsun.platform.repository.WebsitesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


/**
 * @author LJ
 * @time 2025/04/18 23:44
 * @description:WebsitesRepository测试类
 * 逐行注释与实现说明：
 *
 * Mockito：用 @Mock 模拟 ApiService 和 WebsitesDao，避免依赖真实网络或数据库。
 * runBlocking：测试协程函数时，使用 runBlocking 模拟同步执行。
 * Assertions：用 assertEquals 验证返回数据的正确性。
 * 复杂知识点详解：
 *
 * Mockito 的工作原理：
 * Mockito 通过动态代理生成模拟对象，拦截方法调用并返回预设值（如 thenReturn）。
 * 例如，when(apiService.getWebsites()).thenReturn(response) 让模拟的 apiService 返回指定数据。
 * 协程测试：
 * runBlocking 是测试协程的简单方式，但更推荐使用 kotlinx-coroutines-test 库，提供 TestScope 和 runTest 来精确控制协程调度。
 */
class WebsitesRepositoryTest {
    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var websitesDao: WebsitesDao

    private lateinit var repository: WebsitesRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WebsitesRepository(websitesDao)
    }

    @Test
    fun `fetchWebsitesFromNetwork returns success response`() = runBlocking {
        // 模拟网络返回
        val websites = listOf(Website("Tech", "", 1, "https://tech.com", "TechSite", 0, 1))
        val response = Response(data = websites, errorCode = 0, errorMsg = "")
        `when`(apiService.getWebsites()).thenReturn(response)

        // 执行测试
        val result = repository.fetchWebsitesFromNetwork()
        assertEquals(0, result.errorCode)
        assertEquals(1, result.data.size)
        assertEquals("TechSite", result.data[0].name)
    }

    @Test
    fun `getCacheWebsitesFromLocal returns cached websites`() = runBlocking {
        // 模拟数据库返回
        val websites = listOf(Website("Tech", "", 1, "https://tech.com", "TechSite", 0, 1))
        `when`(websitesDao.getAllWebsites()).thenReturn(websites)

        // 执行测试
        val result = repository.getCacheWebsitesFromLocal()
        assertEquals(1, result.size)
        assertEquals("TechSite", result[0].name)
    }
}