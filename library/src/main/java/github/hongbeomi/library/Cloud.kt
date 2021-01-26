package github.hongbeomi.library

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.*
import android.renderscript.RenderScript.RSMessageHandler
import android.view.View
import androidx.annotation.ColorInt


class Cloud(private val context: Context) {

    companion object {
        private const val DEFAULT_RADIUS = 25f
        private const val DEFAULT_COLOR = Color.TRANSPARENT
    }

    private var radius = DEFAULT_RADIUS
        set(value) {
            field = when {
                value > 25f -> DEFAULT_RADIUS
                value < 0f -> 0f
                else -> value
            }
        }
    private var scrollY: Int = 0
    private var scrollX: Int = 0

    @ColorInt
    private var color = DEFAULT_COLOR
    private var view: View? = null
    private var targetView: View? = null
    private var originalBitmap: Bitmap? = null
    private var bluredBitmap: Bitmap? = null
    private var isCleared = false

    fun from(view: View) = apply {
        this.view = view
    }

    fun radius(radius: Float) = apply {
        this.radius = radius
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

    fun into(targetView: View) = apply {
        this.targetView = targetView
    }

    fun blur() {
        isCleared = false
        rs()
        cropAndBlur()
    }

    fun clear() {
        targetView?.apply {
            alpha = 1f
            background = null
        }
        isCleared = true
    }

    fun get(): Bitmap? {
        return bluredBitmap
    }

    private fun rs() {
        val pullBitmap = pull() ?: return

        val rs = RenderScript.create(context)
        rs.messageHandler = RSMessageHandler()

        val blurRadius = radius
        val width = pullBitmap.width
        val height = pullBitmap.height

        val bitmapType: Type = Type.Builder(rs, Element.RGBA_8888(rs))
            .setX(width)
            .setY(height)
            .setMipmaps(false)
            .create()

        val allocation = Allocation.createTyped(rs, bitmapType)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        allocation.copyFrom(pullBitmap)

        blurScript.apply {
            setInput(allocation)
            setRadius(blurRadius)
            forEach(allocation)
        }
        allocation.copyTo(pullBitmap)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RenderScript.releaseAllContexts()
        } else {
            rs.destroy()
        }
        allocation.destroy()
        blurScript.destroy()

        originalBitmap = pullBitmap
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

        bluredBitmap = Bitmap.createBitmap(
            bitmap,
            targetView.left + calculatedScrollX,
            targetView.top + calculatedScrollY,
            targetView.width,
            targetView.height
        )
        setBackground(targetView)
    }

    private fun setBackground(targetView: View) {
        val bitmap = bluredBitmap ?: return
        targetView.background = BitmapDrawable(context.resources, bitmap)
    }

}