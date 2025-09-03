/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.util

import java.awt.image.BufferedImage
import java.io.Closeable
import java.io.File
import javax.imageio.ImageIO

/**
 *       _____ _                      _     _
 *      / ____| |                    | |   | |
 *     | |    | | ___  ___  ___  __ _| |__ | | ___
 *     | |    | |/ _ \/ __|/ _ \/ _` | '_ \| |/ _ \
 *     | |____| | (_) \__ \  __/ (_| | |_) | |  __/
 *      \_____|_|\___/|___/\___|\__,_|_.__/|_|\___|
 *
 *
 */

/**
 *
 */
fun Closeable.safeClose(): Boolean {
    return try {
        this.close()
        true
    }
    catch(e: Exception) {
        false
    }
}

/**
 *      ______ _ _
 *     |  ____(_) |
 *     | |__   _| | ___
 *     |  __| | | |/ _ \
 *     | |    | | |  __/
 *     |_|    |_|_|\___|
 *
 *
 */

/**
 * Check if file is an image
 */
val File.isImage: Boolean
    get() {
        var ret = false

        try {
            if (isFile == true) {
                val bufferedImage: BufferedImage = ImageIO.read(this)

                if (bufferedImage.width > 0 && bufferedImage.height > 0) {
                    ret = true
                }
            }
        }
        catch (e: Exception) {
        }
        finally {
        }

        return ret
    }

/**
 * Delete file.
 */
fun File.remove(checkExist: Boolean = true): Boolean  {
    return if (checkExist == true && exists() == false) {
        false
    }
    else if (delete() == true) {
        true
    }
    else {
        if (this.canWrite() == false) {
            this.setWritable(true)
        }

        delete()
    }
}

/**
 *
 */
fun File.safeRemove(): Boolean {
    return try {
        return remove(false)
    }
    catch(e: Exception) {
        false
    }
}
