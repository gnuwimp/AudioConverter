/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter.convert1

import gnuwimp.audioconverter.Constants
import gnuwimp.audioconverter.Encoders
import gnuwimp.util.FileInfo
import java.io.File

//------------------------------------------------------------------------------
class Parameters(val source: String, val dest: String, val encoder: Encoders, val threads: Int, val overwrite: Constants.Overwrite) {
    var inputFiles: List<FileInfo> = listOf()
    var outputFiles: MutableList<FileInfo> = mutableListOf()

    //--------------------------------------------------------------------------
    fun validate() {
        val s = FileInfo(source)
        val d = FileInfo(dest)
        val sc = s.canonicalPath + File.separator
        val dc = d.canonicalPath + File.separator

        if (d.isDir == false && d.file.mkdirs() == false) {
            throw Exception("error: missing destination directory => '$dest'")
        }

        when {
            s.isDir == false -> throw Exception("error: missing source directory => '$source'")
            s.isCircular == true -> throw Exception("error: start directory han an circular link")
            d.isCircular == true -> throw Exception("error: destination directory han an circular link")
            sc.indexOf(dc) == 0 -> throw Exception("error: keep source and destination directories separate")
            dc.indexOf(sc) == 0 -> throw Exception("error: keep source and destination directories separate")
            threads < 0 || threads > 128 -> throw Exception("error: invalid thread value")
        }
    }
}
