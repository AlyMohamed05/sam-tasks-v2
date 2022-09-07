package com.udacity.project4.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.animateIntoScreen() {
    val viewHeight = height.toFloat()
    val yTransition = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, viewHeight, 0f)
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
    ObjectAnimator.ofPropertyValuesHolder(
        this,
        yTransition,
        scaleX,
        scaleY
    ).apply {
        duration = 375L
        addListener(object : AnimatorListenerAdapter() {
            // Must show the view when animation starts
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
        start()
    }
}

fun FloatingActionButton.animateOutScreen() {
    val viewHeight = height.toFloat()
    val yTransition = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, viewHeight)
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
    ObjectAnimator.ofPropertyValuesHolder(
        this,
        yTransition,
        scaleX,
        scaleY
    ).apply {
        duration = 375L
        addListener(object : AnimatorListenerAdapter() {
            // Must show the view when animation starts
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
        start()
    }
}