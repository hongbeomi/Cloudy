package github.hongbeomi.library

import android.graphics.Bitmap
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class StackBlurNative {

    companion object {

        val EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors()
        val EXECUTOR: ExecutorService = Executors.newFixedThreadPool(EXECUTOR_THREADS)
        private external fun blur(
            bitmapOut: Bitmap,
            radius: Int,
            threadCount: Int,
            threadIndex: Int,
            round: Int
        )
        init {
            System.loadLibrary("blur")
        }
    }

    internal fun blur(original: Bitmap, radius: Int): Bitmap {
        val bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true)
        val cores: Int = EXECUTOR_THREADS
        val horizontal =
            ArrayList<NativeTask>(cores)
        val vertical =
            ArrayList<NativeTask>(cores)
        for (i in 0 until cores) {
            horizontal.add(NativeTask(bitmapOut, radius, cores, i, 1))
            vertical.add(NativeTask(bitmapOut, radius, cores, i, 2))
        }
        try {
            EXECUTOR.invokeAll(horizontal)
        } catch (e: InterruptedException) {
            return bitmapOut
        }
        try {
            EXECUTOR.invokeAll(vertical)
        } catch (e: InterruptedException) {
            return bitmapOut
        }
        return bitmapOut
    }

    private inner class NativeTask(
        private val _bitmapOut: Bitmap,
        private val _radius: Int,
        private val _totalCores: Int,
        private val _coreIndex: Int,
        private val _round: Int
    ) : Callable<Unit> {
        @Throws(Exception::class)
        override fun call() {
            blur(
                _bitmapOut,
                _radius,
                _totalCores,
                _coreIndex,
                _round
            )
        }

    }
}