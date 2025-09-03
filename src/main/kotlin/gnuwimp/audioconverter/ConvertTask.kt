/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.Swing
import gnuwimp.util.FileInfo
import gnuwimp.util.Task
import gnuwimp.util.safeClose
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.InputStream
import java.io.OutputStream

/***
 *       _____                          _ _______        _
 *      / ____|                        | |__   __|      | |
 *     | |     ___  _ ____   _____ _ __| |_ | | __ _ ___| | __
 *     | |    / _ \| '_ \ \ / / _ \ '__| __|| |/ _` / __| |/ /
 *     | |___| (_) | | | \ V /  __/ |  | |_ | | (_| \__ \   <
 *      \_____\___/|_| |_|\_/ \___|_|   \__||_|\__,_|___/_|\_\
 *
 *
 */

/**
 * Thread task for converting audio.
 * Reading from one process and writing to another process.
 */
class ConvertTask(private val inputFile: FileInfo, private val outputFile: FileInfo, private val encoder: Encoders) :
    Task(inputFile.size) {

    /**
     * Execute in a thread.
     */
    override fun run() {
        var decoderBuilder:   ProcessBuilder?
        var decoderProcess:   Process?        = null
        var decoderInpStream: InputStream?    = null
        var encoderBuilder:   ProcessBuilder?
        var encoderProcess:   Process?        = null
        var encoderOutStream: OutputStream?   = null
        var exception                         = ""

        try {
            val decoderParams = Decoder.create(file = inputFile, downmix = false)
            val buffer        = ByteArray(size = 131_072)

            decoderBuilder   = ProcessBuilder(decoderParams)
            decoderProcess   = decoderBuilder.start()
            decoderInpStream = decoderProcess.inputStream
            message          = inputFile.filename

            Swing.logMessage = decoderParams.joinToString(separator = " ")


            while (decoderProcess.isAlive == true) {
                val read = decoderInpStream.read(buffer)

                if (read > 0) {
                    if (encoderOutStream == null) {
                        outputFile.remove()

                        val wavHeader     = WavHeader(buffer, read)
                        val encoderParams = Encoders.createEncoder(encoder, wavHeader, outputFile.filename)
                        Swing.logMessage  = encoderParams.joinToString(separator = " ")

                        encoderBuilder    = ProcessBuilder(encoderParams)
                        encoderProcess    = encoderBuilder.start()
                        encoderOutStream  = encoderProcess.outputStream

                        encoderOutStream?.buffered(131_072)
                        encoderOutStream?.write(buffer, wavHeader.data, read - wavHeader.data)
                    }
                    else {
                        encoderOutStream.write(buffer, 0, read)
                    }

                    ConvertManager.add(read.toLong())
                }

                if (abort == true) {
                    throw Exception(Constants.CANCEL_ERROR)
                }
            }

            decoderInpStream.safeClose()
            decoderProcess.waitFor()

            decoderInpStream = null
            decoderProcess   = null
        }
        catch (e: Exception) {
            exception = e.message.toString()
        }
        finally {
            progress += inputFile.size

            decoderInpStream?.safeClose()
            encoderOutStream?.safeClose()

            if (decoderProcess?.isAlive == true) {
                decoderProcess.waitFor()
            }

            if (encoderProcess?.isAlive == true) {
                encoderProcess.waitFor()
            }

            if (encoderProcess != null && encoderProcess.exitValue() != 0) {
                exception = "Error: encoder failed, exit code=${encoderProcess.exitValue()} - $exception"
            }
            else if (decoderProcess != null && decoderProcess.exitValue() != 0) {
                exception = "Error: decoder failed, exit code=${decoderProcess.exitValue()} - $exception"
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
                    Swing.errorMessage = "Error: for '${outputFile.name}' : failed to read or write tags : ${e.message.toString()}"
                }
            }
        }
    }
}

