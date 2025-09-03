/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

/***
 *       _____                _              _
 *      / ____|              | |            | |
 *     | |     ___  _ __  ___| |_ __ _ _ __ | |_ ___
 *     | |    / _ \| '_ \/ __| __/ _` | '_ \| __/ __|
 *     | |___| (_) | | | \__ \ || (_| | | | | |_\__ \
 *      \_____\___/|_| |_|___/\__\__,_|_| |_|\__|___/
 *
 *
 */

object Constants {
    const val ABOUT_APP          = "About AudioConverter"
    const val APP_NAME           = "AudioConverter"
    const val CANCEL_ERROR       = "user abort!"
    const val HELP               = "Help"
    const val LOGBUTTON_TOOLTIP  = "Show log window with all tasks that have been executed."
    val       OVERWRITE_LIST     = listOf("Don't overwrite existing files", "Overwrite older files", "Overwrite all")
    val       OVERWRITE_LIST_IDX = listOf("0", "1", "2")
    val       THREAD_LIST        = listOf("1", "2", "3", "4", "5", "6", "7", "8", "12", "16", "24", "32", "48", "64", "96", "128")

    /**
     * Running from command line options.
     */
    enum class Auto {
        NO,
        YES_STOP_ON_ERROR,
        YES_QUIT_ON_ERROR
    }

    /**
     * Channels in wav.
     */
    enum class Channels {
        INVALID,
        MONO,
        STEREO,
    }

    /**
     *
     */
    enum class Overwrite {
        NO,
        OLDER,
        ALL
    }

    val HELP_MERGE_DIR: String = "<html>" +
            "<b>Transcode and merge all audio/video files in a directory into one audio file</b><br>" +
            "<br>" +

            "Select a source directory with audio or video files.<br>" +
            "Sub directories are not used.<br>" +
            "Select encoder.<br>" +
            "Use mono option to force stereo tracks to be converted to mono.<br>" +
            "Then convert.<br>" +
            "<br>" +

            "Files must have names that make them sorted in playing order.<br>" +
            "All input files must have same audio properties (<i>mono/stereo/samplerate/bitwidth</i>).<br>" +
            "If tags are omitted it will try to read missing tags from the first file or it will abort." +
            "<br>" +

            "The finished audio file will end up in the destination directory.<br>" +
            "With <i>artist - title (year).encoder_extension</i> or <i>artist - title.encoder_extension</i> as file name<br>" +
            "<br>" +

            "<b>Command line arguments</b><br>" +
            "Probably best to use only ascii characters on Windows.<br>" +
            "Wrap strings that contain spaces with double quotes (\"/my path/to files\").<br>" +
            "<pre>" +
            "--src  [PATH]              source directory with audio files\n" +
            "--dest [PATH]              destination directory for target file\n" +
            "--artist [TEXT]            artist name\n" +
            "--title [TEXT]             album and title name\n" +
            "--comment [TEXT]           comment string (optional)\n" +
            "--cover [PATH]             track cover image (optional)\n" +
            "--year [YYYY]              track year (optional, 1 - 9999)\n" +
            "--genre [TEXT]             genre string (optional, default ${MergeParams.DEFAULT_GENRE})\n" +
            "--gap [SECONDS]            insert silence between tracks (optional, default 0)\n" +
            "                             valid values are: 0 - 5\n" +
            "--mono                     downmix stereo to mono (optional)\n" +
            "--encoder [INDEX]          index in encoder list (optional, default ${Encoders.DEFAULT.ordinal} -> MP3 CBR 128 Kbps)\n" +
            "--overwrite [VALUE]        overwrite destination files (optional, default 0)\n" +
            "                             valid values are: 0 dont overwrite, 1 overwrite older, 2 overwrite all\n" +
            Encoders.toHelp +
            "--auto                     start automatically and quit after successful encoding (optional)\n" +
            "--auto2                    start automatically and quit even for error (optional)\n" +
            "<br>" +
            "<br>" +
            "<br>" +
            "</pre>" +
            "</html>"

    const val HELP_MERGE_FILES: String = "<html>" +
            "<b>Transcode and merge all selected audio/video files into one audio file</b><br>" +
            "<br>" +

            "Select files.<br>" +
            "Select destination directory.<br>" +
            "Select encoder.<br>" +
            "Use mono option to force stereo tracks to be converted to mono.<br>" +
            "Then convert.<br>" +
            "<br>" +

            "All input files must have same audio properties (<i>mono/stereo/samplerate/bitwidth</i>).<br>" +
            "If tags are omitted it will try to read missing tags from the first file or it will abort.<br>" +
            "<br>" +

            "The finished audio file will end up in the destination directory.<br>" +
            "With <i>artist - title (year).encoder_extension</i> or <i>artist - title.encoder_extension</i> as file name<br>" +
            "<br>" +
            "</html>"

    val HELP_CONVERT_DIR: String = "<html>" +
            "<b>Transcode all audio files in a directory tree to separate files</b><br>" +
            "<br>" +

            "Select root start directory.<br>" +
            "Select root destination directory (must be outside of start directory).<br>" +
            "Destination directories will be created to mirror source directories.<br>" +
            "Select encoder.<br>" +
            "Set number of threads.<br>" +
            "Then convert.<br>" +
            "<br>" +

