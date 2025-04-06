/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo

object Decoder {
    //--------------------------------------------------------------------------
    fun create(file: FileInfo, downmix: Boolean): List<String> {
        return if (downmix == true) {
            listOf("ffmpeg", "-loglevel", "level+quiet", "-i", file.filename, "-f", "wav", "-ac", "1", "-")
        }
        else {
            listOf("ffmpeg", "-loglevel", "level+quiet", "-i", file.filename, "-f", "wav", "-")
        }
    }
}
