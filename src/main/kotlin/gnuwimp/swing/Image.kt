/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.ImageIcon

/***
 *      _____                            _____
 *     |_   _|                          |_   _|
 *       | |  _ __ ___   __ _  __ _  ___  | |  ___ ___  _ __
 *       | | | '_ ` _ \ / _` |/ _` |/ _ \ | | / __/ _ \| '_ \
 *      _| |_| | | | | | (_| | (_| |  __/_| || (_| (_) | | | |
 *     |_____|_| |_| |_|\__,_|\__, |\___|_____\___\___/|_| |_|
 *                             __/ |
 *                            |___/
 */

/**
 * Scale image icon and return new icon
 */
fun ImageIcon.scale(maxSize: Double): ImageIcon {
    return if (iconWidth >= iconHeight) {
        val scale = (maxSize / (iconWidth.toDouble() / iconHeight.toDouble())).toInt()
        ImageIcon(image.getScaledInstance(maxSize.toInt(), scale, Image.SCALE_SMOOTH))
    }
    else {
        val scale = (maxSize / (iconHeight.toDouble() / iconWidth.toDouble())).toInt()
        ImageIcon(image.getScaledInstance(scale, maxSize.toInt(), Image.SCALE_SMOOTH))
    }

}

/***
 *      ____         __  __                  _ _____
 *     |  _ \       / _|/ _|                | |_   _|
 *     | |_) |_   _| |_| |_ ___ _ __ ___  __| | | |  _ __ ___   __ _  __ _  ___
 *     |  _ <| | | |  _|  _/ _ \ '__/ _ \/ _` | | | | '_ ` _ \ / _` |/ _` |/ _ \
 *     | |_) | |_| | | | ||  __/ | |  __/ (_| |_| |_| | | | | | (_| | (_| |  __/
 *     |____/ \__,_|_| |_| \___|_|  \___|\__,_|_____|_| |_| |_|\__,_|\__, |\___|
 *                                                                    __/ |
 *                                                                   |___/
 */

/**
 * Scale image and return an icon
 */
fun BufferedImage.toImageIcon(maxSize: Double): ImageIcon {
    return if (width >= height) {
        val scale = (maxSize / (width.toDouble() / height.toDouble())).toInt()
        ImageIcon(getScaledInstance(maxSize.toInt(), scale, Image.SCALE_SMOOTH))
    }
    else {
        val scale = (maxSize / (height.toDouble() / width.toDouble())).toInt()
        ImageIcon(getScaledInstance(scale, maxSize.toInt(), Image.SCALE_SMOOTH))
    }

}

