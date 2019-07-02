package bot.services

import bot.models.Card
import bot.models.Station
import groovy.json.JsonSlurper
import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import static bot.utilities.Browser.*
import static org.joda.time.DateTime.*
import static org.jsoup.Connection.Method.*

@Service
class Gautrain {

    Station start

    @Value('${bot.url.gautrain.card}')
    String cardUrl

    @Value('${bot.url.gautrain.login}')
    String loginUrl

    @Value('${bot.url.gautrain.stations}')
    String stationsUrl

    @Value('${bot.url.gautrain.route}')
    String routeUrl

    @Value('${bot.gautrain.username}')
    String username

    @Value('${bot.gautrain.password}')
    String password

    Card getCard() {
        def id = getSessionId() ?: ""
        String body = Jsoup.connect(cardUrl)
                .header("Cookie", "SESSION=" + id)
                .method(GET)
                .ignoreContentType(true)
                .execute()
                .body()

        def doc = new JsonSlurper().parseText(body)

        return new Card(
                description: doc.description.first(),
                cashBalanceCents: doc.cashBalance.baseAmount.first(),
                fullCardNumber: doc.fullCardNumber.first()
        )
    }

    String getSessionId() {
        String csrf = getCsrf()
        return Jsoup.connect(loginUrl)
                .method(POST)
                .data("username", username)
                .data("password", password)
                .data("_csrf", csrf)
                .execute()
                .cookie("SESSION")
    }

    String getCsrf() {
        return getHTML(loginUrl)
                .select("#frmMapRoutes input[name=_csrf]")
                .first()
                .attr("value")
    }

    Gautrain getNextTrainFrom(Station start) {
        this.start = start
        return this
    }

    String To(Station end) {
        def response = getJSON(getRouteUrl(this.start, end))
        def itinerary = response.itineraries.first()
        def leg = itinerary.legs.first()
        def typeOfTrain = leg.vehicle.designation
        def headsign = leg.vehicle.headsign
        def fare = (int) leg.fare.cost.amount

        DateTime departureTime = parse(itinerary.departureTime as String)
        DateTime arrivalTime = parse(itinerary.arrivalTime as String)
        long diffInMinutes = (arrivalTime.getMillis() - departureTime.getMillis()) / 1000 / 60
        return """*${typeOfTrain}* to ${headsign}, *${diffInMinutes}* minutes, *${fare}* rands"""
    }

    Station getStation(String name) {
        def stations = getJSON(stationsUrl)

        for(def station in stations) {
            String sname = station.name
            if(sname.toLowerCase().startsWith(name.toLowerCase())) {
                BigDecimal [] coords = station.geometry.coordinates
                return new Station(
                        name: sname,
                        longitude: coords[0],
                        latitude: coords[1]
                )
            }
        }

        return null
    }

    String getRouteUrl(Station start, Station end) {
        return String.format(routeUrl, start.longitude, start.latitude, end.longitude, end.latitude)
    }

    boolean isStation(String name) {
        return getStation(name) != null
    }
}
