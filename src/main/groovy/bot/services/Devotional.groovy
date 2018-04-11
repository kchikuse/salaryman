package bot.services

import bot.ChikuseBot
import bot.utilities.*
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
    private Storage storage

    @Inject
    private Browser browser

    @Value('${bot.url.small.straws}')
    private String strawsUrl

    @Scheduled(cron = "0 03 9 * * MON-FRI", zone = "Africa/Johannesburg")
    void sendDailyDevotional() {
        def message = new SendMessage(text: getText()).enableHtml(true)
        storage.allChatUsers.each { e ->
            log.info("broadcasting devotional every weekday at 9:03 am")
            message.chatId = e.chatId
            bot.execute(message)
        }
    }

    private String getText() {
        String todayVerse = ""
        String searchStr = new DateTime().toString("MMMM d, y")
        Elements paragraphs = browser.getHTML(strawsUrl).select("p")
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
