package bot

import groovy.util.logging.Log
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.stereotype.Component
import org.springframework.context.event.EventListener
import org.telegram.telegrambots.*
import javax.inject.Inject

@Log
@Component
class OnStart {

    @Inject
    private ChikuseBot bot

    @EventListener(ApplicationReadyEvent.class)
    private void start() {
        new TelegramBotsApi().registerBot(bot)
    }
}

