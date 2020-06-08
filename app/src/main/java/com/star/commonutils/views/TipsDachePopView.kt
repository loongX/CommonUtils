package com.star.commonutils.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.star.common_utils.utils.AppUtil
import com.star.common_utils.utils.UIUtil
import com.star.common_utils.utils.UIUtil.removeViewFromParent
import com.star.common_utils.widget.TipsBgView
import com.star.commonutils.R


/**
 *   created by xueshanshan on 2020/6/5
 */
class TipsDachePopView(val mContext: Context) {

    private val mRootView = LayoutInflater.from(mContext).inflate(R.layout.dache_tips_container, null)
    private val mTvTip1: TextView = mRootView.findViewById(R.id.tv_tip_1)
    private val mTvTip2: TextView = mRootView.findViewById(R.id.tv_tip_2)
    private val mImgTipClose: ImageView = mRootView.findViewById(R.id.img_tip_close)
    private val mTipsBgView: TipsBgView = mRootView.findViewById(R.id.tips_bg_view)
    private var mCloseClickCallBack: (() -> Unit)? = null
    private var mShowCount = 0;

    init {
        mImgTipClose.setOnClickListener {
            mCloseClickCallBack?.invoke()
            dismiss()
        }
    }

    fun setCloseClickCallBack(calback: (() -> Unit)?) {
        mCloseClickCallBack = calback
    }

    fun setContent(text1: String, text2: String) {
        mTvTip1.text = text1
        mTvTip2.text = text2
        mTvTip2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (if (text1.isEmpty()) AppUtil.dp2px(mContext, 15) else AppUtil.dp2px(mContext, 18)).toFloat())
    }

    fun show(activity: Activity?, view: View?) {
        if (mRootView.parent != null) {
            removeViewFromParent(mRootView)
            mShowCount = 0
        }
        if (activity == null) {
            return
        }
        mShowCount++;
        view?.post {
            view.run {
                if (mShowCount > 0) {
                    val location = IntArray(2)
                    getLocationOnScreen(location)
                    val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    layoutParams.topMargin = (location[1] + height - AppUtil.dp2px(mContext, 10)).toInt()
                    mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    if (location[0] + width / 2 <= UIUtil.getScreenAvailAbleSize(mContext).x / 2 + AppUtil.dp2px(mContext, 5)) {
                        layoutParams.leftMargin = 0
                    } else {
                        layoutParams.leftMargin = UIUtil.getScreenAvailAbleSize(mContext).x - AppUtil.dp2px(mContext, 4) - mRootView.measuredWidth
                    }
                    mTipsBgView.mTriangleLeft = (location[0] + width / 2 - layoutParams.leftMargin - AppUtil.dp2px(mContext, 8)).toFloat()
                    val parent = activity.window.decorView as FrameLayout
                    parent.addView(mRootView, layoutParams)
                    doShowAnimation()
                }
            }
        }
    }

    private fun doShowAnimation() {
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleX", 0f, 1f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleY", 0f, 1f)
        scaleYAnimationContentView.duration = 300
        mRootView.pivotX = mTipsBgView.mTriangleLeft + AppUtil.dp2px(mContext, 8)
        mRootView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()

        mTvTip1.visibility = View.INVISIBLE
        mTvTip2.translationY = 0f
        if (!TextUtils.isEmpty(mTvTip1.text.toString()) && !TextUtils.isEmpty(mTvTip2.text.toString())) {
            mTvTip2.visibility = View.INVISIBLE
            mTvTip1.postDelayed({
                mTvTip1.visibility = View.VISIBLE
                val alphaAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "alpha", 0f, 1f)
                alphaAnimationTip1.duration = 600
                val translateAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "translationY", AppUtil.dp2px(mContext, 30).toFloat(), 0f)
                translateAnimationTip1.duration = 600
                val scaleXAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleX", 1f, 1.2f)
                scaleXAnimationTip1.duration = 600
                val scaleYAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleY", 1f, 1.2f)
                scaleYAnimationTip1.duration = 600
                alphaAnimationTip1.start()
                translateAnimationTip1.start()
                scaleXAnimationTip1.start()
                scaleYAnimationTip1.start()
            }, 200)

            mTvTip2.postDelayed({
                mTvTip2.visibility = View.VISIBLE
                val alphaAnimationTip2 = ObjectAnimator.ofFloat(mTvTip2, "alpha", 0f, 1f)
                alphaAnimationTip2.duration = 400
                val translateAnimationTip2 = ObjectAnimator.ofFloat(mTvTip2, "translationY", AppUtil.dp2px(mContext, 30).toFloat(), AppUtil.dp2px(mContext, 10).toFloat())
                translateAnimationTip2.duration = 400
                alphaAnimationTip2.start()
                translateAnimationTip2.start()

                val translateAnimation1Tip1 = ObjectAnimator.ofFloat(mTvTip1, "translationY", 0f, -AppUtil.dp2px(mContext, 10).toFloat())
                val scaleXAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleX", 1.2f, 1f)
                val scaleYAnimationTip1 = ObjectAnimator.ofFloat(mTvTip1, "scaleY", 1.2f, 1f)
                translateAnimation1Tip1.duration = 400
                scaleXAnimationTip1.duration = 400
                scaleYAnimationTip1.duration = 400
                translateAnimation1Tip1.start()
                scaleXAnimationTip1.start()
                scaleYAnimationTip1.start()
            }, 1000)

            mImgTipClose.visibility = View.INVISIBLE
            mImgTipClose.postDelayed({
                mImgTipClose.visibility = View.VISIBLE
                val alphaAnimation = ObjectAnimator.ofFloat(mImgTipClose, "alpha", 0f, 1f)
                alphaAnimation.duration = 300
                alphaAnimation.start()
            }, 1200)
        }
    }

    fun isShown(): Boolean {
        return mShowCount > 0
    }

    fun dismiss() {
        if (!isShown()) {
            return
        }
        mShowCount--
        val scaleXAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleX", 1f, 0f)
        scaleXAnimationContentView.duration = 300
        val scaleYAnimationContentView = ObjectAnimator.ofFloat(mRootView, "scaleY", 1f, 0f)
        scaleYAnimationContentView.duration = 300
        mRootView.pivotX = mTipsBgView.mTriangleLeft + AppUtil.dp2px(mContext, 8)
        mRootView.pivotY = 0f
        scaleXAnimationContentView.start()
        scaleYAnimationContentView.start()
        scaleXAnimationContentView.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                removeViewFromParent(mRootView)
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }
}