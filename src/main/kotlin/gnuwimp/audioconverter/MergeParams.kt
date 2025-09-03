package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import gnuwimp.util.isImage
import gnuwimp.util.numOrZero
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.Artwork
import java.io.File

/***
 *      __  __                     _____
 *     |  \/  |                   |  __ \
 *     | \  / | ___ _ __ __ _  ___| |__) |_ _ _ __ __ _ _ __ ___  ___
 *     | |\/| |/ _ \ '__/ _` |/ _ \  ___/ _` | '__/ _` | '_ ` _ \/ __|
 *     | |  | |  __/ | | (_| |  __/ |  | (_| | | | (_| | | | | | \__ \
 *     |_|  |_|\___|_|  \__, |\___|_|   \__,_|_|  \__,_|_| |_| |_|___/
 *                       __/ |
 *                      |___/
 */

/**
 * Parameters for transcoding many files into one.
 */
class MergeParams(val audioFiles: List<FileInfo>, val dest: String, val cover: String, var artist: String, var album: String, var year: String, val comment: String, var genre: String, val encoder: Encoders, val gap: String, val mono: Boolean, val overwrite: Constants.Overwrite) {
    var image: Artwork? = null

    /**
     *
     */
    companion object {
        const val DEFAULT_GENRE = "Audiobook"
    }

    /**
     *
     */
    val outputFile: FileInfo
        get() = FileInfo(outputFileName)

    /**
     *
     */
    val outputFileName: String
        get() {
            val name = FileInfo.safeName("$artist - $album")
            val tmp  = if (dest.endsWith(File.separator) == true) dest else dest + File.separator

            return "$tmp$name ($year).${encoder.fileExt}"
        }

    /**
     *
     */
    fun validate() {
        val d = FileInfo(dest)

        if (d.isDir == false && d.file.mkdirs() == false) {
            throw Exception("Error: missing destination directory => '$dest'")
        }

        try {
            val file0 = AudioFileIO.read(audioFiles[0].file)
            val tag0  = file0.tag

            if (tag0 != null) {
                if (artist.isBlank() == true) {
                    artist = tag0.getFirst(FieldKey.ARTIST)
                }

                if (album.isBlank() == true) {
                    album = tag0.getFirst(FieldKey.ALBUM)
                }

                if (genre.isBlank() == true) {
                    genre = tag0.getFirst(FieldKey.GENRE)
                }

                if (year.isBlank() == true) {
                    year = tag0.getFirst(FieldKey.YEAR)

                    if (year.length > 4) {
                        year = year.substring(0, 4)
                    }
                }

                image = tag0.firstArtwork
            }
        }
        catch (_: Exception) {
        }

        when {
            cover.isNotBlank() && File(cover).isImage == false -> throw Exception("Error: image cover file is not an valid image")
            artist.isBlank() -> throw Exception("Error: artist/author string is empty")
            album.isBlank() -> throw Exception("Error: title string is empty")
            year != "" && (year.numOrZero !in 1..9999) -> throw Exception("Error: year is out of range $year (1 - 9999)")
        }

        val out_file = FileInfo(outputFileName)

        if (overwrite == Constants.Overwrite.NO && out_file.isFile == true) {
            throw Exception("Error: destination file exist!\n${out_file.filename}")
        }

        if (overwrite == Constants.Overwrite.OLDER) {
            var count = 0

            for (file in audioFiles) {
                val in_file  = FileInfo(file.filename)
                count += if (out_file.mod < in_file.mod) 1 else 0

            }

            if (count == 0) {
                throw FileExistException("Error: destination file is newer than the input files!\n${out_file.filename}")
            }
        }
    }
}
