package com.jvr.datovaschranka.lib.classes

import android.util.Log
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

class MyGestureListenerExtended(private var eventMethod: ((eventAction : EventAction
                                                           , event: MotionEvent?) -> Unit)?)
    : SimpleOnGestureListener() {
    enum class EventAction {
        onDown, onSingleTapConfirmed, onLongPress, onDoubleTap,onScroll,onFling
    }

    override fun onDown(event: MotionEvent?): Boolean {
        Log.d("TAG", "onDown: ")
        eventMethod?.invoke(EventAction.onDown, event)
        // don't return false here or else none of the other
        // gestures will work
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        //Log.i("TAG", "onSingleTapConfirmed: ")
        eventMethod?.invoke(EventAction.onSingleTapConfirmed, event)
        return true
    }

    override fun onLongPress(event: MotionEvent?) {
        //Log.i("TAG", "onLongPress: ")
        eventMethod?.invoke(EventAction.onLongPress, event)
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        //Log.i("TAG", "onDoubleTap: ")
        eventMethod?.invoke(EventAction.onDoubleTap, event)
        return true
    }

    override fun onScroll(
        e1: MotionEvent?, e2: MotionEvent?,
        distanceX: Float, distanceY: Float
    ): Boolean {
        //Log.i("TAG", "onScroll: ")
        //mainMethod.invoke(event)
        return true
    }

    override fun onFling(
        event1: MotionEvent?, event2: MotionEvent?,
        velocityX: Float, velocityY: Float
    ): Boolean {
        //Log.d("TAG", "onFling: ")
        return true
    }
}