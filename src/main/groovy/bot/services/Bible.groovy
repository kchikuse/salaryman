package bot.services

import bot.models.*
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import static bot.utilities.Browser.*

@Service
class Bible {

    @Value('${bot.url.bible}')
    private String url

    String getVerse() {
        Document doc = getHTML(url)
        String text = doc.select(".bibleVerse").first().ownText()
        String verse = doc.select(".bibleChapter a").first().text()
        return String.format("%s - *%s*", text, verse)
    }

    List<Food> searchForFoods(String food) {

        def foods = []

        for (String f : cleanFoods) {
            if (match(f, food)) {
                foods.add(new Food(name: f, clean: true))
            }
        }

        for (String f : dirtyFoods) {
            if (match(f, food)) {
                foods.add(new Food(name: f, clean: false))
            }
        }

        return foods
    }

    private static boolean match(String foodName, String searchTerm) {
        def words = foodName.toUpperCase().split(" ")

        for (String word in words) {
            if (searchTerm.equalsIgnoreCase(word)) {
                return true
            }
        }

        return false
    }

    private def cleanFoods = [
            'Cricket','Grasshopper','Katydid','Locust','Antelope','Bison','Buffalo','Caribou','Cattle','Beef','Veal','Deer','Reindeer','Gazelle','Giraffe','Goat','Hart','Ibex','Lamb','Hogget','Mutton','Moose','Ox','Cow','Roebuck','Sheep','Addax','Big Horn Sheep','Blackbuck','Blesbok','Bongo','Buffalo','Bushbuck','Dik Dik','Eland','Elk','Gemsbok','Gerenuk','Goral','Hartebeest','Hirola','Impala','Kafue Lachwe','Kafue Lechwe','Kudu','Klipspringer','Markhor','Mule Deer','Muntjac','Musk Ox','Nilgai','Nyala','Okapi','Oribi','Oryx','Pronghorn','Reedbuck','Sable','Sitatunga','Springbuck','Springbok','Steenbok','Takin','Topi','Tsessebe','Urial','Waterbuck','White Tail Deer','Wildebeest','Yak','Albacore','Crevalle','Horse Mackerel','Jack','Alewives','Goldfish','Branch Herring','River Herring','Klippers','Anchovies','Black Drum','Bluebacks','Glut Herrings','Squawfish','Bluebill Sunfish','Bluefish','Blue Runner','Hardtail','Bonitos','Cod','Dab','Crappies','Boston Bluefish','Dolphin fish','Mahimahi','Angelfish','Flying fish','Pollock','Bowfin','Buffalofish','Butterfish','Carp','Chubs','Bloater','Longjaw','Blackfin','Common Sucker','Fresh Water Mullet','White Sucker','Black Crappies','White Crappies','Flounder','Smelt','Gag','Gray Sole','Lemon Sole','Summer Flounder','Winter Flounder','Yellow Tail','Frost Fish','Ice Fish','Groupers','Black Grouper','Nassau Grouper','Red Grouper','Yellowfish Grouper','Grunts','White Grunts','Yellow Grunts','Gulf Pike','Robalo','Snook','Snoek','Sergeant','Haddock','Hake','Halibut','Herring','Kippers','Lake Herring','Sea Herring','Kingfish','King mackerel','Long Nose Sucker','Northern Sucker','Red Striped Sucker','Mackerel','Jack Mackerel','Menhaden','Mullet','Muskeilunge','Jacks','Pickerels','Pig Fish','Pikes','Pompano','Porgy','Scup','Red Drum','Redfish','Red Horse Sucker','Redfin','Red Snapper','Salmon','Chum','Coho','Bonefish','Sardine','Pilchards','Sea Bass','Shad','Sheepshead','Silver Hake','Whiting','Silversides','Spanish Mackerel','Striped Bass','Trouts','Tilapia','Rio Grande perch','Cichlios','Mozambique mouthbrooder','Snapper','Tarpon Fish','Gray Sea Trouts','Weakfish','Lake Trout','Sand Sea Trout','White Sea Trout','Spotted Sea Trout','Tunas','Bluefin','Yellowfin','Skipjack','White Fish','Perch','Yellow Perch','Chicken','Dove','Duck','Goose','Grouse','Guinea Fowl','Partridge','Peafowl','Pheasant','Pigeon','Prairie Chicken','Ptarmigan','Quail','Sagehen','Sparrow','Teal','Turkey','Gnu'
    ]

