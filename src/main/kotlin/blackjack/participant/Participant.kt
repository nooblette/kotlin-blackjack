package blackjack.participant

import blackjack.card.Card
import blackjack.machine.BlackJackMachine
import blackjack.player.Hand
import blackjack.player.Players

interface Participant<out T : Participant<T>> {
    val name: String
    val hand: Hand

    fun isBust(): Boolean = hand.sum() > BlackJackMachine.BLACKJACK

    fun hitCard(card: Card): T
}

fun <T : Participant<T>> createParticipants(dealer: T, players: Players): List<Participant<*>> {
    return listOf(dealer) + players.players
}
