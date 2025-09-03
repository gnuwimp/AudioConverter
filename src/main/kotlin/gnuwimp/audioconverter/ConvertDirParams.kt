/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import java.io.File

/***
 *       _____                          _   _____  _      _____
 *      / ____|                        | | |  __ \(_)    |  __ \
 *     | |     ___  _ ____   _____ _ __| |_| |  | |_ _ __| |__) |_ _ _ __ __ _ _ __ ___  ___
 *     | |    / _ \| '_ \ \ / / _ \ '__| __| |  | | | '__|  ___/ _` | '__/ _` | '_ ` _ \/ __|
 *     | |___| (_) | | | \ V /  __/ |  | |_| |__| | | |  | |  | (_| | | | (_| | | | | | \__ \
 *      \_____\___/|_| |_|\_/ \___|_|   \__|_____/|_|_|  |_|   \__,_|_|  \__,_|_| |_| |_|___/
 *
 *
 */

/**
 * Parameters for converting all tracks in a directory.
 */
class ConvertDirParams(val source: String, val dest: String, val encoder: Encoders, val threads: Int, val overwrite: Constants.Overwrite) {
    var inputFiles: List<FileInfo> = listOf()
    var outputFiles: MutableList<FileInfo> = mutableListOf()

    /**
     *
     */
    fun validate() {
        val s  = FileInfo(source)
        val d  = FileInfo(dest)
        val sc = s.canonicalPath + File.separator
        val dc = d.canonicalPath + File.separator

        if (d.isDir == false && d.file.mkdirs() == false) {
            throw Exception("Error: missing destination directory => '$dest'")
        }

        when {
            s.isDir == false -> throw Exception("Error: missing source directory => '$source'")
            s.isCircular == true -> throw Exception("Error: start directory han an circular link")
            d.isCircular == true -> throw Exception("Error: destination directory han an circular link")
            sc.indexOf(dc) == 0 -> throw Exception("Error: keep source and destination directories separate")
            dc.indexOf(sc) == 0 -> throw Exception("Error: keep source and destination directories separate")
            threads !in 1..128 -> throw Exception("Error: invalid thread value")
        }
    }
}
