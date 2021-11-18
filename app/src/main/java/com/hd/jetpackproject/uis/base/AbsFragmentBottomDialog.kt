package com.hudun.androidrecorder.module.base.fragment


import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.databinding.ViewDataBinding
import com.hd.jetpackproject.uis.base.AbsFragmentDialog

/**
 * @Auther hjw
 * @Date 2021/3/12/16:48
 * @Description 自动进入动画的底部弹窗
 */
abstract class AbsFragmentBottomDialog<T : ViewDataBinding>(layoutId: Int) : AbsFragmentDialog<T>(layoutId) {
    override val gravity: Int
        get() = Gravity.BOTTOM

    override val scaleWidth: Float
        get() = 1f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        slideToUp(dataBinding.root)
    }

    abstract fun initView()

    private fun slideToUp(view: View) {
        val slide: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f
        )
        slide.duration = 400
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)
        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

}