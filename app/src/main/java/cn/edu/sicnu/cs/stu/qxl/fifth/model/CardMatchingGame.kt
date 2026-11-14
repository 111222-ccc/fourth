package cn.edu.sicnu.cs.stu.qxl.fifth.model

import java.io.Serializable

class CardMatchingGame(private val cardCount: Int) : Serializable {

    /** 当前分数，外部只读 */
    var score: Int = 0
        private set

    private val cards = mutableListOf<Card>()

    /** 当前翻开、尚未完成判定的牌（最多 3 张） */
    val chosenCards = mutableListOf<Card>()

    /** 几张牌算一组：三张牌游戏 */
    private val cardsPerGroup = 3

    init {
        newGame()
    }

    fun cardAtIndex(index: Int): Card = cards[index]

    /**
     * 三张牌逻辑：翻够 3 张才结算
     * 匹配成功：三张标记 isMatched，清空 chosenCards，加分
     * 匹配失败：扣分，chosenCards 保留三张，由 Activity 延迟后翻转回去
     */
    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched || card.isChosen) return
        if (chosenCards.size >= cardsPerGroup) return

        card.isChosen = true
        chosenCards.add(card)

        if (chosenCards.size == cardsPerGroup) {
            val others = chosenCards.drop(1).toTypedArray()
            val delta = chosenCards[0].match(others)
            score += delta

            if (delta > 0) {
                chosenCards.forEach {
                    it.isChosen = false
                    it.isMatched = true
                }
                chosenCards.clear()
            }
        }
    }

    /** 三张不匹配时，由 Activity 延迟调用，把牌翻回去 */
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

        val groupCount = cardCount / cardsPerGroup          // 24 / 3 = 8 组
        for (g in 0 until groupCount) {
            val suit = Card.validSuits[g % Card.validSuits.size]
            val rank = Card.rankStrings[(g / Card.validSuits.size) % Card.rankStrings.size]
            repeat(cardsPerGroup) { cards.add(Card(suit, rank)) }
        }
        cards.shuffle()
    }
}
