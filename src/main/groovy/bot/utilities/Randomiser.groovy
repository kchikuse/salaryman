package bot.utilities

import org.springframework.stereotype.Component

import java.util.concurrent.ThreadLocalRandom

@Component
class Randomiser {

    int getRandomInteger(int max = 1) {
        return ThreadLocalRandom.current().nextInt(max)
    }

    def getRandomElement(List<?> elements) {
        Collections.shuffle(elements)
        int index = getRandomInteger(elements.size())
        return elements[ index ]
    }
}
