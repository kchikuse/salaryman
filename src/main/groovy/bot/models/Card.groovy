package bot.models

class Card {
    long cashBalanceCents
    String fullCardNumber
    String description

    @Override
    String toString() {
        return String.format("*%s*\n%s\nhas got *%s* rands",
                description,
                fullCardNumber,
                cashBalanceCents / 100
        )
    }
}