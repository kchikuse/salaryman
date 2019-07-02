package bot

import bot.models.Station
import bot.services.Gautrain

class GautrainSpec extends Spec {

    Gautrain gautrain

    def setup() {
        gautrain = new Gautrain(
                username: "",
                password: "",
                cardUrl: prop('bot.url.gautrain.card'),
                routeUrl: prop('bot.url.gautrain.route'),
                stationsUrl: prop('bot.url.gautrain.stations'),
                loginUrl: prop('bot.url.gautrain.login'))
    }

    /*
    // wiremock doesn't accept POST by default
    def 'Gets the correct balance on my card'() {
        given:
        Card card = gautrain.getCard()

        expect:
        card.cashBalanceCents == 1000
        card.fullCardNumber == '4988167853710000069391105'
        card.description == 'Cool Card'
        card.toString() == '*Cool Card*\n4988167853710000069391105\nhas got *10* rands'
    } */

    def 'Gets the correct next trip'() {
        given:
        Station centurion = gautrain.getStation("Centurion")
        Station park = gautrain.getStation("Park")

        expect:
        gautrain.getNextTrainFrom(centurion).To(park) == '*4 Car* to Park, *14* minutes, *34* rands'
    }

    def 'Gets the correct CSR token from login page'() {
        expect:
        gautrain.getCsrf() == '0a345be8-cd55-4968-a6df-017fec2c6d00'
    }

    def 'Gets the correct station given the name'() {
        given:
        Station station = gautrain.getStation("Centurion")

        expect:
        station.name == 'Centurion'
        station.longitude == 28.1897
        station.latitude == -25.85161
    }

    def 'Gets the correct station given the first 3 characters'() {
        given:
        Station hatfield = gautrain.getStation("HAT")

        expect:
        hatfield.name == 'Hatfield'
        hatfield.longitude == 28.23794
        hatfield.latitude == -25.74762
    }

    def 'Correctly validates a station'() {
        expect:
        gautrain.isStation("CEN")
        gautrain.isStation("HAT")
        gautrain.isStation("MAR")
        gautrain.isStation("MID")
        gautrain.isStation("OR")
        gautrain.isStation("PARK")
        gautrain.isStation("PRE")
        gautrain.isStation("RHO")
        gautrain.isStation("ROS")
        gautrain.isStation("SAN")
    }

    def 'Gets the correct route url'() {
        given:
        gautrain.routeUrl = 'https://route?orgLng=%s&orgLat=%s&dstLng=%s&dstLat=%s&publicOperators=&isParking=false&earliestArrival=&isGeometryReturned=true&isImmutable=false'
        Station hatfield = new Station(longitude: 28.23794, latitude: -25.74762)
        Station centurion = new Station(longitude: 28.1897, latitude: -25.85161)

        when:
        String routeUrl = gautrain.getRouteUrl(hatfield, centurion)

        then:
        routeUrl == 'https://route?orgLng=28.23794&orgLat=-25.74762&dstLng=28.1897&dstLat=-25.85161&publicOperators=&isParking=false&earliestArrival=&isGeometryReturned=true&isImmutable=false'
    }
}
