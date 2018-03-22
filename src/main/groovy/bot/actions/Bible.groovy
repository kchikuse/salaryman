package bot.actions

import bot.common.HttpClient
import groovy.util.logging.Log
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.inject.Inject

import static java.lang.String.*

@Log
@Service
class Bible {

    @Inject
    private HttpClient client

    @Value('${bot.url.bible}')
    private String bibleUrl

    String randomVerse() {
        Document doc = client.getHTML(bibleUrl)
        String text =  doc.select(".bibleVerse").first().ownText()
        String verse = doc.select(".bibleChapter a").first().text()
        return format("%s - *%s*", text, verse)
    }
}
