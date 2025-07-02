import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectbeta.R
import com.example.projectbeta.data.network.Article

class AutoSliderAdapter(
    private val articles: List<Article>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AutoSliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val container: View = itemView // root view for click handling
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_auto_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title
        Glide.with(holder.itemView.context).load(article.urlToImage).into(holder.imageView)

        // Set click listener
        holder.container.setOnClickListener {
            article.url?.let { url -> onItemClick(url) }
        }
    }

    override fun getItemCount() = articles.size
}