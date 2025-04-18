/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.Swing
import gnuwimp.util.find
import java.awt.Font
import java.awt.Image
import java.awt.Toolkit
import java.util.prefs.Preferences
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import kotlin.system.exitProcess

//------------------------------------------------------------------------------
object Main {
    val icon: Image
    val window: MainWindow
    val pref: Preferences

    init {
        try {
            Swing.setup(theme = "nimbus", appName = Constants.ABOUT_APP, aboutText = Constants.aboutApp(), quitLambda = { quit() })

            icon          = "gnuwimp/audioconverter/AudioConverter.png".loadImageFromResource()
            Swing.bigFont = Font(Font.SANS_SERIF, Font.PLAIN, 24)
            Swing.defFont = Font(Font.SANS_SERIF, Font.PLAIN, 12)
            pref          = Preferences.userNodeForPackage(Main.javaClass)
            window        = MainWindow()
        }
        catch(e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, e, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
            exitProcess(status = 1)
        }
    }

    //--------------------------------------------------------------------------
    private fun String.loadImageFromResource(): Image {
        val classLoader = Main::class.java.classLoader
        val pathShell = classLoader.getResource(this)
        return Toolkit.getDefaultToolkit().getImage(pathShell)
    }

    //--------------------------------------------------------------------------
    private fun quit() {
        window.quit()
    }

    //--------------------------------------------------------------------------
    @JvmStatic fun main(args: Array<String>) {
        try {
            SwingUtilities.invokeLater {
                window.prefLoad()
                window.isVisible = true

                val tabs3 = args.find("--mode2") != -1

                if (tabs3 == true && window.tab3.argLoad(args) == true) {
                    window.tabs.selectedIndex = 2

                    if (window.tab3.auto != Constants.Auto.NO) {
                        window.tab3.run()
                    }
                }
                else if (tabs3 == false && window.tab1.argLoad(args) == true) {
                    window.tabs.selectedIndex = 0

                    if (window.tab1.auto != Constants.Auto.NO) {
                        window.tab1.run()
                    }
                }
            }
        }
        catch(e: Exception) {
            JOptionPane.showMessageDialog(null, e, Constants.APP_NAME, JOptionPane.ERROR_MESSAGE)
        }
    }
}
