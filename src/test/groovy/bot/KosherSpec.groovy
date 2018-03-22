package bot

import bot.actions.*

class KosherSpec extends Spec {

    Kosher kosher

    def setup() {
        kosher = new Kosher()
    }

    def 'lamb is clean'() {
        expect:
        def lamb = kosher.check('lamb')
        lamb.clean.size() == 1
        lamb.unclean.size() == 0
        lamb.clean.first() == 'Lamb is *CLEAN*'
    }

    def 'lamb is clean casing'() {
        expect:
        def lamb = kosher.check('LaMb')
        lamb.clean.size() == 1
        lamb.unclean.size() == 0
        lamb.clean.first() == 'Lamb is *CLEAN*'
    }

    def 'pork is unclean'() {
        expect:
        def pork = kosher.check('pork')
        pork.clean.size() == 0
        pork.unclean.size() == 2
        pork.unclean.get(0) == 'Pork is *UNCLEAN*'
        pork.unclean.get(1) == 'Pork sausage is *UNCLEAN*'
    }

    def 'pig'() {
        expect:
        def pig = kosher.check('pig')
        pig.clean.size() == 1
        pig.unclean.size() == 3
        pig.clean.get(0) == 'Pig Fish is *CLEAN*'
        pig.unclean.get(0) == 'Pig is *UNCLEAN*'
        pig.unclean.get(1) == 'Guinea Pig is *UNCLEAN*'
        pig.unclean.get(2) == 'Pig Lard is *UNCLEAN*'
    }

    def 'not known'() {
        expect:
        def other = kosher.check('whooperscooper')
        other.clean.size() == 0
        other.unclean.size() == 0
    }
}
