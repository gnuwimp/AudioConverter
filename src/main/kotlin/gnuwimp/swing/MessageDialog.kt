/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import gnuwimp.util.toHtml
import java.awt.Dimension
import javax.swing.*

/***
 *      __  __                                _____  _       _
 *     |  \/  |                              |  __ \(_)     | |
 *     | \  / | ___  ___ ___  __ _  __ _  ___| |  | |_  __ _| | ___   __ _
 *     | |\/| |/ _ \/ __/ __|/ _` |/ _` |/ _ \ |  | | |/ _` | |/ _ \ / _` |
 *     | |  | |  __/\__ \__ \ (_| | (_| |  __/ |__| | | (_| | | (_) | (_| |
 *     |_|  |_|\___||___/___/\__,_|\__, |\___|_____/|_|\__,_|_|\___/ \__, |
 *                                  __/ |                             __/ |
 *                                 |___/                             |___/
 */

/**
 * A dialog for displaying some message.
 */
class MessageDialog(parent: JFrame? = PARENT, title: String = TITLE, label: String, icon: Icons = Icons.MESSAGE) : BaseDialog(parent, title, true) {
    private val panel = LayoutPanel(size = Swing.defFont.size / 2)
    private val l1    = JLabel()
    private val l2    = JLabel(label.toHtml)
    private val b1    = JButton("Yes")
    private val b2    = JButton("No")
    private val b3    = JButton("Cancel")
    private var res   = YesNoCancel.YES

    /**
     * Set or get label for button 1.
     * Set text to empty to hide button.
     */
    var textYes: String
        get() = b1.text

        set(value) {
            b1.text = value
        }

    /**
     * Set or get label for button 2.
     * Set text to empty to hide button.
     */
    var textNo: String
        get() = b2.text

        set(value) {
            b2.text = value
        }

    /**
     * Set or get label for button 3.
     * Set text to empty to hide button.
     */
    var textCancel: String
        get() = b3.text

        set(value) {
            b3.text = value
        }

    /**
     *
     */
    init {
        try {
            l1.icon = ImageIcon(icon.loadImage())
            panel.add(l2, x =  12,  y =  1,  w = -1,  h =  -6)
        }
        catch (_: Exception) {
            l1.isVisible = false
            panel.add(l2, x =  1,  y =  1,  w = -1,  h =  -6)
        }

        add(panel)
        panel.add(l1, x =   1,  y =  1,  w =  8,  h =  8)
        panel.add(b1, x = -21,  y = -5,  w = 20,  h =  4)
        panel.add(b2, x = -42,  y = -5,  w = 20,  h =  4)
        panel.add(b3, x = -63,  y = -5,  w = 20,  h =  4)
        pack()
        setModal(true)

        l2.border            = BorderFactory.createEtchedBorder()
        l2.verticalAlignment = SwingConstants.TOP
        size                 = Dimension(width, height)
        fontForAll           = Swing.defFont

        /**
         *
         */
        b1.addActionListener {
            res = YesNoCancel.YES
            hideAndDispose()
        }

        /**
         *
         */
        b2.addActionListener {
            res = YesNoCancel.NO
            hideAndDispose()
        }

        /**
         *
         */
        b3.addActionListener {
            res = YesNoCancel.CANCEL
            hideAndDispose()
        }
    }

    /**
     * Use only one button with Ok.
     */
    fun okOnly(): MessageDialog {
        textYes = "Ok"
        textNo = ""
        textCancel = ""
        return this
    }

    /**
     * Run dialog and set default button then center dialog on screen or parent.
     */
    fun run(def: YesNoCancel = YesNoCancel.YES, w: Int = 40, h: Int = 15): YesNoCancel {
        if (textCancel.isEmpty() == true) {
            b3.isVisible = false
        }

        if (textNo.isEmpty() == true) {
            b2.isVisible = false

            if (textCancel.isEmpty() == false) {
                panel.resize(b3, -42, -5, 20, 4)
            }
        }

        when (def) {
            YesNoCancel.YES -> {
                getRootPane().setDefaultButton(b1)
                b1.grabFocus()
            }
            YesNoCancel.NO -> {
                getRootPane().setDefaultButton(b2)
                b2.grabFocus()
            }
            YesNoCancel.CANCEL -> {
                getRootPane().setDefaultButton(b3)
                b3.grabFocus()
            }
        }

        size = Dimension(Swing.defFont.size * w, Swing.defFont.size * h)
        centerWindow()
        isVisible = true

        return res
    }

    companion object {
        /**
         * Show dialog with yes, no and cancel buttons.
         * Cancel button is the default.
         */
        fun ask(label: String, def: YesNoCancel = YesNoCancel.CANCEL): YesNoCancel {
            val dlg = MessageDialog(label = label, icon = Icons.QUESTION)
            return dlg.run(def = def)
        }

        /**
         * Show dialog with ok and cancel buttons.
         * Cancel button is the default.
         */
        fun askOkCancel(label: String, def: YesNoCancel = YesNoCancel.CANCEL): YesNoCancel {
            val dlg = MessageDialog(label = label, icon = Icons.QUESTION)

            dlg.textYes    = "Ok"
            dlg.textNo     = ""

            return dlg.run(def = def)
        }

        /**
         * Show error dialog with an ok button.
         */
        fun error(label: String) {
            val dlg = MessageDialog(label = label, icon = Icons.ERROR)

            dlg.textYes    = "Ok"
            dlg.textCancel = ""
            dlg.textNo     = ""

            dlg.run(def = YesNoCancel.YES)
        }

        /**
         * Show info dialog with an ok button.
         */
        fun info(label: String) {
            val dlg = MessageDialog(label = label, icon = Icons.MESSAGE)

            dlg.textYes    = "Ok"
            dlg.textCancel = ""
            dlg.textNo     = ""

            dlg.run(def = YesNoCancel.YES)
        }

        /**
         * Show warning dialog with an ok button.
         */
        fun warning(label: String) {
            val dlg = MessageDialog(label = label, icon = Icons.WARNING)

            dlg.textYes    = "Ok"
            dlg.textCancel = ""
            dlg.textNo     = ""

            dlg.run(def = YesNoCancel.YES)
        }
    }
}
