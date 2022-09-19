package com.example.imageeditor

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import androidx.core.graphics.get
import androidx.core.graphics.set

object RotateFlip {

    fun rotateBitmap(src: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(src, 0, 0,src.width, src.height, matrix, true)
    }

    fun flipBitmap(src: Bitmap): Bitmap {
        val matrix = Matrix().apply { preScale(-1f,1f) }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }


    // Rotates the image by 90 degrees
    fun rotateImg(src: Bitmap?): Bitmap? {
        // image size
        var src = src
        val width = src!!.width
        val height = src.height

        val newWidth = height
        val newHeight = width
        // create output bitmap
        val bmOut = Bitmap.createBitmap(newWidth, newHeight, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int

        // scan through all pixels
        for (y in 0 until height) {
            var dx = (height - 1) - y
            for (x in 0 until width) {
                var dy = x
                // get pixel color
                pixelColor = src.getPixel(x, y)
                // get color on each channel
                A = Color.alpha(pixelColor)
                R = Color.red(pixelColor)
                G = Color.green(pixelColor)
                B = Color.blue(pixelColor)
                // set new pixel color to output image
                bmOut.setPixel(dx, dy, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }


    // Fips the image Horizontally
    fun flipImgHorizontally(src: Bitmap?): Bitmap? {
        // image size
        var src = src
        val width = src!!.width
        val height = src.height

        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int

        // scan through all pixels
        for (i in 0 until height) {
            for (j in 0 until width) {

                //var dy = (width-1)-x

                // get pixel color
                //pixelColor = src.getPixel(j, i)
                pixelColor = src.getPixel((width-1)-j, i)
                // get color on each channel
                A = Color.alpha(pixelColor)
                R = Color.red(pixelColor)
                G = Color.green(pixelColor)
                B = Color.blue(pixelColor)
                // set new pixel color to output image
                //bmOut.setPixel(j, (height-1)-i, Color.argb(A, R, G, B))
                bmOut.setPixel(j, i, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

    // Flips the image vertically
    fun flipImgVertically(src: Bitmap?): Bitmap? {
        // image size
        var src = src
        val width = src!!.width
        val height = src.height

        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int

        // scan through all pixels
        for (i in 0 until height) {
            for (j in 0 until width) {

                //var dy = (width-1)-x

                // get pixel color
                pixelColor = src.getPixel(j, i)
                // get color on each channel
                A = Color.alpha(pixelColor)
                R = Color.red(pixelColor)
                G = Color.green(pixelColor)
                B = Color.blue(pixelColor)
                // set new pixel color to output image
                bmOut.setPixel(j, (height-1)-i, Color.argb(A, R, G, B))
            }
        }
        return bmOut
    }

}