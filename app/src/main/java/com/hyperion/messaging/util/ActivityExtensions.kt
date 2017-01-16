package com.hyperion.messaging.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.view.View

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 16/01/2017
 */
class ActivityExtensions {
    companion object {
        fun Activity.getScaledBitMapBasedOnScreenSize(srcBitmap: Bitmap): Bitmap {
            var scaledBitmap: Bitmap? = null
            try {
                val metrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(metrics)

                val width = srcBitmap.width
                val height = srcBitmap.height

                val scaleWidth = metrics.scaledDensity
                val scaleHeight = metrics.scaledDensity

                // create a matrix for the manipulation
                val matrix = Matrix()
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight)

                // recreate the new Bitmap
                scaledBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return scaledBitmap ?: srcBitmap
        }

        fun Activity.getScreenShot(): Bitmap {
            val screenView = window.decorView.findViewById(android.R.id.content).rootView
            // this is the important code :)
            // Without it the view will have a dimension of 0,0 and the bitmap will be null
            screenView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            screenView.layout(0, 0, screenView.measuredWidth, screenView.measuredHeight)

            screenView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(screenView.drawingCache)
            screenView.isDrawingCacheEnabled = false
            return bitmap
        }

        fun getScreenShot(view: View): Bitmap {
            val screenView = view.rootView
            screenView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(screenView.drawingCache)
            screenView.isDrawingCacheEnabled = false
            return bitmap
        }
    }


}
