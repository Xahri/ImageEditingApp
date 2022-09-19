package com.example.imageeditor

import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView

class SplinesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spline)
    }

    fun spline(view: View){
        if (pointsArray.size < 2){
            return
        }
        var controlPoints = BezierSplineUtil.getCurveControlPoints(pointsArray)
        var firstCP = controlPoints[0]
        var secondCP = controlPoints[1]

        var p = Path()
        p.moveTo(pointsArray[0].x, pointsArray[0].y)

        for (i in firstCP.indices){
            p.cubicTo(
                firstCP[i]!!.x,
                firstCP[i]!!.y,
                secondCP[i]!!.x,
                secondCP[i]!!.y,
                pointsArray[i + 1].x,
                pointsArray[i + 1].y
            )
        }

        importPath(p)
    }
}