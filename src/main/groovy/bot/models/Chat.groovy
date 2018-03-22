package bot.models

import javax.persistence.*
import static javax.persistence.GenerationType.*

@Entity
@Table(name = "CHATS")
class Chat {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    long id

    long chatId
}