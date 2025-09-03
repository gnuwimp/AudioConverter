/*
 * Copyright 2016 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.swing

import java.awt.Component
import java.awt.Container
import java.awt.Font

/**
 *       _____            _        _
 *      / ____|          | |      (_)
 *     | |     ___  _ __ | |_ __ _ _ _ __   ___ _ __
 *     | |    / _ \| '_ \| __/ _` | | '_ \ / _ \ '__|
 *     | |___| (_) | | | | || (_| | | | | |  __/ |
 *      \_____\___/|_| |_|\__\__,_|_|_| |_|\___|_|
 *
 *
 */

/**
 * Set font for container and all its children recursive.
 */
var Container.fontForAll: Font
    //--------------------------------------------------------------------------
    get() {
        return this.font
    }

    //--------------------------------------------------------------------------
    set(value) {
        this.font = value

        this.components.forEach { component ->
            component?.font = value

            if (component is Container) {
                component.fontForAll = value
            }
        }
    }


/**
 *
 */
fun Container.enable(value: Boolean, container: Container) {
    container.isEnabled = value
    val components: Array<Component> = container.components

    for (component in components) {
        if (component is Container) {
            component.enable(value, component)
        }
    }
}
