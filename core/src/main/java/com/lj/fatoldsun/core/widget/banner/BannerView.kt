package com.lj.fatoldsun.core.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.lj.fatoldsun.core.R
import com.youth.banner.Banner
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.Indicator
import com.youth.banner.listener.OnBannerListener

/**
 * @author LJ
 * @time 2025/03/31 15:43
 * @description:
 * 封装 YouthBanner 的自定义 View
 * 显示图片轮播 支持点击事件
 */

/**
 * Banner 数据接口，定义banner的基础属性，可扩展
 */
interface IBannerItem {
    val imageUrl: String //图片地址
    val title: String // 标题
    val actionUrl: String //点击跳转网页地址
}

/**
 *
 * @param context 上下文
 * @param attrs 属性集，通常从 XML 布局传入
 * @param defStyleAttr 默认样式属性
 * @constructor 使用 @JvmOverloads 注解生成多个构造函数，提升 Java 调用的兼容性
 */

class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //使用接口类型，适配器独立为外部类
    private val banner: Banner<IBannerItem, BannerImageAdapter> = Banner(context, attrs, defStyleAttr)

    init {
        // 从布局资源加载 view_banner.xml 并将其添加到当前 FrameLayout
        // 作用：定义 Banner 的默认布局结构
        // 好处：通过 XML 布局可调整高度等样式，增强灵活性
        LayoutInflater.from(context).inflate(R.layout.view_banner, this, true)
        // 将 banner 添加到 FrameLayout 中，作为子视图显示
        // 作用：确保 Banner 在布局中可见
        addView(banner)

        banner.apply {
            setAdapter(BannerImageAdapter(emptyList()))
            // 参数 null 表示使用默认生命周期（通常由父容器提供）
            addBannerLifecycleObserver(null)

            indicator = CircleIndicator(context)

            setIndicatorGravity(IndicatorConfig.Direction.CENTER)

            setBannerRound(16f)

            setOnBannerListener(object : OnBannerListener<IBannerItem> {
                override fun OnBannerClick(data: IBannerItem?, position: Int) {
                    data?.let {onItemClick?.invoke(it)}
                }

            })
        }
    }
    // 定义点击回调变量，允许外部设置监听器
    // 作用：存储外部传入的点击处理逻辑
    private var onItemClick: ((IBannerItem) -> Unit)? = null

    /**
     * 设置 Banner 数据
     * @param items 数据列表，类型为 IBannerItem 的实现类列表
     * 作用：动态更新 Banner 显示的内容
     */
    fun setItems(items: List<IBannerItem>) {
        banner.setDatas(items)
    }
    /**
     * 设置点击监听器
     * @param listener 点击回调，接收 IBannerItem 参数
     * 作用：允许外部定义点击 Banner 时的行为
     */
    fun setOnItemClickListener(listener: (IBannerItem) -> Unit) {
        this.onItemClick = listener
    }

    /**
     * 设置指示器样式
     * @param indicator 指示器实例
     * 作用：允许自定义指示器
     */
    fun setIndicator(indicator: Indicator) {
        // 更新指示器
        // 作用：支持样式调整
        banner.indicator = indicator
    }

    /**
     * 设置圆角大小
     * @param round 圆角值（像素）
     * 作用：允许自定义圆角
     */
    fun setBannerRound(round: Float) {
        // 更新圆角
        // 作用：支持样式调整
        banner.setBannerRound(round)
    }
}