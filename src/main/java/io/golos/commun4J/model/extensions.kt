package io.golos.commun4J.model

import java.nio.ByteBuffer

public fun String?.asOptionalStringBytes(): ByteArray {
    return if (this == null) ByteArray(1) { 0 }
    else {
        val bytes = this.toByteArray()
        val buffer = ByteBuffer.allocate(bytes.size + 3)
        buffer.put(1)

        var size : Int = bytes.size
        do {
            var b: Byte = (size and 0x7f).toByte()
            size = size shr 7
            b = (b.toInt() or ((if (size > 0) 1 else 0) shl 7)).toByte()
            buffer.put(b)
        } while (size != 0)

        buffer.put(bytes)
        buffer.rewind()
        val out = ByteArray(buffer.remaining())
        buffer.get(out)
        return out
    }
}