            "It will stop for any decoding/encoding error.<br>" +
            "But tag writing errors will be logged only.<br>" +
            "<br>" +

            "All destination files that already exist will be excluded unless overwrite option is turned on.<br>" +
            "So you can stop the encoding and restart at a later point and it will continue where you left off.<br>" +
            "The most common tags (if possible) will be copied including cover art.<br>" +
            "<br>" +

            "<b>Command line arguments</b><br>" +
            "<pre>" +
            "--mode2                    set this mode\n" +
            "--src [PATH]               root directory with audio files\n" +
            "--dest [PATH]              destination directory for target file\n" +
            "--threads [COUNT]          set number of threads to use (optional, default 1)\n" +
            "                             valid values are: 1, 2, 3, 4, 5, 6, 7, 8, 12, 16, 24, 32, 48, 64, 96, 128\n" +
            "--encoder [INDEX]          index in encoder list (optional, default ${Encoders.DEFAULT.ordinal} -> MP3 CBR 128 Kbps)\n" +
            "--overwrite [VALUE]        overwrite destination files (optional, default 0)\n" +
            "                             valid values are: 0 dont overwrite, 1 overwrite older, 2 overwrite all\n" +
            Encoders.toHelp +
            "--auto                     start automatically and quit after successful encoding (optional)\n" +
            "--auto2                    start automatically and quit even for error (optional)\n" +
            "<br>" +
            "<br>" +
            "<br>" +
            "</pre>" +
            "</html>"

    const val HELP_CONVERT_FILES: String = "<html>" +
            "<b>Transcode selected audio/video files to separate audio files</b><br>" +
            "<br>" +

            "Select files.<br>" +
            "Select destination directory.<br>" +
            "Select encoder.<br>" +
            "Set number of threads.<br>" +
            "Then convert.<br>" +
            "<br>" +

            "Files with same name from different directories will not work.<br>" +
            "All audio files will be saved to the same destination directory.<br>" +
            "It will stop for any decoding/encoding error.<br>" +
            "But tag writing errors will be logged only.<br>" +
            "<br>" +

            "All destination files that already exist will be excluded unless overwrite option is turned on.<br>" +
            "The most common tags (if possible) will be copied including cover art.<br>" +
            "<br>" +
            "</html>"

    //----------------------------------------------------------------------
    fun aboutApp(): String {
        var about = "<html>" +

        "<b>AudioConverter 3.01</b><br>" +
        "<br>" +

        "Copyright 2021 - 2025 gnuwimp@gmail.com.<br>" +
        "Released under the GNU General Public License v3.0.<br>" +
        "See <a href=\"https://github.com/gnuwimp/AudioConverter\">https://github.com/gnuwimp/AudioConverter</a>.<br>" +
        "Use AudioConverter with caution and at your own risk.<br>" +
        "<br>" +

        "<b>About</b><br>" +
        "This program transcodes audio and video files to mp3/ogg/aac audio files.<br>" +
        "For best audio quality do use lossless files format for the source.<br>" +
        "Such as wav or flac.<br>" +
        "<br>" +

        "First mode transcodes all audio/video files in one directory into one file.<br>" +
        "Second mode transcodes a list of files into one file.<br>" +
        "Third mode transcodes all files that are found in a directory tree to separate files.<br>" +
        "Fourth mode transcodes a list of files to separate files.<br>" +
        "<br>" +

        "<b>Requirements</b><br>" +
        "Ffmpeg for decoding mp3/flac/wav/ogg/m4a/aac/mp4/avi/mkv files.<br>" +
        "Lame for encoding mp3 files.<br>" +
        "Oggenc for encoding ogg files.<br>" +
        "Qaac64 for encoding aac files.<br>" +
        "Faac for encoding aac files (lower quality than Qaac64).<br>" +
        "Flac for encoding flac files.<br>" +
        "<br>" +

        "Download ffmpeg from <a href=\"https://www.ffmpeg.org\">https://www.ffmpeg.org</a>.<br>" +
        "Download lame from <a href=\"https://lame.sourceforge.net\">https://lame.sourceforge.net</a>.<br>" +
        "Download oggenc from <a href=\"https://www.xiph.org/ogg\">https://www.xiph.org/ogg</a>.<br>" +
        "Download qaac64 from <a href=\"https://github.com/nu774/qaac\">https://github.com/nu774/qaac</a>.<br>" +
        "Download faac from <a href=\"https://github.com/knik0/faac\">https://github.com/knik0/faac</a>.<br>" +
        "Download flac from <a href=\"https://github.com/xiph/flac\">https://github.com/xiph/flac</a>.<br>" +
        "<br>" +

        "<b>Following third party software library are used</b><br>" +
        "JAudioTagger - <a href=\"http://www.jthink.net/jaudiotagger\">http://www.jthink.net/jaudiotagger</a><br>" +
        "<br>" +

        "<b>Versions</b><br>"

        about += "Java: " + System.getProperty("java.version") + "<br>"
        about += "Kotlin: " + KotlinVersion.CURRENT + "<br>"
        about += "JAudioTagger: 3.0.2-SNAPSHOT<br>"
        "<br>" +
        "<br>" +
        "<br>" +
        "<html>"

        return about
    }
}
