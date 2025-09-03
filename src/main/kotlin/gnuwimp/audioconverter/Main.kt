/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.BaseDialog
import gnuwimp.swing.MessageDialog
import gnuwimp.swing.Swing
import gnuwimp.util.find
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
import java.util.prefs.Preferences
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

/***
 *      __  __       _
 *     |  \/  |     (_)
 *     | \  / | __ _ _ _ __
 *     | |\/| |/ _` | | '_ \
 *     | |  | | (_| | | | | |
 *     |_|  |_|\__,_|_|_| |_|
 *
 *
 */

/**
 *
 */
object Main {
    val icon: Image
    val window: MainWindow
    val pref: Preferences

    /**
     *
     */
    init {
        try {
            Swing.setup(theme = "nimbus", appName = Constants.ABOUT_APP, aboutText = Constants.aboutApp(), quitLambda = { quit() })

            icon = "gnuwimp/audioconverter/AudioConverter.png".loadImageFromResource()
            pref = Preferences.userNodeForPackage(Main.javaClass)

            if (pref.fontSize in 8..24) {
                Swing.defFont = Font(Font.SANS_SERIF, Font.PLAIN, pref.fontSize)
                Swing.bigFont = Font(Font.SANS_SERIF, Font.PLAIN, pref.fontSize + 10)
            }

            window            = MainWindow()
            BaseDialog.PARENT = window
            BaseDialog.TITLE  = Constants.APP_NAME
        }
        catch(e: Exception) {
            e.printStackTrace()
            MessageDialog.error(e.stackTraceToString())
            exitProcess(status = 1)
        }
    }

    /**
     *
     */
    private fun String.loadImageFromResource(): Image {
        val classLoader = Main::class.java.classLoader
        val pathShell   = classLoader.getResource(this)

        return Toolkit.getDefaultToolkit().getImage(pathShell)
    }

    /**
     *
     */
    private fun quit() {
        window.quit()
    }

    /**
     *
     */
    @JvmStatic fun main(args: Array<String>) {
        try {
            SwingUtilities.invokeLater {
                window.prefLoad()
                window.isVisible = true

                val tabs3 = args.find("--mode2") != -1

                if (tabs3 == true && window.convertDirPanel.argLoad(args) == true) {
                    window.tabs.selectedIndex = 2

                    if (window.convertDirPanel.auto != Constants.Auto.NO) {
                        window.convertDirPanel.run()
                    }
                }
                else if (tabs3 == false && window.mergeDirPanel.argLoad(args) == true) {
                    window.tabs.selectedIndex = 0

                    if (window.mergeDirPanel.auto != Constants.Auto.NO) {
                        window.mergeDirPanel.run()
                    }
                }
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
            MessageDialog.error(e.stackTraceToString())
        }
    }
}
