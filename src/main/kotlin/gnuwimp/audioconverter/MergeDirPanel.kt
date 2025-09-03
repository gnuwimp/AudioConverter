package gnuwimp.audioconverter

import gnuwimp.swing.*
import gnuwimp.util.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.StandardArtwork
import java.io.File
import javax.swing.*

/***
 *      __  __                     _____  _      _____                 _
 *     |  \/  |                   |  __ \(_)    |  __ \               | |
 *     | \  / | ___ _ __ __ _  ___| |  | |_ _ __| |__) |_ _ _ __   ___| |
 *     | |\/| |/ _ \ '__/ _` |/ _ \ |  | | | '__|  ___/ _` | '_ \ / _ \ |
 *     | |  | |  __/ | | (_| |  __/ |__| | | |  | |  | (_| | | | |  __/ |
 *     |_|  |_|\___|_|  \__, |\___|_____/|_|_|  |_|   \__,_|_| |_|\___|_|
 *                       __/ |
 *                      |___/
 */

/**
 * Merge (transcode) a directory of files into one.
 */
class MergeDirPanel : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val authorInput    = JTextField()
    private val authorLabel    = JLabel("Artist")
    private val channelGroup   = ButtonGroup()
    private val channelLabel   = JLabel("Channels:")
    private val channelMono    = JRadioButton("Mono")
    private val channelStereo  = JRadioButton("Stereo")
    private val commentInput   = JTextField()
    private val commentLabel   = JLabel("Comment:")
    private val convertButton  = JButton("Convert")
    private val destButton     = JButton("Browse")
    private val destInput      = JTextField()
    private val destLabel      = JLabel("Destination:")
    private val encoderCombo   = ComboBox<String>(strings = Encoders.toNames, Encoders.DEFAULT.encoderIndex)
    private val encoderLabel   = JLabel("Encoder:")
    private val gapCombo       = ComboBox<String>(strings = listOf("0", "1", "2", "3", "4", "5"), 0)
    private val gapLabel       = JLabel("Gap:")
    private val genreInput     = JTextField()
    private val genreLabel     = JLabel("Genre:")
    private val helpButton     = JButton("Help")
    private val imageButton    = JButton("Browse")
    private val imageInput     = JTextField()
    private val imageLabel     = JLabel("Cover Image:")
    private val overwriteCombo = ComboBox<String>(strings = Constants.OVERWRITE_LIST, 0)
    private val overwriteLabel = JLabel("Overwrite:")
    private val sourceButton   = JButton("Browse")
    private val sourceInput    = JTextField()
    private val sourceLabel    = JLabel("Source:")
    private val titleInput     = JTextField()
    private val titleLabel     = JLabel("Title:")
    private val yearInput      = JTextField()
    private val yearLabel      = JLabel("Year:")
    var         auto           = Constants.Auto.NO

    /**
     *
     */
    init {
        val w = 16

        add(sourceLabel,    x = 1,      y = 1, w = w,    h = 4)
        add(sourceInput,    x = w + 2,  y = 1, w = -22,  h = 4)
        add(sourceButton,   x = -20,    y = 1, w = -1,   h = 4)

        add(destLabel,      x = 1,      y = 6, w = w,    h = 4)
        add(destInput,      x = w + 2,  y = 6, w = -22,  h = 4)
        add(destButton,     x = -20,    y = 6, w = -1,   h = 4)

        add(imageLabel,     x = 1,      y = 11, w = w,   h = 4)
        add(imageInput,     x = w + 2,  y = 11, w = -22, h = 4)
        add(imageButton,    x = -20,    y = 11, w = -1,  h = 4)

        add(authorLabel,    x = 1,      y = 16, w = w,   h = 4)
        add(authorInput,    x = w + 2,  y = 16, w = -22, h = 4)
        add(helpButton,     x = -20,    y = 16, w = -1,  h = 4)

        add(titleLabel,     x = 1,      y = 21, w = w,   h = 4)
        add(titleInput,     x = w + 2,  y = 21, w = -22, h = 4)
        add(convertButton,  x = -20,    y = 21, w = -1,  h = 4)

        add(commentLabel,   x = 1,      y = 26, w = w,   h = 4)
        add(commentInput,   x = w + 2,  y = 26, w = -22, h = 4)

        add(yearLabel,      x = 1,      y = 31, w = w,   h = 4)
        add(yearInput,      x = w + 2,  y = 31, w = 30,  h = 4)

        add(genreLabel,     x = 1,      y = 36, w = w,   h = 4)
        add(genreInput,     x = w + 2,  y = 36, w = 30,  h = 4)

        add(gapLabel,       x = 1,      y = 41, w = w,   h = 4)
        add(gapCombo,       x = w + 2,  y = 41, w = 30,  h = 4)

        add(encoderLabel,   x = 1,      y = 46, w = w,   h = 4)
        add(encoderCombo,   x = w + 2,  y = 46, w = 30,  h = 4)

        add(channelLabel,   x = 1,      y = 51, w = w,   h = 4)
        add(channelMono,    x = w + 2,  y = 51, w = 15,  h = 4)
        add(channelStereo,  x = w + 17, y = 51, w = 15,  h = 4)

        add(overwriteLabel, x = 1,      y = 56, w = w,   h = 4)
        add(overwriteCombo, x = w + 2,  y = 56, w = 30,  h = 4)

        channelGroup.add(channelMono)
        channelGroup.add(channelStereo)

        authorInput.toolTipText   = "Set artist/author name."
        channelMono.toolTipText   = "Convert stereo tracks to mono."
        channelStereo.isSelected  = true
        channelStereo.toolTipText = "Keep tracks as they are, stereo or mono."
        commentInput.toolTipText  = "Set comment string (optional)."
        convertButton.toolTipText = "Start converting files."
        destInput.toolTipText     = "Select destination directory for the result file."
        encoderCombo.toolTipText  = "Select encoder."
        gapCombo.toolTipText      = "Insert silence between tracks (0 - 5 seconds)."
        genreInput.toolTipText    = "Set track genre (optional)."
        imageInput.toolTipText    = "Select cover image file (optional)."
        sourceInput.toolTipText   = "Select source directory with all audio/video files."
        titleInput.toolTipText    = "Set title/artist name."
        yearInput.toolTipText     = "Set year for the audio book (optional, 1 - 9999)."
        destButton.toolTipText    = destInput.toolTipText
        imageButton.toolTipText   = imageButton.toolTipText
        sourceButton.toolTipText  = sourceInput.toolTipText

        /**
         *
         */
        convertButton.addActionListener {
            run()
        }

        /**
         * Select destination directory.
         */
        destButton.addActionListener {
            val dialog               = JFileChooser(destInput.text.dir(Main.pref.mergeDestFile))
            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                destInput.text      = dialog.selectedFile.canonicalPath
                Main.pref.mergeDest = dialog.selectedFile.canonicalPath
            }
        }

        /**
         *
         */
        helpButton.addActionListener {
            AboutHandler(appName = Constants.HELP, aboutText = Constants.HELP_MERGE_DIR).show(parent = Main.window)
        }

        /**
         * Load cover image.
         */
        imageButton.addActionListener {
            val dialog = ImageFileDialog(imageInput.text.dir(Main.pref.mergeImageFile).canonicalPath, this)
            val file   = dialog.file

            if (file != null && file.isImage == true) {
                imageInput.text      = file.canonicalPath
                Main.pref.mergeImage = file.canonicalPath
            }
            else {
                imageInput.text = ""
            }
        }

        /**
         * Select source directory.
         */
        sourceButton.addActionListener {
            val dialog = JFileChooser(sourceInput.text.dir(Main.pref.mergeSrcFile))

            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                sourceInput.text   = dialog.selectedFile.canonicalPath
                Main.pref.mergeSrc = dialog.selectedFile.canonicalPath
            }
        }
    }

    /**
     * Parse command line arguments.
     */
    fun argLoad(args: Array<String>): Boolean {
        try {
            val src        = args.findString("--src", "")
            val dest       = args.findString("--dest", "")
            val cover      = args.findString("--cover", "")
            val author     = args.findString("--author", "")
            val artist     = args.findString("--artist", "")
            val title      = args.findString("--title", "")
            val comment    = args.findString("--comment", "")
            val year       = args.findString("--year", "")
            val genre      = args.findString("--genre", "")
            val gap        = args.findInt("--gap", 0).toInt()
            val mono       = args.find("--mono") != -1
            val encoder    = args.findInt("--encoder", Encoders.DEFAULT.encoderIndex.toLong()).toInt()
            val overwrite  = args.findString("--overwrite", "0")
            var overwrite2 = overwrite

            if (args.find("--auto2") != -1) {
                auto = Constants.Auto.YES_QUIT_ON_ERROR
            }
            else if (args.find("--auto") != -1) {
                auto = Constants.Auto.YES_STOP_ON_ERROR
            }

            if (src != "") {
                sourceInput.text = src
            }

            if (dest != "") {
                destInput.text = dest
            }

            if (cover != "") {
                imageInput.text = cover
            }

            if (author != "") {
                authorInput.text = author
            }
            else if (artist != "") {
                authorInput.text = artist
            }

            if (title != "") {
                titleInput.text = title
            }

            if (comment != "") {
                commentInput.text = comment
            }

            if (year != "") {
                yearInput.text = year
            }

            if (genre != "") {
                genreInput.text = genre
            }

            if (mono == true) {
                channelMono.isSelected = true
            }

            encoderCombo.selectedIndex = Encoders.toEncoder(encoder).encoderIndex

            if (gap < 0 || gap >= gapCombo.itemCount) {
                throw Exception("Error: invalid value for --gap ($gap)")
            }
            else {
                gapCombo.selectedIndex = gap
            }

            for ((index, choice) in Constants.OVERWRITE_LIST_IDX.withIndex()) {
                if (overwrite == choice) {
                    overwriteCombo.selectedIndex = index
                    overwrite2 = ""
                    break
                }
            }

            if (overwrite2 != "") {
                throw Exception("Error: invalid value for --overwrite ($overwrite)")
            }

            return true
        }
        catch (e: Exception) {
            if (auto == Constants.Auto.YES_QUIT_ON_ERROR) {
                println(e.message!!)
                Main.window.quit()
            }
            else {
                MessageDialog.error(e.message!!)
            }

            return false
        }
    }

    /**
     *
     */
    private fun stage1LoadFiles(): List<FileInfo> {
        if (File(sourceInput.text).isDirectory == false) {
            Exception("Error: missing source directory => '{sourceInput.text}'")
        }

        val files      = FileInfo(sourceInput.text).readDir(FileInfo.ReadDirOption.FILES)
        var audioFiles = files.filter {
            it.isAudioFile
        }

        if (audioFiles.isEmpty() == true) {
            throw Exception("Error: no audio/video files in source directory")
        }

        audioFiles = audioFiles.sortedBy {
            it.filename
        }

        return audioFiles
    }

    /**
     *
     */
    private fun stage2SetParameters(files: List<FileInfo>) : MergeParams {
        val mergeParams = MergeParams(
            audioFiles = files,
            dest       = destInput.text,
            cover      = imageInput.text,
            artist     = authorInput.text,
            album      = titleInput.text,
            year       = yearInput.text,
            comment    = commentInput.text,
            genre      = genreInput.text,
            encoder    = Encoders.toEncoder(encoderCombo.selectedIndex),
            gap        = gapCombo.text,
            mono       = channelMono.isSelected,
            overwrite  = if (overwriteCombo.selectedIndex == 1) Constants.Overwrite.OLDER else if (overwriteCombo.selectedIndex == 2) Constants.Overwrite.ALL else Constants.Overwrite.NO
        )

        mergeParams.validate()
        return mergeParams
    }

    /**
     *
     */
    private fun stage3Convert(mergeParams: MergeParams) {
        val tasks = mutableListOf<Task>(MergeTask(mergeParams = mergeParams))

        val progress = ConvertManager(
            tasks      = tasks,
            maxThreads = 1,
            onError    = TaskManager.Execution.STOP_JOIN,
            onCancel   = TaskManager.Execution.STOP_JOIN
        )

        val dialog = TaskDialog(
            taskManager = progress,
            title       = "Converting And Merging Files",
            type        = TaskDialog.Type.PERCENT,
            parent      = Main.window
        )

        dialog.enableCancel = true
        ConvertManager.clear()
        dialog.start(updateTime = 200L)
        tasks.throwFirstError()
    }

    /**
     *
     */
    private fun stage4WriteTags(mergeParams: MergeParams) {
        try {
            val track = AudioFileIO.read(mergeParams.outputFile.file)
            val tag   = track.tagOrCreateDefault

            tag.setField(FieldKey.ALBUM, mergeParams.album)
            tag.setField(FieldKey.ALBUM_ARTIST, mergeParams.artist)
            tag.setField(FieldKey.ARTIST, mergeParams.artist)
            tag.setField(FieldKey.COMMENT, mergeParams.comment)
            tag.setField(FieldKey.ENCODER, mergeParams.encoder.executable)
            tag.setField(FieldKey.GENRE, if (mergeParams.genre.isBlank() == true) MergeParams.DEFAULT_GENRE else mergeParams.genre)
            tag.setField(FieldKey.TITLE, mergeParams.album)
            tag.setField(FieldKey.TRACK, "1")
            tag.setField(FieldKey.TRACK_TOTAL, "1")

            if (mergeParams.year.numOrMinus >= 0) {
                tag.setField(FieldKey.YEAR, mergeParams.year)
            }

            if (mergeParams.cover.isNotBlank() == true) {
                tag.addField(StandardArtwork.createArtworkFromFile(File(mergeParams.cover)))
            }
            else if (mergeParams.image != null) {
                tag.addField(mergeParams.image)
            }

            track.tag = tag
            track.commit()
        }
        catch (e: Exception) {
            throw Exception("Error: failed to write tags to '${mergeParams.outputFile.name}' -> ${e.message.toString()}")
        }
    }

    /**
     * Run transcoding of files.
     */
    fun run() {
        Swing.logMessage    = ""
        Swing.errorMessage  = ""
        var file: FileInfo? = null

        try {
            val files = stage1LoadFiles()
            val parameters = stage2SetParameters(files)
            file = parameters.outputFile
            stage3Convert(parameters)
            file = null
            stage4WriteTags(parameters)

            val message = if (Swing.hasError == true) {
                "Encoding finished successfully with file:\n'${parameters.outputFile.name}'\nBut there are some errors - check the log!"
            }
            else {
                "Encoding finished successfully with file:\n'${parameters.outputFile.name}'"
            }

            Swing.logMessage = message

            if (auto != Constants.Auto.NO) {
                Main.window.quit()
            }
            else {
                MessageDialog.info(message)
            }
        }
        catch (e: FileExistException) {
            if (auto != Constants.Auto.NO) {
                println(e.message!!)
                Main.window.quit()
            }
            else {
                Swing.errorMessage = e.message!!
                MessageDialog.error(e.message!!)
            }
        }
        catch (e: Exception) {
            file?.remove()

            if (auto == Constants.Auto.YES_QUIT_ON_ERROR) {
                println(e.message!!)
                Main.window.quit()
            }
            else {
                Swing.errorMessage = e.message!!
                MessageDialog.error(e.message!!)
            }
        }
        finally {
            System.gc()
        }
    }
}
