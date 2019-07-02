package bot.services

import bot.ChikuseBot
import bot.utilities.*
import bot.models.*
import groovy.util.logging.Log
import org.jsoup.nodes.*
import org.jsoup.select.Elements
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.SendMessage

import javax.inject.Inject

import static bot.models.Emoji.*
import static bot.utilities.Browser.*

@Log
@Service
class Quotable {

    @Inject
    ChikuseBot bot

    @Inject
    Settings settings

    @Inject
    Randomiser random

    @Value('${bot.url.random.advice}')
    String adviceUrl

    @Value('${bot.url.random.quotes}')
    String quoteUrl

    @Value('${bot.url.random.facts}')
    String factsUrl

    def getRandom() {
        switch (random.getRandomInteger(5)) {
            case 0: return getRandomFact()
            case 1: return getRandomAdvice()
            case 2: return getRandomQuotation()
            case 3: return getRandomMotivational()
            case 4: return getMaritalAdvice(Spouse.WIFE)
            case 5: return getMaritalAdvice(Spouse.HUSBAND)
        }
    }

    @Scheduled(cron = "0 0 17 * * *", zone = "Africa/Johannesburg")
    void sendDailyMotivational() {
        settings.allChatUsers.each { e ->
            log.info("broadcast motivational everyday at 5pm")
            bot.execute(new SendMessage(
                    chatId: e.chatId,
                    text: getRandomMotivational()
            ))
        }
    }

    Photo getRandomFact() {
        Document doc = getHTML(factsUrl)
        Elements facts = doc.select(".fact")
        Element fact = random.getRandomElement(facts) as Element
        Element link = fact.select(".box a").first()
        Element img = fact.select("figure img").first()
        Element caption = fact.select(".fact_text").first()
        String imgUrl = img ? img.absUrl("src") : "http://pipsum.com/285x152.jpg"
        return new Photo(
                imageUrl: imgUrl,
                caption: caption.text(),
                contentUrl: link.absUrl("href")
        )
    }

    String getRandomAdvice() {
        getJSON(adviceUrl).slip.advice.trim()
    }

    String getRandomQuotation() {
        getJSON(quoteUrl).quoteText.trim()
    }

    String getRandomMotivational() {
        random.getRandomElement(MOTIVATIONS)
    }

    String getMaritalAdvice(Spouse gender) {
        def emoji = MENS_SYMBOL
        def source = ADVICE_FOR_HUSBANDS

        if(gender == Spouse.WIFE) {
            emoji = WOMENS_SYMBOL
            source = ADVICE_FOR_WIVES
        }

        return emoji.toString() + " " + random.getRandomElement(source)
    }

