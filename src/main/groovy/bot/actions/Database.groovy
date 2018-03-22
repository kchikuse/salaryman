package bot.actions

import bot.models.*
import bot.repositories.*
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class Database {

    @Inject
    private ChatRepository repo

    List<Chat> findAll() {
        return repo.findAll()
    }

    void saveChat(long chatId) {
        Chat chat = repo.findByChatId(chatId)
        if(chat == null) {
            chat = new Chat(chatId: chatId)
            repo.save(chat)
        }
    }
}
