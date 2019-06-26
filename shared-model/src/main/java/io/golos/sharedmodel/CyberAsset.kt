package io.golos.sharedmodel

data class CyberAsset(val amount: String) {

    companion object {
        val assetRegexp = "([0-9]+\\.[0-9]{3})\\s?[a-zA-Z0-9]{1,7}".toRegex()
    }
}