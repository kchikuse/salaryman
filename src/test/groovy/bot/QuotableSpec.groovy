package bot

import bot.common.HttpClient
import bot.models.Strip
import bot.actions.*
import spock.lang.Ignore

class QuotableSpec extends Spec {

    Quotable quotable

    def setup() {
        quotable = new Quotable(
                client: new HttpClient(),
                adviceUrl: prop('bot.url.random.advice'),
                factsUrl: prop('bot.url.random.facts'),
                quoteUrl: prop('bot.url.random.quotes')
        )
    }

    def 'Get the correct advice text from the website'() {
        expect:
        quotable.getRandomAdvice() == "😄 Everything matters, but nothing matters that much."
    }

    def 'Get the correct quote'() {
        expect:
        quotable.getRandomQuotation() == "😄 A gem cannot be polished without friction, nor a man perfected without trials."
    }

    @Ignore
    def 'Get a random fact'() {
        given:
        //when(utils.getRandomNumber(8)).thenReturn(0)
        quotable = new Quotable(
                client: new HttpClient(),
                factsUrl: prop('bot.url.random.facts')
        )

        when:
        Strip fact = quotable.getRandomFact()

        then:
        fact.contentUrl == "https://funfactz.com/law-and-crime-facts/mikerowesoft/"
        fact.imageUrl == "https://funfactz.com/res/uploads/fact/3518/mikerowesoft-285.jpg"
        fact.caption == "Microsoft once sued a student named Mike Rowe for registering the domain 'MikeRoweSoft.com'."
    }

    @Ignore
    def 'Get a random fact without a photo'() {
        given:
        //when(utils.getRandomNumber(8)).thenReturn(0)
        quotable = new Quotable(
                client: new HttpClient(),
                factsUrl: prop('bot.url.random.facts')
        )

        when:
        Strip fact = quotable.getRandomFact()

        then:
        fact.contentUrl == "https://funfactz.com/language-facts/the-words-racecar-kayak-and-level-are-the/"
        fact.imageUrl == "http://pipsum.com/285x152.jpg"
        fact.caption == "The words ‘racecar,’ ‘kayak’ and ‘level’ are the same whether they are read left to right or right to left (palindromes)."
    }
}