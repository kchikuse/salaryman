package bot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.telegram.telegrambots.ApiContextInitializer

@EnableScheduling
@SpringBootApplication
class Application {
    static void main(String [] args) {
        ApiContextInitializer.init()
        SpringApplication.run(Application.class, args)
    }
}
