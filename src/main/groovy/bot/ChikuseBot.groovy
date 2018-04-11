package bot

import bot.models.*
import bot.services.*
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.*
import org.telegram.telegrambots.api.objects.*
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.bots.TelegramLongPollingBot

import javax.inject.Inject
import java.util.logging.Level

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*

@Log
@Service
@Scope(SCOPE_SINGLETON)
class ChikuseBot extends TelegramLongPollingBot {

    private long chatId

    @Value('${bot.token}')
    private String token

    @Value('${bot.username}')
    private String username

    @Inject
    private Bible bible

    @Inject
    private Google google

    @Inject
    private Storage storage

    @Inject
    private Quotable quotes

    @Inject
    private CommitStrip comic

    @Override
    void onUpdateReceived(Update update) {
        try {

            if (!update.hasMessage() || !update.message.hasText()) {
                return
            }

            Message msg = update.message
            String input = msg.text.toUpperCase()
            String firstName = msg.chat.firstName
            chatId = msg.chatId

            storage.saveChatUser(chatId)

            if (["/START", "HELP", "HELLO"].any { input == it }) {
                textReply(storage.help(firstName))
                return
            }

            if (input.startsWith('!')) {
                textReply(bible.randomVerse)
                return
            }

            if (input.startsWith('??')) {
                photoReply(quotes.randomFact)
                return
            }

            if (input.startsWith('?')) {
                textReply(quotes.rollDice())
                return
            }

            if (input.startsWith('$')) {
                textReply(google.changeDollarsToRands(input))
                return
            }

            if (input.startsWith('R')) {
                textReply(google.changeRandsToDollars(input))
                return
            }

            def foods = bible.checkFood(input)
            if (foods.size() > 0) {
                textReply(bible.format(foods))
                return
            }

            photoReply(comic.strip)
        }
        catch (Exception e) {
            log.log(Level.SEVERE, e.message, e)
        }
    }

    private void textReply(String text) {
        def message = new SendMessage()
        message.text = text
        message.chatId = chatId
        message.enableMarkdown(true)
        execute(message)
    }

    private void photoReply(Strip strip) {
        def photo = new SendPhoto()
        photo.chatId = chatId
        photo.photo = strip.imageUrl
        photo.caption = strip.caption

        if(strip.contentUrl) {
            def buttons = [ new InlineKeyboardButton(text: "Read more", url: strip.contentUrl) ]
            photo.replyMarkup = new InlineKeyboardMarkup(keyboard: [ buttons ])
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
