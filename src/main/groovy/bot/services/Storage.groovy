package bot.services

import bot.models.*
import bot.repositories.*
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class Storage {

    @Inject
    private ChatRepository repo

    List<Chat> getAllChatUsers() {
        return repo.findAll()
    }

    void saveChatUser(long chatId) {
        Chat chat = repo.findByChatId(chatId)
        if(chat == null) {
            repo.save(new Chat(chatId: chatId))
        }
    }

    static String help(String firstName) {
        return """
        `Hello ${firstName}, you can use these commands:`
          
        *\$* ` get dollar rand exchange rate`
        
        *\$50* ` convert 50 dollars to rands`
        
        *R500* ` convert 500 rands to dollars`
        
        *?* ` get a random quote`
        
        *??* ` get some random trivia`
        
        *!* ` get a random Bible verse`
        
        *pork* ` check if pork (or any other meat) is biblically clean or unclean`
        
        `If I don't understand your command I'll send you a commit strip` ${Emoji.FACE_WITH_TEARS_OF_JOY}
        """
    }
}
