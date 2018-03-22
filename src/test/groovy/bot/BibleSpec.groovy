package bot

import bot.common.HttpClient
import bot.actions.*

class BibleSpec extends Spec {

    def 'Gets the correct random verse'() {
        given:
        Bible bible = new Bible(
                client: new HttpClient(),
                bibleUrl: prop('bot.url.bible')
        )

        when:
        String verse = bible.randomVerse()

        then:
        verse == 'Today in the town of David a Savior has been born to you; he is the Messiah, the Lord. - *Luke 2:11*'
    }
}