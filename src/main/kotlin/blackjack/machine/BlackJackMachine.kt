package blackjack.machine

import blackjack.card.Card
import blackjack.player.Player
import blackjack.player.Players
import blackjack.view.InputView
import blackjack.view.ResultView

class BlackJackMachine {
    fun play() {
        val players = Players(players = InputView.inputPlayerNames().map { Player.from(name = it) })
        ResultView.printPlayersName(players = players)
        ResultView.printPlayersCardStatus(players = players)

        val gameResult = Players(players = players.players.map { hitMoreCard(it) })
        ResultView.printPlayersCardStatusAndSum(players = gameResult)
    }

    private fun hitMoreCard(player: Player): Player =
        if (!player.isHitCard()) {
            player
        } else if (!InputView.isHitCard(player)) {
            player
                .also { ResultView.printPlayerCard(player = it) }
        } else {
            player
                .hitCard(Card.random())
                .also { ResultView.printPlayerCard(player = it) }
        }

    companion object {
        const val BLACKJACK = 21
    }
}
