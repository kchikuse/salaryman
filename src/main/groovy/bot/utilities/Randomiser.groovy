package bot.utilities

import org.springframework.stereotype.Service

import java.util.concurrent.ThreadLocalRandom

@Service
class Randomiser {

    int integer(int size = 1) {
        return ThreadLocalRandom.current().nextInt(size)
    }

    def item(List<?> items) {
        Collections.shuffle(items)
        int index = integer(items.size())
        return items[ index ]
    }
}
