package github.hongbeomi.library

import android.content.Context
import android.view.View


object Cloudy {

    fun with(context: Context): Builder {
        return Builder(context)
    }

    class Builder(context: Context) {
        private val cloud = Cloud(context)

        fun from(sourceView: View) = apply {
            cloud.from(sourceView)
        }

        fun into(targetView: View): Cloud {
            cloud.into(targetView)
            return cloud
        }
    }

}