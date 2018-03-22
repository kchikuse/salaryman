package bot.actions

import bot.common.HttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.inject.Inject
import static java.lang.String.format

@Service
class Google {

    @Inject
    private HttpClient client

    @Value('${bot.url.dollars.to.rands}')
    private String toRandsUrl

    @Value('${bot.url.rands.to.dollars}')
    private String toDollarsUrl

    String convertDollarsToRands(String text) {
        String dollars = text.replace('$', '') ?: 1
        String rands = convert(toRandsUrl, dollars)
        return format('$%s = *%s Rands*', dollars, rands)
    }

    String convertRandsToDollars(String text) {
        String rands = text.replace('R', '') ?: 1
        String dollars = convert(toDollarsUrl, rands)
        return format('R%s = *%s US Dollars*', rands, dollars)
    }

    private String convert(String url, String amount) {
        return client.getHTML(format(url, amount))
                .select("#knowledge-currency__tgt-amount")
                .first()
                .text()
    }
}
