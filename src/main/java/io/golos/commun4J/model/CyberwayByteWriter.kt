package io.golos.commun4J.model

import com.memtrip.eos.abi.writer.ByteWriter
import com.memtrip.eos.abi.writer.bytewriter.DefaultByteWriter

class CyberwayByteWriter(private val defaultWriter: DefaultByteWriter = DefaultByteWriter(512)) : ByteWriter by defaultWriter {
    override fun putString(value: String) {
        //  putVariableUInt(value.length.toLong())
        //        putBytes(value.toByteArray())
        val byteArray = value.toByteArray()
        defaultWriter.putVariableUInt(byteArray.size.toLong())
        defaultWriter.putBytes(byteArray)
    }
}