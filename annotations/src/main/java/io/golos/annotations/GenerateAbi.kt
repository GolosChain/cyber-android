package io.golos.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class GenerateAbi(
        val contractName: String,
        val generatedPackageNamePostfix: String
)