    private def dirtyFoods = [
            'Butterfly','Ambush Bug','Ant','Aphid','Armyworm','Assassin Bug','Atlas Moth','Backswimmer','Bedbug','Bee','Beetle','Bluet','Borer','Bug','Bumblebee','Butterfly','Carpenter Ant','Caterpillar','Cicada','Cockroach','Crane Fly','Cutworm','Damselfly','Darkling Beetle','Dragonfly','Dung Beetle','Earwig','Fire Ant','Firefly','Flea','Fruitfly','Gnat','Ground Beetle','Gypsy Moth','Harlequin Bug','Horse Fly','Honeybee','Hornet','House Fly','Hover Fly','Japanese Beetle','Jumping Bean','Junebug','Kissing Bug','Ladybug','Lacewing','Leafcutter Ant','Leafhopper','Lice','Lightning Bug','Longhorn Beetle','Louse','Luna Moth','Maggot','Mayfly','Meadowhawk','Mealworm','Midge','Milkweed Bug','Monarch','Morpho','Mosquito','Moth','Nymph','Owl Butterfly','Paper Wasp','Planthopper','Pond Skater','Prawn','Praying Mantid','Praying Mantis','Robber Fly','Scarab','Silkworm','Silverfish','Skipper','Spittlebug','Springtail','Stag Beetle','Stink Bug','Stonefly','Termite','Thrip','Tiger Beetle','Tiger Moth','Tsetse Fly','Walkingstick','Wasp','Water Boatman','Waterbug','Waterstrider','Weevil','Woodborer','Yellowjacket','Albatross','Bittern','Buzzard','Condor','Coot','Cormorant','Crane','Crow','Cuckoo','Eagle','Falcon','Flamingo','Grebe','Grosbeak','Gull','Hadida','Hawk','Heron','Ibis','Kite','Lapwing','Loon','Magpie','Osprey','Ostrich','Owl','Parrot','Pelican','Penguin','Plover','Rail','Raven','Roadrunner','Sandpiper','Seagull','Stork','Swallow','Swift','Vulture','Water hen','Woodpecker','Abalone','Bullhead','Catfish','Clams','Conch','Crabs','Crayfish','Crawfish','Crawdad','Cuttlefish','Dolphin','Otter','Eel','Conger eel','Gar','Jellyfish','Limpet','Lobster','Marlin','Mussel','Octopus','Oysters','Paddlefish','Trunkfish','Blowfish','Cowfish','Porpoise','Scallop','Seal','Shark','Shrimp','Snail','Squid','Calamari','Stickleback','Cutlassfish','Scabbardfish','Lamprey','Sturgeon','Toadfish','Swordfish','Walrus','Whale','Alligator','Alpaca','Llama','Armadillo','Ass','Badger','Bat','Bear','Panda','Beaver','Caiman','Camel','Cats','Cheetah','Cougar','Jaguar','Leopard','Lion','Puma','Panther','Tiger','Crocodile','Dogs','Coyote','Fox','Hyena','Jackal','Wolf','Elephant','Frog','Gorilla','Groundhog','Guinea Pig','Hippopotamus','Horse','Donkey','Mule','Onager','Zebra','Kangaroo','Koala','Echidna','Lemur','Lizards','Mice','Mole','Monkey','Baboon','Chimpanzee','Muskrat','Newt','Porcupine','Possum','Rabbit','Hyrax','Raccoon','Rats','Coney','Rhinoceros','Salamander','Skunk','Snake','Spider','Squirrel','Swine','Boar','Hog','Pig','Warthog','Peccary','Platypus','Bacon','Pork','Pork sausage','Pangolin','Ham','Pig Lard','Toad','Turtle','Wallaby','Weasel','Wolverine','Sea eagle','Nighthawk','Nightjar','Northern carmine bee eater','Glede','Hoopoe','Indian roller','Kingfisher','Ossifrage','Peacock','Swan','Red kite','Ferret','Stoat','Shrew','Chameleon','Skink','Mole rat','Scorpion','Centipede','Ants','Mopane worms','Tortoise','Escargot','Slug','Caviar','Koi fish','Earthworm','Hare','Mongoose','Gopher','Bandicoot','Axolotl','Polar bear','Hedgehog','Burro','Prairie dog','Cookoo','Kookaburra','Wombat','Macrotis','Sugar Glider','Tiger Quoll','Cane Toad','Thorny Dragon','Tasmanian Devil','Goanna','Dingo','Quagga','Tasmanian Tiger','Sea Cow','Auk','Dodo','Woolly Mammoth','Sabre-toothed Tiger','Brown bear','Mouse','Orangutan','Dromedary','Xerocole','Jackrabbit','Iguana','Gila monster','Hamster','Jerboa','Meerkat','Mulgara','Sandgrouse','Sidewinder','Krill','Tern','Nematodes','Orca','Petrel','Skua','Ermine','Lemming','Narwhal','Puffin','Aye-aye','Binturong','Candiru','Civet','Coati','Coatimundi','Electric eel','Anaconda','Macaw','Ocelot','Piranha','Sloth','Lungfish','Coelacanth','Snail fish','Tapir','Tarsier','Toucan', 'Bilby','Brolga','Budgerigar','Cassowary','Dugong','Manatee','Emu','Galah','Numbat','Quokka','Yabby','Aardwolf','Aardvark','Bonobo','Bushbaby','Kori bustard','Nile monitor','Secretary bird','Serval','Sea anemone','Sea lion','Seahorse','Starfish','Anteater','Gharial','Grizzly bear','Jaguarundi','Tarantula','Clownfish','Uakari','Hummingbird'
    ]
}
