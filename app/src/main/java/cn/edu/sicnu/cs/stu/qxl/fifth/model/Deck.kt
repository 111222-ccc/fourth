package cn.edu.sicnu.cs.stu.qxl.fifth.model

import java.io.Serializable
import java.util.Random

class Deck() : Serializable {
    private val cards = mutableListOf<Card>()
    private val r = Random()

    init {
        for (suit in Card.validSuits) {
            for (rank in Card.rankStrings) {
                val card = Card(suit = suit, rank = rank)
                cards.add(card)
            }
        }
    }

    fun drawRandomCard(): Card? {
        var randomCard: Card? = null
        if (cards.size > 0) {
            randomCard = cards.removeAt(r.nextInt(cards.size))
        }
        return randomCard
    }
}
