package io.golos.abi.implementation

import com.memtrip.eos.abi.writer.AbiWriter
import io.golos.annotations.Contract
import io.golos.annotations.GenerateAbi

@GenerateAbi([Contract("gls.publish", "publish"),
    Contract("gls.social", "social"),
    Contract("gls.vesting", "vesting"),
    Contract("cyber.token", "token"),
    Contract("cyber.domain", "domain"),
    Contract("gls.ctrl", "ctrl")])
@AbiWriter
class Cyber




