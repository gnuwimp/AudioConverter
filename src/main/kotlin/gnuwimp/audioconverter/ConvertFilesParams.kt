/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo

/***
 *       _____                          _   ______ _ _           _____
 *      / ____|                        | | |  ____(_) |         |  __ \
 *     | |     ___  _ ____   _____ _ __| |_| |__   _| | ___  ___| |__) |_ _ _ __ __ _ _ __ ___  ___
 *     | |    / _ \| '_ \ \ / / _ \ '__| __|  __| | | |/ _ \/ __|  ___/ _` | '__/ _` | '_ ` _ \/ __|
 *     | |___| (_) | | | \ V /  __/ |  | |_| |    | | |  __/\__ \ |  | (_| | | | (_| | | | | | \__ \
 *      \_____\___/|_| |_|\_/ \___|_|   \__|_|    |_|_|\___||___/_|   \__,_|_|  \__,_|_| |_| |_|___/
 *
 *
 */

/**
 * Parameters for converting a list of files.
 */
class ConvertFilesParams(val files: List<String>, val dest: String, val encoder: Encoders, val threads: Int, val overwrite: Constants.Overwrite) {
    var inputFiles: List<FileInfo> = listOf()
    var outputFiles: MutableList<FileInfo> = mutableListOf()

    /**
     *
     */
    fun validate() {
        val d   = FileInfo(dest)

        if (d.isDir == false && d.file.mkdirs() == false) {
            throw Exception("Error: missing destination directory => '${dest}'")
        }

        if (threads !in 1..128) {
            throw Exception("Error: invalid thread value")
        }
    }
}
