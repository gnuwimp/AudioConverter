package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import java.io.File

/***
 *      ______ _ _      _____        __
 *     |  ____(_) |    |_   _|      / _|
 *     | |__   _| | ___  | |  _ __ | |_ ___
 *     |  __| | | |/ _ \ | | | '_ \|  _/ _ \
 *     | |    | | |  __/_| |_| | | | || (_) |
 *     |_|    |_|_|\___|_____|_| |_|_| \___/
 *
 *
 */

/**
 * Parse file extension for valid input files.
 */
val FileInfo.isAudioFile: Boolean
    get() {
        if (file.isFile == false) {
            return false
        }

        return when(ext.lowercase()) {
            "mp3"-> true
            "aac" -> true
            "m4a" -> true
            "m4b" -> true
            "flac" -> true
            "ogg" -> true
            "wav" -> true
            "avi" -> true
            "mkv" -> true
            "mp4" -> true
            else -> false
        }
    }

/***
 *       _____ _        _
 *      / ____| |      (_)
 *     | (___ | |_ _ __ _ _ __   __ _
 *      \___ \| __| '__| | '_ \ / _` |
 *      ____) | |_| |  | | | | | (_| |
 *     |_____/ \__|_|  |_|_| |_|\__, |
 *                               __/ |
 *                              |___/
 */

/**
 *
 */
fun String.dir(def: File): File {
    val file = File(this)

    if (file.isDirectory == true) {
        return file
    }

    return def
}

/***
 *      _______
 *     |__   __|
 *        | | __ _  __ _
 *        | |/ _` |/ _` |
 *        | | (_| | (_| |
 *        |_|\__,_|\__, |
 *                  __/ |
 *                 |___/
 */

/**
 *
 */
fun Tag.copyArtwork(from: Tag) {
    try {
        val cover = from.firstArtwork

        if (cover != null) {
            addField(cover)
        }
    }
    catch (_: Exception) {
    }
}

/**
 *
 */
fun Tag.copyField(field: FieldKey, from: Tag) {
    var value = ""

    try {
        value = from.getFirst(field)
    }
    catch (_: Exception) {
    }

    try {
        setField(field, value)
    }
    catch (_: Exception) {
    }
}
