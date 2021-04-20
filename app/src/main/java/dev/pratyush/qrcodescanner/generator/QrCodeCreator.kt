package dev.pratyush.qrcodescanner.generator

import android.content.res.Resources
import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import dev.pratyush.qrcodescanner.R
import java.io.IOException

class QrCodeCreator {

    @Throws(WriterException::class, IOException::class)
    fun createQRCode(
        qrCodeData: String,
        hintMap: Map<EncodeHintType, *>?,
        resource: Resources,

        set: String = "UTF-8",
        qrCodeHeight: Int = 500,
        qrCodeWidth: Int = 500
    ): Bitmap? {

        val charset = charset(set)

        val result: BitMatrix = MultiFormatWriter().encode(
            String(qrCodeData.toByteArray(charset), charset),
            BarcodeFormat.QR_CODE,
            qrCodeWidth,
            qrCodeHeight,
            hintMap
        )

        val w: Int = result.width
        val h: Int = result.height
        val pixels = IntArray(w * h)

        val colorBlack = resource.getColor(R.color.black, resource.newTheme())
        val colorWhite = resource.getColor(R.color.white_50, resource.newTheme())


        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) colorBlack else colorWhite
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(
            pixels,
            0,
            qrCodeWidth,
            0,
            0,
            w,
            h
        )

        return bitmap
    }

}