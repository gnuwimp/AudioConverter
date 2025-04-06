package gnuwimp.audioconverter.merge2

import gnuwimp.audioconverter.*
import gnuwimp.swing.*
import gnuwimp.util.*
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.StandardArtwork
import java.io.File
import java.util.*
import javax.swing.*

//------------------------------------------------------------------------------
@Suppress("UNUSED_VALUE")
class Panel : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val authorInput    = JTextField()
    private val authorLabel    = JLabel("Artist")
    private val channelGroup   = ButtonGroup()
    private val channelLabel   = JLabel("Channels:")
    private val channelMono    = JRadioButton("Mono")
    private val channelStereo  = JRadioButton("Stereo")
    private val clearButton    = JButton("Clear files")
    private val commentInput   = JTextField()
    private val commentLabel   = JLabel("Comment:")
    private val convertButton  = JButton("Convert")
    private val destButton     = JButton("Browse")
    private val destInput      = JTextField()
    private val destLabel      = JLabel("Destination:")
    private val encoderCombo   = ComboBox<String>(strings = Encoders.Companion.toNames, Encoders.Companion.DEFAULT.encoderIndex)
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
    private val sourceButton   = JButton("Add files")
    private val sourceInput    = JList<String>()
    private val sourceLabel    = JLabel("Files:")
    private val titleInput     = JTextField()
    private val titleLabel     = JLabel("Title:")
    private val yearInput      = JTextField()
    private val yearLabel      = JLabel("Year:")

    //--------------------------------------------------------------------------
    init {
        val w = 16

        add(sourceLabel,    x = 1,      y = 1,   w = w,   h = 4)
        add(sourceInput,    x = w + 2,  y = 1,   w = -22, h = -56)
        add(sourceButton,   x = -20,    y = 1,   w = -1,  h = 4)
        add(clearButton,    x = -20,    y = 6,   w = -1,  h = 4)

        add(destLabel,      x = 1,      y = -55, w = w,   h = 4)
        add(destInput,      x = w + 2,  y = -55, w = -22, h = 4)
        add(destButton,     x = -20,    y = -55, w = -1,  h = 4)

        add(imageLabel,     x = 1,      y = -50, w = w,   h = 4)
        add(imageInput,     x = w + 2,  y = -50, w = -22, h = 4)
        add(imageButton,    x = -20,    y = -50, w = -1,  h = 4)

        add(authorLabel,    x = 1,      y = -45, w = w,   h = 4)
        add(authorInput,    x = w + 2,  y = -45, w = -22, h = 4)
        add(helpButton,     x = -20,    y = -45, w = -1,  h = 4)

        add(titleLabel,     x = 1,      y = -40, w = w,   h = 4)
        add(titleInput,     x = w + 2,  y = -40, w = -22, h = 4)
        add(convertButton,  x = -20,    y = -40, w = -1,  h = 4)

        add(commentLabel,   x = 1,      y = -35, w = w,   h = 4)
        add(commentInput,   x = w + 2,  y = -35, w = -22, h = 4)

        add(yearLabel,      x = 1,      y = -30, w = w,   h = 4)
        add(yearInput,      x = w + 2,  y = -30, w = 30,  h = 4)

        add(genreLabel,     x = 1,      y = -25, w = w,   h = 4)
        add(genreInput,     x = w + 2,  y = -25, w = 30,  h = 4)

        add(gapLabel,       x = 1,      y = -20, w = w,   h = 4)
        add(gapCombo,       x = w + 2,  y = -20, w = 30,  h = 4)

        add(encoderLabel,   x = 1,      y = -15, w = w,   h = 4)
        add(encoderCombo,   x = w + 2,  y = -15, w = 30,  h = 4)

        add(channelLabel,   x = 1,      y = -10, w = w,   h = 4)
        add(channelMono,    x = w + 2,  y = -10, w = 15,  h = 4)
        add(channelStereo,  x = w + 17, y = -10, w = 15,  h = 4)

        add(overwriteLabel, x = 1,      y = -5,  w = w,   h = 4)
        add(overwriteCombo, x = w + 2,  y = -5,  w = 30,  h = 4)

        channelGroup.add(channelMono)
        channelGroup.add(channelStereo)

        authorInput.toolTipText   = "Set artist/author name."
        channelMono.toolTipText   = "Convert stereo tracks to mono."
        channelStereo.isSelected  = true
        channelStereo.toolTipText = "Keep tracks as they are, stereo or mono."
        clearButton.toolTipText   = "Clear all files."
        commentInput.toolTipText  = "Set comment string (optional)."
        convertButton.toolTipText = "Start converting files."
        destInput.toolTipText     = "Select destination directory for the result file."
        encoderCombo.toolTipText  = "Select encoder."
        gapCombo.toolTipText      = "Insert silence between tracks (0 - 5 seconds)."
        genreInput.toolTipText    = "Set track genre (optional)."
        imageInput.toolTipText    = "Select cover image file (optional)."
        sourceInput.border        = BorderFactory.createEtchedBorder()
        sourceInput.toolTipText   = "Select source directory with all audio/video files."
        titleInput.toolTipText    = "Set title/artist name."
        yearInput.toolTipText     = "Set year for the audio book (optional, 1 - 9999)."
        destButton.toolTipText    = destInput.toolTipText
        imageButton.toolTipText   = imageButton.toolTipText
        sourceButton.toolTipText  = sourceInput.toolTipText

        //----------------------------------------------------------------------
        clearButton.addActionListener {
            sourceInput.setListData(Vector<String>())
        }

        //----------------------------------------------------------------------
        convertButton.addActionListener {
            run()
        }

        //----------------------------------------------------------------------
        destButton.addActionListener {
            val dialog               = JFileChooser(destInput.text.dir(Main.pref.mergeDestFile))
            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                destInput.text      = dialog.selectedFile.canonicalPath
                Main.pref.mergeDest = dialog.selectedFile.canonicalPath
            }
        }

        //----------------------------------------------------------------------
        helpButton.addActionListener {
            AboutHandler(appName = Constants.HELP, aboutText = Constants.TAB2_HELP).show(parent = Main.window)
        }

        //----------------------------------------------------------------------
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

        //----------------------------------------------------------------------
        sourceButton.addActionListener {
            val dialog                     = JFileChooser(Main.pref.mergeSrcFile)
            dialog.fileSelectionMode       = JFileChooser.FILES_ONLY
            dialog.fontForAll              = Swing.defFont
            dialog.isMultiSelectionEnabled = true

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                var files = dialog.selectedFiles

                if (files.size > 0) {
                    var strings = mutableListOf<String>()

                    sourceFiles().forEach { strings.add(it) }

                    files.forEach {
                        strings.add(it.canonicalFile.toString())
                    }

                    Main.pref.mergeSrc = files[0].canonicalPath
                    sourceInput.setListData(Vector(strings))
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    private fun sourceFiles() : List<String> {
        val files: MutableList<String> = mutableListOf()

        for (i in 0..< sourceInput.model.size) {
            files.add(sourceInput.model.getElementAt(i))
        }

        return files
    }

    //--------------------------------------------------------------------------
    private fun stage1LoadFiles(): List<FileInfo> {
        var audioFiles = mutableListOf<FileInfo>()

        for (file in sourceFiles()) {
            val f = FileInfo(file)

            if (f.isAudioFile == true) {
                audioFiles.add(f)
            }
        }

        if (audioFiles.isEmpty() == true) {
            throw Exception("error: no audio/video files in list")
        }

        return audioFiles
    }

    //--------------------------------------------------------------------------
    private fun stage2SetParameters(files: List<FileInfo>) : gnuwimp.audioconverter.merge1.Parameters {
        val parameters = gnuwimp.audioconverter.merge1.Parameters(
            audioFiles = files,
            dest       = destInput.text,
            cover      = imageInput.text,
            artist     = authorInput.text,
            album      = titleInput.text,
            year       = yearInput.text,
            comment    = commentInput.text,
            genre      = genreInput.text,
            encoder    = Encoders.Companion.toEncoder(encoderCombo.selectedIndex),
            gap        = gapCombo.text,
            mono       = channelMono.isSelected,
            overwrite  = if (overwriteCombo.selectedIndex == 1) Constants.Overwrite.OLDER else if (overwriteCombo.selectedIndex == 2) Constants.Overwrite.ALL else Constants.Overwrite.NO
        )

        parameters.validate()
        return parameters
    }

    //--------------------------------------------------------------------------
    private fun stage3Convert(parameters: gnuwimp.audioconverter.merge1.Parameters) {
        val tasks = mutableListOf<gnuwimp.audioconverter.merge1.Task>(gnuwimp.audioconverter.merge1.Task(parameters = parameters))

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
        ConvertManager.Companion.clear()
        dialog.start(updateTime = 200L)
        tasks.throwFirstError()
    }

    //--------------------------------------------------------------------------
    private fun stage4WriteTags(parameters: gnuwimp.audioconverter.merge1.Parameters) {
        try {
            val track = AudioFileIO.read(parameters.outputFile.file)
            val tag   = track.tagOrCreateDefault

            tag.setField(FieldKey.ALBUM, parameters.album)
            tag.setField(FieldKey.ALBUM_ARTIST, parameters.artist)
            tag.setField(FieldKey.ARTIST, parameters.artist)
            tag.setField(FieldKey.COMMENT, parameters.comment)
            tag.setField(FieldKey.ENCODER, parameters.encoder.executable)
            tag.setField(FieldKey.GENRE, if (parameters.genre.isBlank() == true) gnuwimp.audioconverter.merge1.Parameters.Companion.DEFAULT_GENRE else parameters.genre)
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
        var file: FileInfo? = null

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
            JOptionPane.showMessageDialog(this, message, Constants.APP_NAME, JOptionPane.INFORMATION_MESSAGE)
        }
        catch (e: Exception) {
            file?.remove()
            Swing.errorMessage = e.message ?: "!"
            JOptionPane.showMessageDialog(this, e.message, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
        }
        finally {
            System.gc()
        }
    }
}
