package com.myapps.mypsikolog.models

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.*
import java.lang.reflect.Array.get
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class FacialExpressionRecognition internal constructor(assetManager: AssetManager, context: Context, modelPath: String, inputSize: Int) {

    private var interpreter: Interpreter
    private var sizeInp = 0
    private var height = 0
    private var width = 0
    private var gpuDelegate: GpuDelegate
    private lateinit var cascadeClassifier: CascadeClassifier

    init {
        this.sizeInp = inputSize
        val options: Interpreter.Options = Interpreter.Options()
        gpuDelegate = GpuDelegate()
        options.addDelegate(gpuDelegate)
        options.setNumThreads(4)
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)
        Log.d("facial_Expression", "Model is loaded")

        try {
            val inputStream: InputStream =
                context.resources.openRawResource(com.myapps.mypsikolog.R.raw.haarcascade_frontalface_alt)
            val cascadeDir: File = context.getDir("cascade", Context.MODE_PRIVATE)
            val mCascadeFile = File(cascadeDir, "haarcascade_frontalface_alt")
            val outputStream = FileOutputStream(mCascadeFile)
            val buffer = ByteArray(4096)
            var byteRead: Int
            while (inputStream.read(buffer).also { byteRead = it } != -1) {
                outputStream.write(buffer, 0, byteRead)
            }
            inputStream.close()
            outputStream.close()
            cascadeClassifier = CascadeClassifier(mCascadeFile.absolutePath)
            Log.d("facial_Expression", "Classifier is loaded")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun recognizeImage(matImage: Mat): Mat {

        Core.flip(matImage.t(), matImage, 1)

        val grayScaleImage = Mat()
        Imgproc.cvtColor(matImage, grayScaleImage, Imgproc.COLOR_RGBA2GRAY)

        height = grayScaleImage.height()
        width = grayScaleImage.width()

        val faceSize: Int = (height * 0.1).toInt()
        val faces = MatOfRect()
        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(
                grayScaleImage,
                faces,
                1.1,
                2,
                2,
                Size(faceSize.toDouble(), faceSize.toDouble()),
                Size()
            )
        }

        val faceArray = faces.toArray()

        for (i in faceArray.indices) {
            Imgproc.rectangle(
                matImage, faceArray[i].tl(), faceArray[i].br(), Scalar(
                    0.0, 255.0, 0.0,
                    255.0
                ), 2
            )
            val roi = Rect(
                faceArray[i].tl().x.toInt(), faceArray[i].tl().y.toInt(),
                faceArray[i].br().x.toInt() - faceArray[i].tl().x.toInt(),
                faceArray[i].br().y.toInt() - faceArray[i].tl().y.toInt()
            )

            val cropRgba = Mat(matImage, roi)
            var bitmap: Bitmap? = null
            bitmap = Bitmap.createBitmap(
                cropRgba.cols(),
                cropRgba.rows(),
                Bitmap.Config.ARGB_8888
            )
            Utils.matToBitmap(cropRgba, bitmap)
            val scaleBitmap = Bitmap.createScaledBitmap(bitmap, 48, 48, false)
            val byteBuffer: ByteBuffer = convertBitmapToByteBuffer(scaleBitmap)
            val emotion = Array(1) {
                FloatArray(
                    1
                )
            }

            interpreter.run(byteBuffer, emotion)
            val emotionValue: Float = get(get(emotion, 0), 0) as Float

            Log.d("facial_Expression", "Output: $emotionValue")

            val emotionSet : String = getEmotionTextValueAnalysis(emotionValue)
            Imgproc.putText(matImage, "$emotionSet ($emotionValue)", Point((faceArray[i].tl().x.toInt() + 10).toDouble(),
                (faceArray[i].tl().y.toInt() + 20).toDouble()
            ), 1, 1.5, Scalar(0.0, 0.0, 255.0, 150.0), 2)
        }

        Core.flip(matImage.t(), matImage, 0)
        return matImage
    }

    private fun getEmotionTextValueAnalysis(emotionValue: Float?): String {
        var value: String = ""
        if (emotionValue != null) {
            value = if (emotionValue >= 0 && emotionValue < 0.5){
                "Terkejut"
            }else if(emotionValue >= 0.5 && emotionValue < 1.5){
                "Takut"
            }else if(emotionValue >= 1.5 && emotionValue < 2.5){
                "Marah"
            }else if(emotionValue >= 2.5 && emotionValue < 3.5){
                "Netral"
            }else if(emotionValue >= 3.5 && emotionValue < 4.5){
                "Sedih"
            }else if(emotionValue >= 4.5 && emotionValue < 5.5){
                "Menjijikan"
            }else {
                "Senang"
            }
        }
        return value
    }

    private fun convertBitmapToByteBuffer(scaleBitmap: Bitmap?): ByteBuffer {
        val byteBuffer: ByteBuffer
        val sizeImage: Int = sizeInp
        byteBuffer = ByteBuffer.allocateDirect(4 * 1 * sizeImage * sizeImage * 3)

        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(sizeImage * sizeImage)
        scaleBitmap!!.getPixels(
            intValues,
            0,
            scaleBitmap.width,
            0,
            0,
            scaleBitmap.width,
            scaleBitmap.height
        )

        var pixel = 0

        for (i in 0 until sizeImage) {
            for (j in 0 until sizeImage) {
                val `value` = intValues[pixel++]
                byteBuffer.putFloat((`value` shr 16 and 0xFF) / 255.0f)
                byteBuffer.putFloat((`value` shr 8 and 0xFF) / 255.0f)
                byteBuffer.putFloat((`value` and 0xFF) / 255.0f)
            }
        }
        return byteBuffer
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val assetFileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel

        val startOffset: Long = assetFileDescriptor.startOffset
        val declareLength: Long = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declareLength)
    }

}