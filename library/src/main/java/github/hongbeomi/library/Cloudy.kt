package github.hongbeomi.library

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt


object Cloudy {

    fun with(context: Context): Builder {
        return Builder(context)
    }

    class Builder(context: Context) {
        private val cloud = Cloud(context)

        fun from(sourceView: View, isPreBlur: Boolean) = apply {
            cloud.from(sourceView, isPreBlur)
        }

        // recommend argb
        fun color(@ColorInt colorInt: Int) = apply {
            cloud.color(colorInt)
        }

        fun radius(radius: Float) = apply {
            cloud.changeRadius(radius)
        }

        fun into(targetView: View): Cloud {
            cloud.into(targetView)
            return cloud
        }
    }

}