import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Assert
import org.junit.Test

class Flags {

    @Test
    fun testOne() {
        val moshi = Moshi.Builder().build()

        val codesString = this::class.java.getResource("/posts.json").readText()
        Assert.assertNotNull(codesString)


        val phoneCodesList = moshi.adapter<List<CountryPhoneCode>>(Types.newParameterizedType(List::class.java, CountryPhoneCode::class.java))
                .fromJson(codesString)!!

        val countryCodesString = this::class.java.getResource("/codes.json").readText()
        Assert.assertNotNull(countryCodesString)

        val countryCodes = moshi.adapter<List<CountryCodes>>(Types.newParameterizedType(List::class.java, CountryCodes::class.java))
                .fromJson(countryCodesString)!!


        val out = phoneCodesList.map { countryWithPhoneCode ->
            val countryWithCountryCode = countryCodes.find { it.name == countryWithPhoneCode.label }
            if (countryWithCountryCode == null)
                println("cannot find country with name ${countryWithPhoneCode.label}")
            Country(countryWithPhoneCode.code,
                    countryWithCountryCode!!.code,
                    countryWithPhoneCode.label,
                    "https://www.countryflags.io/${countryWithCountryCode.code}/flat/64.png")
        }

        val outString = moshi.adapter<List<Country>>(Types.newParameterizedType(List::class.java,
                Country::class.java)).toJson(out)
    }
}

//https://www.countryflags.io/sz/flat/64.png
class CountryPhoneCode(
        @Json(name = "code")
        val code: Int,
        val label: String) {
    override fun toString(): String {
        return "CountryPhoneCode(countryCode=$code, label='$label')"
    }
}

class CountryCodes(val name: String,
                   val code: String) {
    override fun toString(): String {
        return "CountryCodes(name='$name', countryCode='$code')"
    }
}

class Country(
        val countryPhoneCode: Int,
        val countryCode: String,
        val countryName: String,
        val thumbNailUrl: String //size 64*64
)

