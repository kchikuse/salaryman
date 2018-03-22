package bot

import bot.models.*
import bot.actions.*
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.*
import org.telegram.telegrambots.api.objects.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import javax.inject.Inject

import static bot.common.Utils.*
import static java.util.logging.Level.*
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*

@Log
@Service
@Scope(value = SCOPE_SINGLETON)
class ChikuseBot extends TelegramLongPollingBot {

    @Value('${bot.token}')
    private String token

    @Value('${bot.username}')
    private String username

    @Inject
    private Bible bible

    @Inject
    private Google google

    @Inject
    private Database database

    @Inject
    private Quotable quotable

    @Inject
    private CommitStrip commit

    @Inject
    private Kosher kosher

    @Override
    void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.message.hasText()) {

                Message msg = update.message
                long chatId = msg.chatId
                String text = msg.text.toUpperCase()

                database.saveChat(chatId)

                if (text == "/START" || text == 'HELP') {
                    sendText(chatId, help(msg.chat.firstName))
                    return
                }

                if (text.startsWith('#')) {
                    sendText(chatId, bible.randomVerse())
                    return
                }

                if (text.startsWith('??')) {
                    sendPhoto(chatId, quotable.getRandomFact())
                    return
                }

                if (text.startsWith('?')) {
                    sendText( chatId, getRandomNumber() ? quotable.getRandomAdvice() : quotable.getRandomQuotation() )
                    return
                }

                if (text.startsWith('$')) {
                    String rands = google.convertDollarsToRands(text)
                    sendText(chatId, rands)
                    return
                }

                if (text.startsWith('R')) {
                    String dollars = google.convertRandsToDollars(text)
                    sendText(chatId, dollars)
                    return
                }

                String analysis = kosher.check(text)
                if(analysis != null) {
                    sendText(chatId, analysis)
                    return
                }

                sendPhoto(chatId, commit.getStrip())
            }
        }
        catch (Exception e) {
            log.log(SEVERE, e.message, e)
        }
    }

    def sendText(long chatId, String text) {
        def message = new SendMessage()
        message.chatId = chatId
        message.text = text
        message.enableMarkdown(true)
        execute(message)
    }

    def sendPhoto(long chatId, Strip strip) {
        def photo = new SendPhoto()
        photo.chatId = chatId
        photo.photo = strip.imageUrl
        photo.caption = strip.caption

        if(strip.contentUrl) {
            photo.replyMarkup = moreButton(strip)
        }

        sendPhoto(photo)
    }

    @Override
    String getBotToken() {
        return token
    }

    @Override
    String getBotUsername() {
        return username
    }
}
