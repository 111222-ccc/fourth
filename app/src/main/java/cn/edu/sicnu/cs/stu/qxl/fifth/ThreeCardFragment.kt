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
import cn.edu.sicnu.cs.stu.qxl.fifth.model.CardMatchingGame
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ThreeCardFragment : Fragment() {

    private lateinit var rvCards: RecyclerView
    private lateinit var tvScore: TextView
    private lateinit var btnReset: Button
    private lateinit var adapter: CardAdapter
    private var game: CardMatchingGame? = null
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        @JvmStatic
        fun newInstance() = ThreeCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCards = view.findViewById(R.id.rv_cards)
        tvScore = view.findViewById(R.id.tv_score)
        btnReset = view.findViewById(R.id.btn_reset)

        // 初始化适配器，传入卡牌点击回调
        adapter = CardAdapter { pos ->
            game?.chooseCardAtIndex(pos)
            updateUI()
            // 选满3张，且没有得分（代表不匹配），延迟翻回
            val g = game ?: return@CardAdapter
            if(g.chosenCards.size == 3 && g.score <=0){
                handler.postDelayed({
                    g.clearChosenCards()
                    updateUI()
                },800)
            }
        }

        // 根据屏幕方向设置列数
        val spanCount = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 4
        rvCards.layoutManager = GridLayoutManager(requireContext(), spanCount)
        rvCards.adapter = adapter

        // 读取本地序列化保存的游戏对象；读不到就新建24张三牌游戏
        game = loadGame() ?: CardMatchingGame(24)
        updateUI()

        // 重置按钮
        btnReset.setOnClickListener {
            game?.reset()
            updateUI()
        }
    }

    /** 更新界面：刷新卡牌列表、更新分数 */
    private fun updateUI() {
        val g = game ?: return
        val cardList = mutableListOf<cn.edu.sicnu.cs.stu.qxl.fifth.model.Card>()
        for(i in 0 until 24){
            cardList.add(g.cardAtIndex(i))
        }
        adapter.updateData(cardList)
        tvScore.text = "Score:${g.score}"
    }

    /** onStop的时候保存游戏状态到文件 */
    override fun onStop() {
        super.onStop()
        saveGame()
    }

    /** 序列化保存游戏对象到文件 game3.dat */
    private fun saveGame(){
        val g = game ?: return
        try {
            val outputStream: FileOutputStream? = requireActivity().openFileOutput("game3.dat", Activity.MODE_PRIVATE)
            val objOut = ObjectOutputStream(outputStream)
            objOut.writeObject(g)
            objOut.close()
            outputStream?.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /** 从文件读取序列化的CardMatchingGame对象 */
    private fun loadGame(): CardMatchingGame?{
        return try {
            val inputStream: FileInputStream? = requireActivity().openFileInput("game3.dat")
            val objIn = ObjectInputStream(inputStream)
            val obj = objIn.readObject() as CardMatchingGame
            objIn.close()
            inputStream?.close()
            obj
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
