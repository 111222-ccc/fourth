package cn.edu.sicnu.cs.stu.qxl.fifth.model

import java.io.Serializable

class CardPairGame(private val cardCount: Int) : Serializable {

    var score: Int = 0
        private set

    private val cards = mutableListOf<Card>()
    val chosenCards = mutableListOf<Card>()

    // 两张一组
    private val cardsPerGroup = 2

    init {
        newGame()
    }

    fun cardAtIndex(index: Int): Card = cards[index]

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        // 和原版一模一样：已经匹配 / 已经翻开，不能点
        if (card.isMatched || card.isChosen) return
        // 和原版一模一样：选满2张就不再接受新牌
        if (chosenCards.size >= cardsPerGroup) return

        card.isChosen = true
        chosenCards.add(card)

        if (chosenCards.size == cardsPerGroup) {
            val c1 = chosenCards[0]
            val c2 = chosenCards[1]
            // 两张点数相同=匹配成功
            val delta = if (c1.getRank() == c2.getRank()) 2 else -1
            score += delta

            if (delta > 0) {
                // ✅匹配成功，和3张逻辑一样：取消翻开，标记已匹配，清空选中列表
                chosenCards.forEach {
                    it.isChosen = false
                    it.isMatched = true
                }
                chosenCards.clear()
            }
            // ❌匹配失败：这里什么都不写！chosenCards保留，交给Fragment延时clearChosenCards
        }
    }

    fun clearChosenCards() {
        chosenCards.forEach { it.isChosen = false }
        chosenCards.clear()
    }

    fun reset() = newGame()

    fun isFinished(): Boolean = cards.all { it.isMatched }

    private fun newGame() {
        score = 0
        chosenCards.clear()
        cards.clear()

        val groupCount = cardCount / cardsPerGroup
        // 16 / 2 = 8组
        for (g in 0 until groupCount) {
            val suit = Card.validSuits[g % Card.validSuits.size]
            val rank = Card.rankStrings[(g / Card.validSuits.size) % Card.rankStrings.size]
            repeat(cardsPerGroup) { cards.add(Card(suit, rank)) }
        }
        cards.shuffle()
    }
}
