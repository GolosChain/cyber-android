import com.memtrip.eos.abi.writer.DataCompress
import com.memtrip.eos.http.rpc.model.account.request.AccountName
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.moshi.Moshi
import io.golos.cyber4j.model.AbiStruct
import io.golos.cyber4j.model.CyberName
import io.golos.cyber4j.model.EosAbi
import io.golos.cyber4j.utils.CyberNameAdapter
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import java.io.File
import java.lang.reflect.Type
import java.math.BigInteger

class MiscTests {
    val moshi = Moshi.Builder().add(CyberName::class.java, CyberNameAdapter()).build()

    @Test
    fun getAbi() {
        val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()


        val resp = okHttpClient.newCall(Request.Builder()
                .post(RequestBody.create(MediaType.get("application/json"), moshi.toJson(AccountName("gls.publish"))))
                .url("http://46.4.96.246:8888/v1/chain/get_abi")
                .build())
                .execute()

        val respBody = resp.body()!!.string()
        println(respBody)

        val abiObject = moshi.fromJson<EosAbi>(respBody)!!

        val file = File(".", "gls.json")

        file.writeText(moshi.toJson(abiObject))
    }


    private inline fun <reified T> Moshi.fromJson(json: String) = adapter<T>(T::class.java).fromJson(json)
    private inline fun <reified T> Moshi.toJson(`object`: T) = adapter<T>(T::class.java).toJson(`object`)

    @Test
    fun convertAbi() {
        val buildInTypes = hashMapOf(*ClassName("kotlin", "String").createVariations("string"),
                *(CyberName::class.java.asTypeName() as ClassName).createVariations("name"),
                * (arrayOf("int8", "uint8", "int16", "uint16", "int32", "uint32", "int64", "uint64")
                        .map { it to intStringToClassName(it) })
                        .toList()
                        .let { listOfPairs ->
                            val copy = ArrayList<Pair<String, TypeName>>(listOfPairs.size)
                            listOfPairs.forEach { pair ->
                                copy.addAll(pair.second.createVariations(pair.first))
                            }
                            copy.toTypedArray()
                        })
                .apply {
                    putAll(ClassName("kotlin", "Byte").createVariations("bool"))
                    putAll(ClassName("kotlin", "String").createVariations("asset"))
                    putAll(ClassName("kotlin", "Byte").createVariations("symbol_code"))
                    putAll(ClassName("kotlin", "String").createVariations("symbol"))
                }

        val abi = moshi.fromJson<EosAbi>(File(".", "gls.json").readText())!!
        abi.abi.types.forEach {
            val type = it.type
            val resolvedClassName =
                    if (!buildInTypes.containsKey(it.type) && type.matches(integerRegex)) {
                        intStringToClassName(it.type)
                    } else buildInTypes[it.type]
                            ?: throw IllegalStateException("type ${it.type} not found")
            buildInTypes.putAll(resolvedClassName.createVariations(it.new_type_name))
        }

        abi.abi.structs.forEach {
            buildInTypes.putAll(ClassName("", it.generateClassName()).createVariations(it.name))
        }


        abi.abi.structs.forEach { abiStruct ->
            val className = abiStruct.generateClassName()

            val classFile = FileSpec.builder("", "${abiStruct.name.capitalize()}Struct")
                    .addType(TypeSpec.classBuilder(className)
                            .addModifiers(KModifier.DATA)
                            .primaryConstructor(FunSpec.constructorBuilder()
                                    .also { builder: FunSpec.Builder ->
                                        abiStruct.fields.forEach { stuctField ->
                                            builder.addParameter(stuctField.name,
                                                    buildInTypes[stuctField.type]
                                                            ?: throw java.lang.IllegalStateException("cannot find ClassName for type ${stuctField.type}"))
                                        }

                                    }
                                    .build())
                            .also { builder: TypeSpec.Builder ->
                                abiStruct.fields.forEach { stuctField ->
                                    builder.addProperty(
                                            PropertySpec
                                                    .builder(stuctField.name,
                                                            buildInTypes[stuctField.type]!!,
                                                            KModifier.PRIVATE)
                                                    .initializer(stuctField.name)
                                                    .build())


                                }
                            }
                            .also { builder: TypeSpec.Builder ->
                                abiStruct.fields.forEach { structField ->
                                    builder.addProperty(
                                            PropertySpec.builder("get${structField.name.capitalize()}",
                                                    buildInTypes[structField.type]!!)
                                                    .getter(FunSpec.getterBuilder()
                                                            .addStatement("return ${structField.name}")
                                                            .build())
                                                    .build()
                                    )
                                }
                            }
                            .build())
                    .build()

            classFile.writeTo(File(".", "src${File.separator}test${File.separator}kotlin${File.separator}generated"))
        }
    }

    private fun AbiStruct.generateClassName() = "${this.name.capitalize()}Struct"

    private fun TypeName.createVariations(forName: String): Array<Pair<String, TypeName>> =
            arrayOf(forName to this,
                    forName.plus("?") to this.copy(true),
                    "$forName[]" to ClassName("", "List").parameterizedBy(this))


    private val integerRegex = Regex("(u)?int(\\d){1,3}")

    fun intStringToClassName(integerString: String): ClassName {
        if (!integerString.matches(integerRegex)) throw IllegalArgumentException("string $integerString not matches with $integerRegex regexp")
        val intSize = Integer.parseInt(integerString.replace("\\D".toRegex(), ""))
        return when {
            intSize <= 8 -> Byte::class.java.asClassName()
            intSize <= 16 -> Short::class.java.asClassName()
            intSize <= 32 -> Int::class.java.asClassName()
            intSize <= 64 -> Long::class.java.asClassName()
            else -> BigInteger::class.java.asClassName()
        }
    }

    private fun Type.asClassName() = asTypeName() as ClassName
}