package com.example.androidapp.retrofit.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.example.androidapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

// Klasse für das Verarbeiten von Bildern
class ConvertPicture {

    // Bilder im Storage des Handy löschen
    fun deletePicturesOnStorage(pfad: String) {
        val imageFileFolder = File(pfad)
        Log.d("deletePicturesOnStorage", "pfad|$pfad, exists= ${imageFileFolder.exists()}")
        if (imageFileFolder.exists()) {
            imageFileFolder.deleteRecursively()
        }
    }

    // Bild vom Handy laden und in einen String convertieren
    @RequiresApi(Build.VERSION_CODES.O)
    fun bildUrlToDecodedStringFromActivityResult(uri: String, context: Context): String {
        var decodedPicture = ""
        try {
            val item = context.contentResolver.openInputStream(uri.toUri())
            val bytes = item?.readBytes()
            item?.close()
            decodedPicture = java.util.Base64.getEncoder().encodeToString(bytes)
            Log.d("bildUrlToDecodedStringFromActivityResult()", "decidedPicture=$decodedPicture")
        } catch (e: Exception) {
            Log.d("bildUrlToDecodedStringFromActivityResult()", "${e.message}")
        }

        return decodedPicture
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun bildUrlToDecodedString(bildpfad: String): String {
        Log.d("bildUrlToDecodedString", "bildpfad: $bildpfad")
        try {
            val fileContent: ByteArray = File(bildpfad).readBytes()
            val decodedPicture = java.util.Base64.getEncoder().encodeToString(fileContent)
            Log.d("bildUrlToDecodedString", "decodedString: $decodedPicture")
            return decodedPicture
        } catch (e: Exception) {
            return "/9j/4AAQSkZJRgABAQEAYABgAAD/4QAiRXhpZgAATU0AKgAAAAgAAQESAAMAAAABAAEAAAAAAAD/4QAC/9sAQwACAQECAQECAgICAgICAgMFAwMDAwMGBAQDBQcGBwcHBgcHCAkLCQgICggHBwoNCgoLDAwMDAcJDg8NDA4LDAwM/9sAQwECAgIDAwMGAwMGDAgHCAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwM/8AAEQgA+gD6AwEiAAIRAQMRAf/EAB8AAAEFAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A/ZiiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoooo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cAk9SfqaBl2iiigAooooAKKKKACiiigAooooAKKKKACiiigAoPSiigDz+Pw1fb13Wc3y+OW1LlekGGxN/u5xzVzV9CvJ1+I+y1mZtWsVitcL/wAfTCwMeF9cOdv1NdpRQBwMsOrwa3LC0OvW1ubK0W1GkwQJ9tdYsOLi4ZS0ZVyVALIAvIJJNVfAljfeCpNBuJtNvrp18OQ6bPb2qo11ZyRzM2TGzKfLfdjcMgNGucAgj0isvXvBum+JbiGa8t3ae3VkjlhuJbeVVbBZd8bKxU4B2k446UAed6PpN9qGm6Rq0cGqW0Vlq2si5h0to3ubbzp2AKAhlkVGRkOzJ2uSuRmuy+HGlyW0mrXklvrEP9oXKMr6nMjXFyEjVBI0aovldNoBJYhASFzit7S9KttE02Gzs4I7a1t12RxRrhUHt+p9ySetWKAMPSdPnt/i1qF80TrbSaZaQpNj5SyzXDOoPsGQn6iuT8D6PqHgiPw/f3Gl6hNFHo8umTxW8PmT2b/afNUmMfMVdeCVBwVXOAc16RRQB50+kalYWlvqzaXfnzvFb6y9lCqyXMED2zwgsobG8th2UEkeYe4Ndb8RrCbUvh54htbeNpri40y6hijTkyO0LqoH1JA/GtiigDkI2uvB/ja41KXTtSvrXUtNtIFks4fOltZITLujZAdwVvMDBvu5Ug44NZOj6LqXhJ/DmoXGl3kkccmqtcWtmguJLD7XMk0abVPzBQhDFcgE9xzXovWigDzzS9C1K5nsZ5tNurct4ym1No5ArNDA1tKEdtpI4ZlGQSA3QnrUviXw3qE3iPWtQgsprhbfUdJ1COIYVr5Ldf3ioSQCy9QCRyoHpXfUUAcS/wBo8Q+LNYvm0nVrexk8PtZK0tuEnuWEkjFVRiDnDYUPtyc9Bgnq9AhFtoNjGEmjEdtEoSVQsiYQDDAEgMOhAJAOeTVuigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigD/2Q=="
        }
    }

    // Bild vom String in eine Bitmap umwandeln
    @RequiresApi(Build.VERSION_CODES.O)
    fun encodedStringToBitmap(base64String: String, context: Context): Bitmap {

        val imageBytes: ByteArray
        var decodedImage: Bitmap

        try {
            imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            Log.d("MainActivity", "decodedStringFailed: " + e.message)

            val option = BitmapFactory.Options()
            option.inPreferredConfig = Bitmap.Config.ARGB_8888


            decodedImage = BitmapFactory.decodeResource(
                //LocalContext.current.resources,
                context.applicationContext.resources,
                R.drawable.imagenotfound,
                option
            )
        }
        return decodedImage
    }


    // Bitmap auf dem Handy speichern
    fun saveBitmapImage(bitmap: Bitmap?, number: Int, context: Context): String {
        if (bitmap == null) return "Nope"

        val timestamp = System.currentTimeMillis()

        //Tell the media scanner about the new file so that it is immediately available to the user.
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, number)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
            values.put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/" + context.getString(R.string.app_name)
            )
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            val uri =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = context.contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            outputStream.close()
                        } catch (e: Exception) {
                            Log.e(ContentValues.TAG, "saveBitmapImage: ", e)
                        }
                    }
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)

                    Toast.makeText(context, "Saved...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "saveBitmapImage: ", e)
                }
            }
        } else {
            val imageFileFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + '/' + context.getString(R.string.app_name)
            )
            if (!imageFileFolder.exists()) {
                imageFileFolder.mkdirs()
            }
            val mImageName = "$timestamp.png"
            val imageFile = File(imageFileFolder, mImageName)
            try {
                val outputStream: OutputStream = FileOutputStream(imageFile)
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "saveBitmapImage: ", e)
                }
                values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                Toast.makeText(context, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "saveBitmapImage: ", e)
            }

            return mImageName
        }
        return "$timestamp.png"
    }
}