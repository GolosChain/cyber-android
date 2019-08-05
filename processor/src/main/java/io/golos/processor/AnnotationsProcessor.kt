package io.golos.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.golos.annotations.ExcludeFromGeneration
import io.golos.annotations.GenerateCoroutinesAdapter
import io.golos.annotations.ShutDownMethod
import org.jetbrains.annotations.Nullable
import java.io.File
import java.util.concurrent.ExecutorService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.tools.Diagnostic
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

@AutoService(Processor::class)
class AnnotationsProcessor : AbstractProcessor() {
    private lateinit var messager: Messager
    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnv: RoundEnvironment): Boolean {
        messager = processingEnv.messager
        roundEnv.getElementsAnnotatedWith(GenerateCoroutinesAdapter::class.java)
                .forEach {
                    createClass(it)
                }
        return true
    }

    private fun createClass(forElement: Element) {
        val fileName = "${forElement.simpleName}CoroutinesAdapter"

        val packageName = processingEnv.elementUtils.getPackageOf(forElement).toString()

        val wrappedObjectName = forElement.simpleName.toString().toLowerCase()
        val wrappedObjectClass = ClassName(packageName, forElement.toString())

        val executorName = "executor"
        val executorClass = ExecutorService::class.java

        val builder = FileSpec.builder(packageName, fileName)
                .addImport(java.util.concurrent.Executors::class.java, "")
                .addImport("kotlin.coroutines", "resume", "resumeWithException", "suspendCoroutine")

                .addType(TypeSpec.classBuilder(fileName)
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter(
                                        wrappedObjectName, wrappedObjectClass)
                                .addParameter(ParameterSpec
                                        .builder(executorName, executorClass)
                                        .defaultValue("Executors.newFixedThreadPool(64)")
                                        .build())
                                .build())
                        .addProperty(PropertySpec.builder(wrappedObjectName,
                                wrappedObjectClass, KModifier.PRIVATE)
                                .initializer(wrappedObjectName)
                                .build())

                        .addProperty(PropertySpec.builder(executorName,
                                executorClass, KModifier.PRIVATE)
                                .initializer(executorName)
                                .build())
                        .also {
                            val typeMirror = forElement.asType()
                            val fieldTypeElement = (processingEnv.typeUtils.asElement(typeMirror) as TypeElement).enclosedElements
                            fieldTypeElement.forEach { element ->
                                if (element.kind == ElementKind.METHOD
                                        && !element.modifiers.contains(Modifier.PRIVATE)
                                        && element.getAnnotation(ExcludeFromGeneration::class.java) == null) {
                                    if (element.getAnnotation(ShutDownMethod::class.java) != null) {
                                        it.addFunction(FunSpec.builder("shutdown")
                                                .addCode("""
$executorName.shutdown()
$wrappedObjectName.shutdown()
                                                """.trimIndent())
                                                .build())
                                        return@forEach
                                    }
                                    val functionName = element.simpleName.toString()
                                    val paramsOfAFunction = ArrayList<String>(6)
                                    val returnType: TypeName
                                    val tenTabs = "\t\t\t\t\t\t\t\t\t\t"
                                    it
                                            .addFunction(FunSpec.builder(functionName)//adding function name
                                                    .addModifiers(KModifier.SUSPEND)
                                                    .also { funcBuilder ->
                                                        element as ExecutableElement
                                                        returnType = javaToKotlinTypeName(element.returnType.asTypeName())
                                                        element.parameters.forEach { variableElement ->
                                                            //function parameters
                                                            val parameterName = variableElement.simpleName.toString()
                                                            paramsOfAFunction.add(parameterName)

                                                            val parameterTypeName = variableElement.asType().asTypeName()
                                                            val finalElementType = javaToKotlinTypeName(parameterTypeName)
                                                            val typeNameWithResolvedNullability = finalElementType.resolveNullability(variableElement)
                                                            funcBuilder.addParameter(parameterName,
                                                                    typeNameWithResolvedNullability)

                                                        }

                                                    }
                                                    .addCode("""
return suspendCoroutine {
            $executorName.execute {
                try {
                    val result =
                            $wrappedObjectName
                            .$functionName${paramsOfAFunction.joinToString(",\n$tenTabs", "(", ")")}
                    it.resume(result)
                } catch (e: Exception) {
                    it.resumeWithException(e)
                }
            }
        }
 """)
                                                    .returns(returnType)
                                                    .build())
                                }
                            }
                        }
                        .build())
        val stubName = "/a.java"
        val filePath = processingEnv.filer.createSourceFile("a").name.replace(stubName, "")
        val file = builder.build()
        file.writeTo(File(filePath))
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GenerateCoroutinesAdapter::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private fun javaToKotlinSimpleTypeName(typeName: TypeName): TypeName {
        val kotlinClassName = JavaToKotlinClassMap
                .INSTANCE
                .mapJavaToKotlin(FqName(typeName.toString()))
                ?.asSingleFqName()?.asString()
        return kotlinClassName?.let { ClassName.bestGuess(it) } ?: typeName
    }

    private fun TypeName.resolveNullability(element: Element): TypeName {
        return if (element.getAnnotation(Nullable::class.java) != null) this.copy(true)
        else this
    }

    private fun javaToKotlinTypeName(element: TypeName): TypeName {
        return if (element is ParameterizedTypeName) {
            (javaToKotlinSimpleTypeName(element.rawType) as ClassName).parameterizedBy(* element.typeArguments.map {
                javaToKotlinTypeName(it)
            }.toTypedArray())
        } else javaToKotlinSimpleTypeName(element)
    }
}

