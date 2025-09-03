package gnuwimp.audioconverter

import gnuwimp.swing.Swing
import gnuwimp.util.*
import java.io.InputStream
import java.io.OutputStream

/***
 *      __  __                  _______        _
 *     |  \/  |                |__   __|      | |
 *     | \  / | ___ _ __ __ _  ___| | __ _ ___| | __
 *     | |\/| |/ _ \ '__/ _` |/ _ \ |/ _` / __| |/ /
 *     | |  | |  __/ | | (_| |  __/ | (_| \__ \   <
 *     |_|  |_|\___|_|  \__, |\___|_|\__,_|___/_|\_\
 *                       __/ |
 *                      |___/
 */

/**
 * Thread task for transcoding many tracks into one.
 */
class MergeTask(val mergeParams: MergeParams) : Task(max = mergeParams.audioFiles.sumByLong(FileInfo::size)) {
    /**
     *
     */
    override fun run() {
        var decoderBuilder: ProcessBuilder?
        var decoderProcess: Process?        = null
        var decoderStream:  InputStream?    = null
        var encoderBuilder: ProcessBuilder?
        var encoderProcess: Process?        = null
        var encoderStream:  OutputStream?   = null
        var wavHeader                       = WavHeader()
        var exception                       = ""

        try {
            var gap: ByteArray? = null

            for (file in mergeParams.audioFiles) {
                val buffer        = ByteArray(size = 131_072)
                val decoderParams = Decoder.create(file, mergeParams.mono)
                var parseHeader   = true
                message           = "${file.filename}\n${mergeParams.outputFileName}"

                Swing.logMessage = decoderParams.joinToString(separator = " ")

                decoderBuilder = ProcessBuilder(decoderParams)
                decoderProcess = decoderBuilder.start()
                decoderStream  = decoderProcess.inputStream

                while (decoderProcess.isAlive == true) {
                    val read = decoderStream.read(buffer)

                    if (read > 0) {
                        ConvertManager.add(read.toLong())

                        if (encoderProcess == null) {
                            mergeParams.outputFile.remove()

                            wavHeader         = WavHeader(buffer, read)
                            val encoderParams = Encoders.createEncoder(mergeParams.encoder, wavHeader, mergeParams.outputFile.filename)
                            Swing.logMessage  = encoderParams.joinToString(separator = " ")
                            encoderBuilder    = ProcessBuilder(encoderParams)
                            encoderProcess    = encoderBuilder.start()
                            encoderStream     = encoderProcess.outputStream
                            parseHeader       = false
                            val seconds       = mergeParams.gap.numOrZero.toInt()

                            if (seconds != 0) {
                                gap = ByteArray(size = (wavHeader.sampleRate * wavHeader.channels.ordinal * 2 * seconds))
                            }

                            encoderStream?.write(buffer, wavHeader.data, read - wavHeader.data)
                        }
                        else if (parseHeader == true) {
                            val currentHeader = WavHeader(buffer, read)

                            if (currentHeader.sampleRate != wavHeader.sampleRate || currentHeader.channels != wavHeader.channels || currentHeader.bitWidth != wavHeader.bitWidth) {
                                throw Exception("Error: channels or samplerate or bitwidth are different for these tracks\n${mergeParams.audioFiles[0].name} (${wavHeader.sampleRateString} Khz, ${wavHeader.channelString}, ${wavHeader.bitWidth} bit)\n${file.name} (${currentHeader.sampleRateString} Khz, ${currentHeader.channelString}, ${currentHeader.bitWidth} bit)")
                            }

                            encoderStream?.write(buffer, currentHeader.data, read - currentHeader.data)
                            parseHeader = false
                        }
                        else {
                            encoderStream?.write(buffer, 0, read)
                        }
                    }

                    if (abort == true) {
                        throw Exception(Constants.CANCEL_ERROR)
                    }
                }

                if (gap != null && file != mergeParams.audioFiles.last()) {
                    encoderStream?.write(gap, 0, gap.size)
                }

                progress += file.size

                decoderStream.safeClose()
                decoderProcess.waitFor()

                if (decoderProcess.exitValue() != 0) {
                    throw Exception("")
                }

                decoderStream  = null
                decoderProcess = null
            }
        }
        catch (e: Exception) {
            exception = e.message.toString()
        }
        finally {
            decoderStream?.safeClose()
            encoderStream?.safeClose()

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
                throw Exception(exception)
            }
        }
    }
}
