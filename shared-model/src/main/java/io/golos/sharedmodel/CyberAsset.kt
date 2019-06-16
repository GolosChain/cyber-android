package io.golos.sharedmodel

data class CyberAsset(val amount: String) {
    init {
        if (!amount.matches(assetRegexp)) {
            throw IllegalArgumentException("wrong currency format. Must have 3 points precision, like 12.000 or 0.001")
        }
    }

    companion object {
        val assetRegexp = "([0-9]+\\.[0-9]{3})".toRegex()
    }
}