    def MOTIVATIONS = [
            "God loves each of us as if there were only one of us",
            "God never said that the journey would be easy, but He did say that the arrival would be worthwhile",
            "God’s work done in God’s way will never lack God’s supplies.",
            "God will meet you where you are in order to take you where He wants you to go.",
            "Let God’s promises shine on your problems.",
            "Christ literally walked in our shoes.",
            "He is no fool who gives what he cannot keep, to gain what he cannot lose.",
            "Remember who you are. Don’t compromise for anyone, for any reason. You are a child of the Almighty God. Live that truth.",
            "If you can’t fly, then run, If you can’t run, then walk, If you can’t walk, then crawl, but whatever you do, you have to keep moving forward.",
            "Our greatest fear should not be of failure but of succeeding at things in life that don’t really matter.",
            "If God is your partner, make your plans BIG!",
            "You are the only Bible some unbelievers will ever read.",
            "We gain strength, and courage, and confidence by each experience in which we really stop to look fear in the face...we must do that which we think we cannot.",
            "He who lays up treasures on earth spends his life backing away from his treasures. To him, death is loss. He who lays up treasures in heaven looks forward to eternity; he’s moving daily toward his treasures. To him, death is gain.",
            "God does not give us everything we want, but He does fulfill His promises, leading us along the best and straightest paths to Himself.",
            "The Christian life is not a constant high. I have my moments of deep discouragement. I have to go to God in prayer with tears in my eyes, and say, 'O God, forgive me,' or 'Help me.'",
            "If you believe in a God who controls the big things, you have to believe in a God who controls the little things.  It is we, of course, to whom things look 'little' or 'big'.",
            "There is no one who is insignificant in the purpose of God.",
            "This is our time on the history line of God. This is it. What will we do with the one deep exhale of God on this earth? For we are but a vapor and we have to make it count. We’re on. Direct us, Lord, and get us on our feet.",
            "We are all faced with a series of great opportunities brilliantly disguised as impossible situations.",
            "Be faithful in small things because it is in them that your strength lies.",
            "The greater your knowledge of the goodness and grace of God on your life, the more likely you are to praise Him in the storm.",
            "Continuous effort -- not strength nor intelligence -- is the key to unlocking our potential.",
            "God is most glorified in us when we are most satisfied in Him",
            "Keep a gratitude journal and add to it everyday.",
            "Tell someone you love them and how much you appreciate them.",
            "Notice the beauty in nature each day.",
            "Nurture the friendships you have, good friends don’t come along every day.",
            "Smile more often.",
            "Watch inspiring videos that will remind you of the good in the world.",
            "Include an act of kindness in your life each day.",
            "Avoid negative media and movies with destructive content.",
            "Call your Mom more often.",
            "Cook meals with love, think of the people you will feed.",
            "Volunteer for organizations that help others.",
            "Don’t gossip or speak badly about anyone.",
            "Spend quality time with your kids.",
            "Remember to compliment your friends and family when they look good.",
            "Write a card to someone you haven’t seen in a while and tell them something nice.",
            "Add to your gratitude list daily, at least one more thing each day.",
            "When you think a negative thought, try to see the positive side in the situation.",
            "Commit to one day a week when you won’t complain about anything.",
            "Try to take note when people do a good job and give recognition when it’s due at work.",
            "Reward effort, if someone does something nice for you, do something nice for them.",
            "Meditate with your gratitude list, giving thanks for all your good fortune.",
            "Live mindfully, not worrying about the past or future.",
            "Thank the people who serve you in the community. The shopkeeper, the bus drivers, etc.",
            "Say thank you for the little things loved ones do for you, things you normally take for granted.",
            "Post quotes and images that remind you to be grateful around your house.",
            "Call into an elderly neighbor and say thank you for their presence in your life.",
            "Call your grandparents and tell them you love them.",
            "Embrace challenges and turn them into opportunities to grow.",
            "Send love to your enemies or people you dislike.",
            "Be thankful when you learn something new.",
            "See the growth opportunity in your mistakes.",
            "Help your friends see the positive side to life.",
            "When times are bad, focus on your friends who are at your side.",
            "When time are good, notice and help others.",
            "Make a gratitude collage, cut out pictures of all the things that you are grateful for.",
            "Make gratitude a part of family life, share it with each other during meal time.",
            "Practice gratitude at the same time everyday to make it a habit.",
            "Focus on your strengths.",
            "Share the benefits of gratitude with family and friends.",
            "In the middle of every difficulty lies opportunity.",
            "Big jobs usually go to the men who prove their ability to outgrow small ones.",
            "It is not the mountain we conquer but ourselves.",
            "He who has a why to live can bear almost any how.",
            "Defeat may serve as well as victory to shake the soul and let the glory out.",
            "Begin anywhere.",
            "If you don’t like the road you’re walking, start paving another one.",
            "Whatever you can do, or dream you can, begin it. Boldness has genius, power, and magic in it.",
            "Overcome the notion that you must be regular. It robs you of the chance to be extraordinary",
            "Have faith in yourself and in the future.",
            "It’s kind of fun to do the impossible.",
            "Everything you can imagine is real.",
            "Find your purpose and fling your life out to it. Find a way or make one. Try with all your might. Self-made or never made.",
            "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle.",
            "Most obstacles melt away when we make up our minds to walk boldly through them.",
            "Find out who you are and do it on purpose.",
            "The future belongs to those who believe in the beauty of their dreams.",
            "Do not go where the path may lead. Go instead where there is no path and leave a trail.",
            "What lies behind us and what lies before us are small matters compared to what lies within us.",
            "If we all did the things we are capable of doing we would literally astound ourselves.",
            "You are never too old to set another goal or to dream a new dream.",
            "Dreams are illustrations from the book your soul is writing about you.",
            "Faith is to believe what you do not see; the reward of this faith is to see what you believe.",
            "A happy person is not a person in a certain set of circumstances, but rather a person with a certain set of attitudes.",
            "The only journey is the one within.",
            "The degree of responsibility you take for your life determines how much change you can create in it.",
            "Start where you are, with what you have, and that will always lead you into something greater.",
            "The really great man is the man who makes every man feel great.",
            "Be willing to be a beginner every single morning",
            "Ask what makes you come alive and go do it. Because what the world needs is people who have come alive.",
            "Your own path you make with every step you take. That’s why it’s your path.",
            "If the mind thinks with a believing attitude one can do amazing things.",
            "Anything I’ve ever done that ultimately was worthwhile initially scared me to death.",
            "As fast as each opportunity presents itself, use it! No matter how tiny an opportunity it may be, use it!",
            "The golden opportunity you are seeking is in yourself.",
            "The greatest act of faith some days is to simply get up and face another day.",
            "Great things are not done by impulse, but by a series of small things brought together.",
            "There is real magic in enthusiasm. It spells the difference between mediocrity and accomplishment.",
            "Compassion is language the deaf can hear and the blind can see.",
            "The art of living lies less in eliminating our troubles than in growing with them.",
            "It doesn't matter who you are, where you come from. The ability to triumph begins with you. Always.",
            "Trust yourself. You know more than you think you do.",
            "Go confidently in the direction of your dreams. Live the life you have imagined.",
            "I'm not afraid of storms, for I'm learning to sail my ship.",
            "If you have knowledge, let others light their candles in it.",
            "If your ship doesn't come in, swim out to it.",
            "It is more important to know where you are going than to get there quickly.",
            "Life isn’t about finding yourself. Life is about creating yourself.",
            "The best way to prepare for life is to begin to live.",
            "Don't judge each day by the harvest you reap but by the seeds that you plant.",
            "Write it on your heart that every day is the best day in the year.",
            "Every moment is a fresh beginning.",
            "Without His love I can do nothing, with His love there is nothing I cannot do.",
            "Everything you’ve ever wanted is on the other side of fear.",
            "Begin at the beginning... and go on till you come to the end: then stop.",
            "Dwell on the beauty of life. Watch the stars, and see yourself running with them.",
            "Perfection is not attainable, but if we chase perfection we can catch excellence.",
            "Put your heart, mind, and soul into even your smallest acts. This is the secret of success.",
            "Wherever you go, go with all your heart.",
            "You cannot tailor-make the situations in life but you can tailor-make the attitudes to fit those situations.",
            "There are two ways of spreading light: to be the candle or the mirror that reflects it.",
            "It is never too late to be what you might have been.",
            "The day is what you make it! So why not make it a great one?",
            "The roots of education are bitter, but the fruit is sweet.",
            "Tough times never last, but tough people do.",
            "To be the best, you must be able to handle the worst.",
            "What we’ve got to do is keep hope alive. Because without it we’ll sink.",
            "Learn from the mistakes of others. You can’t live long enough to make them all yourselves!",
            "The key to immortality is first living a life worth remembering",
            "Believe and act as if it were impossible to fail.",
            "Action is the foundational key to all success.",
            "Turn your wounds into wisdom",
            "Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment.",
            "You must be the change you wish to see in the world.",
            "I can't change the direction of the wind, but I can adjust my sails to always reach my destination.",
            "Don’t regret the past, just learn from it.",
            "Keep your face always toward the sunshine - and shadows will fall behind you",
            "The greatest discovery of all time is that a person can change his future by merely changing his attitude."
    ]

