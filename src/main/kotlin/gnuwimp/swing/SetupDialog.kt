/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTabbedPane

/***
 *       _____      _               _____                 _
 *      / ____|    | |             |  __ \               | |
 *     | (___   ___| |_ _   _ _ __ | |__) |_ _ _ __   ___| |
 *      \___ \ / _ \ __| | | | '_ \|  ___/ _` | '_ \ / _ \ |
 *      ____) |  __/ |_| |_| | |_) | |  | (_| | | | |  __/ |
 *     |_____/ \___|\__|\__,_| .__/|_|   \__,_|_| |_|\___|_|
 *                           | |
 *                           |_|
 */

/**
 * A base class for creating panels for a preference dialog
 */
abstract class SetupPanel(val label: String) : LayoutPanel(size = Swing.defFont.size / 2) {
    /**
     * Load data to widget
     */
    abstract fun load()

    /**
     * Save data in widget
     * Return false to keep dialog running, true to close dialog
     */
    abstract fun save(): Boolean
}

/***
 *       _____      _               _____  _       _
 *      / ____|    | |             |  __ \(_)     | |
 *     | (___   ___| |_ _   _ _ __ | |  | |_  __ _| | ___   __ _
 *      \___ \ / _ \ __| | | | '_ \| |  | | |/ _` | |/ _ \ / _` |
 *      ____) |  __/ |_| |_| | |_) | |__| | | (_| | | (_) | (_| |
 *     |_____/ \___|\__|\__,_| .__/|_____/|_|\__,_|_|\___/ \__, |
 *                           | |                            __/ |
 *                           |_|                           |___/
 */

/**
 * A preference (setup) dialog
 * Create panels that are inherited from SetupPanel
 */
class SetupDialog(parent: JFrame?, label: String, panels: List<SetupPanel>, width: Int = Swing.defFont.size * 60, height: Int = Swing.defFont.size * 50) : BaseDialog(parent, label, true) {
    private val _close = JButton("Close")
    private val _tabs  = JTabbedPane()

    init {
        val panel = LayoutPanel(size = Swing.defFont.size / 2)

        add(panel)
        panel.add(_tabs,  x =   1, y =  1, w = -1, h = -6)
        panel.add(_close, x = -21, y = -5, w = 20, h =  4)
        pack()

        panels.forEach {
            _tabs.addTab(it.label, it)
            it.load()
        }

        size       = Dimension(width, height)
        fontForAll = Swing.defFont

        centerWindow()

        //----------------------------------------------------------------------
        _close.addActionListener {
            panels.forEach {
                if (!it.save()) {
                    return@addActionListener
                }
            }

            hideAndDispose()
        }
    }
}
