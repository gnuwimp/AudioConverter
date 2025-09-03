/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.swing.SetupPanel
import gnuwimp.swing.Swing
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

/***
 *       _____      _
 *      / ____|    | |                /\
 *     | (___   ___| |_ _   _ _ __   /  \   _ __  _ __
 *      \___ \ / _ \ __| | | | '_ \ / /\ \ | '_ \| '_ \
 *      ____) |  __/ |_| |_| | |_) / ____ \| |_) | |_) |
 *     |_____/ \___|\__|\__,_| .__/_/    \_\ .__/| .__/
 *                           | |           | |   | |
 *                           |_|           |_|   |_|
 */

/**
 * Change default application font.
 */
class SetupApp : SetupPanel("Setup") {
    val spinnerLabel = JLabel("Set font size (8 - 24) - restart to apply")
    val spinnerModel = SpinnerNumberModel(Swing.defFont.size, 8, 24, 1)
    val spinner      = JSpinner(spinnerModel)

    /**
     *
     */
    init {
        add(spinnerLabel,   x =  1, y = 1, w = 40, h = 4)
        add(spinner,        x = 41, y = 1, w = 20, h = 4)
    }

    /**
     *
     */
    override fun load() {
    }

    /**
     *
     */
    override fun save(): Boolean {
        val value = spinner.value as Int

        Swing.defFont = Font(Font.SANS_SERIF, Font.PLAIN, value)
        Swing.bigFont = Font(Font.SANS_SERIF, Font.PLAIN, value * 2)

        return true
    }
}
