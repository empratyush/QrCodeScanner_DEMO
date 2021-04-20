package dev.pratyush.qrcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dev.pratyush.qrcodescanner.databinding.ActivityMainBinding
import dev.pratyush.qrcodescanner.generator.QrCodeCreator
import dev.pratyush.qrcodescanner.generator.QrGeneratorActivity
import dev.pratyush.qrcodescanner.scan.ScanActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val views = ActivityMainBinding.inflate(layoutInflater)
        window.setDecorFitsSystemWindows(false)
        setContentView(views.root)

        views.scan.setOnClickListener{

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(this, ScanActivity::class.java))
            }else{
                Snackbar.make(
                    views.root,
                    "Please grant camera permission and try again",
                    Snackbar.LENGTH_SHORT
                ).show()

                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    0
                )
            }

        }
        views.generate.setOnClickListener{
            startActivity(Intent(this, QrGeneratorActivity::class.java))
        }

    }

}