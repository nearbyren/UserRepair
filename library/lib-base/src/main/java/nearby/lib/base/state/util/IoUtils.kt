package nearby.lib.base.state.util

import java.io.Closeable
import java.io.IOException

object IoUtils {

    fun close(vararg closeables: Closeable?) {
        if (closeables.isNotEmpty()) {
            for (io in closeables) {
                if (io != null) {
                    try {
                        io.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}