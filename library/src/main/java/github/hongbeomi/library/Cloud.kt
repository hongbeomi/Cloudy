package github.hongbeomi.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.annotation.ColorInt


class Cloud(private val context: Context) {

    companion object {
        private const val DEFAULT_RADIUS = 25f
        private const val DEFAULT_COLOR = Color.TRANSPARENT
    }

    private val blurNative = StackBlurNative()
    @ColorInt
    private var color = DEFAULT_COLOR
    private var radius = DEFAULT_RADIUS
        set(value) {
            field = when {
                value < 0f -> 0f
                else -> value
            }
        }
    private var scrollY: Int = 0
    private var scrollX: Int = 0

    private var view: View? = null
    private var targetView: View? = null

    private var originalBitmap: Bitmap? = null
    private var blurredBitmap: Bitmap? = null
    private var isCleared = false

    private fun stackBlur() {
        val pullBitmap = pull() ?: return
        originalBitmap = blurNative.blur(pullBitmap, radius.toInt())
    }

    private fun pull(): Bitmap? {
        val view = this.view ?: return null
        if (view.measuredWidth == 0 || view.measuredHeight == 0) {
            return null
        }

        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            flags = Paint.FILTER_BITMAP_FLAG
            colorFilter = PorterDuffColorFilter(
                this@Cloud.color,
                PorterDuff.Mode.MULTIPLY
            )
        }

        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return bitmap
    }

    private fun cropAndBlur() {
        val targetView = targetView ?: return
        val bitmap = originalBitmap ?: return
        val calculatedScrollX =
            if (scrollX < 0 || ((scrollX + targetView.left + targetView.width) > bitmap.width)) {
                0
            } else {
                scrollX
            }

        val calculatedScrollY =
            if (scrollY < 0 || ((scrollY + targetView.top + targetView.height) > bitmap.height)) {
                0
            } else {
                scrollY
            }

        blurredBitmap = Bitmap.createBitmap(
            bitmap,
            targetView.left + calculatedScrollX,
            targetView.top + calculatedScrollY,
            targetView.width,
            targetView.height
        )
        setBackground(targetView)
    }

    private fun setBackground(targetView: View) {
        val bitmap = blurredBitmap ?: return
        targetView.background = BitmapDrawable(context.resources, bitmap)
    }

    internal fun from(view: View) = apply {
        this.view = view
    }

    internal fun into(targetView: View) = apply {
        this.targetView = targetView
    }

    fun radius(radius: Float) = apply {
        println(radius.toString())
        this.radius = radius
        if (!isCleared) {
            stackBlur()
            cropAndBlur()
        }
    }

    fun color(@ColorInt color: Int) = apply {
        this.color = color
    }

    fun onVerticalScroll(scrollY: Int) = apply {
        this.scrollY = scrollY
        if (!isCleared) cropAndBlur()
    }

    fun onHorizontalScroll(scrollX: Int) = apply {
        this.scrollX = scrollX
        if (!isCleared) cropAndBlur()
    }

    fun blur() {
        isCleared = false
        stackBlur()
        cropAndBlur()
    }

    fun clear() {
        targetView?.background = null
        isCleared = true
    }

    fun get(): Bitmap? {
        return blurredBitmap
    }

}