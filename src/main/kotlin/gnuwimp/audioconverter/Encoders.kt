/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo

/***
 *      ______                     _
 *     |  ____|                   | |
 *     | |__   _ __   ___ ___   __| | ___ _ __ ___
 *     |  __| | '_ \ / __/ _ \ / _` |/ _ \ '__/ __|
 *     | |____| | | | (_| (_) | (_| |  __/ |  \__ \
 *     |______|_| |_|\___\___/ \__,_|\___|_|  |___/
 *
 *
 */

/**
 * All encoder arguments are set here.
 */
enum class Encoders(index: Int, name: String, ext: String, exe: String, vararg params: String) {
    MP3_CBR_32(0, "MP3 CBR 32 Kbps", "mp3", "lame", "-b", "32", "--cbr"),
    MP3_CBR_48(1, "MP3 CBR 48 Kbps", "mp3", "lame", "-b", "48", "--cbr"),
    MP3_CBR_64(2, "MP3 CBR 64 Kbps", "mp3", "lame", "-b", "64", "--cbr"),
    MP3_CBR_96(3, "MP3 CBR 96 Kbps", "mp3", "lame", "-b", "96", "--cbr"),
    MP3_CBR_128(4, "MP3 CBR 128 Kbps", "mp3", "lame", "-b", "128", "--cbr"),
    MP3_CBR_192(5, "MP3 CBR 192 Kbps", "mp3", "lame", "-b", "192", "--cbr"),
    MP3_CBR_256(6, "MP3 CBR 256 Kbps", "mp3", "lame", "-b", "256", "--cbr"),
    MP3_CBR_320(7, "MP3 CBR 320 Kbps", "mp3", "lame", "--preset", "insane"),

    MP3_VBR_160(8, "MP3 VBR ~160 Kbps", "mp3", "lame", "--preset", "medium"),
    MP3_VBR_190(9, "MP3 VBR ~190 Kbps", "mp3", "lame", "--preset", "standard"),
    MP3_VBR_240(10, "MP3 VBR ~240 Kbps", "mp3", "lame", "--preset", "extreme"),

    OGG_45(11, "Ogg ~45 Kbps", "ogg", "oggenc", "-q-1"),
    OGG_64(12, "Ogg ~64 Kbps", "ogg", "oggenc", "-q0"),
    OGG_96(13, "Ogg ~96 Kbps", "ogg", "oggenc", "-q2"),
    OGG_128(14, "Ogg ~128 Kbps", "ogg", "oggenc", "-q4"),
    OGG_192(15, "Ogg ~192 Kbps", "ogg", "oggenc", "-q6"),
    OGG_256(16, "Ogg ~256 Kbps", "ogg", "oggenc", "-q8"),
    OGG_320(17, "Ogg ~320 Kbps", "ogg", "oggenc", "-q9"),
    OGG_500(18, "Ogg ~500 Kbps", "ogg", "oggenc", "-q10"),

    QAAC_CBR_48(19, "Q-AAC HE/CBR 48 Kbps", "m4a", "qaac64", "--he", "--cbr", "48"),
    QAAC_CBR_80(20, "Q-AAC HE/CBR 80 Kbps", "m4a", "qaac64", "--he", "--cbr", "80"),
    QAAC_CVBR_96(21, "Q-AAC CVBR ~96 Kbps", "m4a", "qaac64", "--cvbr", "96"),
    QAAC_TVBR_128(22, "Q-AAC TVBR63 ~128 Kbps", "m4a", "qaac64", "--tvbr", "63"),
    QAAC_TVBR_192(23, "Q-AAC TVBR91 ~192 Kbps", "m4a", "qaac64", "--tvbr", "91"),
    QAAC_TVBR_256(24, "Q-AAC TVBR109 ~256 Kbps", "m4a", "qaac64", "--tvbr", "109"),
    QAAC_TVBR_320(25, "Q-AAC TVBR127 ~320 Kbps", "m4a", "qaac64", "--tvbr", "127"),
    QAAC_ALAC(26, "Q-AAC ALAC", "m4a", "qaac64", "--alac"),