    def ADVICE_FOR_HUSBANDS = [
            "Don't increase the necessary work of the house by leaving all your things lying about in different places. If you are not tidy by nature, at least be thoughtful for others",
            "Don't always refuse to go shopping with your wife. Of course it’s a nuisance, but sometimes she honestly wants your advice, and you ought to be pleased to give it",
            "Don't sit down to breakfast in your shirt-sleeves in hot weather on the ground that ˝only your wife is present. She is a woman like any other woman. The courtesies you give to womankind are her due, and she will appreciate them",
            "Don't take it out on your poor wife every time you have a headache or a cold",
            "It isn’t her fault, and she has enough in nursing you, without having to put up with ill-humour into the bargain",
            "Don't ˝let off steam on your wife every time anything goes wrong at the office, in the garage or garden. Try to realize that she has nothing to do with it and it is unfair to make her suffer for it",
            "Don't be too grave and solemn. Raise a bit of fun in the home now and then",
            "Don't keep all your best jokes for your men friends. Let your wife share them",
            "Don't look at things solely from a man s point of view. Put yourself in your wife’s place and see how you would like some of the things she has to put up with",
            "Don't be conceited about your good looks. It is more than probable that no one but yourself is aware of them; anyway, you are not responsible for them, and vanity in a man is ridiculous",
            "Don't put on too much of the “lord of creation” air. It will only make you look absurd",
            "Don't hang about the house all day if your occupation does not take you out. Don't inflict your company on your wife during every minute of the day. She is fond of you, but she wants to be free sometimes. And SHE has business to do, if you haven't",
            "Don't condescend or talk down to your wife; you are not the only person in the house with brains. She has as much intelligence as you",
            "Don't keep your wife in cotton-wool. She isn’t wax) she’s a woman",
            "Don't shelter her from every wind that blows. You will kill her soul that way, if you save her body",
            "Don't omit to bring home an occasional bunch of flowers or a few chocolates. Your wife will value even a penny bunch of violets for your thought of her",
            "Don't rush out of the house in such a hurry that you haven t time to kiss your wife good-bye", "She will grieve over the omission",
            "Don't belittle your wife before visitors. You may think it a joke to speak of her foolish foibles, but she will not easily forgive you",
            "Don't be careless about keeping promises made to your wife",
            "Don't hesitate to mention the fact when you think your wife looks especially nice. Your thinking so can give her no pleasure unless you speak out your thought",
            "Don't forget your wife's birthday. Even if she doesn’t want the whole world to know her age, she doesn’t like YOU to forget",
            "Don't think that because you can’t afford to buy an expensive present, it is best to take no notice at all. The smallest gift will be appreciated if prompted by love",
            "Don't sulk when things go wrong. If you can’t help being vexed, say so, and get it over",
            "Don't nag your wife. If she has burnt the meal or forgotten to sew on a button, she doesn’t want to be told of it over and over again",
            "Don't tell your wife a lie about anything. There should be entire confidence between you. If she once finds you out in a lie, she will not believe you when you do speak the truth",
            "Don't think that it is no longer necessary to show your love for your wife, as she ˝ought to know it by this time. A woman likes to be kissed and caressed and to receive little lover-like attentions from her husband even when she is a grandmother",
            "Don't think that because you and your wife married for love there will never be a cloud in your sky. Neither of you is perfect, and you will have to learn to avoid treading on each other’s corns",
            "Don't dwell on any lack of physical perfection in your wife",
            "Don't despise your wife's everyday qualities because she is not what the world would call brilliant",
            "Don't keep your wife outside your business interests. It is foolish to say that she knows nothing about the business, and therefore it can’t interest her. You will often find that her fresh mind will see a way out of some little difficulty that has not occurred to you. No doubt you are a very clever fellow, and it is an education for her to listen to you, but she also may have some views worth mentioning",
            "Don't expect to understand every detail of the working of your wife’s mind. A woman arrives at things by different ways, and it is useless to worry her with why does she think this or that",
            "Don't try to keep bad news from your wife. She will guess that something is wrong and will worry far more than if you tell her straight out",
            "Don't allow the habit of silence at home to grow upon you. Some husbands never seem to think it worthwhile to talk to their wives about anything, although if a friend comes in, they will at once begin an animated conversation",
            "Don't expect you can live your lives apart under the same roof and still be happy. Marriage is a joint affair, and cannot comfortably be worked along separate lines",
            "Don't insist upon having the last word. If you know when to drop an argument, you are a wise man",
            "Don't try to regulate every detail of your wife’s life. Even your wife is an individual, and must be allowed some scope",
            "Don't expect your wife to hold the same views as yours on every conceivable question. Some men like an echo, it is true, but it becomes very wearisome in time",
            "Don't drop, when alone with your wife, the little courtesies you would offer other women, or fail to treat her with due respect. For instance, always get up to open a door for her, as you would a lady guest",
            "Don't try to drive your wife. You will find it much easier to lead her",
            "Don't chide your wife in public, whatever you may feel, but do it in private",
            "Don't be slow to give praise where praise is due. It will not only make your wife happier, but will even confirm your own good-humour; and good-humour is always worth cultivating for its own sake",
            "Don't flatter your wife. Unless she is very vain, she is sure to see through you, and she will be more hurt than pleased. Praise where you can, but leave flattery alone",
            "Don't forget that actions speak louder than words. It’s no use telling your wife how much you care for her if you do the very things that you know will make her unhappy",
            "Don't assume that it is always your wife who is wrong whenever you have a difference of opinion. After all, you are not infallible",
            "Don't judge your wife's motives. She may do a thing from a motive that would never occur to you, and be perfectly justified in her action",
            "Don't be reserved with your wife, however natural it is to you to be reserved with others. Be perfectly open and confiding in all your dealings with her. She will be hurt if she is left to discover for herself something that she had a right to expect you to tell her",
            "Don't let ambition crowd out life. There ought to be room for both in your life, but some men are so busy ˝getting on that they have no time to show love to their wives",
            "Don't think you can soothe her wounded feelings with material gifts",
            "Don't start arguments about religion unless you and your wife can both discuss the matter quite impersonally. The bitterest quarrels sometimes arise from religious discussion",
            "Don't make fun of your wife if she happens to make a little mistake",
            "Don't forget that it is the little things that count in married life. Avoid trivial jealousies; trivial selfishness; tiny irritants; small outbursts of temper; short sarcastic comments. If you can’t say something pleasant, learn to keep silent",
            "Don't be jealous and insult your wife by trying to shut your wife away from other men",
            "Don't tease your wife about every pretty girl you meet, or dwell on the beauty of other women if you know your wife to be sensitive on the point. She may not be jealous to begin with, but after a while she may begin to think that there is something in it",
            "Don't be continually telling your wife how lucky Charles is in having a wife who can cook such dainty dishes, or who can keep the house clean and spotless. You can’t expect her to relish having the good qualities of other women rammed down her throat",
            "Don't throw your mother s perfections at her head, or you needn’t t be surprised if she suggests that you might as well return to your mother’s wing",
            "Don't be jealous of your wife's girlfriends. If she wants to spend the day with them now and then, spare her with a good blessing. Don't let her feel that you are a selfish tyrant",
            "Don't flirt with other women. Your wife will certainly despise you if you do",
            "Don't forget to trust your wife in everything. Trust her to the utmost, and you will rarely find your trust misplaced",
            "Don't spend the best years of your life in thinking of nothing but moneymaking. Enjoy your life to the full with your wife, and relegate money-getting to its proper place) necessarily and important one, but not the only thing to be thought of",
            "Don't let all the economizing be on your wife's side. Perhaps you could do with a little less of this and that, if you tried",
            "Don't run away with the idea that there is nothing to do in a house, and that your wife'should therefore never be busy or tired",
            "Don't grumble day after day at your wife's untidiness if you happen to be a methodical man. It will be much easier, and will save friction, if you quietly put away the things she leaves lying about. Her untidiness may be a constitutional defect, and, if so, no amount of grumbling will cure it",
            "Don't be unsympathetic if your wife's worries seem to you to be trivial. You can try to sweep all her trouble away with a little kindly sympathy, or you can make it worse by refusing to see that there is any trouble",
            "Don't be afraid of lending a hand in the house. It will do you no harm at all to learn some household chore",
            "Don't let your wife become merely a domestic machine. If she doesn’t want to broaden her horizon, see that you do it for her",
            "Don't show your worst side at home. Let your wife have the benefit of your best qualities",
            "Don't selfishly refuse to go out in the evening because you have been amongst other people all day. Remember that your wife hasn’t and a change is good for her",
            "Don't spend night after night at your club, leaving your wife alone at home",
            "Don't settle down into an old married man while you are still in the prime of life. Take your wife out and about",
            "Don't say your wife wastes time reading, even if she reads only fiction. Above all things don't put on the schoolmaster air. Rather let her pick her reading for herself; tastes differ",
            "Don't growl every time your wife invites anyone to the house. It takes quite half her pleasure away to know that you think it’s a nuisance having people about",
            "Don't be impatient or unsympathetic if your wife is sick, but on the other hand, don't pet your wife when her little finger aches until she imagines herself a martyr to ill-health, when there is really nothing the matter with her",
            "Don't be continually worrying about your health. If you really feel ill, or suspect that anything is wrong, consult a doctor, instead of causing your wife untold anxiety by throwing out vague suggestions as to what may be the matter with you",
            "Don't grudge your wife new clothes because you haven t noticed that she needs them",
            "Don't refuse to listen to your wife's suggestions on matters of dress. Most of the time women know what suits men better than men know themselves",
            "Don't take so little interest in your wife's dress that she might as well wear a piece of old sackcloth as far as you are concerned. It is very discouraging to a woman to find that her husband neither knows nor cares how she dresses",
            "Don't spend all your money on the garden, or some other thing, because it is your hobby, and leave none for the house if that happens to be your wife's hobby",
            "Don't let any hobby so overmaster you that you spend every minute on it when you are at home, especially if it is something in which your wife can take no part",
            "Don't separate your pursuits from your wife's more than is necessary. Where possible; work, talk, and plan together",
            "Don't try to control your wife's church-going or non-church-going. The question is for her alone to decide, and you should leave her entirely free, whatever your own views might be",
            "Don't always say to your children, “Ask your mother”, when you don't want to be bothered. It is conceivable that she doesn’t either",
            "Don't be unreasonable in your demands on your wife's time during the child-rearing years",
            "Don't leave your wife everything in connection with the education and upbringing of the children. Discuss all points of difficulty with her, and come to an agreement as to the best way to act under given circumstances"
    ]

