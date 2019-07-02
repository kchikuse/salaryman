package bot.models

class Food {
    String name
    boolean clean

    @Override
    String toString() {
        return "${this.name} is *${this.clean ? 'CLEAN' : 'UNCLEAN'}*"
    }
}