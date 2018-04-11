package bot

import bot.utilities.Browser
import bot.models.Strip
import bot.services.*

class CommitStripSpec extends Spec {

    CommitStrip commit

    def setup() {
        commit = new CommitStrip(
                browser: new Browser(),
                url: prop('bot.url.commitstrip')
        )
    }

    def 'Get the correct data from the commitstrip website'() {
        when:
        Strip strip = commit.strip

        then:
        strip.contentUrl == null
        strip.caption == "All the same | CommitStrip"
        strip.imageUrl == "https://www.commitstrip.com/wp-content/uploads/2017/01/Strip-Régulateur-de-vitesse-english650-final2.jpg"
    }
}
