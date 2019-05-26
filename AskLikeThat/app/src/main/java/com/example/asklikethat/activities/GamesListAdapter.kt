package com.example.asklikethat.activities

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asklikethat.R
import com.example.asklikethat.firebase.OneVsOneGame
import kotlinx.android.synthetic.main.room_list_item_layout.view.*

class GamesListAdapter(
    private val dataset: ArrayList<OneVsOneGame>,
    val context: Context,
    private val clickListener: (OneVsOneGame) -> Unit
) : RecyclerView.Adapter<GamesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.room_list_item_layout, parent, false))
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position], clickListener)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(game: OneVsOneGame, clickListener: (OneVsOneGame) -> Unit) {
            val textViewRoomName = itemView.roomNameTextView.apply {
                text = game.gameName
            }
            val statusTextView = itemView.playersStateTextView
            statusTextView.text = if (game.isEnded()) {
                "Game finished"
            } else {
                "Waiting for ${game.nextToPlay.name} to play"
            }
            itemView.setOnClickListener { clickListener(game) }
        }
    }
}