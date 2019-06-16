package io.golos.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class GenerateAbi(
        val contracts: Array<Contract>)

annotation class Contract(
        val contractName: String,
        val generatedPackageNamePostfix: String
)