package cn.edu.sicnu.cs.stu.qxl.fifth

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.stu.qxl.fifth.model.Card
import cn.edu.sicnu.cs.stu.qxl.fifth.model.CardPairGame
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class TwoCardFragment : Fragment() {

    private lateinit var rvTwoCards: RecyclerView
    private lateinit var tvTwoScore: TextView
    private lateinit var btnTwoReset: Button
    private lateinit var adapter: CardAdapter
    private var game: CardPairGame? = null
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        @JvmStatic
        fun newInstance() = TwoCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_two_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTwoCards = view.findViewById(R.id.rv_two_cards)
        tvTwoScore = view.findViewById(R.id.tv_two_score)
        btnTwoReset = view.findViewById(R.id.btn_two_reset)

        adapter = CardAdapter { pos ->
            game?.chooseCardAtIndex(pos)
            updateUI()
            val g = game ?: return@CardAdapter
            if (g.chosenCards.size == 2) {
                handler.postDelayed({
                    g.clearChosenCards()
                    updateUI()
                }, 800)
            }
        }

        val spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 4
        rvTwoCards.layoutManager = GridLayoutManager(requireContext(), spanCount)
        rvTwoCards.adapter = adapter

        game = loadGame() ?: CardPairGame(16)
        updateUI()

        btnTwoReset.setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            game?.reset()
            updateUI()
        }
    }

    private fun updateUI() {
        val g = game ?: return
        val list = mutableListOf<Card>()
        for (i in 0 until 16) {
            list.add(g.cardAtIndex(i))
        }
        adapter.updateData(list)
        tvTwoScore.text = "Score:${g.score}"
    }

    override fun onStop() {
        super.onStop()
        saveGame()
    }

    private fun saveGame() {
        val g = game ?: return
        try {
            val output: FileOutputStream? = requireActivity().openFileOutput("game2.dat", Activity.MODE_PRIVATE)
            val objOut = ObjectOutputStream(output)
            objOut.writeObject(g)
            objOut.close()
            output?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadGame(): CardPairGame? {
        return try {
            val input: FileInputStream? = requireActivity().openFileInput("game2.dat")
            val objIn = ObjectInputStream(input)
            val obj = objIn.readObject() as CardPairGame
            objIn.close()
            input?.close()
            obj
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
