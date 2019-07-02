package bot

import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

abstract class Spec extends Specification {

    static boolean setUpfinished = false

    static WireMockServer wireMockServer

    def setup() {
        if (setUpfinished) {
            return
        }

        wireMockServer = new WireMockServer(options().dynamicPort())
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            void run() {
                wireMockServer.stop()
            }
        }))

        wireMockServer.start()
        setUpfinished = true
    }

    static String prop(String key) {
        return "http://localhost:${wireMockServer.port()}/${ResourceBundle.getBundle("application-tests").getString(key)}"
    }
}
