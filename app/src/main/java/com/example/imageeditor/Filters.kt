package com.example.imageeditor

import android.graphics.Bitmap
import android.graphics.Color


/*A bitmap configuration describes how pixels are stored.
This affects the quality (color depth) as well as the ability to display transparent/translucent colors*/

object Filters {
    fun sepia(src: Bitmap?): Bitmap {
        // image size
        var src = src
        val width = src!!.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // constant grayscale
        val GS_RED = 0.3
        val GS_GREEN = 0.59
        val GS_BLUE = 0.11
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixelColor = src.getPixel(x, y)
                // get color on each channel
                A = Color.alpha(pixelColor)
                R = Color.red(pixelColor)
                G = Color.green(pixelColor)
                B = Color.blue(pixelColor)
                // apply grayscale sample
                R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = R
                B = G

                // apply intensity level for sepid-toning on each channel
                R += 110
                if (R > 255) {
                    R = 255
                }
                G += 65
                if (G > 255) {
                    G = 255
                }
                B += 20
                if (B > 255) {
                    B = 255
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        //src.recycle()
        //src = null
        return bmOut
    }

    fun invert(src: Bitmap?): Bitmap? {
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
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get pixel color
                pixelColor = src.getPixel(x, y)
                // get color on each channel and subtract it from 255
                A = Color.alpha(pixelColor)
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        //src.recycle()
        //src = null
        return bmOut
    }


    fun colorFilter(src: Bitmap?, red: Double, green: Double, blue: Double): Bitmap? {
        var src = src
        var red = red
        var green = green
        var blue = blue
        red = red / 100
        green = green / 100
        blue = blue / 100

        // image size
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
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixelColor = src.getPixel(x, y)
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixelColor)
                R = (Color.red(pixelColor) * red).toInt()
                G = (Color.green(pixelColor) * green).toInt()
                B = (Color.blue(pixelColor) * blue).toInt()
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        //src.recycle()
        //src = null
        return bmOut
    }
}