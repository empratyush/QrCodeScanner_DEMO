package dev.pratyush.qrcodescanner.scan

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.BarcodeFormat.*
import com.google.zxing.common.HybridBinarizer
import java.util.*


class QRCodeImageAnalyzer(
    private var hints : List<BarcodeFormat> = allSupportedFormat,
    private val listener: (qrCode: String?) -> Unit
    ) : Analyzer {

    companion object{
        val allSupportedFormat = listOf(
            AZTEC,
            CODABAR,
            CODE_39,
            CODE_93,
            CODE_128,
            DATA_MATRIX,
            EAN_8,
            EAN_13,
            ITF,
            MAXICODE,
            PDF_417,
            QR_CODE,
            RSS_14,
            RSS_EXPANDED,
            UPC_A,
            UPC_E,
            UPC_EAN_EXTENSION
        )

    }

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
                val supportedHints: MutableMap<DecodeHintType, Any> = EnumMap(
                    DecodeHintType::class.java
                )
                supportedHints[DecodeHintType.POSSIBLE_FORMATS] = hints
                val result = MultiFormatReader().decode(
                    binaryBitmap,
                    supportedHints
                )
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