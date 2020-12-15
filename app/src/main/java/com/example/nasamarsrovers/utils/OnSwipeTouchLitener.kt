package com.example.nasamarsrovers.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

        })

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    fun onSwipeTop() {}

    fun onSwipeBottom() {}

    fun onSwipeLeft() {}

    fun onSwipeRight() {}

    internal class GestureListener : GestureDetector.SimpleOnGestureListener() {

        companion object {
            private const val SWIPE_THRESHOLD = 100
            private const val SWIPE_SPEED_THRESHOLD = 10
        }

        override fun onDown(e: MotionEvent?) = true

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = event2.y - event1.y
                val diffX = event2.x - event1.x
                if (abs(diffX) > SWIPE_THRESHOLD && abs(diffX) > SWIPE_SPEED_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    result = true
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(diffY) > SWIPE_SPEED_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}