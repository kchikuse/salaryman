package bot.actions

import bot.ChikuseBot
import bot.common.HttpClient
import bot.common.Utils
import bot.models.*
import groovy.util.logging.Log
import org.jsoup.nodes.*
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.SendMessage

import javax.inject.Inject

import static bot.common.Utils.*
import static java.lang.String.*

@Log
@Service
class Quotable {

    @Inject
    private ChikuseBot bot

    @Inject
    private HttpClient client

    @Inject
    private Database database

    @Value('${bot.url.random.advice}')
    private String adviceUrl

    @Value('${bot.url.random.quotes}')
    private String quoteUrl

    @Value('${bot.url.random.facts}')
    private String factsUrl

    private static final String PLACEHOLDER = "http://pipsum.com/285x152.jpg"

    private static final Emoji emoji = Emoji.SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES

    String getRandomAdvice() {
        def response = client.getJSON(adviceUrl)
        String advice = response.slip.advice
        return format("%s %s", emoji, advice)
    }

    String getRandomQuotation() {
        def response = client.getJSON(quoteUrl)
        String quote = response.quoteText
        return format("%s %s", emoji, quote.trim())
    }

    Strip getRandomFact() {
        Document doc = client.getHTML(factsUrl)
        Elements facts = doc.select(".fact")
        Element fact = getRandomItem(facts) as Element
        Element link = fact.select(".box a").first()
        Element img = fact.select("figure img").first()
        Element caption = fact.select(".fact_text").first()
        String imgUrl = img ? img.absUrl("src") : PLACEHOLDER
        return new Strip(
                imageUrl: imgUrl,
                caption: caption.text(),
                contentUrl: link.absUrl("href")
        )
    }

    @Scheduled(cron = "0 00 11 * * SUN-SAT", zone = "Africa/Johannesburg")
    void sendDailyMotivational() {
        String text = getRandomItem(motivations)
        def message = new SendMessage().enableHtml(true)

        database.findAll().each { e ->
            log.info("broadcast every weekday at 11am")
            message.text = text
            message.chatId = e.chatId
            bot.execute(message)
        }
    }

    private static final List<String> motivations = [
            "Keep a Gratitude journal and add to it everyday.",
            "Tell someone you love them and how much you appreciate them.",
            "Notice the beauty in nature each day.",
            "Nurture the friendships you have, good friends don’t come along every day.",
            "Smile more often.",
            "Watch inspiring videos that will remind you of the good in the world.",
            "Include an act of kindness in your life each day.",
            "Avoid negative media and movies with destructive content.",
            "Call your Mom more often.",
            "Cook meals with love, think of the people you will feed.",
            "Volunteer for organizations that help others.",
            "Don’t gossip or speak badly about anyone.",
            "Spend quality time with your kids.",
            "Remember to compliment your friends and family when they look good.",
            "Write a card to someone you haven’t seen in a while and tell them something nice.",
            "Add to your gratitude list daily, at least one more thing each day.",
            "When you think a negative thought, try to see the positive side in the situation.",
            "Commit to one day a week when you won’t complain about anything.",
            "Try to take note when people do a good job and give recognition when it’s due at work.",
            "Reward effort, if someone does something nice for you, do something nice for them.",
            "Meditate with your gratitude list, giving thanks for all your good fortune.",
            "Live mindfully, not worrying about the past or future.",
            "Thank the people who serve you in the community. The shopkeeper, the bus drivers, etc.",
            "Say thank you for the little things loved ones do for you, things you normally take for granted.",
            "Post quotes and images that remind you to be grateful around your house.",
            "Call into an elderly neighbor and say thank you for their presence in your life.",
            "Call your grandparents and tell them you love them.",
            "Embrace challenges and turn them into opportunities to grow.",
            "Send love to your enemies or people you dislike.",
            "Be thankful when you learn something new.",
            "See the growth opportunity in your mistakes.",
            "Help your friends see the positive side to life.",
            "When times are bad, focus on your friends who are at your side.",
            "When time are good, notice and help others.",
            "Make a gratitude collage, cut out pictures of all the things that you are grateful for.",
            "Make gratitude a part of family life, share it with each other during meal time.",
            "Practice gratitude at the same time everyday to make it a habit.",
            "Focus on your strengths.",
            "Share the benefits of gratitude with family and friends.",
            "Share gratitude each day by posting a tweet, Facebook post"
    ]
}
