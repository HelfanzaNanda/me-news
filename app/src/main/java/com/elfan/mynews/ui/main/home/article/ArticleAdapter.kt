package com.elfan.mynews.ui.main.home.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elfan.mynews.R
import com.elfan.mynews.models.Article
import com.elfan.mynews.ui.detail_article.DetailArticleActivity
import com.elfan.mynews.utils.Constants
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(private var articles : MutableList<Article>, private var context: Context)
    : RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(articles[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(article: Article, context: Context){
            with(itemView) {
                txt_name.text = article.user.name
                txt_email.text = article.user.email
                txt_title.text = article.title
                txt_content.text = Constants.getSafeSubstring(article.content!!, 100)+"..."
                txt_total_comment.text = "${article.totalComment} Komentar"
                img_article.load(article.image)
                setOnClickListener {
                    context.startActivity(Intent(context, DetailArticleActivity::class.java).apply {
                        putExtra("ARTICLE", article)
                    })
                }
            }
        }
    }

    fun changelist(a : List<Article>){
        articles.clear()
        articles.addAll(a)
        notifyDataSetChanged()
    }
}