package com.elfan.mynews.ui.detail_article

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.elfan.mynews.models.Comment
import kotlinx.android.synthetic.main.item_comment.view.*


class SetUpInComment {
    fun setUpRecyclerInComment(itemView: View, context: Context){
        with(itemView){
            recycler_view.apply {
                adapter = CommentAdapter(mutableListOf(), context)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    fun handleComment(view: View, comments : List<Comment>){
        view.recycler_view.adapter?.let { adapter ->
            if (adapter is CommentAdapter){
                adapter.changelist(comments)
            }
        }
    }
}