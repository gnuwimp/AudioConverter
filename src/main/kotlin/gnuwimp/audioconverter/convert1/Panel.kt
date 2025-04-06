/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter.convert1

import gnuwimp.audioconverter.*
import gnuwimp.swing.*
import gnuwimp.util.*
import java.io.File
import javax.swing.*

//------------------------------------------------------------------------------
@Suppress("UNUSED_VALUE")
class Panel : LayoutPanel(size = Swing.defFont.size / 2 + 1) {
    private val convertButton  = JButton("Convert")
    private val destButton     = JButton("Browse")
    private val destInput      = JTextField()
    private val destLabel      = JLabel("Destination:")
    private val encoderCombo   = ComboBox<String>(strings = Encoders.Companion.toNames, Encoders.Companion.DEFAULT.encoderIndex)
    private val encoderLabel   = JLabel("Encoder:")
    private val helpButton     = JButton("Help")
    private val overwriteCombo = ComboBox<String>(strings = Constants.OVERWRITE_LIST, 0)
    private val overwriteLabel = JLabel("Overwrite:")
    private val sourceButton   = JButton("Browse")
    private val sourceInput    = JTextField()
    private val sourceLabel    = JLabel("Source:")
    private val threadsCombo   = ComboBox<String>(strings = Constants.THREAD_LIST, 0)
    private val threadsLabel   = JLabel("Threads:")
    var         auto           = Constants.Auto.NO

    //--------------------------------------------------------------------------
    init {
        val w = 16

        add(sourceLabel,    x = 1,     y = 1,  w = w,   h = 4)
        add(sourceInput,    x = w + 2, y = 1,  w = -22, h = 4)
        add(sourceButton,   x = -20,   y = 1,  w = -1,  h = 4)

        add(destLabel,      x = 1,     y = 6,  w = w,   h = 4)
        add(destInput,      x = w + 2, y = 6,  w = -22, h = 4)
        add(destButton,     x = -20,   y = 6,  w = -1,  h = 4)

        add(encoderLabel,   x = 1,     y = 11, w = w,   h = 4)
        add(encoderCombo,   x = w + 2, y = 11, w = 30,  h = 4)
        add(helpButton,     x = -20,   y = 11, w = -1,  h = 4)

        add(threadsLabel,   x = 1,     y = 16, w = w,   h = 4)
        add(threadsCombo,   x = w + 2, y = 16, w = 30,  h = 4)
        add(convertButton,  x = -20,   y = 16, w = -1,  h = 4)

        add(overwriteLabel, x = 1,     y = 21, w = w,   h = 4)
        add(overwriteCombo, x = w + 2, y = 21, w = 30,  h = 4)

        convertButton.toolTipText = "Start converting files."
        destInput.toolTipText     = "Select destination directory."
        encoderCombo.toolTipText  = "Select encoder."
        sourceInput.toolTipText   = "Select start directory with all audio files."
        threadsCombo.toolTipText  = "Set number of threads to use when converting files."
        destButton.toolTipText    = destInput.toolTipText
        sourceButton.toolTipText  = sourceInput.toolTipText

        //----------------------------------------------------------------------
        convertButton.addActionListener {
            run()
        }

        //----------------------------------------------------------------------
        destButton.addActionListener {
            val dialog               = JFileChooser(destInput.text.dir(Main.pref.convertDestFile))
            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                destInput.text        = dialog.selectedFile.canonicalPath
                Main.pref.convertDest = dialog.selectedFile.canonicalPath
            }
        }

        //----------------------------------------------------------------------
        helpButton.addActionListener {
            AboutHandler(appName = Constants.HELP, aboutText = Constants.TAB3_HELP).show(parent = Main.window)
        }

