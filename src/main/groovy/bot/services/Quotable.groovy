package bot.services

import bot.ChikuseBot
import bot.utilities.*
import bot.models.*
import groovy.util.logging.Log
import org.jsoup.nodes.*
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.SendMessage

import javax.inject.Inject

@Log
@Service
class Quotable {

    @Inject
    private ChikuseBot bot

    @Inject
    private Browser browser

    @Inject
    private Storage storage

    @Inject
    private Randomiser random

    @Value('${bot.url.random.advice}')
    private String adviceUrl

    @Value('${bot.url.random.quotes}')
    private String quoteUrl

    @Value('${bot.url.random.facts}')
    private String factsUrl

    String getRandomAdvice() {
        def json = browser.getJSON(adviceUrl)
        return json.slip.advice.trim()
    }

    String getRandomQuotation() {
        def json = browser.getJSON(quoteUrl)
        return json.quoteText.trim()
    }

    String getRandomMotivational() {
        return random.item(MOTIVATIONS)
    }

    Strip getRandomFact() {
        Document doc = browser.getHTML(factsUrl)
        Elements facts = doc.select(".fact")
        Element fact = random.item(facts) as Element
        Element link = fact.select(".box a").first()
        Element img = fact.select("figure img").first()
        Element caption = fact.select(".fact_text").first()
        String imgUrl = img ? img.absUrl("src") : "http://pipsum.com/285x152.jpg"
        return new Strip(
                imageUrl: imgUrl,
                caption: caption.text(),
                contentUrl: link.absUrl("href")
        )
    }

    String rollDice() {
        String quote = ""
        int dice = random.integer(2)
        if(dice == 0) quote = getRandomAdvice()
        if(dice == 1) quote = getRandomQuotation()
        if(dice == 2) quote = getRandomMotivational()
        return quote + " " + Emoji.SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES
    }

    @Scheduled(cron = "0 0 7,17 * * *", zone = "Africa/Johannesburg")
    void sendDailyMotivational() {
        storage.allChatUsers.each { e ->
            log.info("broadcast motivational everyday at 7am & 5pm")
            bot.execute(new SendMessage(
                    chatId: e.chatId,
                    text: getRandomMotivational()
            ))
        }
    }

    private static final List<String> MOTIVATIONS = [
            "Keep a gratitude journal and add to it everyday.",
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
            "In the middle of every difficulty lies opportunity.",
            "Big jobs usually go to the men who prove their ability to outgrow small ones.",
            "It is not the mountain we conquer but ourselves.",
            "He who has a why to live can bear almost any how.",
            "Defeat may serve as well as victory to shake the soul and let the glory out.",
            "Begin anywhere.",
            "If you don’t like the road you’re walking, start paving another one.",
            "Whatever you can do, or dream you can, begin it. Boldness has genius, power, and magic in it.",
            "Overcome the notion that you must be regular. It robs you of the chance to be extraordinary",
            "Have faith in yourself and in the future.",
            "It’s kind of fun to do the impossible.",
            "Everything you can imagine is real.",
            "Find your purpose and fling your life out to it. Find a way or make one. Try with all your might. Self-made or never made.",
            "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle.",
            "Most obstacles melt away when we make up our minds to walk boldly through them.",
            "Find out who you are and do it on purpose.",
            "The future belongs to those who believe in the beauty of their dreams.",
            "Do not go where the path may lead. Go instead where there is no path and leave a trail.",
            "What lies behind us and what lies before us are small matters compared to what lies within us.",
            "If we all did the things we are capable of doing we would literally astound ourselves.",
            "You are never too old to set another goal or to dream a new dream.",
            "Dreams are illustrations from the book your soul is writing about you.",
            "Faith is to believe what you do not see; the reward of this faith is to see what you believe.",
            "A happy person is not a person in a certain set of circumstances, but rather a person with a certain set of attitudes.",
            "The only journey is the one within.",
            "The degree of responsibility you take for your life determines how much change you can create in it.",
            "Start where you are, with what you have, and that will always lead you into something greater.",
            "The really great man is the man who makes every man feel great.",
            "Be willing to be a beginner every single morning",
            "Ask what makes you come alive and go do it. Because what the world needs is people who have come alive.",
            "Your own path you make with every step you take. That’s why it’s your path.",
            "If the mind thinks with a believing attitude one can do amazing things.",
            "Anything I’ve ever done that ultimately was worthwhile initially scared me to death.",
            "As fast as each opportunity presents itself, use it! No matter how tiny an opportunity it may be, use it!",
            "The golden opportunity you are seeking is in yourself.",
            "The greatest act of faith some days is to simply get up and face another day.",
            "Great things are not done by impulse, but by a series of small things brought together.",
            "There is real magic in enthusiasm. It spells the difference between mediocrity and accomplishment.",
            "Compassion is language the deaf can hear and the blind can see.",
            "The art of living lies less in eliminating our troubles than in growing with them.",
            "It doesn't matter who you are, where you come from. The ability to triumph begins with you. Always.",
            "Trust yourself. You know more than you think you do.",
            "Go confidently in the direction of your dreams. Live the life you have imagined.",
            "I'm not afraid of storms, for I'm learning to sail my ship.",
            "If you have knowledge, let others light their candles in it.",
            "If your ship doesn't come in, swim out to it.",
            "It is more important to know where you are going than to get there quickly.",
            "Life isn’t about finding yourself. Life is about creating yourself.",
            "The best way to prepare for life is to begin to live.",
            "Don't judge each day by the harvest you reap but by the seeds that you plant.",
            "Write it on your heart that every day is the best day in the year.",
            "Every moment is a fresh beginning.",
            "Without His love I can do nothing, with His love there is nothing I cannot do.",
            "Everything you’ve ever wanted is on the other side of fear.",
            "Begin at the beginning... and go on till you come to the end: then stop.",
            "Dwell on the beauty of life. Watch the stars, and see yourself running with them.",
            "Perfection is not attainable, but if we chase perfection we can catch excellence.",
            "Put your heart, mind, and soul into even your smallest acts. This is the secret of success.",
            "Wherever you go, go with all your heart.",
            "You cannot tailor-make the situations in life but you can tailor-make the attitudes to fit those situations.",
            "There are two ways of spreading light: to be the candle or the mirror that reflects it.",
            "It is never too late to be what you might have been.",
            "The day is what you make it! So why not make it a great one?",
            "The roots of education are bitter, but the fruit is sweet.",
            "Tough times never last, but tough people do.",
            "To be the best, you must be able to handle the worst.",
            "What we’ve got to do is keep hope alive. Because without it we’ll sink.",
            "Learn from the mistakes of others. You can’t live long enough to make them all yourselves!",
            "The key to immortality is first living a life worth remembering",
            "Believe and act as if it were impossible to fail.",
            "Action is the foundational key to all success.",
            "Turn your wounds into wisdom",
            "Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment.",
            "You must be the change you wish to see in the world.",
            "I can't change the direction of the wind, but I can adjust my sails to always reach my destination.",
            "Don’t regret the past, just learn from it.",
            "Keep your face always toward the sunshine - and shadows will fall behind you",
            "The greatest discovery of all time is that a person can change his future by merely changing his attitude."
    ]
}
