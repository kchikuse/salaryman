package bot

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.*
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import static com.github.tomakehurst.wiremock.client.WireMock.*

@SpringBootTest
@ActiveProfiles("tests")
abstract class Spec extends Specification {

    static boolean setUpfinished = false

    static WireMockServer wireMockServer = new WireMockServer(options().port(20000))

    def setup() {
        if (setUpfinished) {
            return
        }

        wireMockServer.start()

        mockUrl("/Facts", "facts.html")
        mockUrl("/Quote", "quote.json")
        mockUrl("/Random", "comic.html")
        mockUrl("/Advice", "advice.json")
        mockUrl("/Scripture", "scripture.html")
        mockUrl("/Straws", "small-straws.html")
        mockUrl("/Rand", "google-rand-to-dollar.html")
        mockUrl("/Dollar", "google-dollar-to-rand.html")

        setUpfinished = true
    }

    def mockUrl(String url, String file) {
        wireMockServer.stubFor(get(url).willReturn(ok().withBodyFile(file)))
    }

    def prop(String key) {
        return ResourceBundle.getBundle("application-tests").getString(key)
    }
}
