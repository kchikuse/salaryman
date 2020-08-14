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
        String fact = quotable.getRandom()

        then:
        fact == "Children laugh about 400 times a day, while adults laugh on average only 15 times a day."
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