    def ADVICE_FOR_WIVES = [
            "Don't think that there is any satisfactory substitute for love between husband and wife. Respect and esteem make a good foundation, but they won’t do alone",
            "Don't think that because you have married for love, you can never know a moment s unhappiness. Life is not a bed of roses, but love will extract the thorns",
            "Don't lose heart when life seems hard. Look forward to the corner you are bound to turn soon, and point it out to your husband",
            "Don't try to excite your husband’s jealousy by flirting with other men. You may succeed better than you want to. It is like playing with tigers and edged tools and volcanoes all in one",
            "Don't expect your husband to have all the feminine virtues as well as all the masculine ones. There would be nothing left for you if you if your other half were such a paragon. Don't worry about little faults in your husband which merely amused you in your lover. If they were not important then, they are not important now. Besides, what about yours?",
            "Don't be everlastingly trying to change your husband’s habits, unless they are VERY bad ones. Take him as you find him, and leave him at peace",
            "Don't impose your ideas on your husband in matter of individual taste so long as his style is not bad. He has a right to his own views",
            "Don't expect your husband to be an angel. You would get very tired of him if he were",
            "Don't tell all your women friends of your husband’s faults, neither shout his perfections into the ears of every women you meet",
            "Don't expect a man to see everything from a women’s point of view. Try putting yourself in his place for a change",
            "Don't advise your husband on subjects of which you are, if anything, rather more ignorant than he",
            "Don't try to model your husband on some other women’s husband. Let him be himself and make the best of him",
            "Don't be too serious and heavy at home. Make things bright for your husband",
            "Don't vegetate as you grow older if you happen to live in the country.  Keep both brain and body on the move. There is no need to stagnate",
            "Don't omit to pay your husband an occasional compliment",
            "Don't say “I told you so” to your husband, however much you feel tempted to. It does no good, and he will be grateful to you for not saying it",
            "Don't expect your husband to make you happy while you are simply a passive agent. Do your best to make him happy and you will find happiness yourself",
            "Don't nag your husband. If he won’t carry out your wishes for love of you, he certainly won’t because you nag him",
            "Don't sulk with your husband. If he has annoyed you about something, get it off your chest",
            "Don't expect all the give to be on his side, and all the take on yours",
            "Don't argue with a stubborn husband. Drop the matter before argument leads to temper. You can generally gain your point in some other way",
            "Don't manage your husband too visibly. Of course, he may require the most careful management, but you don't want your friends to think of him as a hen-pecked husband. Above all, never let him think you manage him",
            "Don't wash your dirty linen in public, or even before your most intimate friends. If there are certain disagreeable matters to discuss, take care to discuss them in complete privacy",
            "Don't be shy of showing your love. Don't expect him to take it for granted. A playful caress as you pass, an unexpected touch on the shoulder, makes all the difference between merely knowing that you care for him and actually feeling it",
            "Don't go to sleep feeling cross with your husband. If he has annoyed you during the evening, forgive him and close your eyes at peace with him. “Let not the sun go down upon your wrath” is a very good motto",
            "Don't return to an old grievance. Once the matter has been thrashed out, let it be forgotten, or at least never allude to it again",
            "Don't be too proud to give way about trifles. When a principle is at stake, it is a different matter, but most matrimonial differences arise from trifles",
            "Don't say bitter things when you are angry. They not only sting at the time, but they eat their way in and are remembered long after you have forgotten them",
            "Don't keep your sweetest smiles and your best manners for outsiders; let your husband come first",
            "Don't think money makes happiness. It helps to procure comfort, but true happiness lies deeper than that",
            "Don't spend all the best years of your life pinching and saving unnecessarily, until you are too old to get any pleasure out of your money",
            "Don't pile up money for your children. Give them the best education possible, and let them make their own way",
            "Don't omit the kiss of greeting. It cheers a man when he is tired to feel that his wife is glad to see him home",
            "Don't spend all your time with the children, and leave none over for your husband",
            "Don't tell your husband of every petty annoyance and pin-prick you have suffered from during the day; but do tell him of your real troubles; he expects to share them, and his advice may help you. In any case, his empathy will halve the trouble",
            "Don't forget the anniversary of your wedding. Keep it up. The little celebration will draw you closer together year by year",
            "Don't expect your husband to always share your recreations while you refuse to share his",
            "Don't let your husband feel that you are constantly criticizing everything he does. Leave the role of critic to others. This does not mean that you are not to give friendly criticism. There is a happy medium between constant carping and fulsome flattery which you should seek",
            "Don't take your husband on a laborious shopping expedition, and expect him to remain good tempered throughout",
            "Don't keep the house so tidy that your husband is afraid to leave a newspaper lying about",
            "Don't quarrel with your husband’s relatives. If you can’t get on with them, don't ask them to visit you, but persuade your husband to visit them occasionally. As a rule, however, a little tact and patience will carry you over the thin ice",
            "Don't snub your husband. Nothing is more unpleasant for lookers-on than to hear a snub administered by a wife, and it is more than unpleasant; it is degrading",
            "Don't allow outsiders to interfere in your household management. Even mothers should lie low",
            "Don't refuse to listen to good advice from people of experience, and act upon it if you can",
            "Don't be jealous, anyway. It belittles you, puts you at a disadvantage, and, if your husband thinks about it, is apt to make him unbearably conceited",
            "Don't get angry if your husband says that he never now tastes cake like that his mother used to make. Ask her for the recipe"
    ]
}
