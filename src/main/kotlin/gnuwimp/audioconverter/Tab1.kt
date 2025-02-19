/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.*
import gnuwimp.util.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.StandardArtwork
import java.io.File
import javax.swing.*

//------------------------------------------------------------------------------
@Suppress("UNUSED_VALUE")
class Tab1 : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val sourceLabel    = JLabel("Source:")
    private val sourceInput    = JTextField()
    private val sourceButton   = JButton("Browse")
    private val destLabel      = JLabel("Destination:")
    private val destInput      = JTextField()
    private val destButton     = JButton("Browse")
    private val imageLabel     = JLabel("Cover Image:")
    private val imageInput     = JTextField()
    private val imageButton    = JButton("Browse")
    private val authorLabel    = JLabel("Artist")
    private val authorInput    = JTextField()
    private val titleLabel     = JLabel("Title:")
    private val titleInput     = JTextField()
    private val commentLabel   = JLabel("Comment:")
    private val commentInput   = JTextField()
    private val yearLabel      = JLabel("Year:")
    private val yearInput      = JTextField()
    private val genreLabel     = JLabel("Genre:")
    private val genreInput     = JTextField()
    private val encoderLabel   = JLabel("Encoder:")
    private val encoderCombo   = ComboBox<String>(strings = Encoders.toNames, Encoders.DEFAULT.encoderIndex)
    private val gapLabel       = JLabel("Gap:")
    private val gapCombo       = ComboBox<String>(strings = listOf("0", "1", "2", "3", "4", "5"), 0)
    private val channelLabel   = JLabel("Channels:")
    private val channelGroup   = ButtonGroup()
    private val channelMono    = JRadioButton("Mono")
    private val channelStereo  = JRadioButton("Stereo")
    private val overwriteLabel = JLabel("Overwrite:")
    private val overwriteCombo = ComboBox<String>(strings = listOf("Don't overwrite existing files", "Overwrite older files", "Overwrite all"), 0)
    private val helpButton     = JButton("Help")
    private val convertButton  = JButton("Convert")
    var         auto           = Constants.Auto.NO

    //--------------------------------------------------------------------------
    init {
        val w = 16
        var y = 1

        add(sourceLabel, x = 1, y = y, w = w, h = 4)
        add(sourceInput, x = w + 2, y = y, w = -22, h = 4)
        add(sourceButton, x = -20, y = y, w = -1, h = 4)

        y += 5
        add(destLabel, x = 1, y = y, w = w, h = 4)
        add(destInput, x = w + 2, y = y, w = -22, h = 4)
        add(destButton, x = -20, y = y, w = -1, h = 4)

        y += 5
        add(imageLabel, x = 1, y = y, w = w, h = 4)
        add(imageInput, x = w + 2, y = y, w = -22, h = 4)
        add(imageButton, x = -20, y = y, w = -1, h = 4)

        y += 5
        add(authorLabel, x = 1, y = y, w = w, h = 4)
        add(authorInput, x = w + 2, y = y, w = -22, h = 4)
        add(helpButton, x = -20, y = y, w = -1, h = 4)

        y += 5
        add(titleLabel, x = 1, y = y, w = w, h = 4)
        add(titleInput, x = w + 2, y = y, w = -22, h = 4)
        add(convertButton, x = -20, y = y, w = -1, h = 4)

        y += 5
        add(commentLabel, x = 1, y = y, w = w, h = 4)
        add(commentInput, x = w + 2, y = y, w = -22, h = 4)

        y += 5
        add(yearLabel, x = 1, y = y, w = w, h = 4)
        add(yearInput, x = w + 2, y = y, w = 30, h = 4)

        y += 5
        add(genreLabel, x = 1, y = y, w = w, h = 4)
        add(genreInput, x = w + 2, y = y, w = 30, h = 4)

        y += 5
        add(gapLabel, x = 1, y = y, w = w, h = 4)
        add(gapCombo, x = w + 2, y = y, w = 30, h = 4)

        y += 5
        add(encoderLabel, x = 1, y = y, w = w, h = 4)
        add(encoderCombo, x = w + 2, y = y, w = 30, h = 4)

        y += 5
        add(channelLabel, x = 1, y = y, w = w, h = 4)
        add(channelMono, x = w + 2, y = y, w = 15, h = 4)
        add(channelStereo, x = w + 17, y = y, w = 15, h = 4)

        y += 5
        add(overwriteLabel, x = 1, y = y, w = w, h = 4)
        add(overwriteCombo, x = w + 2, y = y, w = 30, h = 4)

        channelGroup.add(channelMono)
        channelGroup.add(channelStereo)
        channelStereo.isSelected = true

        sourceInput.toolTipText   = Constants.TAB1_SOURCEINPUT_TOOLTIP
        destInput.toolTipText     = Constants.TAB1_DESTINPUT_TOOLTIP
        imageInput.toolTipText    = Constants.TAB1_IMAGEINPUT_TOOLTIP
        authorInput.toolTipText   = Constants.TAB1_AUTHORINPUT_TOOLTIP
        commentInput.toolTipText  = Constants.TAB1_COMMENTINPUT_TOOLTIP
        titleInput.toolTipText    = Constants.TAB1_TITLEINPUT_TOOLTIP
        yearInput.toolTipText     = Constants.TAB1_YEARINPUT_TOOLTIP
        genreInput.toolTipText    = Constants.TAB1_GENREINPUT_TOOLTIP
        encoderCombo.toolTipText  = Constants.ENCODERCOMBO_TOOLTIP
        gapCombo.toolTipText      = Constants.TAB1_GAPCOMBO_TOOLTIP
        channelMono.toolTipText   = Constants.TAB1_CHANNELMONO_TOOLTIP
        channelStereo.toolTipText = Constants.TAB1_CHANNELSTEREO_TOOLTIP

        sourceButton.toolTipText  = Constants.TAB1_SOURCEINPUT_TOOLTIP
        destButton.toolTipText    = Constants.TAB1_DESTINPUT_TOOLTIP
        imageButton.toolTipText   = Constants.TAB1_IMAGEINPUT_TOOLTIP
        convertButton.toolTipText = Constants.CONVERTBUTTON_TOOLTIP

        //----------------------------------------------------------------------
        convertButton.addActionListener {
            run()
        }

        //----------------------------------------------------------------------
        destButton.addActionListener {
            val dialog = JFileChooser(Main.pref.getFile(destInput.text, Main.pref.tab1DestFile))

            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                destInput.text         = dialog.selectedFile.canonicalPath
                Main.pref.tab1DestPath = dialog.selectedFile.canonicalPath
            }
        }

        //----------------------------------------------------------------------
        helpButton.addActionListener {
            AboutHandler(appName = Constants.HELP, aboutText = Constants.TAB1_HELP_TEXT).show(parent = Main.window)
        }

        //----------------------------------------------------------------------
        imageButton.addActionListener {
            val dialog = ImageFileDialog(Main.pref.getFile(imageInput.text, Main.pref.tab1ImageFile).canonicalPath, this)
            val file   = dialog.file

            if (file != null && file.isImage == true) {
                imageInput.text         = file.canonicalPath
                Main.pref.tab1ImagePath = file.parentFile.canonicalPath
            }
            else {
                imageInput.text = ""
            }
        }

        //----------------------------------------------------------------------
        sourceButton.addActionListener {
            val dialog = JFileChooser(Main.pref.getFile(sourceInput.text, Main.pref.tab1SourceFile))

            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                sourceInput.text         = dialog.selectedFile.canonicalPath
                Main.pref.tab1SourcePath = dialog.selectedFile.canonicalPath
            }
        }
    }

    //--------------------------------------------------------------------------
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
                throw Exception("error: invalid value for --gap ($gap)")
            }
            else {
                gapCombo.selectedIndex = gap
            }

            for ((index, choice) in Constants.TAB2_OVERWRITE.withIndex()) {
                if (overwrite == choice) {
                    overwriteCombo.selectedIndex = index
                    overwrite2 = ""
                    break
                }
            }

            if (overwrite2 != "") {
                throw Exception("error: invalid value for --overwrite ($overwrite)")
            }

            return true
        }
        catch (e: Exception) {
            if (auto == Constants.Auto.YES_QUIT_ON_ERROR) {
                println(e.message)
                Main.window.quit()
            }
            else {
                JOptionPane.showMessageDialog(null, e.message, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
            }

            return false
        }
    }

    //--------------------------------------------------------------------------
    private fun stage1LoadFiles(): List<FileInfo> {
        if (File(sourceInput.text).isDirectory == false) {
            Exception("error: missing source directory => '{sourceInput.text}'")
        }

        val files = FileInfo(sourceInput.text).readDir(FileInfo.ReadDirOption.FILES_ONLY_IN_START_DIRECTORY)
        var audioFiles: List<FileInfo> = listOf()

        audioFiles = files.filter {
            it.isAudioFile
        }

        if (audioFiles.isEmpty() == true) {
            throw Exception("error: no audio/video files in source directory")
        }

        audioFiles = audioFiles.sortedBy {
            it.filename
        }

        return audioFiles
    }

    //--------------------------------------------------------------------------
    private fun stage2SetParameters(files: List<FileInfo>) : Tab1Parameters {
        val parameters = Tab1Parameters(
            audioFiles = files,
            source     = sourceInput.text,
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

        parameters.validate()
        return parameters
    }

    //--------------------------------------------------------------------------
    private fun stage3Convert(parameters: Tab1Parameters) {
        val tasks    = mutableListOf<Task>(Tab1Task(parameters = parameters))
        val progress = ConvertManager(tasks = tasks, maxThreads = 1, onError = TaskManager.Execution.STOP_JOIN, onCancel = TaskManager.Execution.STOP_JOIN)
        val dialog   = TaskDialog(taskManager = progress, title = "Converting Files", type = TaskDialog.Type.PERCENT, parent = Main.window)

        dialog.enableCancel = true
        ConvertManager.clear()
        dialog.start(updateTime = 200L)
        tasks.throwFirstError()
    }

    //--------------------------------------------------------------------------
    private fun stage4WriteTags(parameters: Tab1Parameters) {
        try {
            val track = AudioFileIO.read(parameters.outputFile)
            val tag   = track.tagOrCreateDefault

            tag.setField(FieldKey.ALBUM, parameters.album)
            tag.setField(FieldKey.ALBUM_ARTIST, parameters.artist)
            tag.setField(FieldKey.ARTIST, parameters.artist)
            tag.setField(FieldKey.COMMENT, parameters.comment)
            tag.setField(FieldKey.ENCODER, parameters.encoder.executable)
            tag.setField(FieldKey.GENRE, if (parameters.genre.isBlank() == true) Tab1Parameters.DEFAULT_GENRE else parameters.genre)
            tag.setField(FieldKey.TITLE, parameters.album)
            tag.setField(FieldKey.TRACK, "1")
            tag.setField(FieldKey.TRACK_TOTAL, "1")

            if (parameters.year.numOrMinus >= 0) {
                tag.setField(FieldKey.YEAR, parameters.year)
            }

            if (parameters.cover.isNotBlank() == true) {
                tag.addField(StandardArtwork.createArtworkFromFile(File(parameters.cover)))
            }
            else if (parameters.image != null) {
                tag.addField(parameters.image)
            }

            track.tag = tag
            track.commit()
        }
        catch (e: Exception) {
            throw Exception("error: failed to write tags to '${parameters.outputFile.name}' -> ${e.message.toString()}")
        }
    }

    //--------------------------------------------------------------------------
    fun run() {
        var file: File? = null

        Swing.logMessage = ""
        Swing.errorMessage = ""

        try {
            val files = stage1LoadFiles()
            val parameters = stage2SetParameters(files)
            file = parameters.outputFile
            stage3Convert(parameters)
            file = null
            stage4WriteTags(parameters)

            val message = if (Swing.hasError == true) {
                "encoding finished successfully with file '${parameters.outputFile.name}' but there are some errors - check the log"
            }
            else {
                "encoding finished successfully with file '${parameters.outputFile.name}'"
            }

            Swing.logMessage = message

            if (auto != Constants.Auto.NO) {
                Main.window.quit()
            }
            else {
                JOptionPane.showMessageDialog(this, message, Constants.APP_NAME, JOptionPane.INFORMATION_MESSAGE)
            }
        }
        catch (e: FileExistException) {
            if (auto != Constants.Auto.NO) {
                println("${e.message}")
                Main.window.quit()
            }
            else {
                Swing.errorMessage = e.message ?: "!"
                JOptionPane.showMessageDialog(this, e.message, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
            }
        }
        catch (e: Exception) {
            if (file != null && file.isFile == true) {
                file.remove()
            }

            if (auto == Constants.Auto.YES_QUIT_ON_ERROR) {
                println("${e.message}")
                Main.window.quit()
            }
            else {
                Swing.errorMessage = e.message ?: "!"
                JOptionPane.showMessageDialog(this, e.message, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
            }
        }
        finally {
            System.gc()
        }
    }
}
