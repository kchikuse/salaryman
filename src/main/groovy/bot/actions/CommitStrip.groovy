package bot.actions

import bot.common.HttpClient
import bot.models.Strip
import org.jsoup.nodes.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class CommitStrip {

    @Inject
    private HttpClient client

    @Value('${bot.url.commitstrip}')
    private String url

    Strip getStrip() {
        Document doc = client.getHTML(url)
        Element img = doc.select("div.entry-content p img").first()
        return new Strip(caption:  doc.title(), imageUrl: img.attr("src"))
    }
}