        //----------------------------------------------------------------------
        sourceButton.addActionListener {
            val dialog               = JFileChooser(sourceInput.text.dir(Main.pref.convertSrcFile))
            dialog.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialog.fontForAll        = Swing.defFont

            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION && dialog.selectedFile.isDirectory == true) {
                sourceInput.text     = dialog.selectedFile.canonicalPath
                Main.pref.convertSrc = dialog.selectedFile.canonicalPath
            }
        }
    }

    //--------------------------------------------------------------------------
    fun argLoad(args: Array<String>): Boolean {
        try {
            val start      = args.findString("--src", "")
            val dest       = args.findString("--dest", "")
            val encoder    = args.findInt("--encoder", Encoders.Companion.DEFAULT.encoderIndex.toLong()).toInt()
            val threads    = args.findString("--threads", "1")
            var threads2   = threads
            val overwrite  = args.findString("--overwrite", "0")
            var overwrite2 = overwrite

            if (args.find("--auto2") != -1) {
                auto = Constants.Auto.YES_QUIT_ON_ERROR
            }
            else if (args.find("--auto") != -1) {
                auto = Constants.Auto.YES_STOP_ON_ERROR
            }

            if (start != "") {
                sourceInput.text = start
            }

            if (dest != "") {
                destInput.text = dest
            }

            encoderCombo.selectedIndex = encoder

            for ((index, choice) in Constants.THREAD_LIST.withIndex()) {
                if (threads == choice) {
                    threadsCombo.selectedIndex = index
                    threads2 = ""
                    break
                }
            }

            if (threads2 != "") {
                throw Exception("error: invalid value for --threads ($threads)")
            }

            for ((index, choice) in Constants.OVERWRITE_LIST_IDX.withIndex()) {
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
    private fun stage1SetParameters() : Parameters {
        val parameters = Parameters(
            source    = sourceInput.text,
            dest      = destInput.text,
            encoder   = Encoders.Companion.toEncoder(encoderCombo.selectedIndex),
            threads   = threadsCombo.text.toInt(),
            overwrite = if (overwriteCombo.selectedIndex == 1) Constants.Overwrite.OLDER else if (overwriteCombo.selectedIndex == 2) Constants.Overwrite.ALL else Constants.Overwrite.NO
        )

        parameters.validate()
        return parameters
    }

    //--------------------------------------------------------------------------
    private fun stage2LoadFiles(parameters: Parameters) {
        val files = FileInfo(parameters.source).readDir(FileInfo.ReadDirOption.RECURSIVE)

        parameters.inputFiles = files.filter {
            it.isAudioFile
        }

        if (parameters.inputFiles.isEmpty() == true) {
            throw Exception("error: no audio/video files found in ${parameters.source}")
        }

        parameters.inputFiles = parameters.inputFiles.sortedBy {
            it.filename
        }

        val start = FileInfo(parameters.source).canonicalPath
        val dest  = FileInfo(parameters.dest).canonicalPath

        parameters.inputFiles.forEach {
            val sourceDir = FileInfo(it.path).canonicalPath
            val destDir   = FileInfo(dest + sourceDir.replace(start, "")).canonicalPath
            val destFile  = FileInfo(destDir + File.separator + it.name.replaceAfterLast(".", parameters.encoder.fileExt))

            parameters.outputFiles.add(destFile)
        }
    }

    //--------------------------------------------------------------------------
    private fun stage3CreateDirectories(parameters: Parameters) {
        parameters.outputFiles.forEach {
            val parent = FileInfo(it.path)

            if (parent.isDir == false && parent.file.mkdir() == false) {
                throw Exception("error: can't create directory '${parent.filename}'")
            }
        }
    }

    //--------------------------------------------------------------------------
    private fun stage4CreateTasks(parameters: Parameters): List<gnuwimp.util.Task> {
        val tasks    = mutableListOf<gnuwimp.util.Task>()
        val outfiles = mutableMapOf<String, Boolean>()

        for (index in parameters.inputFiles.indices) {
            val infile  = parameters.inputFiles[index]
            val outfile = parameters.outputFiles[index]

            if (outfile.isMissing == true && outfiles[outfile.filename] == null) {
                outfiles[outfile.filename] = true
                tasks.add(Task(parameters.inputFiles[index], outfile, parameters.encoder))
            }
            else if (parameters.overwrite == Constants.Overwrite.OLDER && outfile.mod < infile.mod) {
                outfiles[outfile.filename] = true
                tasks.add(Task(parameters.inputFiles[index], outfile, parameters.encoder))
            }
            else if (parameters.overwrite == Constants.Overwrite.ALL) {
                outfiles[outfile.filename] = true
                tasks.add(Task(parameters.inputFiles[index], outfile, parameters.encoder))
            }
        }

        if (tasks.isEmpty() == true) {
            throw FileExistException("error: no files to convert\nall files already converted")
        }

        return tasks
    }

    //--------------------------------------------------------------------------
    private fun stage5Transcoding(parameters: Parameters, tasks: List<gnuwimp.util.Task>) {
        val progress = ConvertManager(
            tasks      = tasks,
            maxThreads = parameters.threads,
            onError    = TaskManager.Execution.STOP_JOIN,
            onCancel   = TaskManager.Execution.STOP_JOIN
        )
        val dialog = TaskDialog(taskManager = progress, title = "Converting Files", type = TaskDialog.Type.PERCENT, parent = Main.window, height = Swing.defFont.size * 26)

        dialog.enableCancel = true
        ConvertManager.Companion.clear()
        dialog.start(updateTime = 200L, messages = parameters.threads + 1)
        tasks.throwFirstError()
    }

    //--------------------------------------------------------------------------
    fun run() {
        Swing.logMessage = ""
        Swing.errorMessage = ""

        try {
            val parameters = stage1SetParameters()
            stage2LoadFiles(parameters)
            stage3CreateDirectories(parameters)
            val tasks = stage4CreateTasks(parameters)
            stage5Transcoding(parameters, tasks)

            val message = if (Swing.hasError == true) {
                "all (${tasks.size}) files encoded successfully but there are some errors - check the log"
            }
            else {
                "all (${tasks.size}) files encoded successfully"
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
