/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo

/***
 *      _____                     _
 *     |  __ \                   | |
 *     | |  | | ___  ___ ___   __| | ___ _ __
 *     | |  | |/ _ \/ __/ _ \ / _` |/ _ \ '__|
 *     | |__| |  __/ (_| (_) | (_| |  __/ |
 *     |_____/ \___|\___\___/ \__,_|\___|_|
 *
 *
 */

/**
 * All decoding of audio/video files are done by ffmpeg.
 */
object Decoder {
    /**
     *
     */
    fun create(file: FileInfo, downmix: Boolean): List<String> {
        return if (downmix == true) {
            listOf("ffmpeg", "-loglevel", "level+quiet", "-i", file.filename, "-f", "wav", "-ac", "1", "-")
        }
        else {
            listOf("ffmpeg", "-loglevel", "level+quiet", "-i", file.filename, "-f", "wav", "-")
        }
    }
}
