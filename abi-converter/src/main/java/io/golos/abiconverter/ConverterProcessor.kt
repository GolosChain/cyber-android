package io.golos.abiconverter

import com.google.auto.service.AutoService
import io.golos.annotations.GenerateAbi
import io.golos.sharedmodel.CyberName
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class ConverterProcessor : AbstractProcessor() {
    private lateinit var messager: Messager
    private val kaptKotlinGeneratedOption = "kapt.kotlin.generated"
    private lateinit var txt: File

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        txt = File(processingEnv!!.options[kaptKotlinGeneratedOption])

    }

    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnv: RoundEnvironment): Boolean {
        messager = processingEnv.messager
        messager.printMessage(Diagnostic.Kind.WARNING, "txt = $txt")
        roundEnv.getElementsAnnotatedWith(GenerateAbi::class.java)
                .forEach {
                    createSources(it as TypeElement)
                }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenerateAbi::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun createSources(typeElement: TypeElement) {
        val annotation = typeElement.getAnnotation(GenerateAbi::class.java)!!
        val abi = getAbi(CyberName(annotation.contractName))
        val postfix = annotation.generatedPackageNamePostfix
        val stubName = "/b.java"
        val filePath = File(processingEnv.filer.createSourceFile("b").name.replace(stubName, ""), "")
        val packageName = "${processingEnv.elementUtils.getPackageOf(typeElement)}.$postfix"
        messager.printMessage(Diagnostic.Kind.WARNING, """creating abi for ${annotation.contractName}
            | contract with filepath $filePath and packageName = $packageName """.trimMargin())
        generateClasses(abi, packageName, filePath)
    }
}