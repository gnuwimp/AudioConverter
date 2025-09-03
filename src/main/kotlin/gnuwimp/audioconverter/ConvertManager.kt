/*
 * Copyright 2021 - 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

import gnuwimp.util.Task
import gnuwimp.util.TaskManager

/***
 *       _____                          _   __  __
 *      / ____|                        | | |  \/  |
 *     | |     ___  _ ____   _____ _ __| |_| \  / | __ _ _ __   __ _  __ _  ___ _ __
 *     | |    / _ \| '_ \ \ / / _ \ '__| __| |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '__|
 *     | |___| (_) | | | \ V /  __/ |  | |_| |  | | (_| | | | | (_| | (_| |  __/ |
 *      \_____\___/|_| |_|\_/ \___|_|   \__|_|  |_|\__,_|_| |_|\__,_|\__, |\___|_|
 *                                                                    __/ |
 *                                                                   |___/
 */

/**
 * Thread manager for all tasks.
 */
class ConvertManager(tasks: List<Task>, maxThreads: Int = 1, onError: Execution = Execution.CONTINUE, onCancel: Execution = Execution.STOP_JOIN) :
    TaskManager(tasks, maxThreads, onError, onCancel) {

    companion object {
        private var countDecoded  = 0L

        /**
         * Add byte count.
         */
        @Synchronized fun add(value: Long) {
            countDecoded += value
        }

        /**
         *
         */
        @Synchronized fun clear() {
            countDecoded  = 0
        }
    }

    /**
     * Create progress message.
     */
    override fun message(threadCount: Int): String {
        var decoded = if (countDecoded < 1_000_000_000) "Decoded total ${countDecoded / 1_000_000} MB" else "Decoded total %.2f GB".format(countDecoded.toFloat() / 1_000_000_000.0)

        decoded += "\n"
        decoded += super.message(threadCount)

        return decoded
    }
}
