package com.example.imageeditor

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.imageeditor.databinding.ActivityEditImageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class EditImageActivity : AppCompatActivity() {

    /*View binding is a feature that allows you to more easily write code that interacts with views.
    Once view binding is enabled in a module, it generates a binding class for each XML layout file present in that module.*/

    /*The LayoutInflater class is used to instantiate the contents of layout XML files into their corresponding View objects.
    In other words, it takes an XML file as input and builds the View objects from it.*/

    /*binding.root is reference to root view. root view is outermost view container in your layout.
    when you call binding.root ,will return LinearLayout root view.*/

    private lateinit var binding: ActivityEditImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        displayImagePreview()

        val rotateIcon = findViewById<ImageView>(R.id.rotateImage)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val filtersIcon = findViewById<ImageView>(R.id.photoFiltersIcon)
        val filtersSV = findViewById<HorizontalScrollView>(R.id.filtersScrollView)

        val filterOneIcon = findViewById<ImageView>(R.id.filterOne)
        val filterTwoIcon = findViewById<ImageView>(R.id.filterTwo)
        val filterThreeIcon = findViewById<ImageView>(R.id.filterThree)
        val filterFourIcon = findViewById<ImageView>(R.id.filterFour)

        val mImageView = findViewById<ImageView>(R.id.imagePreview)
        val mSeekBar = findViewById<SeekBar>(R.id.seekBar)

        val flipIcon = findViewById<ImageView>(R.id.flipImage)
        val sharpenIcon = findViewById<ImageView>(R.id.sharpenIcon)
        val scaleIcon = findViewById<ImageView>(R.id.scaleIcon)
        val scaleSeekBar = findViewById<SeekBar>(R.id.scalingSeekBar)
        var scaleTxt = findViewById<TextView>(R.id.scaleTxt)
        val scaleUpIcon = findViewById<ImageView>(R.id.scaleUpIcon)
        val scaleDownIcon = findViewById<ImageView>(R.id.scaleDownIcon)
        val applyScaleIcon = findViewById<ImageView>(R.id.applyScalingIcon)
        var scaleMultiplier = 1.0
        var scaleUpBool = false
        var scaleDownBool = false


        var previewImgBitmap : Bitmap = mImageView.drawable.toBitmap()
        val previewWidth = previewImgBitmap.width
        val previewHeight = previewImgBitmap.height

        var bitmapForSharpening = previewImgBitmap

        val mWidth = previewImgBitmap.width / 4
        val mHeight = previewImgBitmap.height / 4
        var thumbnailOriginal : Bitmap = ThumbnailUtils.extractThumbnail(previewImgBitmap, mWidth, mHeight)
        var sepiaBitmap = previewImgBitmap
        var invertBitmap = previewImgBitmap
        var redBitmap = previewImgBitmap

        var imgTwoClicked: Boolean = false
        var imgThreeClicked: Boolean = false
        var imgFourClicked: Boolean = false

        var dimTxt = findViewById<TextView>(R.id.dimensionsTxt)
        dimTxt.setText(previewWidth.toString() + "*" + previewHeight.toString())

        var sharpnessTxt = findViewById<TextView>(R.id.sharpnessTxt)
        var sharpMultiplier = 1

        var applySharpIcon = findViewById<ImageView>(R.id.applySharpIcon)

        var applyFilterIcon = findViewById<ImageView>(R.id.applyFilterIcon)

        rotateIcon.setOnClickListener{
            previewImgBitmap = RotateFlip.rotateImg(previewImgBitmap)!!
            binding.imagePreview.setImageBitmap(previewImgBitmap)

        }
        flipIcon.setOnClickListener{
            previewImgBitmap = RotateFlip.flipImgHorizontally(previewImgBitmap)!!
            binding.imagePreview.setImageBitmap(previewImgBitmap)
        }
        sharpenIcon.setOnClickListener{
            mSeekBar.visibility = View.VISIBLE
            sharpnessTxt.visibility = View.VISIBLE
            applySharpIcon.visibility = View.VISIBLE
        }
        applySharpIcon.setOnClickListener{

            if (sharpMultiplier == 0){
                binding.imagePreview.setImageBitmap(previewImgBitmap)
            }
            else{
                bitmapForSharpening = Sharpen.sharpenImg(previewImgBitmap, sharpMultiplier)
                binding.imagePreview.setImageBitmap(bitmapForSharpening)
            }

            mSeekBar.visibility = View.INVISIBLE
            sharpnessTxt.visibility = View.INVISIBLE
            applySharpIcon.visibility = View.INVISIBLE
        }

        scaleIcon.setOnClickListener{

            bottomNav.visibility = View.INVISIBLE
            scaleSeekBar.visibility = View.VISIBLE
            scaleDownIcon.visibility = View.VISIBLE
            scaleUpIcon.visibility = View.VISIBLE
            scaleTxt.visibility = View.VISIBLE
        }
        scaleUpIcon.setOnClickListener{
            scaleDownIcon.visibility = View.INVISIBLE
            scaleUpIcon.visibility = View.INVISIBLE
            applyScaleIcon.visibility = View.VISIBLE

            scaleDownBool = false
            scaleUpBool = true
        }
        scaleDownIcon.setOnClickListener{
            scaleDownIcon.visibility = View.INVISIBLE
            scaleUpIcon.visibility = View.INVISIBLE
            applyScaleIcon.visibility = View.VISIBLE

            scaleDownBool = true
            scaleUpBool = false
        }
        applyScaleIcon.setOnClickListener{
            if (scaleMultiplier == 1.0){
                binding.imagePreview.setImageBitmap(previewImgBitmap)
                applyScaleIcon.visibility = View.INVISIBLE
                scaleSeekBar.visibility = View.INVISIBLE
                scaleTxt.visibility = View.INVISIBLE
                bottomNav.visibility = View.VISIBLE
            }
            if (scaleUpBool == true){
                scaleUp(scaleMultiplier, previewImgBitmap, previewWidth, previewHeight)
                applyScaleIcon.visibility = View.INVISIBLE
                scaleSeekBar.visibility = View.INVISIBLE
                scaleTxt.visibility = View.INVISIBLE
                bottomNav.visibility = View.VISIBLE
                scaleUpBool = false
            }
            else if (scaleDownBool == true){
                scaleDown(scaleMultiplier, previewImgBitmap, previewWidth, previewHeight, thumbnailOriginal, mWidth, mHeight)
                applyScaleIcon.visibility = View.INVISIBLE
                scaleSeekBar.visibility = View.INVISIBLE
                scaleTxt.visibility = View.INVISIBLE
                bottomNav.visibility = View.VISIBLE
                scaleDownBool = false
            }
        }

        filtersIcon.setOnClickListener{
            bottomNav.visibility = View.INVISIBLE
            filtersSV.visibility = View.VISIBLE
            applyFilterIcon.visibility = View.VISIBLE
        }

        applyFilterIcon.setOnClickListener{
            filtersSV.visibility = View.INVISIBLE
            applyFilterIcon.visibility = View.INVISIBLE
            bottomNav.visibility = View.VISIBLE
        }


        // Setting filters thumbnails with the resized(small) bitmap of the original image
        binding.filterOne.setImageBitmap(thumbnailOriginal)

        sepiaBitmap = Filters.sepia(thumbnailOriginal)
        binding.filterTwo.setImageBitmap(sepiaBitmap)

        invertBitmap = Filters.invert(thumbnailOriginal)!!
        binding.filterThree.setImageBitmap(invertBitmap)

        redBitmap = Filters.colorFilter(thumbnailOriginal, 255.0, 0.0, 0.0)!!
        binding.filterFour.setImageBitmap(redBitmap)


        // Changing the original image filter when clicking the thumbnails
        filterOneIcon.setOnClickListener{
            binding.imagePreview.setImageBitmap(previewImgBitmap)
        }
        filterTwoIcon.setOnClickListener{
            if (!imgTwoClicked){
                sepiaBitmap = Filters.sepia(previewImgBitmap)
                imgTwoClicked = true
            }
            binding.imagePreview.setImageBitmap(sepiaBitmap)
        }
        filterThreeIcon.setOnClickListener{
            if (!imgThreeClicked){
                invertBitmap = Filters.invert(previewImgBitmap)!!
                imgThreeClicked = true
            }
            binding.imagePreview.setImageBitmap(invertBitmap)
        }
        filterFourIcon.setOnClickListener{
            if (!imgFourClicked){
                redBitmap = Filters.colorFilter(previewImgBitmap, 255.0, 0.0, 0.0)!!
                imgFourClicked = true
            }
            binding.imagePreview.setImageBitmap(redBitmap)
        }

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean){
                sharpnessTxt.setText(progress.toString())
                sharpMultiplier = progress

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        scaleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean){
                scaleTxt.setText(progress.toString() + "%")
                scaleMultiplier = (progress).toDouble()/100

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })



    }

    private fun displayImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            var bitmap = BitmapFactory.decodeStream(inputStream)

            // Main Image Preview
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }
        binding.imageHome.setOnClickListener{
            onBackPressed()
        }
    }

    private fun scaleUp(coef: Double, previewImgBitmap: Bitmap, previewWidth: Int, previewHeight: Int){

        val newBitmap = Bitmap.createBitmap((previewWidth*coef).toInt(), (previewHeight*coef).toInt(), previewImgBitmap.config)

        val pixelsArray = IntArray(previewWidth * previewHeight)
        previewImgBitmap.getPixels(pixelsArray, 0, previewWidth, 0, 0, previewWidth, previewHeight)

        val pixels = Scaling.resizeBilinear(pixelsArray, previewWidth, previewHeight, newBitmap.width, newBitmap.height)

        newBitmap.setPixels(pixels, 0, newBitmap.width, 0, 0, newBitmap.width, newBitmap.height)

        binding.imagePreview.setImageBitmap(newBitmap)

        var dimTxt = findViewById<TextView>(R.id.dimensionsTxt)
        dimTxt.setText(newBitmap.width.toString() + "*" + newBitmap.height.toString())

    }

    private fun scaleDown(coef: Double, previewImgBitmap: Bitmap, previewWidth: Int, previewHeight: Int, thumbnailOriginal: Bitmap, mWidth: Int, mHeight: Int){

        val newBitmap = Bitmap.createBitmap((previewWidth/coef).toInt(), (previewHeight/coef).toInt(), previewImgBitmap.config)

        val pixelsArrayBig = IntArray(previewWidth * previewHeight)
        previewImgBitmap.getPixels(pixelsArrayBig, 0, previewWidth, 0, 0, previewWidth, previewHeight)
        val pixelsArraySmall = IntArray(mWidth * mHeight)
        thumbnailOriginal.getPixels(pixelsArraySmall, 0, mWidth, 0, 0, mWidth, mHeight)


        val pixels = Scaling.trilinearImageScaling(pixelsArrayBig, previewWidth, previewHeight, pixelsArraySmall, mWidth, mHeight, newBitmap.width, newBitmap.height)


        newBitmap.setPixels(pixels, 0, newBitmap.width, 0, 0, newBitmap.width, newBitmap.height)

        binding.imagePreview.setImageBitmap(newBitmap)

        var dimTxt = findViewById<TextView>(R.id.dimensionsTxt)
        dimTxt.setText(newBitmap.width.toString() + "*" + newBitmap.height.toString())

    }
}