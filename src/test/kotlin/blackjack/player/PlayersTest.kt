package blackjack.player

import betting.Bet
import betting.BetResult
import blackjack.card.CardFixture
import blackjack.card.Rank
import blackjack.dealer.Dealer
import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test

class PlayersTest {
    private lateinit var dealer: Dealer
    private lateinit var pobi: Player
    private lateinit var jason: Player
    private lateinit var players: Players

    @BeforeEach
    fun setUp() {
        dealer = Dealer()
        pobi =
            Player(
                name = "pobi",
                hand =
                    Hand(
                        cards =
                            listOf(
                                CardFixture.generateTestCard(Rank.TWO),
                                CardFixture.generateTestCard(Rank.FIVE),
                            ),
                    ),
                betResult = BetResult.Default(bet = Bet(amount = 1_000.0)),
            )
        jason =
            Player(
                name = "jason",
                hand =
                    Hand(
                        cards =
                            listOf(
                                CardFixture.generateTestCard(Rank.TEN),
                                CardFixture.generateTestCard(Rank.FIVE),
                            ),
                    ),
                betResult = BetResult.Default(bet = Bet(amount = 2_000.0)),
            )
        players = Players(players = listOf(pobi, jason))
    }

    @Test
    fun `플레이어가 패배하는 경우 베팅금을 모두 잃는다`() {
        val (playersAfterTurn, dealer) =
            players.play(
                isHitCard = { true },
                draw = { CardFixture.generateTestCard(Rank.NINE) },
                afterPlay = {},
                dealer = dealer,
            )

        playersAfterTurn.players
            .filter { it.isBust() }
            .forAll {
                it.betResult should beInstanceOf<BetResult.Lose>()
                it.winningAmount shouldBe it.bet.negative()
            }
        dealer.betResult should beInstanceOf<BetResult.Win>()

        val loserBetAmount =
            playersAfterTurn.players
                .filter { it.isBust() }
                .sumOf { it.betAmount }
        dealer.winningAmount shouldBe loserBetAmount
    }
}