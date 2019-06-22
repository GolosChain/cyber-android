package io.golos.abi.implementation;

import io.golos.annotations.Contract;
import io.golos.annotations.GenerateAbi;

@GenerateAbi(contracts = {
        @Contract(contractName = "gls.publish", generatedPackageNamePostfix = "publish"),
        @Contract(contractName = "gls.social", generatedPackageNamePostfix = "social"),
        @Contract(contractName = "gls.vesting", generatedPackageNamePostfix = "vesting"),
        @Contract(contractName = "cyber.token", generatedPackageNamePostfix = "token"),
        @Contract(contractName = "cyber.domain", generatedPackageNamePostfix = "domain"),
        @Contract(contractName = "gls.ctrl", generatedPackageNamePostfix = "ctrl")}
)

public class SupportedContracts {
    private SupportedContracts() {
    }
}


