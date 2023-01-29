package com.adrpien.tiemed.core.dialogs.signature_dialog

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.adrpien.tiemed.R
import kotlin.math.abs


class NumberView(context: Context): View(context) {
    constructor(context: Context, attributeSet: AttributeSet): this(context) {
    }

    // Stroke width
    private val STROKE_WIDTH = 6f

    // The Path class encapsulates compound geometric paths consisting of straight line segments, quadratic curves, and cubic curves.
    // Cached Path
    private var currentPath = Path()

    // Contain cached Bitmap and Canvas
    private lateinit var extraBitmap: Bitmap
    private  lateinit var extraCanvas: Canvas

    // Colours definitions
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.signature_view_background_color, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.signature_view_color, null)

    // Frame
    private lateinit var frame: Rect

    // Values to store motion coordinates
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f

    // Implementation of Paint object
    //The Paint class holds the style and color information about how to draw geometries, text and bitmaps.
    val paint = Paint().apply {
        color = drawColor
        // isAntiAlias defines whether to apply edge smoothing.
        // Setting isAntiAlias to true, smoothes out the edges of what is drawn without affecting the shape.
        isAntiAlias = true
        // isDither, when true, affects how colors with higher-precision than the device are down-sampled.
        // For example, dithering is the most common means of reducing the color range of images down to the 256 (or fewer) colors.
        isDither = true
        style = Paint.Style.STROKE
        // strokeJoin of Paint.Join specifies how lines and curve segments join on a stroked path.
        strokeJoin = Paint.Join.ROUND // default: MITER
        //strokeCap sets the shape of the end of the line to be a cap
        strokeCap = Paint.Cap.ROUND // default: BUTT
        // strokeWidth specifies the width of the stroke in pixels
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }


    // Touch tolerance infulence on interpolation a path between point to increase phone perfomance
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop


    // The onSizeChanged() method is called by the Android system whenever a view changes size.
    // Because the view starts out with no size, the view's onSizeChanged() method is also called after the Activity first creates and inflates it.
    // This onSizeChanged() method is therefore the ideal place to create and set up the view's canvas.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Prevents bitmap leakage
        if (::extraBitmap.isInitialized) extraBitmap.recycle()

        // Setting extraBitmap
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        // Create extraCanvas using extraBitmap
        extraCanvas = Canvas(extraBitmap)

        // Setting drawColor
        extraCanvas.drawColor(backgroundColor)

        // Calculate a rectangular frame around the picture.
        val inset = 10
        frame = Rect(inset, inset, w - inset, h - inset)

    }

    // onDraw implementation
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Draw extraBitmap, which contains what was drawn earlier
        if (canvas != null) {
            // Drawing cached Bitmap on canvas
            canvas.drawBitmap(extraBitmap, 0f, 0f, null)
            // Draw a frame around the canvas.
            extraCanvas.drawRect(frame, paint)
        }
    }

    // onTouchEvent implementation
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            motionTouchEventY = event.y
            motionTouchEventX = event.x
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDown()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove()
            }

        }
        return true
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
          // Creating curve between points when finger is dragged more tan touchTolerance
          currentPath.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
          currentX = motionTouchEventX
          currentY = motionTouchEventY
          // Draw the path in the extra bitmap to cache it.
          extraCanvas.drawPath(currentPath, paint)

        }
        // Force to redraw
        invalidate()
    }

    private fun touchUp() {
        // Reset current path
        currentPath.reset()
    }

    private fun touchDown() {
        currentPath.reset()
        currentPath.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }
}