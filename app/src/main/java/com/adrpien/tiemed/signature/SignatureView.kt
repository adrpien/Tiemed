package com.adrpien.tiemed.signature

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.adrpien.tiemed.R

class SignatureView(context: Context): View(context) {

    val backgroundColor = ResourcesCompat.getColor(R.color.white)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchDown()
            MotionEvent.ACTION_UP -> touchUp()
            MotionEvent.ACTION_MOVE -> touchMove()

        }
        return true
    }

    private fun touchMove() {
        TODO("Not yet implemented")
    }

    private fun touchUp() {
        TODO("Not yet implemented")
    }

    private fun touchDown() {
        TODO("Not yet implemented")
    }
}