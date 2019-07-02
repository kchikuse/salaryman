package bot

import bot.utilities.*
import bot.models.*
import bot.services.*

import static bot.models.Emoji.*

class QuotableSpec extends Spec {

    private Quotable quotable

    private Randomiser randomiser

    def setup() {
        randomiser = Stub(Randomiser.class)
        quotable = new Quotable(
                random: randomiser,
                adviceUrl: prop('bot.url.random.advice'),
                factsUrl: prop('bot.url.random.facts'),
                quoteUrl: prop('bot.url.random.quotes')
        )
    }


    def 'Get the correct random fact'() {
        given:
        seedRandomiser(0, 4)

        when:
        Photo fact = quotable.getRandom() as Photo

        then:
        fact.contentUrl == "https://funfactz.com/law-and-crime-facts/mikerowesoft/"
        fact.imageUrl == "https://funfactz.com/res/uploads/fact/3518/mikerowesoft-285.jpg"
        fact.caption == "Microsoft once sued a student named Mike Rowe for registering the domain 'MikeRoweSoft.com'."
    }


    def 'Get the correct random fact without a photo'() {
        given:
        seedRandomiser(0, 5)

        when:
        Photo fact = quotable.getRandom() as Photo

        then:
        fact.contentUrl == "https://funfactz.com/language-facts/the-words-racecar-kayak-and-level-are-the/"
        fact.imageUrl == "http://pipsum.com/285x152.jpg"
        fact.caption == "The words ‘racecar,’ ‘kayak’ and ‘level’ are the same whether they are read left to right or right to left (palindromes)."
    }


    def 'Get the correct random advice'() {
        given:
        seedRandomiser(1)

        expect:
        quotable.getRandom() == "Everything matters, but nothing matters that much."
    }


    def 'Get the correct random quotation'() {
        given:
        seedRandomiser(2)

        expect:
        quotable.getRandom() == "A gem cannot be polished without friction, nor a man perfected without trials."
    }


    def 'Get the correct random motivational'() {
        given:
        seedRandomiser(3, 17)

        expect:
        quotable.getRandom() == "There is no one who is insignificant in the purpose of God."
    }


    def 'GGet the correct random marital advice for wives'() {
        given:
        seedRandomiser(4, 7)

        when:
        String adviceForWife = quotable.getRandom()

        then:
        adviceForWife == WOMENS_SYMBOL.toString() + " Don't expect your husband to be an angel. You would get very tired of him if he were"
    }


    def 'Get the correct random marital advice for husbands'() {
        given:
        seedRandomiser(5, 7)

        when:
        String adviceForHusband = quotable.getRandom()

        then:
        adviceForHusband == MENS_SYMBOL.toString() + " Don't keep all your best jokes for your men friends. Let your wife share them"
    }


    void seedRandomiser(int randomInteger, int randomElement = 0) {
        randomiser.getRandomInteger(5) >> randomInteger
        randomiser.getRandomElement(_ as List<?>) >> { it.first().get(randomElement) }
    }
}