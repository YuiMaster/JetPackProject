package com.hd.jetpackproject.uis.base


import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app._DialogFragment

/**
 * @Auther hjw
 * @Date 2021/3/12/16:44
 * @Description FragmentDialog基类
 */
abstract class AbsFragmentDialog<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : _DialogFragment() {
    protected lateinit var dataBinding: T
    protected var mActivity: Activity? = null
    private var dismissListener: DismissListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.setCanceledOnTouchOutside(canceledOnTouchOutside)
            it.setCancelable(canceled)
            it.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP) {
                    !onBackPressed()
                } else false
            }
            val win = it.window
            win?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            // 一定要设置Background，如果不设置，window属性设置无效
            if (win != null) {
                win.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = win.attributes
                params.gravity = gravity
                params.dimAmount = dimAmount
                // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
                params.width = windowWidth
                params.height = windowHeight
                win.attributes = params
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.let {
            if (dialogAnim != -1 && it.window != null) {
                val attributes = it.window?.attributes
                if (attributes != null) {
                    attributes.windowAnimations = dialogAnim
                }
            }
        }
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, container, false)
        return dataBinding.root
    }

    /**
     * dialog的动画
     *
     * @return
     */
    open val dialogAnim: Int
        get() = -1

    open val scaleWidth: Float
        get() = 0.8f

    open val dimAmount: Float
        get() = 0.7f

    /**
     * dialog的位置
     *
     * @return Gravity
     */
    open val gravity: Int
        get() = Gravity.CENTER

    /**
     * @return 窗口的宽度
     */
    open val windowWidth: Int
        get() {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            return (scaleWidth * dm.widthPixels).toInt()
        }

    /**
     * @return 窗口的高度
     */
    open val windowHeight: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    open val canceledOnTouchOutside: Boolean
        get() = true

    open val canceled: Boolean
        get() = true

    open fun onBackPressed() = true


    interface DismissListener {
        fun onDismiss()
    }

    fun setDismissListener(dismissListener: DismissListener?) {
        this.dismissListener = dismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (dismissListener != null) {
            dismissListener!!.onDismiss()
        }
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, this::class.java.toString())
    }
}