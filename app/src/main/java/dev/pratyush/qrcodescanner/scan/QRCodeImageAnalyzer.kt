package dev.pratyush.qrcodescanner.scan

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader

class QRCodeImageAnalyzer( private val listener: (qrCode: String?) -> Unit) : Analyzer {

    override fun analyze(image: ImageProxy) {

        if (image.format == ImageFormat.YUV_420_888
            || image.format == ImageFormat.YUV_422_888
            || image.format == ImageFormat.YUV_444_888
        ) {

            val byteBuffer = image.planes[0].buffer
            val imageData = ByteArray(byteBuffer.capacity())
            byteBuffer[imageData]
            val source = PlanarYUVLuminanceSource(
                imageData,
                image.width, image.height,
                0, 0,
                image.width, image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = QRCodeMultiReader().decode(binaryBitmap)
                listener.invoke(result.text)
            } catch (e: FormatException) {
                e.fillInStackTrace()
            } catch (e: ChecksumException) {
                e.fillInStackTrace()
            } catch (e: NotFoundException) {
                e.fillInStackTrace()
            }
        }
        image.close()
    }
}