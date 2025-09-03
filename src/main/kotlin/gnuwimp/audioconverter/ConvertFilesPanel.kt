/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.*
import gnuwimp.util.FileInfo
import gnuwimp.util.Task
import gnuwimp.util.TaskManager
import gnuwimp.util.throwFirstError
import java.io.File
import java.util.*
import javax.swing.*

/***
 *       _____                          _   ______ _ _           _____                 _
 *      / ____|                        | | |  ____(_) |         |  __ \               | |
 *     | |     ___  _ ____   _____ _ __| |_| |__   _| | ___  ___| |__) |_ _ _ __   ___| |
 *     | |    / _ \| '_ \ \ / / _ \ '__| __|  __| | | |/ _ \/ __|  ___/ _` | '_ \ / _ \ |
 *     | |___| (_) | | | \ V /  __/ |  | |_| |    | | |  __/\__ \ |  | (_| | | | |  __/ |
 *      \_____\___/|_| |_|\_/ \___|_|   \__|_|    |_|_|\___||___/_|   \__,_|_| |_|\___|_|
 *
 *
 */

/**
 * Transcode a selection of files.
 */
class ConvertFilesPanel : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val clearButton    = JButton("Clear files")
    private val convertButton  = JButton("Convert")
    private val destButton     = JButton("Browse")
    private val destInput      = JTextField()
    private val destLabel      = JLabel("Destination:")
    private val encoderCombo   = ComboBox<String>(strings = Encoders.toNames, Encoders.DEFAULT.encoderIndex)
    private val encoderLabel   = JLabel("Encoder:")
    private val helpButton     = JButton("Help")
    private val overwriteCombo = ComboBox<String>(strings = Constants.OVERWRITE_LIST, 0)
    private val overwriteLabel = JLabel("Overwrite:")
    private val sourceButton   = JButton("Add files")
    private val sourceInput    = JList<String>()
    private val sourceLabel    = JLabel("Files:")
    private val threadsCombo   = ComboBox<String>(strings = Constants.THREAD_LIST, 0)
    private val threadsLabel   = JLabel("Threads:")

    /**
     *
     */
    init {
        val w = 16

        add(sourceLabel,    x = 1,     y =   1, w =   w, h =   4)
        add(sourceInput,    x = w + 2, y =   1, w = -22, h = -21)
        add(sourceButton,   x = -20,   y =   1, w =  -1, h =   4)
        add(clearButton,    x = -20,   y =   6, w =  -1, h =   4)

        add(destLabel,      x = 1,     y = -20, w =   w, h =   4)
        add(destInput,      x = w + 2, y = -20, w = -22, h =   4)
        add(destButton,     x = -20,   y = -20, w =  -1, h =   4)

        add(encoderLabel,   x = 1,     y = -15, w =  w,  h =   4)
        add(encoderCombo,   x = w + 2, y = -15, w = 30,  h =   4)
        add(helpButton,     x = -20,   y = -15, w = -1,  h =   4)

        add(threadsLabel,   x = 1,     y = -10, w =  w,  h =   4)
        add(threadsCombo,   x = w + 2, y = -10, w = 30,  h =   4)
        add(convertButton,  x = -20,   y = -10, w = -1,  h =   4)

        add(overwriteLabel, x = 1,     y =  -5, w =  w,  h =   4)
        add(overwriteCombo, x = w + 2, y =  -5, w = 30,  h =   4)

        clearButton.toolTipText   = "Clear all files."
        convertButton.toolTipText = "Start converting files."
        destInput.toolTipText     = "Select destination directory."
        encoderCombo.toolTipText  = "Select encoder."
        sourceInput.border        = BorderFactory.createEtchedBorder()
        sourceInput.toolTipText   = "Select audio files."
        destButton.toolTipText    = destInput.toolTipText
        sourceButton.toolTipText  = sourceInput.toolTipText

        /**
         *
         */
        clearButton.addActionListener {
            sourceInput.setListData(Vector<String>())
        }

        /**
         *
         */
        convertButton.addActionListener {
            run()
        }

        /**
         *
         */
        destButton.addActionListener {
            val dialog               = JFileChooser(destInput.text.dir(Main.pref.convertDestFile))
            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                destInput.text        = dialog.selectedFile.canonicalPath
                Main.pref.convertDest = dialog.selectedFile.canonicalPath
            }
        }

        /**
         *
         */
        helpButton.addActionListener {
            AboutHandler(appName = Constants.HELP, aboutText = Constants.HELP_CONVERT_FILES).show(parent = Main.window)
        }

        /**
         *
         */
        sourceButton.addActionListener {
            val dialog                     = JFileChooser(Main.pref.convertSrcFile)
            dialog.fileSelectionMode       = JFileChooser.FILES_ONLY
            dialog.fontForAll              = Swing.defFont
            dialog.isMultiSelectionEnabled = true

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                var files = dialog.selectedFiles

                if (files.size > 0) {
                    var strings = mutableListOf<String>()

                    files.forEach {
                        strings.add(it.canonicalFile.toString())
                    }

                    Main.pref.convertSrc = files[0].canonicalPath
                    sourceFiles().forEach { strings.add(it) }
                    sourceInput.setListData(Vector(strings.distinctBy { it }.sortedBy { it }))
                }
            }
        }
    }

    /**
     *
     */
    private fun sourceFiles() : List<String> {
        val files: MutableList<String> = mutableListOf()

        for (i in 0..< sourceInput.model.size) {
            files.add(sourceInput.model.getElementAt(i))
        }

        return files.distinctBy { it }
    }

    /**
     *
     */
    private fun stage1SetParameters() : ConvertFilesParams {
        val convertFilesParams = ConvertFilesParams(
            files     = sourceFiles(),
            dest      = destInput.text,
            encoder   = Encoders.toEncoder(encoderCombo.selectedIndex),
            threads   = threadsCombo.text.toInt(),
            overwrite = if (overwriteCombo.selectedIndex == 1) Constants.Overwrite.OLDER else if (overwriteCombo.selectedIndex == 2) Constants.Overwrite.ALL else Constants.Overwrite.NO
        )

        convertFilesParams.validate()
        return convertFilesParams
    }

    /**
     *
     */
    private fun stage2LoadFiles(convertFilesParams: ConvertFilesParams) {
        val audioFiles = mutableListOf<FileInfo>()

        for (file in convertFilesParams.files) {
            val f = FileInfo(file)

            if (f.isAudioFile == true) {
                audioFiles.add(f)
            }
        }

        convertFilesParams.inputFiles = audioFiles

        if (convertFilesParams.inputFiles.isEmpty() == true) {
            throw Exception("Error: no audio/video files in list")
        }

        val dest = FileInfo(convertFilesParams.dest).canonicalPath

        convertFilesParams.inputFiles.forEach {
            val sourceDir = FileInfo(it.path).canonicalPath
            val destDir   = FileInfo(dest + sourceDir.replace(sourceDir, "")).canonicalPath
            val destFile  = FileInfo(destDir + File.separator + it.name.replaceAfterLast(".", convertFilesParams.encoder.fileExt))

            convertFilesParams.outputFiles.add(destFile)
        }

        convertFilesParams.outputFiles = convertFilesParams.outputFiles.distinctBy { it.filename }.toMutableList()

        if (convertFilesParams.inputFiles.size != convertFilesParams.outputFiles.size) {
            throw Exception("Error: input list is different from output list, probably duplicate input filenames")
        }
    }

    /**
     *
     */
    private fun stage3CreateTasks(convertFilesParams: ConvertFilesParams): List<Task> {
        val tasks    = mutableListOf<Task>()
        val outfiles = mutableMapOf<String, Boolean>()

        for (index in convertFilesParams.inputFiles.indices) {
            val infile  = convertFilesParams.inputFiles[index]
            val outfile = convertFilesParams.outputFiles[index]

            if (outfile.isMissing == true && outfiles[outfile.filename] == null) {
                outfiles[outfile.filename] = true
                tasks.add(ConvertTask(convertFilesParams.inputFiles[index], outfile, convertFilesParams.encoder))
            }
            else if (convertFilesParams.overwrite == Constants.Overwrite.OLDER && outfile.mod < infile.mod) {
                outfiles[outfile.filename] = true
                tasks.add(ConvertTask(convertFilesParams.inputFiles[index], outfile, convertFilesParams.encoder))
            }
            else if (convertFilesParams.overwrite == Constants.Overwrite.ALL) {
                outfiles[outfile.filename] = true
                tasks.add(ConvertTask(convertFilesParams.inputFiles[index], outfile, convertFilesParams.encoder))
            }
        }

        if (tasks.isEmpty() == true) {
            throw FileExistException("Error: no files to convert.\nAll files already converted!")
        }

        return tasks
    }

    /**
     *
     */
    private fun stage4Transcoding(convertFilesParams: ConvertFilesParams, tasks: List<Task>) {
        val progress = ConvertManager(
            tasks      = tasks,
            maxThreads = convertFilesParams.threads,
            onError    = TaskManager.Execution.STOP_JOIN,
            onCancel   = TaskManager.Execution.STOP_JOIN
        )
        val dialog = TaskDialog(taskManager = progress, title = "Converting Files", type = TaskDialog.Type.PERCENT, parent = Main.window, height = Swing.defFont.size * 26)

        dialog.enableCancel = true
        ConvertManager.clear()
        dialog.start(updateTime = 200L, messages = convertFilesParams.threads + 1)
        tasks.throwFirstError()
    }

    /**
     *
     */
    fun run() {
        Swing.logMessage   = ""
        Swing.errorMessage = ""
        var tasks          = listOf<Task>()

        try {
            val parameters = stage1SetParameters()
            stage2LoadFiles(parameters)
            tasks = stage3CreateTasks(parameters)
            stage4Transcoding(parameters, tasks)

            val ok = tasks.count { it.status == Task.Status.OK }

            val message = if (ok != tasks.size) {
                "$ok files encoded successfully of ${tasks.size}.\nBut there are some errors!\nCheck the log."
            }
            else {
                "$ok files encoded successfully."
            }

            Swing.logMessage = message
            MessageDialog.info(message)
        }
        catch (e: FileExistException) {
            val ok      = tasks.count { it.status == Task.Status.OK }
            val message = e.message!! + "\n$ok files encoded of ${tasks.size}"

            Swing.errorMessage = message
            MessageDialog.error(message)
        }
        catch (e: Exception) {
            val ok      = tasks.count { it.status == Task.Status.OK }
            val message = e.message!! + "\n$ok files encoded of ${tasks.size}"

            Swing.errorMessage = message
            MessageDialog.error(message)
        }
        finally {
            System.gc()
        }
    }
}
