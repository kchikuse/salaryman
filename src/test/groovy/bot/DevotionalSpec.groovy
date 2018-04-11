package bot

import bot.utilities.*
import bot.models.*
import bot.services.*
import org.telegram.telegrambots.api.methods.send.SendMessage

import static org.joda.time.DateTimeUtils.*

class DevotionalSpec extends Spec {

    ChikuseBot bot

    Storage storage

    Devotional devotional

    def setup(){
        bot = Mock(ChikuseBot.class)
        storage = Stub(Storage.class)
        storage.allChatUsers >> [
                new Chat(id: 1, chatId: 111),
                new Chat(id: 2, chatId: 222) ]

        setCurrentMillisFixed(1517911200000) //February 6, 2018
    }

    def 'Sends the devotional to the correct people'() {
        given:
        devotional = new Devotional(
                browser: new Browser(),
                strawsUrl: prop('bot.url.small.straws'),
                storage: storage,
                bot: bot
        )

        when:
        devotional.sendDailyDevotional()

        then: 'execute should be called 2 times with the correct devotional'
        2 * bot.execute(_ as SendMessage) >> { SendMessage m ->
            assert ["111", "222"].contains(m.chatId)
            assert m.text == 'February 6, 2018: When your peace is disturbed by circumstances, look to Me. I will lead you in the way of tranquility. Even though your soul is in turmoil, I am with you to calm your storm. I will bring you into quiet rest and establish you in a place of peace. Be still and deliberately seek My face as you yield to the work that I am doing in you right now. <em>Psalm 46:10 Be still, and know that I am God.</em>'
        }

        cleanup:
        setCurrentMillisSystem()
    }
}