package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.udacity.Constants.ANIMATION_DURATION
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var textHeight = 0F
    private var textWidth = 0F
    private var textOffset = 0F

    private var bounds = RectF().apply {
        left = 0F
        top = 0F
        right = 0F
        bottom = 0F
    }

    private var buttonLabel : String = resources.getString(R.string.button_name)
    private var buttonProgress = 0F
        set(value) {
            field = value
            invalidate()
        }
    private var arcProgress = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var valueAnimator = ValueAnimator()

    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.colorPrimary)
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private val buttonAnimationPaint = Paint().apply {
        color = resources.getColor(R.color.colorPrimaryDark)
        style = Paint.Style.FILL
    }

    private val arcPaint = Paint().apply { color = resources.getColor(R.color.colorAccent) }

    private var arcDiameter = 0F
    private var arcMargin = 0F

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 24F * resources.displayMetrics.scaledDensity
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                buttonLabel = resources.getString(R.string.button_loading)
                valueAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
                    duration = ANIMATION_DURATION
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    addUpdateListener {
                        buttonProgress = animatedValue as Float * measuredWidth.toFloat()
                        arcProgress = animatedValue as Float * 360F
                    }
                    start()
                }
            }

            ButtonState.Completed -> {
                buttonLabel = resources.getString(R.string.button_name)
                valueAnimator.end()
            }
        }
    }


    init {
        isClickable = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0F, 0F, widthSize.toFloat(), heightSize.toFloat(), buttonPaint)

        if(buttonState == ButtonState.Loading){
            canvas?.drawRect(0F, 0F, buttonProgress, heightSize.toFloat(), buttonAnimationPaint)

            canvas?.drawArc(
                    widthSize - arcDiameter - 100F,
                    arcMargin,
                    widthSize - 100F,
                    arcMargin + arcDiameter,
                    0F,
                    arcProgress,
                    true,
                    arcPaint
            )
        }

        canvas?.drawText(buttonLabel, bounds.centerX(), bounds.centerY() + textOffset, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h

        textHeight = (textPaint.descent().toDouble() - textPaint.ascent()).toFloat()
        textWidth = textPaint.measureText(buttonLabel)
        textOffset = (textHeight / 2) - textPaint.descent()

        arcDiameter = heightSize * 0.6F
        arcMargin = heightSize * 0.2F

        bounds.right = widthSize.toFloat()
        bounds.bottom = heightSize.toFloat()

        setMeasuredDimension(w, h)
    }

    fun setState(state: ButtonState){
        this.buttonState = state
    }

}