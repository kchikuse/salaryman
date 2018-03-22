package bot.actions

import bot.ChikuseBot
import bot.common.HttpClient
import groovy.util.logging.Log
import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.safety.Whitelist
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.SendMessage

import javax.inject.Inject

@Log
@Service
class Devotional {

    @Inject
    private ChikuseBot bot

    @Inject
    private Database database

    @Inject
    private HttpClient client

    @Value('${bot.url.small.straws}')
    private String strawsUrl

    @Scheduled(cron = "0 00 10 * * MON-FRI", zone = "Africa/Johannesburg")
    void sendDailyDevotional() {
        String text = getHTML()
        def message = new SendMessage().enableHtml(true)
        database.findAll().each { e ->
            log.info("broadcast every weekday at 10am")
            message.text = text
            message.chatId = e.chatId
            bot.execute(message)
        }
    }

    private String getHTML() {
        String todayVerse = ""
        String searchStr = new DateTime().toString("MMMM d, y")
        Elements paragraphs = client.getHTML(strawsUrl).select("p")
        for(Element p in paragraphs) {
            if(p.text().contains(searchStr)) {
                todayVerse = p.html()
                break
            }
        }

        return Jsoup.clean(todayVerse, Whitelist.simpleText())
                .replaceAll("&nbsp;", "")
                .replaceAll("[\n\r]", "")
    }
}
