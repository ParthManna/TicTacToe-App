package com.example.tictactoe2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class LineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.black) // Default line color
        strokeWidth = 8f // Line thickness
        style = Paint.Style.STROKE
        isAntiAlias = true // Smooth edges for better quality
    }

    private var startX: Float = 0f
    private var startY: Float = 0f
    private var endX: Float = 0f
    private var endY: Float = 0f
    private var isLineVisible: Boolean = false // Visibility flag for the win line

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isLineVisible) {
            canvas.drawLine(startX, startY, endX, endY, paint)
        }
    }

    /**
     * Draw a line for indicating the winner.
     * @param startX Starting X-coordinate of the line
     * @param startY Starting Y-coordinate of the line
     * @param endX Ending X-coordinate of the line
     * @param endY Ending Y-coordinate of the line
     */
    fun drawWinLine(startX: Float, startY: Float, endX: Float, endY: Float) {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY
        isLineVisible = true
        invalidate() // Redraw the view to show the updated line
    }

    /**
     * Clears the win line from the view.
     */
    fun clearLine() {
        isLineVisible = false
        invalidate() // Redraw the view to clear the line
    }
}
