/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

@file:Suppress("SameReturnValue")

package gnuwimp.swing

import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JComponent

/***
 *           _  _____                                             _
 *          | |/ ____|                                           | |
 *          | | |     ___  _ __ ___  _ __   ___  _ __   ___ _ __ | |_
 *      _   | | |    / _ \| '_ ` _ \| '_ \ / _ \| '_ \ / _ \ '_ \| __|
 *     | |__| | |___| (_) | | | | | | |_) | (_) | | | |  __/ | | | |_
 *      \____/ \_____\___/|_| |_| |_| .__/ \___/|_| |_|\___|_| |_|\__|
 *                                  | |
 *                                  |_|
 */

/**
 * Create an etched around the widget or turn it off
 */
var JComponent.etchedBorder: Boolean
    get() = false

    set(value) {
        if (value == true) {
            this.border = BorderFactory.createEtchedBorder()
        }
        else {
            this.border = BorderFactory.createEmptyBorder()
        }
    }

/**
 * Create a border around the widget with one pixel or turn it off
 */
var JComponent.lineBorder: Boolean
    get() = false

    set(value) {
        if (value == true) {
            this.border = BorderFactory.createLineBorder(Color.BLACK, 1)
        }
        else {
            this.border = BorderFactory.createEmptyBorder()
        }
    }

/**
 * Create a border around the widget with one pixel or turn it off
 */
var JComponent.lineGrayBorder: Boolean
    get() = false

    set(value) {
        if (value == true) {
            this.border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        }
        else {
            this.border = BorderFactory.createEmptyBorder()
        }
    }

