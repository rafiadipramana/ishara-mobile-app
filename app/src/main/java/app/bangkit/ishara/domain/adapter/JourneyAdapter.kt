package app.bangkit.ishara.domain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.bangkit.ishara.R
import app.bangkit.ishara.data.models.Item
import app.bangkit.ishara.databinding.StageLayoutBinding
import app.bangkit.ishara.databinding.LevelLayoutBinding

class JourneyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val items: MutableList<Item> = mutableListOf()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitList(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.StageItem -> VIEW_TYPE_JOURNEY
            is Item.StageLevelItem -> VIEW_TYPE_LEVEL
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_JOURNEY -> {
                val binding =
                    StageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                JourneyViewHolder(binding)
            }

            VIEW_TYPE_LEVEL -> {
                val binding =
                    LevelLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LevelViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JourneyViewHolder -> {
                val journeyItem = items[position] as Item.StageItem
                holder.bind(journeyItem)
                holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(journeyItem) }
            }

            is LevelViewHolder -> {
                val levelItem = items[position] as Item.StageLevelItem
                holder.bind(levelItem)
                holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(levelItem) }
            }
        }
    }

    class JourneyViewHolder(private val binding: StageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(journeyItem: Item.StageItem) {
            binding.tvStageName.text = journeyItem.stageData.name
            binding.tvStageDescription.text = journeyItem.stageData.description
            if (journeyItem.stageData.isUnlocked) {
                binding.cardViewStageItem.setCardBackgroundColor(itemView.context.getColor(R.color.blue))
            } else {
                binding.cardViewStageItem.setCardBackgroundColor(itemView.context.getColor(R.color.grey))
            }
        }
    }

    class LevelViewHolder(private val binding: LevelLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val starContainer: LinearLayout = binding.starContainer

        fun bind(levelItem: Item.StageLevelItem) {
            starContainer.removeAllViews()

            val numberOfStars = levelItem.levelData.userLevelStar?.obtainedStars
            val maxStars = 5
            val starWidth =
                itemView.context.resources.getDimensionPixelSize(R.dimen.star_image_width)
            val starMarginStart =
                itemView.context.resources.getDimensionPixelSize(R.dimen.star_image_margin_start)

            if (levelItem.levelData.isStageUnlocked == true) {
                if (numberOfStars != null) {
                    binding.cvLevel.setCardBackgroundColor(itemView.context.getColor(R.color.orange))
                    for (i in 0 until numberOfStars) {
                        val starActiveImageView = ImageView(itemView.context)
                        val params = LinearLayout.LayoutParams(
                            starWidth,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.marginStart = starMarginStart
                        starActiveImageView.layoutParams = params
                        starActiveImageView.adjustViewBounds = true
                        starActiveImageView.setImageResource(R.drawable.star_active)

                        starContainer.addView(starActiveImageView)
                    }
                    for (i in 0 until (maxStars - numberOfStars)) {
                        val starInactiveImageView = ImageView(itemView.context)
                        val params = LinearLayout.LayoutParams(
                            starWidth,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.marginStart = starMarginStart
                        starInactiveImageView.layoutParams = params
                        starInactiveImageView.adjustViewBounds = true
                        starInactiveImageView.setImageResource(R.drawable.star_inactive)

                        starContainer.addView(starInactiveImageView)
                    }
                } else {
                    binding.cvLevel.setCardBackgroundColor(itemView.context.getColor(R.color.lightGrey))
                }
            } else {
                binding.cvLevel.setCardBackgroundColor(itemView.context.getColor(R.color.grey))
            }

            binding.tvLevelName.text = levelItem.levelData.name
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Item)
    }

    companion object {
        private const val VIEW_TYPE_JOURNEY = 1
        private const val VIEW_TYPE_LEVEL = 2
    }
}
