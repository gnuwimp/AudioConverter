/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.FileInfo
import java.io.File
import java.util.prefs.Preferences

//------------------------------------------------------------------------------
var Preferences.convertDest: String
    get() = get("convert_dest", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isDirectory == true) {
            put("convert_dest", f.canonicalPath)
        }
    }

//------------------------------------------------------------------------------
val Preferences.convertDestFile: File
    get() = File(convertDest)

//------------------------------------------------------------------------------
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

//------------------------------------------------------------------------------
val Preferences.convertSrcFile: File
    get() = File(convertSrc)

//------------------------------------------------------------------------------
var Preferences.mergeDest: String
    get() = get("merge_dest", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isDirectory == true) {
            put("merge_dest", f.canonicalPath)
        }
    }

//------------------------------------------------------------------------------
val Preferences.mergeDestFile: File
    get() = File(mergeDest)

//------------------------------------------------------------------------------
var Preferences.mergeImage: String
    get() = get("merge_img", File(System.getProperty("user.home")).canonicalPath)

    set(value) {
        val f = File(value)

        if (f.isFile == true) {
            put("merge_img", f.parentFile.canonicalPath)
        }
    }

//------------------------------------------------------------------------------
val Preferences.mergeImageFile: File
    get() = File(mergeImage)

//------------------------------------------------------------------------------
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

//------------------------------------------------------------------------------
val Preferences.mergeSrcFile: File
    get() = File(mergeSrc)

//------------------------------------------------------------------------------
var Preferences.winHeight: Int
    get() = getInt("win_height", 600)

    set(value) {
        putInt("win_height", value)
    }

//------------------------------------------------------------------------------
var Preferences.winMax: Boolean
    get() = getBoolean("win_max", false)

    set(value) {
        putBoolean("win_max", value)
    }

//------------------------------------------------------------------------------
var Preferences.winWidth: Int
    get() = getInt("win_width", 800)

    set(value) {
        putInt("win_width", value)
    }

//------------------------------------------------------------------------------
var Preferences.winX: Int
    get() = getInt("win_x", 50)

    set(value) {
        putInt("win_x", value)
    }

//------------------------------------------------------------------------------
var Preferences.winY: Int
    get() = getInt("win_y", 50)

    set(value) {
        putInt("win_y", value)
    }