    FAAC_64(27, "F-AAC ABR ~64 Kbps", "m4a", "faac", "-b", "64"),
    FAAC_128(28, "F-AAC ABR ~128 Kbps", "m4a", "faac", "-b", "128"),
    FAAC_192(29, "F-AAC ABR ~192 Kbps", "m4a", "faac", "-b", "192"),
    FAAC_256(30, "F-AAC ABR ~256 Kbps", "m4a", "faac", "-b", "256"),
    FAAC_320(31, "F-AAC ABR ~320 Kbps", "m4a", "faac", "-b", "320"),

    FLAC(32, "FLAC", "flac", "flac");

    var encoderIndex = index
    var encoderName  = name
    var executable   = exe
    val encoderArg   = mutableListOf<String>()
    var fileExt      = ext

    /**
     *
     */
    init {
        params.forEach {
            encoderArg.add(it)
        }
    }
    companion object {
        private val LAST = FLAC
        val DEFAULT = MP3_CBR_128

        /**
         * Create encoder.
         */
        fun createEncoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            return when (encoder.executable) {
                "lame" -> {
                    createLameEncoder(encoder, wavHeader, outName)
                }
                "faac" -> {
                    createFaacEncoder(encoder, wavHeader, outName)
                }
                "qaac64" -> {
                    createQaac64Encoder(encoder, wavHeader, outName)
                }
                "oggenc" -> {
                    createOggEncEncoder(encoder, wavHeader, outName)
                }
                "flac" -> {
                    createFlacEncoder(encoder, wavHeader, outName)
                }
                else -> {
                    throw Exception("Error: unknown encoder executable - ${encoder.executable}")
                }
            }
        }

        /**
         *
         */
        private fun createFaacEncoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            val list = mutableListOf<String>()

            list.add(encoder.executable)
            list.add("-v0")
            list.add("-P")
            list.add("-X")
            list.add("-C")

            when (wavHeader.channels) {
                Constants.Channels.MONO -> {
                    list.add("1")
                }
                Constants.Channels.STEREO -> {
                    list.add("2")
                }
                else -> {
                    throw Exception("internal error")
                }
            }

            list.add("-R")
            list.add(wavHeader.sampleRateString2)

            list.add("-B")
            list.add("${wavHeader.bitWidth}")

            encoder.encoderArg.forEach {
                list.add(it)
            }

            list.add("-o")
            list.add(outName)
            list.add("-")

