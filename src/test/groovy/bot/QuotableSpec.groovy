package bot

import bot.utilities.*
import bot.models.*
import bot.services.*

class QuotableSpec extends Spec {

    Quotable quotable

    Randomiser random

    def setup() {
        random = Stub(Randomiser.class)
        quotable = new Quotable(
                browser: new Browser(),
                random: random,
                adviceUrl: prop('bot.url.random.advice'),
                factsUrl: prop('bot.url.random.facts'),
                quoteUrl: prop('bot.url.random.quotes')
        )
    }

    def 'Get the correct advice text from the website'() {
        expect:
        quotable.randomAdvice == "Everything matters, but nothing matters that much."
    }

    def 'Get the correct quote'() {
        expect:
        quotable.randomQuotation == "A gem cannot be polished without friction, nor a man perfected without trials."
    }

    def 'Get a random fact'() {
        given:
        random.item(_ as List<?>) >> { it.first().get(4) }
        quotable = new Quotable(
                browser: new Browser(),
                random: random,
                factsUrl: prop('bot.url.random.facts')
        )

        when:
        Strip fact = quotable.randomFact

        then:
        fact.contentUrl == "https://funfactz.com/law-and-crime-facts/mikerowesoft/"
        fact.imageUrl == "https://funfactz.com/res/uploads/fact/3518/mikerowesoft-285.jpg"
        fact.caption == "Microsoft once sued a student named Mike Rowe for registering the domain 'MikeRoweSoft.com'."
    }

    def 'Get a random fact without a photo'() {
        given:
        random.item(_ as List<?>) >> { it.first().get(5) }
        quotable = new Quotable(
                browser: new Browser(),
                random: random,
                factsUrl: prop('bot.url.random.facts')
        )

        when:
        Strip fact = quotable.randomFact

        then:
        fact.contentUrl == "https://funfactz.com/language-facts/the-words-racecar-kayak-and-level-are-the/"
        fact.imageUrl == "http://pipsum.com/285x152.jpg"
        fact.caption == "The words ‘racecar,’ ‘kayak’ and ‘level’ are the same whether they are read left to right or right to left (palindromes)."
    }
}