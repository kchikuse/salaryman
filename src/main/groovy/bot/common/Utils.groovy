package bot.common

import bot.models.Strip
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton

class Utils {

    static int getRandomNumber(int size = 1) {
        return new Random().nextInt(size)
    }

    static def getRandomItem(Collection collection) {
        return collection[ getRandomNumber(collection.size()) ]
    }

    static help (String firstName) {
        return """
        
        Hello ${firstName}, you can use these commands:
        
        *?* - gets a random quotation
        *??* - returns some random trivia
        *#* - returns a random Bible verse
        *\$* - converts 1 dollar to rands
        *R* - converts 1 rand to dollars
        *\$20* - converts 20 dollars to rands
        *R200* - converts 200 rand amount to dollars
        
        Salaryman can also tell you if an animal is clean or unclean 
        """
    }

    static moreButton(Strip pic) {
        def buttons = [ new InlineKeyboardButton(text: "Read more", url: pic.contentUrl) ]
        return new InlineKeyboardMarkup(keyboard: [
                buttons
        ])
    }

    static boolean match(String search, String key) {
        key = key.toUpperCase()
        search = search.toUpperCase()
        return search == key || search.startsWith(key)
    }
}
