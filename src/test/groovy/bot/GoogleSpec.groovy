package bot

import bot.utilities.*
import bot.services.*

class GoogleSpec extends Spec {

    Google google

    def setup() {
        google = new Google(
                browser: new Browser(),
                toRandsUrl: prop('bot.url.dollars.to.rands'),
                toDollarsUrl: prop('bot.url.rands.to.dollars')
        )
    }

    def 'Correctly convert rands to dollars'() {
        expect:
        google.changeRandsToDollars('R') == 'R1 = *0,084 US Dollars*'
    }

    def 'Correctly convert dollars to rands'() {
        expect:
        google.changeDollarsToRands('$') == '$1 = *11,95 Rands*'
    }
}
