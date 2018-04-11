package bot.utilities

import groovy.json.JsonSlurper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

@Service
class Browser {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"

    static Document getHTML(String url) {
        return Jsoup.connect(url)
                .header("User-Agent", USER_AGENT)
                .get()
    }

    static def getJSON(String url) {
        return new JsonSlurper().parseText(Jsoup.connect(url)
                .header("User-Agent", USER_AGENT)
                .ignoreContentType(true)
                .execute()
                .body()
        )
    }
}