/*
 * Copyright 2025 gnuwimp@gmail.com
 * Released under the GNU General Public License v3.0
 */

package gnuwimp.audioconverter

/***
 *      ______ _ _      ______      _     _   ______                    _   _
 *     |  ____(_) |    |  ____|    (_)   | | |  ____|                  | | (_)
 *     | |__   _| | ___| |__  __  ___ ___| |_| |__  __  _____ ___ _ __ | |_ _  ___  _ __
 *     |  __| | | |/ _ \  __| \ \/ / / __| __|  __| \ \/ / __/ _ \ '_ \| __| |/ _ \| '_ \
 *     | |    | | |  __/ |____ >  <| \__ \ |_| |____ >  < (_|  __/ |_) | |_| | (_) | | | |
 *     |_|    |_|_|\___|______/_/\_\_|___/\__|______/_/\_\___\___| .__/ \__|_|\___/|_| |_|
 *                                                               | |
 *                                                               |_|
 */


/**
 * Exception for when a destination files exist and it is not allowed to overwrite it.
 */
class FileExistException : Exception {
    /**
     *
     */
    constructor(message: String) : super(message)
}
