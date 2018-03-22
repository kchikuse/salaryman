package bot

import bot.common.HttpClient
import bot.actions.*

class GoogleSpec extends Spec {

    Google google

    def setup() {
        google = new Google(
                client: new HttpClient(),
                toRandsUrl: prop('bot.url.dollars.to.rands'),
                toDollarsUrl: prop('bot.url.rands.to.dollars')
        )
    }

    def 'Correctly convert rands to dollars'() {
        expect:
        google.convertRandsToDollars('R') == 'R1 = *0,084 US Dollars*'
    }

    def 'Correctly convert dollars to rands'() {
        expect:
        google.convertDollarsToRands('$') == '$1 = *11,95 Rands*'
    }
}
