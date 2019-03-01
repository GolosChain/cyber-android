package io.golos.cyber4j.model

import com.memtrip.eos.abi.writer.ByteWriter
import com.memtrip.eos.abi.writer.bytewriter.DefaultByteWriter

/**  [ByteWriter] implementation, that fixes bug in utf-8 serialisation implementation in [DefaultByteWriter]
 * */
class CyberwayByteWriter(private val defaultWriter: DefaultByteWriter = DefaultByteWriter(512)) : ByteWriter by defaultWriter {
    override fun putString(value: String) {
        val byteArray = value.toByteArray()
        defaultWriter.putVariableUInt(byteArray.size.toLong())
        defaultWriter.putBytes(byteArray)
    }
}