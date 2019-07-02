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
import java.util.regex.Pattern

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*

@Log
@Service
@Scope(SCOPE_SINGLETON)
class ChikuseBot extends TelegramLongPollingBot {

    long chatId

    @Value('${bot.token}')
    String token

    @Value('${bot.username}')
    String username

    @Inject
    Bible bible

    @Inject
    Google google

    @Inject
    Settings settings

    @Inject
    Quotable quotes

    @Inject
    Comic comic

    @Inject
    Gautrain gautrain

    @Override
    void onUpdateReceived(Update update) {
        try {

            if (!update.hasMessage() || !update.message.hasText()) {
                return
            }

            Message msg = update.message
            String input = msg.text.toUpperCase()
            chatId = msg.chatId

            settings.saveChatUser(chatId, msg.from.id)

            if (["/START", "HELP"].any { input == it }) {
                sendTextReply(settings.intro)
                return
            }

            // Gautrain
            if (input == "/G") {
                Station a = gautrain.getStation("CENTURION")
                Station b = gautrain.getStation("PARK")
                sendTextReply(gautrain.getNextTrainFrom(a).To(b))
                return
            }

            // quotations & advice
            if (input.startsWith('?')) {
                def quote = quotes.random
                if (quote.class == Photo.class) {
                    sendPhotoReply(quote as Photo)
                } else {
                    sendTextReply(quote as String)
                }
                return
            }

            // dollars to rands
            if (input.startsWith('$')) {
                sendTextReply(google.changeDollarsToRands(input))
                return
            }

            // rands to dollars
            if(Pattern.compile(/R[0-9.]+/).matcher(input)){
                sendTextReply(google.changeRandsToDollars(input))
                return
            }

            // bible verse
            if (input.startsWith('+')) {
                sendTextReply(bible.verse)
                return
            }

            // check if this meat is good to eat
            def foods = bible.searchForFoods(input)
            if ( ! foods.empty) {
                String response = foods.collect { it.toString() }.join("\n")
                sendTextReply(response)
                return
            }

            // if all else fails, show a comic
            sendPhotoReply(comic.strip)
        }
        catch (Exception e) {
            log.log(Level.SEVERE, e.message, e)
        }
    }

    def sendTextReply(String text) {
        def message = new SendMessage()
        message.text = text
        message.chatId = chatId
        message.enableMarkdown(true)
        execute(message)
    }

    def sendPhotoReply(Photo photo) {
        def message = new SendPhoto()
        message.chatId = chatId
        message.photo = photo.imageUrl
        message.caption = photo.caption

        if(photo.contentUrl) {
            def buttons = [ new InlineKeyboardButton(text: "Read more", url: photo.contentUrl) ]
            message.replyMarkup = new InlineKeyboardMarkup(keyboard: [ buttons ])
        }

        sendPhoto(message)
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
