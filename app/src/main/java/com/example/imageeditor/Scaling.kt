package com.example.imageeditor

/*The value 0xff is equivalent to 255 in unsigned decimal*/

/*you get the RGB value all in one integer, with one byte for each component.
Something like 0xAARRGGBB (alpha, red, green, blue). By performing a bitwise-and with 0xFF, you keep just the last part.
For other channels, you'd use:
int alpha = (rgb >>> 24) & 0xFF;
int red   = (rgb >>> 16) & 0xFF;
int green = (rgb >>>  8) & 0xFF;
int blue  = (rgb >>>  0) & 0xFF;*/

object Scaling {
    fun resizeBilinear(pixels: IntArray, w: Int, h: Int, w2: Int, h2: Int): IntArray {
        val temp = IntArray(w2 * h2)
        var a: Int
        var b: Int
        var c: Int
        var d: Int
        var x: Int
        var y: Int
        var index: Int
        val x_ratio = (w - 1).toFloat() / w2
        val y_ratio = (h - 1).toFloat() / h2
        var x_diff: Float
        var y_diff: Float
        var blue: Float
        var red: Float
        var green: Float
        var offset = 0
        for (i in 0 until h2) {
            for (j in 0 until w2) {
                x = (x_ratio * j).toInt()
                y = (y_ratio * i).toInt()
                x_diff = x_ratio * j - x
                y_diff = y_ratio * i - y
                index = y * w + x
                a = pixels[index]
                b = pixels[index + 1]
                c = pixels[index + w]
                d = pixels[index + w + 1]

                // blue element
                blue =
                    (a and 0xff) * (1 - x_diff) * (1 - y_diff) + (b and 0xff) * x_diff * (1 - y_diff) + (c and 0xff) * y_diff * (1 - x_diff) + (d and 0xff) * (x_diff * y_diff)

                // green element
                green =
                    (a shr 8 and 0xff) * (1 - x_diff) * (1 - y_diff) + (b shr 8 and 0xff) * x_diff * (1 - y_diff) + (c shr 8 and 0xff) * y_diff * (1 - x_diff) + (d shr 8 and 0xff) * (x_diff * y_diff)

                // red element
                red =
                    (a shr 16 and 0xff) * (1 - x_diff) * (1 - y_diff) + (b shr 16 and 0xff) * x_diff * (1 - y_diff) + (c shr 16 and 0xff) * y_diff * (1 - x_diff) + (d shr 16 and 0xff) * (x_diff * y_diff)
                temp[offset++] = -0x1000000 or  // hardcode alpha
                        (red.toInt() shl 16 and 0xff0000) or
                        (green.toInt() shl 8 and 0xff00) or
                        blue.toInt()
            }
        }
        return temp
    }


    fun trilinearImageScaling(
        pixels: IntArray, w: Int, h: Int,  // larger image
        pixels2: IntArray, w2: Int, h2: Int,  // smaller image
        width: Int, height: Int
    ): IntArray? {
        val temp = IntArray(width * height)
        var index: Int
        var index2: Int
        var A: Int
        var B: Int
        var C: Int
        var D: Int
        var E: Int
        var F: Int
        var G: Int
        var H: Int
        var x: Float
        var y: Float
        var x2: Float
        var y2: Float
        var w_diff: Float
        var h_diff: Float
        var w2_diff: Float
        var h2_diff: Float
        var red: Float
        var green: Float
        var blue: Float
        // find ratio for larger image
        val w_ratio = (w - 1).toFloat() / width
        val h_ratio = (h - 1).toFloat() / height
        // find ratio for smaller image
        val w2_ratio = (w2 - 1).toFloat() / width
        val h2_ratio = (h2 - 1).toFloat() / height
        // estimate h3 distance
        val h3_diff = (w - width) / (w - w2).toFloat()
        var offset = 0
        for (i in 0 until height) {
            for (j in 0 until width) {
                // find w1 and h1 for larger image
                x = w_ratio * j
                y = h_ratio * i
                w_diff = x - x.toInt()
                h_diff = y - y.toInt()
                index = y.toInt() * w + x.toInt()
                A = pixels[index]
                B = pixels[index + 1]
                C = pixels[index + w]
                D = pixels[index + w + 1]
                // find w2 and h2 for smaller image
                x2 = w2_ratio * j
                y2 = h2_ratio * i
                w2_diff = x2 - x2.toInt()
                h2_diff = y2 - y2.toInt()
                index2 = y2.toInt() * w2 + x2.toInt()
                E = pixels2[index2]
                F = pixels2[index2 + 1]
                G = pixels2[index2 + w2]
                H = pixels2[index2 + w2 + 1]

                // trilinear interpolate blue element
                blue =
                    (A and 0xff) * (1 - w_diff) * (1 - h_diff) * (1 - h3_diff) + (B and 0xff) * w_diff * (1 - h_diff) * (1 - h3_diff) + (C and 0xff) * h_diff * (1 - w_diff) * (1 - h3_diff) + (D and 0xff) * w_diff * h_diff * (1 - h3_diff) + (E and 0xff) * (1 - w2_diff) * (1 - h2_diff) * h3_diff + (F and 0xff) * w2_diff * (1 - h2_diff) * h3_diff + (G and 0xff) * h2_diff * (1 - w2_diff) * h3_diff + (H and 0xff) * w2_diff * h2_diff * h3_diff

                // trilinear interpolate green element
                green =
                    (A shr 8 and 0xff) * (1 - w_diff) * (1 - h_diff) * (1 - h3_diff) + (B shr 8 and 0xff) * w_diff * (1 - h_diff) * (1 - h3_diff) + (C shr 8 and 0xff) * h_diff * (1 - w_diff) * (1 - h3_diff) + (D shr 8 and 0xff) * w_diff * h_diff * (1 - h3_diff) + (E shr 8 and 0xff) * (1 - w2_diff) * (1 - h2_diff) * h3_diff + (F shr 8 and 0xff) * w2_diff * (1 - h2_diff) * h3_diff + (G shr 8 and 0xff) * h2_diff * (1 - w2_diff) * h3_diff + (H shr 8 and 0xff) * w2_diff * h2_diff * h3_diff

                // trilinear interpolate red element
                red =
                    (A shr 16 and 0xff) * (1 - w_diff) * (1 - h_diff) * (1 - h3_diff) + (B shr 16 and 0xff) * w_diff * (1 - h_diff) * (1 - h3_diff) + (C shr 16 and 0xff) * h_diff * (1 - w_diff) * (1 - h3_diff) + (D shr 16 and 0xff) * w_diff * h_diff * (1 - h3_diff) + (E shr 16 and 0xff) * (1 - w2_diff) * (1 - h2_diff) * h3_diff + (F shr 16 and 0xff) * w2_diff * (1 - h2_diff) * h3_diff + (G shr 16 and 0xff) * h2_diff * (1 - w2_diff) * h3_diff + (H shr 16 and 0xff) * w2_diff * h2_diff * h3_diff
                temp[offset++] = -0x1000000 or  // hardcode alpha
                        blue.toInt() or (
                        green.toInt() shl 8) or (
                        red.toInt() shl 16)
            }
        }
        return temp
    }
}