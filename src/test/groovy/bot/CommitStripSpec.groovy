package bot

import bot.common.HttpClient
import bot.models.Strip
import bot.actions.*

class CommitStripSpec extends Spec {

    def 'Get the correct image url from the commitstrip website'() {
        given:
        CommitStrip commit = new CommitStrip(
                client: new HttpClient(),
                url: prop('bot.url.commitstrip')
        )

        when:
        Strip strip = commit.getStrip()

        then:
        strip.contentUrl == null
        strip.caption == "All the same | CommitStrip"
        strip.imageUrl == "https://www.commitstrip.com/wp-content/uploads/2017/01/Strip-Régulateur-de-vitesse-english650-final2.jpg"
    }
}