            return list
        }

        /**
         *
         */
        private fun createFlacEncoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            val list = mutableListOf<String>()

            list.add(encoder.executable)
            list.add("--totally-silent")
            list.add("--force-raw-format")
            list.add("--endian=little")
            list.add("--sign=signed")
            list.add("--sample-rate=${wavHeader.sampleRateString2}")
            list.add("--bps=${wavHeader.bitWidth}")

            when (wavHeader.channels) {
                Constants.Channels.MONO -> {
                    list.add("--channels=1")
                }
                Constants.Channels.STEREO -> {
                    list.add("--channels=2")
                }
                else -> {
                    throw Exception("internal error")
                }
            }

            encoder.encoderArg.forEach {
                list.add(it)
            }

            list.add("-")
            list.add("-o")
            list.add(outName)

            return list
        }

        /**
         *
         */
        private fun createLameEncoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            val list = mutableListOf<String>()

            list.add(encoder.executable)
            list.add("--quiet")

            if (wavHeader.channels == Constants.Channels.MONO) {
                list.add("-m")
                list.add("m")
            }

            list.add("-r")
            list.add("-s")
            list.add(wavHeader.sampleRateString)

            list.add("--bitwidth")
            list.add("${wavHeader.bitWidth}")

            encoder.encoderArg.forEach {
                list.add(it)
            }

            list.add("-")
            list.add(outName)

            return list
        }

        /**
         *
         */
        private fun createOggEncEncoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            val list = mutableListOf<String>()

            list.add(encoder.executable)
            list.add("--quiet")

            list.add("-r")

            list.add("-B")
            list.add("${wavHeader.bitWidth}")

            list.add("-C")
            list.add("${wavHeader.channels.ordinal}")

            list.add("-R")
            list.add("${wavHeader.sampleRate}")

            encoder.encoderArg.forEach {
                list.add(it)
            }

            list.add("-o")
            list.add(outName)
            list.add("-")

            return list
        }

        /**
         *
         */
        private fun createQaac64Encoder(encoder: Encoders, wavHeader: WavHeader, outName: String): List<String> {
            val list = mutableListOf<String>()

            if (FileInfo.isLinux == true) {
                list.add("wine")
                list.add(FileInfo.homedir.filename + "/.wine/drive_c/windows/" + encoder.executable + ".exe")
            }
            else {
                list.add(encoder.executable)
            }

            list.add("--silent")
            list.add("--raw")
            list.add("--raw-channels")

            when (wavHeader.channels) {
                Constants.Channels.MONO -> {
                    list.add("1")
                }
                Constants.Channels.STEREO -> {
                    list.add("2")
                }
                else -> {
                    throw Exception("internal error")
                }
            }

            list.add("--raw-rate")
            list.add(wavHeader.sampleRateString2)

            list.add("--raw-format")
            list.add("S${wavHeader.bitWidth}L")

            encoder.encoderArg.forEach {
                list.add(it)
            }

            list.add("-")
            list.add("-o")
            list.add(outName)

            return list
        }

        /**
         *
         */
        fun toEncoder(index: Int): Encoders {
            return when(index) {
                MP3_CBR_32.encoderIndex -> MP3_CBR_32
                MP3_CBR_48.encoderIndex -> MP3_CBR_48
                MP3_CBR_64.encoderIndex -> MP3_CBR_64
                MP3_CBR_96.encoderIndex -> MP3_CBR_96
                MP3_CBR_128.encoderIndex -> MP3_CBR_128
                MP3_CBR_192.encoderIndex -> MP3_CBR_192
                MP3_CBR_256.encoderIndex -> MP3_CBR_256
                MP3_CBR_320.encoderIndex -> MP3_CBR_320
                MP3_VBR_160.encoderIndex -> MP3_VBR_160
                MP3_VBR_190.encoderIndex -> MP3_VBR_190
                MP3_VBR_240.encoderIndex  -> MP3_VBR_240
                OGG_45.encoderIndex -> OGG_45
                OGG_64.encoderIndex -> OGG_64
                OGG_96.encoderIndex -> OGG_96
                OGG_128.encoderIndex -> OGG_128
                OGG_192.encoderIndex -> OGG_192
                OGG_256.encoderIndex -> OGG_256
                OGG_320.encoderIndex -> OGG_320
                OGG_500.encoderIndex -> OGG_500
                QAAC_CBR_48.encoderIndex -> QAAC_CBR_48
                QAAC_CBR_80.encoderIndex -> QAAC_CBR_80
                QAAC_CVBR_96.encoderIndex -> QAAC_CVBR_96
                QAAC_TVBR_128.encoderIndex -> QAAC_TVBR_128
                QAAC_TVBR_192.encoderIndex -> QAAC_TVBR_192
                QAAC_TVBR_256.encoderIndex -> QAAC_TVBR_256
                QAAC_TVBR_320.encoderIndex -> QAAC_TVBR_320
                QAAC_ALAC.encoderIndex -> QAAC_ALAC
                FAAC_64.encoderIndex -> FAAC_64
                FAAC_128.encoderIndex -> FAAC_128
                FAAC_192.encoderIndex -> FAAC_192
                FAAC_256.encoderIndex -> FAAC_256
                FAAC_320.encoderIndex -> FAAC_320
                FLAC.encoderIndex -> FLAC
                else -> throw Exception("Error: encoder index is out of range ($index)\nvalid values are from 0 to ${LAST.encoderIndex}")
            }
        }

        /**
         * Create help text.
         */
        val toHelp: String
            get() {
                var res = ""

                for (f in 0 .. LAST.encoderIndex) {
                    val e = toEncoder(f)
                    res += "                             ${e.encoderIndex} = ${e.encoderName}\n"
                }

                return res
            }

        /**
         * Create list of encoder names.
         */
        val toNames: List<String>
            get() {
            val res = mutableListOf<String>()

            for (f in 0 .. LAST.encoderIndex) {
                res.add(toEncoder(f).encoderName)
            }

            return res
        }
    }
}
