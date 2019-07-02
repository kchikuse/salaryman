package bot.services

import bot.ChikuseBot
import bot.utilities.Browser
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
class Worship {

    @Inject
    private ChikuseBot bot

    @Inject
    private Settings settings

    @Value('${bot.url.small.straws}')
    private String url

    @Scheduled(cron = "0 03 9 * * MON-FRI", zone = "Africa/Johannesburg")
    void sendDailyDevotional() {
        def message = new SendMessage(text: text()).enableHtml(true)
        settings.allChatUsers.each { e ->
            log.info("broadcasting devotional every weekday at 9:03 am")
            message.chatId = e.chatId
            bot.execute(message)
        }
    }

    private String text() {
        String todayVerse = ""
        String searchStr = new DateTime().toString("MMMM d, y")
        Elements paragraphs = Browser.getHTML(url).select("p")
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
