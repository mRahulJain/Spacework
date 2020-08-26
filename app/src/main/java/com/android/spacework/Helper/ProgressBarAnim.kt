package com.android.spacework.Helper

import android.content.Context
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

public class ProgressBarAnim : Animation {

    var context : Context? = null
    var progressBar : ProgressBar? = null
    var from : Float = 0f
    var to : Float = 0f

    constructor(context: Context, progressBar: ProgressBar, from : Float, to : Float) {
        this.context = context
        this.progressBar = progressBar
        this.from = from
        this.to = to
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        val value = from + (to - from) * interpolatedTime
        progressBar!!.setProgress(value.toInt())
    }
}