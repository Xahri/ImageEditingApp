package com.example.imageeditor

import android.graphics.Bitmap
import android.graphics.Color
import com.example.imageeditor.Sharpen

object Sharpen {
    private const val SIZE = 3

    // computeConvolution3x3
    fun sharpenImg(src: Bitmap, multiplier: Int): Bitmap {

        var weight = 4 * multiplier
        var n = -1 * multiplier
        weight += 1

        // Kernel
        val matrix = arrayOf(
            intArrayOf(0, n, 0),
            intArrayOf(n, weight, n),
            intArrayOf(0, n, 0)
        )

        val width = src.width
        val height = src.height
        val result = Bitmap.createBitmap(width, height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var sumR: Int
        var sumG: Int
        var sumB: Int
        val pixels = Array(SIZE) { IntArray(SIZE) }
        for (y in 0 until height - 2) {
            for (x in 0 until width - 2) {

                // get pixel matrix
                for (i in 0 until SIZE) {
                    for (j in 0 until SIZE) {
                        pixels[i][j] = src.getPixel(x + i, y + j)
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[1][1])

                // init color sum
                sumB = 0
                sumG = sumB
                sumR = sumG

                // get sum of RGB on matrix
                for (i in 0 until SIZE) {
                    for (j in 0 until SIZE) {
                        sumR += (Color.red(pixels[i][j]) * matrix[i][j]).toInt()
                        sumG += (Color.green(pixels[i][j]) * matrix[i][j]).toInt()
                        sumB += (Color.blue(pixels[i][j]) * matrix[i][j]).toInt()
                    }
                }

                // get final Red
                R = sumR
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }

                // get final Green
                G = sumG
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }

                // get final Blue
                B = sumB
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                }

                // apply new pixel
                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B))
            }
        }

        // final image
        return result
    }
}