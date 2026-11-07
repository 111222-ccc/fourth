package cn.edu.sicnu.cs.stu.qxl.fifth

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.stu.qxl.fifth.databinding.ItemCardBinding
import cn.edu.sicnu.cs.stu.qxl.fifth.model.Card

class CardAdapter(
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var cardList: List<Card> = emptyList()

    inner class CardViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val ivCard: ImageView = binding.ivCard
        private val tvText: TextView = binding.tvText

        init {
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    onCardClick(pos)
                }
            }
        }

        fun bind(card: Card) {
            when {
                card.isMatched -> {
                    tvText.text = card.toString()
                    ivCard.setImageResource(android.R.color.white)
                    binding.root.isEnabled = false
                }
                card.isChosen -> {
                    tvText.text = card.toString()
                    ivCard.setImageResource(android.R.color.white)
                    binding.root.isEnabled = true
                }
                else -> {
                    tvText.text = ""
                    ivCard.setImageResource(R.drawable.background)
                    binding.root.isEnabled = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int = cardList.size

    // 替换数据集，通知RecyclerView全部刷新
    fun updateData(newList: List<Card>){
        cardList = newList.toList()
        notifyDataSetChanged()
    }
}
