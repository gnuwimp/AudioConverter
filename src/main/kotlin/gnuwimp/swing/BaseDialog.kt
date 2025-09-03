/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Image
import java.awt.Point
import java.awt.Toolkit
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.JFrame

/**
 * Enums for message dialog.
 */
enum class YesNoCancel {
    YES,
    NO,
    CANCEL,
}

/**
 * Enums for input dialogs.
 */
enum class OkCancel {
    OK,
    CANCEL,
}

/**
 * Icon type.
 */
enum class Icons {
    MESSAGE,
    WARNING,
    ERROR,
    QUESTION,
    EMPTY,
}

/**
 * Load image form resources.
 */
fun Icons.loadImage(): Image {
    val img = when (this) {
        Icons.MESSAGE -> ImageIO.read(ClassLoader.getSystemResource("gnuwimp/swing/message.png"))
        Icons.WARNING -> ImageIO.read(ClassLoader.getSystemResource("gnuwimp/swing/warning.png"))
        Icons.ERROR -> ImageIO.read(ClassLoader.getSystemResource("gnuwimp/swing/error.png"))
        Icons.QUESTION -> ImageIO.read(ClassLoader.getSystemResource("gnuwimp/swing/question.png"))
        Icons.EMPTY -> throw Exception("Empty icon requested!")
    }

    return if (Swing.defFont.size == 14) {
        img
    }
    else {
        val factor = Swing.defFont.size.toDouble() / 14.0
        val scale  = (56.0 * factor).toInt()
        img.getScaledInstance(scale, scale, Image.SCALE_SMOOTH)
    }
}

/**
 * Base dialog window.
 * Dialog window can't be closed with window button.
 */
abstract class BaseDialog(val parentWindow: JFrame? = PARENT, title: String = TITLE, modal: Boolean = true) : JDialog(parentWindow, title, modal) {
    companion object {
        var PARENT: JFrame? = null
        var TITLE: String = ""
    }

    init {
         defaultCloseOperation = DO_NOTHING_ON_CLOSE
    }

    /**
     * Center dialog window.
     * If it has no parent then it will be centered on screen, otherwise it will be centered on parent window.
     */
    fun centerWindow() {
        val sdim = Toolkit.getDefaultToolkit().screenSize
        val dim  = size
        var xpos = sdim.width / 2
        var ypos = sdim.height / 2

        if (parentWindow != null) {
            val pdim = parentWindow.size

            if (pdim.height > 100 && pdim.width > 100) {
                xpos = parentWindow.location.x + pdim.width / 2
                ypos = parentWindow.location.y + pdim.height / 2
            }
        }

        location = Point(xpos - (dim.width / 2), ypos - (dim.height / 2))
    }

    /**
     * Center dialog window on top of parent window.
     * Works only if dialog has a parent.
     */
    fun centerWindowOnTop() {
        require(parentWindow != null)

        val dim  = parentWindow.size
        val xpos = parentWindow.location.x + dim.width / 2
        val ypos = parentWindow.location.y

        location = Point(xpos - (width / 2), ypos)
    }

    /**
     * Hide dialog and delete resources.
     */
    fun hideAndDispose() {
        isVisible = false
        dispose()
    }
}
