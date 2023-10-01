package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    private lateinit var canvas: Canvas
    private var textToShow = R.string.button_name


    init {
        //No need for "isClickable = true" because, we have a clickListener that can do the same job
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        //Left = starting point (x)
        //top = starting point (y)
        val rectangle = Rect(0, 0, widthSize, heightSize)
        canvas.drawRect(rectangle, paint)
        paint.color = Color.WHITE
        showText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    //The performClick() method calls onClickListener()
    override fun performClick(): Boolean {
        //should start BG animation here, change text, animate circle
        textToShow = R.string.button_loading
        invalidate()
        return super.performClick()
    }

    private fun showText(canvas: Canvas){
        //Calculate text offset to center text vertically: https://knowledge.udacity.com/questions/438647
        val textOffset = (paint.descent() - paint.ascent()) / 2 - paint.descent()
        canvas.drawText(
            context.getString(textToShow),
            widthSize.toFloat() / 2,
            (heightSize.toFloat() / 2) + textOffset,
            paint
        )
    }

}