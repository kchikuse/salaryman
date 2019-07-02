package bot.services

import bot.models.*
import org.jsoup.nodes.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import static bot.utilities.Browser.*

@Service
class Comic {

    @Value('${bot.url.commitstrip}')
    private String url

    Photo getStrip() {
        Document doc = getHTML(url)
        Element img = doc.select("div.entry-content p img").first()
        return new Photo(caption: doc.title(), imageUrl: img.attr("src"))
    }
}