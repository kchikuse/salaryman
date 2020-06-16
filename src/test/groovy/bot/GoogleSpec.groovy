package bot

import bot.services.*

class GoogleSpec extends Spec {

    Google google

    def setup() {
        google = new Google(
                toRandsUrl: prop('bot.url.dollars.to.rands'),
                toDollarsUrl: prop('bot.url.rands.to.dollars')
        )
    }

    def 'Correctly convert rands to dollars'() {
        expect:
        google.changeRandsToDollars('R') == 'R1 = *0,058 US Dollars*'
    }

    def 'Correctly convert dollars to rands'() {
        expect:
        google.changeDollarsToRands('$') == '$1 = *17,20 Rands*'
    }
}
