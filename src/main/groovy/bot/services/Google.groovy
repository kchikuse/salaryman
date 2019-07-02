package bot.services

import bot.utilities.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import static java.lang.String.*

@Service
class Google {

    @Value('${bot.url.dollars.to.rands}')
    private String toRandsUrl

    @Value('${bot.url.rands.to.dollars}')
    private String toDollarsUrl

    String changeDollarsToRands(String text) {
        String dollars = text.replace('$', '') ?: 1
        String rands = convert(toRandsUrl, dollars)
        return format('$%s = *%s Rands*', dollars, rands)
    }

    String changeRandsToDollars(String text) {
        String rands = text.replace('R', '') ?: 1
        String dollars = convert(toDollarsUrl, rands)
        return format('R%s = *%s US Dollars*', rands, dollars)
    }

    private static String convert(String url, String amount) {
        return Browser.getHTML(format(url, amount))
                .select("#knowledge-currency__tgt-amount")
                .first()
                .text()
    }
}
