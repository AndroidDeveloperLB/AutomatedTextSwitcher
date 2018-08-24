package com.lb.automated_text_switcher.library

import android.widget.TextView
import java.util.*

class AutomatedTextSwitcher : android.widget.TextSwitcher {
    private val handler2 = android.os.Handler()
    var currentlyShownText: CharSequence? = null
        private set
    private var counter = 0
    private val texts = ArrayList<CharSequence>()
    var timeBetweenAutoSwitchingInMs = 1000L
        set(value) {
            if (value <= 0L)
                throw IllegalArgumentException("illegal value for delay between text switches :$value")
            field = value
        }
    var textViewResId: Int = 0
        set(value) {
            field = value
            setFactory {
                if (value == 0)
                    android.widget.TextView(context)
                else
                    android.view.LayoutInflater.from(context).inflate(value, this@AutomatedTextSwitcher, false)
            }
        }

    var isAnimating = false
        private set(value) {
            if (value == field)
                return
            field = value
            handler2.removeCallbacks(showNextRunnable)
            if (value)
                handler2.postDelayed(showNextRunnable, timeBetweenAutoSwitchingInMs)
        }

    private val showNextRunnable: Runnable = object : Runnable {
        override fun run() {
            showNext(true)
            handler2.postDelayed(this, timeBetweenAutoSwitchingInMs)
        }
    }

    constructor(context: android.content.Context) : this(context, null)
    constructor(context: android.content.Context, attrs: android.util.AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutomatedTextSwitcher, 0, 0)
        if (typedArray.hasValue(R.styleable.AutomatedTextSwitcher_timeBetweenAutoSwitchingInMs))
            timeBetweenAutoSwitchingInMs = typedArray.getInteger(R.styleable.AutomatedTextSwitcher_timeBetweenAutoSwitchingInMs, timeBetweenAutoSwitchingInMs.toInt()).toLong()
        if (typedArray.hasValue(R.styleable.AutomatedTextSwitcher_textLayoutResId))
            textViewResId = typedArray.getResourceId(R.styleable.AutomatedTextSwitcher_textLayoutResId, 0)
        else if (isInEditMode) {
            setFactory { TextView(context) }
            if (isInEditMode)
                super.setCurrentText(AutomatedTextSwitcher::class.java.simpleName)
        }
        typedArray.recycle()
    }


    fun setTextsToShow(animateFirst: Boolean, vararg charSequences: CharSequence) {
        counter = 0
        texts.clear()
        isAnimating = false
        if (charSequences.size == 1) {
            setTextToShow(charSequences[0], animateFirst)
            return
        }
        texts.addAll(charSequences)
        showNext(animateFirst)
        isAnimating = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isAnimating = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (texts.isNotEmpty() && !isInEditMode)
            isAnimating = true
    }

    private fun showNext(animate: Boolean = true) {
        val textsCount = texts.size
        if (textsCount == 0) {
            setTextToShow(null, animate)
            return
        }
        if (textsCount == 1) {
            setTextToShow(texts[0], animate)
            return
        }
        counter = (counter + 1) % textsCount
        setTextToShow(texts[counter], animate)
    }

    private fun setTextToShow(text: CharSequence?, animate: Boolean) {
        val needToAnimate = animate && text != currentlyShownText && !(text.isNullOrBlank() && currentlyShownText.isNullOrBlank())
        currentlyShownText = text
        if (needToAnimate)
            super.setText(text)
        else super.setCurrentText(text)
    }

    override fun setText(text: CharSequence?) {
        isAnimating = false
        texts.clear()
        if (text != currentlyShownText)
            super.setText(text)
        else super.setCurrentText(text)
        currentlyShownText = text
        texts.clear()
        counter = 0
        if (text != null)
            texts.add(text)
    }

    override fun setCurrentText(text: CharSequence?) {
        isAnimating = false
        texts.clear()
        if (text != currentlyShownText)
            super.setCurrentText(text)
        currentlyShownText = text
        texts.clear()
        counter = 0
        if (text != null)
            texts.add(text)
    }

}
