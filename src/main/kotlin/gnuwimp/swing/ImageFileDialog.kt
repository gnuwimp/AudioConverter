/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter



/***
 *      _____  _           _        _____                _
 *     |  __ \| |         | |      |  __ \              (_)
 *     | |__) | |__   ___ | |_ ___ | |__) | __ _____   ___  _____      __
 *     |  ___/| '_ \ / _ \| __/ _ \|  ___/ '__/ _ \ \ / / |/ _ \ \ /\ / /
 *     | |    | | | | (_) | || (_) | |   | | |  __/\ V /| |  __/\ V  V /
 *     |_|    |_| |_|\___/ \__\___/|_|   |_|  \___| \_/ |_|\___| \_/\_/
 *
 *
 */

/**
 * A photo preview for the dialog.
 */
private class PhotoPreview(fileChooser: JFileChooser) : JComponent() {
    private var thumbnail: ImageIcon? = null
    private var file:      File?      = null

    init {
        preferredSize = Dimension(320, 320)

        fileChooser.addPropertyChangeListener { pce ->
            var update = false
            val prop   = pce.propertyName

            if (JFileChooser.DIRECTORY_CHANGED_PROPERTY == prop) {
                file   = null
                update = true
            }
            else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY == prop) {
                file   = pce.newValue as File?
                update = true
            }

            if (update == true) {
                thumbnail = null

                if (isShowing) {
                    loadImage()
                    repaint()
                }
            }
        }
    }

    /**
     * Load image from disk.
     */
    private fun loadImage() {
        if (file != null) {
            try {
                val bufferedImage = ImageIO.read(file)

                if (bufferedImage != null) {
                    thumbnail = bufferedImage.toImageIcon(320.0)
                }
            }
            catch (_: Exception) {
                thumbnail = null
            }
        }
    }

    /**
     * Draw image icon.
     */
    override fun paintComponent(graphics: Graphics) {
        if (thumbnail == null) {
            loadImage()
        }

        if (thumbnail != null) {
            val w = thumbnail?.iconWidth ?: 0
            val h = thumbnail?.iconHeight ?: 0
            var x = width / 2 - w / 2
            var y = height / 2 - h / 2

            if (y < 0) {
                y = 0
            }

            if (x < 5) {
                x = 5
            }

            thumbnail?.paintIcon(this, graphics, x, y)
        }
    }
}

/***
 *      _____                            ______ _ _      _____  _       _
 *     |_   _|                          |  ____(_) |    |  __ \(_)     | |
 *       | |  _ __ ___   __ _  __ _  ___| |__   _| | ___| |  | |_  __ _| | ___   __ _
 *       | | | '_ ` _ \ / _` |/ _` |/ _ \  __| | | |/ _ \ |  | | |/ _` | |/ _ \ / _` |
 *      _| |_| | | | | | (_| | (_| |  __/ |    | | |  __/ |__| | | (_| | | (_) | (_| |
 *     |_____|_| |_| |_|\__,_|\__, |\___|_|    |_|_|\___|_____/|_|\__,_|_|\___/ \__, |
 *                             __/ |                                             __/ |
 *                            |___/                                             |___/
 */

/**
 * A file dialog for choosing an image.
 * File filter for the most used images is set.
 * And photo preview are turned on.
 */
class ImageFileDialog(val path: String, val parentWindow: Component? = null) : JFileChooser() {
    companion object {
        val IMAGE_TYPES: Array<String> = ImageIO.getWriterFormatNames()
    }

    init {
        val file  = File(path)

        currentDirectory = if (file.isDirectory == false) {
            File(System.getProperty("user.home"))
        }
        else {
            file
        }

        fileFilter = object : FileFilter() {
            /**
             *
             */
            override fun getDescription() = "Image Files"

            /**
             * Accept image file that java has support for.
             * And directories.
             */
            override fun accept(file: File): Boolean {
                return file.isDirectory == true || IMAGE_TYPES.any {
                    file.name.endsWith(suffix = it, ignoreCase = true)
                }
            }

        }

        accessory  = PhotoPreview(this)
        fontForAll = Swing.defFont
    }

    /**
     * Show dialog and return a file  object or null.
     */
    val file: File?
        get() {
            return if (showOpenDialog(parentWindow) == APPROVE_OPTION && selectedFile != null) {
                selectedFile
            }
            else {
                null
            }
        }
}
