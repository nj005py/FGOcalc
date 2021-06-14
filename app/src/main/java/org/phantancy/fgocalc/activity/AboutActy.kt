package org.phantancy.fgocalc.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.adapter.AboutAdapter
import org.phantancy.fgocalc.databinding.ActyAboutBinding
import org.phantancy.fgocalc.entity.AboutEntity
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration
import org.phantancy.fgocalc.util.BaseUtils
import org.phantancy.fgocalc.util.ToolCase

class AboutActy : BaseActy() {
    private lateinit var binding: ActyAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle("关于")
        val adapter = AboutAdapter()
        binding.rvCast.adapter = adapter;
        binding.rvCast.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx,5.0f)))
        adapter.submitList(getCast())
    }

    fun getCast(): List<AboutEntity> {
        return listOf(
                AboutEntity(R.mipmap.ic_launcher, "FGOcalc v2\n${packageName(ctx)}"),
                AboutEntity(R.drawable.image1005, "纯蓝魔法使(蓝魔)\napp开发、数据录入"),
                AboutEntity(R.drawable.bingbing, "单调的冰(冰块)\n图标+启动图绘制"),
                AboutEntity(R.drawable.image1004, "strawberryG(黄昏现白骨)\nv1数据库指导"),
                AboutEntity(R.drawable.image1001, "遗忘的银灵\n解包+图片素材"),
                AboutEntity(R.drawable.jianren, "湖中神剑\n数据抓取"),
                AboutEntity(R.drawable.leo, "Leo\n数据抓取"),
                AboutEntity(R.drawable.loading, "数据来源：\nfgowiki\nmooncell"),
                AboutEntity(R.drawable.loading, "v1数据协助录入：\nLevsLeos(妹红)\n" +
                        "高川\n" +
                        "shenzhixiyue(夕月)\n" +
                        "major56y(56)\n" +
                        "白我风闻(风子)\n" +
                        "黑璃v镜(镜子)\n" +
                        "saiya(夜魇悲歌)\n" +
                        "红尘眷恋N(红秃子)\n" +
                        "晓澄若笙凉(夏兮)")
        )
    }

    fun packageName(context: Context): String {
        val manager = context.getPackageManager();
        var name = ""
        try {
            val info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        return name;
    }

}