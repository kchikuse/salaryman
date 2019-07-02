package bot.services

import bot.models.*
import bot.repositories.*
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class Settings {

    @Inject
    private ChatRepository db

    List<Chat> getAllChatUsers() {
        return db.findAll()
    }

    void saveChatUser(long chatId, long userId) {
        Chat chat = db.findByChatId(chatId)
        if(chat == null) {
            db.save(new Chat(
                    chatId: chatId,
                    userId: userId)
            )
        }
    }

    static final String intro = '''
    `Hello, you can use these commands:`
      
    *$* ` get dollar rand exchange rate`
    
    *$30* ` convert 30 dollars to rands`
    
    *R30* ` convert 30 rands to dollars`
    
    *+* ` get a random Bible verse`
    
    *?* ` get some random advice, trivia or quotation`
        
    *duck* ` check if duck (or any other meat) is biblically clean or unclean`
    
    `If I don't understand, I'll send you a comic strip`
    '''
}
