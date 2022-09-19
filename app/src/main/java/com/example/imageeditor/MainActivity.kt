package com.example.imageeditor

import android.content.Intent
import android.graphics.Color
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.imageeditor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding: ActivityMainBinding

    var splinesActive = false
    var cubeActive = false
    var imageActive = false

    private lateinit var glView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()

        binding.splinesIcon.setOnClickListener{
            splinesActive = true
            cubeActive = false
            imageActive = false

            binding.splinesIcon.setImageResource(R.drawable.splines_p)
            binding.openImage.setImageResource(R.drawable.bttn_p)
            binding.cubeIcon.setImageResource(R.drawable.cube_grey)
            binding.imageIcon.setImageResource(R.drawable.image_grey)

            binding.splinesText.setTextColor(Color.WHITE)
            binding.imageText.setTextColor(Color.argb(255, 89, 89, 89)) // Grey
            binding.cubeText.setTextColor(Color.argb(255, 89, 89, 89))
        }

        binding.imageIcon.setOnClickListener{
            splinesActive = false
            cubeActive = false
            imageActive = true

            binding.splinesIcon.setImageResource(R.drawable.splines_grey)
            binding.openImage.setImageResource(R.drawable.bttn_p)
            binding.cubeIcon.setImageResource(R.drawable.cube_grey)
            binding.imageIcon.setImageResource(R.drawable.image_p)

            binding.splinesText.setTextColor(Color.argb(255, 89, 89, 89))
            binding.imageText.setTextColor(Color.WHITE)
            binding.cubeText.setTextColor(Color.argb(255, 89, 89, 89))
        }

        binding.cubeIcon.setOnClickListener{
            splinesActive = false
            cubeActive = true
            imageActive = false

            binding.splinesIcon.setImageResource(R.drawable.splines_grey)
            binding.openImage.setImageResource(R.drawable.bttn_p)
            binding.cubeIcon.setImageResource(R.drawable.cube_p)
            binding.imageIcon.setImageResource(R.drawable.image_grey)

            binding.splinesText.setTextColor(Color.argb(255, 89, 89, 89))
            binding.imageText.setTextColor(Color.argb(255, 89, 89, 89))
            binding.cubeText.setTextColor(Color.WHITE)
        }
    }

    fun myGLActivity(){
        // Allocate a custom subclass of GLSurfaceView (NEW)
        glView = MyGLSurfaceView(this);
        setContentView(glView);  // Set View (NEW)
    }

    private fun setListeners() {

        binding.openImage.setOnClickListener() {
            if (imageActive == true){
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ).also{ pickerIntent ->
                    pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(pickerIntent, REQUEST_CODE_PICK_IMAGE)
                }
            }

            else if (splinesActive == true){
                Intent(applicationContext, SplinesActivity::class.java).also { splineActivityIntent ->
                    startActivity(splineActivityIntent)
                }
            }

            else if (cubeActive == true){
                myGLActivity()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK){
            data?.data?.let{ imageUri ->
                Intent(applicationContext, EditImageActivity::class.java).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
                    startActivity(editImageIntent)
                }
            }
        }
    }

    fun startFadeoutAnim(){
        val pattern = findViewById<View>(R.id.greyPattern) as ImageView
        val animFadeout = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        pattern!!.startAnimation(animFadeout)
        //pattern.clearAnimation()

        val handler = Handler()
        handler.postDelayed({
            startFadeinAnim()
        }, 400)
    }

    fun startFadeinAnim(){
        val pattern = findViewById<View>(R.id.greyPattern) as ImageView
        val animFadein = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        pattern.setImageResource(R.drawable.pattern_g)
        pattern!!.startAnimation(animFadein)

        val handler = Handler()
        handler.postDelayed({
            pattern.clearAnimation()
        }, 1000)
    }
}