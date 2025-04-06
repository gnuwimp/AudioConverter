/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter.convert1

import gnuwimp.audioconverter.*
import gnuwimp.swing.Swing
import gnuwimp.util.FileInfo
import gnuwimp.util.Task
import gnuwimp.util.safeClose
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.InputStream
import java.io.OutputStream

//------------------------------------------------------------------------------
class Task(private val inputFile: FileInfo, private val outputFile: FileInfo, private val encoder: Encoders) : Task(inputFile.size) {
    //--------------------------------------------------------------------------
    override fun run() {
        var decoderBuilder: ProcessBuilder?
        var decoderProcess: Process?        = null
        var decoderStream:  InputStream?    = null
        var encoderBuilder: ProcessBuilder?
        var encoderProcess: Process?        = null
        var encoderStream:  OutputStream?   = null
        var exception                       = ""

        try {
            val decoderParams = Decoder.create(inputFile, false)
            val buffer        = ByteArray(size = 131_072)

            decoderBuilder = ProcessBuilder(decoderParams)
            decoderProcess = decoderBuilder.start()
            decoderStream  = decoderProcess.inputStream
            message        = inputFile.filename

            Swing.logMessage = decoderParams.joinToString(separator = " ")

            while (decoderProcess.isAlive == true) {
                val read = decoderStream.read(buffer)

                if (read > 0) {
                    if (encoderStream == null) {
                        outputFile.remove()

                        val wavHeader     = WavHeader(buffer, read)
                        val encoderParams = Encoders.Companion.createEncoder(encoder, wavHeader, outputFile.filename)
                        Swing.logMessage  = encoderParams.joinToString(separator = " ")

                        encoderBuilder = ProcessBuilder(encoderParams)
                        encoderProcess = encoderBuilder.start()
                        encoderStream  = encoderProcess.outputStream

                        encoderStream?.write(buffer, wavHeader.data, read - wavHeader.data)
                    }
                    else {
                        encoderStream.write(buffer, 0, read)
                    }

                    ConvertManager.Companion.add(read.toLong())
                }

                if (abort == true) {
                    throw Exception(Constants.CANCEL_ERROR)
                }
            }

            decoderStream.safeClose()
            decoderProcess.waitFor()

            decoderStream  = null
            decoderProcess = null
        }
        catch (e: Exception) {
            exception = e.message.toString()
        }
        finally {
            progress += inputFile.size

            decoderStream?.safeClose()
            encoderStream?.safeClose()

            if (decoderProcess?.isAlive == true) {
                decoderProcess.waitFor()
            }

            if (encoderProcess?.isAlive == true) {
                encoderProcess.waitFor()
            }

            if (encoderProcess != null && encoderProcess.exitValue() != 0) {
                exception = "error: encoder failed, exit code=${encoderProcess.exitValue()} - $exception"
            }
            else if (decoderProcess != null && decoderProcess.exitValue() != 0) {
                exception = "error: decoder failed, exit code=${decoderProcess.exitValue()} - $exception"
            }

            if (exception != "") {
                outputFile.remove()
                throw Exception(exception)
            }
            else {
                try {
                    val fromFile = AudioFileIO.read(inputFile.file)
                    val toFile   = AudioFileIO.read(outputFile.file)
                    val fromTag  = fromFile.tag
                    val toTag    = toFile.createDefaultTag()

                    if (fromTag != null && toTag != null) {
                        toTag.copyField(FieldKey.ALBUM, fromTag)
                        toTag.copyField(FieldKey.ALBUM_ARTIST, fromTag)
                        toTag.copyField(FieldKey.ARTIST, fromTag)
                        toTag.copyField(FieldKey.ARTIST_SORT, fromTag)
                        toTag.copyField(FieldKey.COMMENT, fromTag)
                        toTag.copyField(FieldKey.COMPOSER, fromTag)
                        toTag.copyField(FieldKey.GENRE, fromTag)
                        toTag.copyField(FieldKey.TITLE, fromTag)
                        toTag.copyField(FieldKey.TRACK, fromTag)
                        toTag.copyField(FieldKey.TRACK_TOTAL, fromTag)
                        toTag.copyField(FieldKey.YEAR, fromTag)
                        toTag.setField(FieldKey.ENCODER, encoder.executable)
                        toTag.copyArtwork(fromTag)

                        toFile.tag = toTag
                        toFile.commit()
                    }
                }
                catch (e: Exception) {
                    Swing.errorMessage = "error: for '${outputFile.name}' : failed to read or write tags : ${e.message.toString()}"
                }
            }
        }
    }
}

