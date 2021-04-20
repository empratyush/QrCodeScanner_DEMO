package dev.pratyush.qrcodescanner.scan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import dev.pratyush.qrcodescanner.databinding.ScanActivityBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var views : ScanActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views = ScanActivityBinding.inflate(layoutInflater)
        window.setDecorFitsSystemWindows(false)
        setContentView(views.root)

        startCamera()

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val cameraController = LifecycleCameraController(this)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = cameraSelector


        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(views.textureView.surfaceProvider)
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(this),
                    QRCodeImageAnalyzer { _qrCode ->
                        if (_qrCode != null) {
                            showAsToast(_qrCode)
                        }
                    }
                )


                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                } catch (exc: Exception) {
                }

            },
            ContextCompat.getMainExecutor(this)
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showAsToast(result: String?){
        views.response.text = "Response : $result"
    }

}