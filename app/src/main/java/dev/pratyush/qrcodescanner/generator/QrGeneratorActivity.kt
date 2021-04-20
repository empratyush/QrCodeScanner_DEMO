package dev.pratyush.qrcodescanner.generator

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dev.pratyush.qrcodescanner.R
import dev.pratyush.qrcodescanner.databinding.GeneratorActivityBinding

class QrGeneratorActivity : AppCompatActivity() {

    private lateinit var views : GeneratorActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views = GeneratorActivityBinding.inflate(layoutInflater)
        window.setDecorFitsSystemWindows(false)
        setContentView(views.root)

        views.generate.setOnClickListener{
            generateNdSetQrCode(
                views.input.text?.toString() ?: ""
            )
        }

    }

    private fun generateNdSetQrCode(input: String) {
        val hashMap = mutableMapOf<EncodeHintType, Any>()

        hashMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        hashMap[EncodeHintType.MARGIN] = 1

        try {
            views.myImageView.setImageBitmap(
                QrCodeCreator().createQRCode(
                    input,
                    hashMap,
                    resources
                )
            )
        } catch (e: Exception) {
            println("Error during qr Generation")
            println("Error " + e.localizedMessage)
        }
    }

}