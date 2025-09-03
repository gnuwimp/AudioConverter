/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.*
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTabbedPane
import kotlin.system.exitProcess

/***
 *      __  __       _   __          ___           _
 *     |  \/  |     (_)  \ \        / (_)         | |
 *     | \  / | __ _ _ _ _\ \  /\  / / _ _ __   __| | _____      __
 *     | |\/| |/ _` | | '_ \ \/  \/ / | | '_ \ / _` |/ _ \ \ /\ / /
 *     | |  | | (_| | | | | \  /\  /  | | | | | (_| | (_) \ V  V /
 *     |_|  |_|\__,_|_|_| |_|\/  \/   |_|_| |_|\__,_|\___/ \_/\_/
 *
 *
 */


/**
 * Main window with global buttons.
 * And four tabs.
 */
class MainWindow : JFrame(Constants.APP_NAME) {
    private val main              = LayoutPanel(size = Swing.defFont.size / 2)
    val         tabs              = JTabbedPane()
    val         mergeDirPanel     = MergeDirPanel()
    val         mergeFilesPanel   = MergeFilesPanel()
    val         convertDirPanel   = ConvertDirPanel()
    val         convertFilesPanel = ConvertFilesPanel()
    private val quitButton        = JButton("Quit")
    private val aboutButton       = JButton("About")
    private val logButton         = JButton("Show Log")
    private val setupButton       = JButton("Setup")

    /**
     *
     */
    init {
        iconImage   = Main.icon
        contentPane = main

        tabs.border = BorderFactory.createEmptyBorder(4, 4, 0, 4)
        mergeDirPanel.border = BorderFactory.createEtchedBorder()
        mergeFilesPanel.border = BorderFactory.createEtchedBorder()
        convertDirPanel.border = BorderFactory.createEtchedBorder()
        convertFilesPanel.border = BorderFactory.createEtchedBorder()

        tabs.addTab("Merge (directory)", null, mergeDirPanel, "Merge a directory of audio/video files into one audio file.")
        tabs.addTab("Merge (files)", null, mergeFilesPanel, "Merge selected audio/video files into one audio file.")
        tabs.addTab("Convert (directory)", null, convertDirPanel, "Convert all files in a directory tree.")
        tabs.addTab("Convert (files)", null, convertFilesPanel, "Convert selected audio/video files.")

        main.add(tabs,        x = 0,   y = 0,  w = 0,  h = -6)
        main.add(quitButton,  x = -21, y = -5, w = 20, h = 4)
        main.add(setupButton, x = -42, y = -5, w = 20, h = 4)
        main.add(aboutButton, x = -63, y = -5, w = 20, h = 4)
        main.add(logButton,   x = -84, y = -5, w = 20, h = 4)

        logButton.toolTipText = Constants.LOGBUTTON_TOOLTIP

        pack()

        /**
         *
         */
        addWindowListener( object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                quit()
            }
        })

        /**
         *
         */
        aboutButton.addActionListener {
            AboutHandler(appName = Constants.ABOUT_APP, aboutText = Constants.aboutApp()).show(parent = Main.window, height = Swing.defFont.size * 55)
        }

        /**
         *
         */
        logButton.addActionListener {
            val dialog = TextDialog(text = Swing.logMessage + "\n\n" + Swing.errorMessage, showLastLine = true, title = "Log", parent = Main.window)

            dialog.isVisible = true
        }

        /**
         *
         */
        quitButton.addActionListener {
            Main.window.quit()
        }

        /**
         * Show setup dialog.
         */
        setupButton.addActionListener {
            val dialog = SetupDialog(parent = this, label = "AudioConverter", panels = listOf(SetupApp()), width = Swing.defFont.size * 40, height = Swing.defFont.size * 20)
            dialog.isVisible = true
        }
    }

    /**
     *
     */
    fun quit() {
        prefSave()
        isVisible = false
        dispose()
        exitProcess(status = 0)
    }

    /**
     *
     */
    fun prefLoad() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        fontForAll            = Swing.defFont

        val w  = Main.pref.winWidth
        val h  = Main.pref.winHeight
        var x  = Main.pref.winX
        var y  = Main.pref.winY
        val sc = Toolkit.getDefaultToolkit().screenSize

        if (x > sc.getWidth() || x < -50) {
            x = 0
        }

        if (y > sc.getHeight() || y < -50) {
            y = 0
        }

        setLocation(x, y)
        setSize(w, h)

        if (Main.pref.winMax == true) {
            extendedState = MAXIMIZED_BOTH
        }
    }

    /**
     *
     */
    private fun prefSave() {
        try {
            Main.pref.winWidth  = size.width
            Main.pref.winHeight = size.height
            Main.pref.winX      = location.x
            Main.pref.winY      = location.y
            Main.pref.winMax    = (extendedState and MAXIMIZED_BOTH != 0)
            Main.pref.fontSize  = Swing.defFont.size

            Main.pref.flush()
        }
        catch (_: Exception) {
        }
    }
}
