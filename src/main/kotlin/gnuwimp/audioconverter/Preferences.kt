/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import java.io.File
import java.util.prefs.Preferences

/***
 *      _____           __
 *     |  __ \         / _|
 *     | |__) | __ ___| |_ ___ _ __ ___ _ __   ___ ___  ___
 *     |  ___/ '__/ _ \  _/ _ \ '__/ _ \ '_ \ / __/ _ \/ __|
 *     | |   | | |  __/ ||  __/ | |  __/ | | | (_|  __/\__ \
 *     |_|   |_|  \___|_| \___|_|  \___|_| |_|\___\___||___/
 *
 *
 */

/**
 * Destination directory for converting files.
 */
var Preferences.convertDest: String
    get() = get("convert_dest", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isDirectory == true) {
            put("convert_dest", f.canonicalPath)
        }
    }

/**
 * Destination directory (as a File) for converting files.
 */
val Preferences.convertDestFile: File
    get() = File(convertDest)

/**
 * Source directory for converting files.
 */
var Preferences.convertSrc: String
    get() = get("convert_src", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = FileInfo(value)

        if (f.isDir == true) {
            put("convert_src", f.filename)
        }
        else if (f.isFile == true) {
            put("convert_src", f.path)
        }
    }

/**
 * Source directory (as a File) for converting files.
 */
val Preferences.convertSrcFile: File
    get() = File(convertSrc)

/**
 * Main font size.
 */
var Preferences.fontSize: Int
    get() = getInt("font_size", 14)

    set(value) {
        putInt("font_size", value)
    }

/**
 * Destination directory for merging files.
 */
var Preferences.mergeDest: String
    get() = get("merge_dest", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isDirectory == true) {
            put("merge_dest", f.canonicalPath)
        }
    }

/**
 * Destination directory (as a File) for merging files.
 */
val Preferences.mergeDestFile: File
    get() = File(mergeDest)

/**
 * Cover image directory for merging files.
 */
var Preferences.mergeImage: String
    get() = get("merge_img", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isFile == true) {
            put("merge_img", f.parentFile.canonicalPath)
        }
    }

/**
 * Cover image directory (as a File) for merging files.
 */
val Preferences.mergeImageFile: File
    get() = File(mergeImage)

/**
 * Source directory for merging files.
 */
var Preferences.mergeSrc: String
    get() = get("merge_src", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = FileInfo(value)

        if (f.isDir == true) {
            put("merge_src", f.filename)
        }
        else if (f.isFile == true) {
            put("merge_src", f.path)
        }
    }

/**
 * Source directory (as a File) for merging files.
 */
val Preferences.mergeSrcFile: File
    get() = File(mergeSrc)

/**
 *
 */
var Preferences.winHeight: Int
    get() = getInt("win_height", 600)

    set(value) {
        putInt("win_height", value)
    }

/**
 *
 */
var Preferences.winMax: Boolean
    get() = getBoolean("win_max", false)

    set(value) {
        putBoolean("win_max", value)
    }

/**
 *
 */
var Preferences.winWidth: Int
    get() = getInt("win_width", 800)

    set(value) {
        putInt("win_width", value)
    }

/**
 *
 */
var Preferences.winX: Int
    get() = getInt("win_x", 50)

    set(value) {
        putInt("win_x", value)
    }

/**
 *
 */
var Preferences.winY: Int
    get() = getInt("win_y", 50)

    set(value) {
        putInt("win_y", value)
    }

