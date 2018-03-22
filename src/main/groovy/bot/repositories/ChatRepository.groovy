package bot.repositories

import org.springframework.data.jpa.repository.JpaRepository
import bot.models.*

interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByChatId(Long chatId)
}