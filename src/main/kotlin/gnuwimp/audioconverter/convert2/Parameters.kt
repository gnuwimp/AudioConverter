/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter.convert2

import gnuwimp.audioconverter.Constants
import gnuwimp.audioconverter.Encoders
import gnuwimp.util.FileInfo

//------------------------------------------------------------------------------
class Parameters(val files: List<String>, val dest: String, val encoder: Encoders, val threads: Int, val overwrite: Constants.Overwrite) {
    var inputFiles: List<FileInfo> = listOf()
    var outputFiles: MutableList<FileInfo> = mutableListOf()

    //--------------------------------------------------------------------------
    fun validate() {
        val d   = FileInfo(dest)

        if (d.isDir == false && d.file.mkdirs() == false) {
            throw Exception("error: missing destination directory => '${dest}'")
        }

        if (threads < 0 || threads > 128) {
            throw Exception("error: invalid thread value")
        }
    }
}
