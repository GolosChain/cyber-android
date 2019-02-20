package io.golos.commun4J

import com.memtrip.eos.core.crypto.EosPrivateKey
import com.memtrip.eos.core.crypto.EosPublicKey
import io.golos.commun4J.model.AuthType
import io.golos.commun4J.model.CommunName
import io.golos.commun4J.utils.Pair
import io.golos.commun4J.utils.StringSigner

@SuppressWarnings("unused")
fun main(args: Array<String>) {
    val activeUserName = CommunName("destroyer2k")
    val accName = "freya11"
    val pass = "aadgsd23523wtesgdsdt235rsdgtr1"

    val storage = io.golos.commun4J
            .CommunKeyStorage()
            .apply { addAccountKeys(activeUserName, setOf(Pair(AuthType.ACTIVE, "5JagnCwCrB2sWZw6zCvaBw51ifoQuNaKNsDovuGz96wU3tUw7hJ"))) }

    val eos = io.golos.commun4J.Commun4J(keyStorage = storage, config = Commun4JConfig(blockChainHttpApiUrl = "http://159.69.85.233:8888/",
            isPrivateTestNet = true))

    val string = StringSigner.signString("c18b52cda8dee7bd7323cfb13736d29dc05a6668",
            "5JCc3yxnppzNSLKeNyAzdvt38iF3W4i2eFeZakmAo6BZzoJBUDY")


}






