package io.golos.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class CoroutinesAdapterProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnv: RoundEnvironment): Boolean {
        println("process annotations $annotations")
        roundEnv.getElementsAnnotatedWith(GenerateCoroutinesAdapter::class.java)
                .forEach {
                    createClass(it)
                }

        return true
    }

    private fun createClass(forElement: Element) {
        val fileName = "${forElement.simpleName}CoroutinesAdapter"
        val packageName = processingEnv.elementUtils.getPackageOf(forElement).toString()

        val file = FileSpec.builder(packageName, fileName)
                .addType(TypeSpec.classBuilder(fileName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter(
                                        forElement.simpleName.toString().toLowerCase(),
                                        ClassName(packageName, forElement.toString()))
                                .build())
                        .addProperty()
                        .build())
                .build()

        val s = File.separator

        file.writeTo(File("build${s}generated${s}source${s}kapt${s}main"))
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenerateCoroutinesAdapter::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}
