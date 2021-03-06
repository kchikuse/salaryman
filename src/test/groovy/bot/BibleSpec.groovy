package bot

import bot.services.*

class BibleSpec extends Spec {

    Bible bible

    def setup() {
        bible = new Bible(url: prop('bot.url.bible'))
    }

    def 'Gets the correct random verse'() {
        when:
        String verse = bible.getVerse()

        then:
        verse == 'Today in the town of David a Savior has been born to you; he is the Messiah, the Lord. - *Luke 2:11*'
    }

    def "lamb is clean"() {
        when: "i search for lamb"
        def foods = bible.searchForFoods("lamb")

        then: "one clean food is returned"
        foods.findAll { it.clean }.size() == 1

        and: "no unclean foods are returned"
        foods.findAll { ! it.clean }.isEmpty()
    }

    def "lamb is clean - case insensitivity"() {
        when: "i search for lamb"
        def foods = bible.searchForFoods("laMB")

        then: "one clean food is returned"
        foods.findAll { it.clean }.size() == 1

        and: "no unclean foods are returned"
        foods.findAll { ! it.clean }.isEmpty()
    }

    def "raven is unclean"() {
        when: "i search for Raven"
        def foods = bible.searchForFoods("Raven")

        then: "no clean foods are returned"
        foods.findAll { it.clean }.isEmpty()

        and: "1 unclean foods are returned"
        def unclean = foods.findAll { ! it.clean }
        unclean.size() == 1
        unclean[0].name == "Raven"
    }

    def "pork is unclean"() {
        when: "i search for pork"
        def foods = bible.searchForFoods("pork")

        then: "no clean foods are returned"
        foods.findAll { it.clean }.isEmpty()

        and: "2 unclean foods are returned"
        def unclean = foods.findAll { ! it.clean }
        unclean.size() == 2
        unclean[0].name == "Pork"
        unclean[1].name == "Pork sausage"
    }

    def "multiple results"() {
        when: "i search for pig"
        def foods = bible.searchForFoods("pig")

        then: "one clean food is returned"
        def clean = foods.findAll { it.clean }
        clean.size() == 1
        clean[0].name == "Pig Fish"

        and: "3 unclean foods are returned"
        def unclean = foods.findAll { ! it.clean }
        unclean.size() == 3
        unclean[0].name == "Guinea Pig"
        unclean[1].name == "Pig"
        unclean[2].name == "Pig Lard"
    }

    def "food is not known"() {
        when: "the food is not in our list"
        def foods = bible.searchForFoods("whooperscooper")

        then: "nothing is returned"
        foods.isEmpty()
    }

    def "markdown formatting"() {
        given: "a list of foods"
        def foods = bible.searchForFoods("pig")

        when: "they are formatted into a string"
        String markdown = foods.collect { it.toString() }.join("\n")

        then: "the string is correct markdown"
        markdown == "Pig Fish is *CLEAN*\nGuinea Pig is *UNCLEAN*\nPig is *UNCLEAN*\nPig Lard is *UNCLEAN*"
    }
}
