package app.bangkit.ishara.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.bangkit.ishara.R
import app.bangkit.ishara.ui.main.ui.home.TodaySign


class TodaySignAdapter(
    private val context: Context,
    private val todaySigns: List<TodaySign>
) : RecyclerView.Adapter<TodaySignAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cv_today_sign)
        val ivTodaySign: ImageView = itemView.findViewById(R.id.iv_today_sign)
        val tvTodaySign: TextView = itemView.findViewById(R.id.tv_today_sign)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_today_sign, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sign = todaySigns[position]

        holder.ivTodaySign.setImageResource(sign.imagePath)
        holder.tvTodaySign.text = sign.alphabet
    }

    override fun getItemCount(): Int {
        return todaySigns.size
    }
}
