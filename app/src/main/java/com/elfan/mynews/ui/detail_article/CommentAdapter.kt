package com.elfan.mynews.ui.detail_article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.elfan.mynews.R
import com.elfan.mynews.models.Comment
import com.elfan.mynews.utils.Constants
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter (private var comments : MutableList<Comment>, private var context: Context)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_comment_other, parent, false)))
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(comments[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val setUpInComment = SetUpInComment()
        fun bind(comment: Comment, context: Context){
            with(itemView){
                txt_name.text = comment.user.name
                txt_email.text = comment.user.email
                txt_comment.text = comment.comment
            }
        }
    }

    fun changelist(c : List<Comment>){
        comments.clear()
        comments.addAll(c)
        notifyDataSetChanged()
    }
}