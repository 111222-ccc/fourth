package cn.edu.sicnu.cs.stu.qxl.fifth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.edu.sicnu.cs.stu.qxl.fifth.databinding.ActivityCardGameBinding
import cn.edu.sicnu.cs.stu.qxl.fifth.model.CardMatchingGame

class CardGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardGameBinding
    private lateinit var game: CardMatchingGame
    private lateinit var adapter: CardAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var isWaiting = false
    private val totalCards = 24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvScore: TextView = binding.scoreTv
        val btnReset = binding.btnReset

        // 横竖屏自动切换列数
        val widthDp = resources.displayMetrics.widthPixels / resources.displayMetrics.density
        val spanCount = if (widthDp > 600) 6 else 4
        binding.recyclerCards.layoutManager = GridLayoutManager(this, spanCount)

        initGame()

        adapter = CardAdapter { pos ->
            if (isWaiting) return@CardAdapter

            game.chooseCardAtIndex(pos)
            refreshUI(tvScore)

            // 选满3张，延迟翻面
            if (game.chosenCards.size == 3) {
                isWaiting = true
                handler.postDelayed({
                    game.clearChosenCards()
                    refreshUI(tvScore)
                    isWaiting = false
                }, 700)
            }

            if (game.isFinished()) {
                Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerCards.adapter = adapter
        refreshUI(tvScore)

        btnReset.setOnClickListener {
            isWaiting = false
            handler.removeCallbacksAndMessages(null)
            game.reset()
            refreshUI(tvScore)
        }
    }

    private fun initGame() {
        game = CardMatchingGame(totalCards)
    }

    private fun refreshUI(tvScore: TextView) {
        tvScore.text = getString(R.string.score_text, game.score)
        val list = (0 until totalCards).map { game.cardAtIndex(it) }
        adapter.updateData(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
