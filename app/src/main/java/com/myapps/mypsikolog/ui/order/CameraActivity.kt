package com.myapps.mypsikolog.ui.order

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.models.FacialExpressionRecognition
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.io.IOException
import org.opencv.android.BaseLoaderCallback as BaseLoaderCallback1

class CameraActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        const val TAG: String = "MainActivity"
    }

    private lateinit var mRgba: Mat
    private lateinit var mGray: Mat
    lateinit var openCvCamera: CameraBridgeViewBase
    private lateinit var facialExpressionRecognition: FacialExpressionRecognition
    private val mLoaderCallback: BaseLoaderCallback1 =
        object : org.opencv.android.BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    LoaderCallbackInterface.SUCCESS -> {
                        Log.d(TAG, "OpenCv is loaded")
                        openCvCamera.enableView()
                    }

                    else -> {
                        super.onManagerConnected(status)
                    }

                }
            }
        }

    public fun cameraActivity() {
        Log.i(TAG, "Instantiated new ${this.classLoader}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val PERMISSIONS_REQUEST_CAMERA = 0
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA
            )
        }

        setContentView(R.layout.activity_camera)

        openCvCamera = findViewById(R.id.frame_surface)
        openCvCamera.visibility = SurfaceView.VISIBLE
        openCvCamera.setCvCameraViewListener(this)
        try {
            val inputSize = 48;
            facialExpressionRecognition =
                FacialExpressionRecognition(assets, this@CameraActivity, "model.tflite", inputSize)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            //if load success
            Log.d(TAG, "Opencv initialize is done")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        } else {
            //if not loaded
            Log.d(TAG, "Opencv is not loaded, try again")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        openCvCamera.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        openCvCamera.disableView()
    }


    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = object : Mat(height, width, CvType.CV_8UC4) {}
        mGray = object : Mat(height, width, CvType.CV_8UC1) {}
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame?.rgba()!!
        mGray = inputFrame.gray()
        mRgba=facialExpressionRecognition.recognizeImage(mRgba)
        return mRgba
    }

}