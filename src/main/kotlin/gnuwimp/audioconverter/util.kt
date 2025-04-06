package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import java.io.File

//--------------------------------------------------------------------------
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

//--------------------------------------------------------------------------
fun String.dir(def: File): File {
    val file = File(this)

    if (file.isDirectory == true) {
        return file
    }

    return def
}


//------------------------------------------------------------------------------
fun Tag.copyArtwork(from: Tag) {
    try {
        val cover = from.firstArtwork

        if (cover != null) {
            addField(cover)
        }
    }
    catch (e: Exception) {
    }
}

//------------------------------------------------------------------------------
fun Tag.copyField(field: FieldKey, from: Tag) {
    var value = ""

    try {
        value = from.getFirst(field)
    }
    catch (e: Exception) {
    }

    try {
        setField(field, value)
    }
    catch (e: Exception) {
    }
}
