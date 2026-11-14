package cn.edu.sicnu.cs.stu.qxl.fifth.model

import java.io.Serializable

class Card(
    private var suit: String,
    private var rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) : Serializable {
    companion object {
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String = suit + rank

    /**
     * 三张牌判定：otherCards 传另外 2 张牌
     * 三张点数+花色全同 → 12 分
     * 三张点数相同、花色不同 → 6 分
     * 三张花色相同、点数不同 → 3 分
     * 其他情况 → -3 分（不匹配，扣分）
     */
    fun match(otherCards: Array<Card>): Int {
        if (otherCards.isEmpty()) return 0
        val allSameRank = otherCards.all { it.rank == rank }
        val allSameSuit = otherCards.all { it.suit == suit }
        val multiplier = 1 + otherCards.size

        // 三张牌时 = 3
        return when {
            allSameRank && allSameSuit -> 4 * multiplier
            allSameRank -> 2 * multiplier
            allSameSuit -> 1 * multiplier
            else -> -1 * multiplier
        }
    }

    fun getSuit(): String = suit
    fun getRank(): String = rank
}
