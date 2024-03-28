package biz.filmeroo.premier.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.databinding.ItemFilmBinding
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import javax.inject.Inject

internal class FilmAdapter @Inject constructor(private val picasso: Picasso) :
    ListAdapter<ApiFilm, FilmAdapter.Holder>(diffCallback) {

    init {
        setHasStableIds(true)
    }

    private var onClick: ((Long) -> Unit)? = null

    fun setOnClick(onClick: (Long) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    override fun getItemId(position: Int) = getItem(position).id

    inner class Holder(private val itemFilmBinding: ItemFilmBinding) : RecyclerView.ViewHolder(itemFilmBinding.root) {
        fun bind(item: ApiFilm) {
            itemFilmBinding.title.text = item.title
            itemFilmBinding.overview.text = item.overview
            itemFilmBinding.rating.text = item.voteAverage
            val cornerRadius = itemFilmBinding.root.resources.getDimensionPixelSize(R.dimen.image_corner_radius)
            picasso
                .load(FilmService.buildImageUrl(item.posterPath))
                .transform(RoundedCornersTransformation(cornerRadius, 0))
                .into(itemFilmBinding.image)
            itemFilmBinding.root.setOnClickListener { onClick?.invoke(item.id) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ApiFilm>() {
            override fun areItemsTheSame(old: ApiFilm, new: ApiFilm) = old.id == new.id
            override fun areContentsTheSame(old: ApiFilm, new: ApiFilm) = old == new
        }
    }
}
