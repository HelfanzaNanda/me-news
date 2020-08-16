package com.elfan.mynews.ui.detail_article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.elfan.mynews.R
import com.elfan.mynews.models.Review
import com.elfan.mynews.utils.Constants
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.item_comment.view.*

class ReviewAdapter (private var reviews : MutableList<Review>,
                     private var context: Context,
                     private var reviewViewModel: ReviewViewModel)
    : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false))
    }

    override fun getItemCount(): Int =reviews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(reviews[position], context, reviewViewModel)


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val setUpInComment = SetUpInComment()
        fun bind(review: Review, context: Context, reviewViewModel: ReviewViewModel){
            with(itemView){
                txt_name.text = review.user.name
                txt_email.text = review.user.email
                txt_comment.isLongClickable = true
                txt_comment.text = review.comment
                setUpInComment.setUpRecyclerInComment(itemView, context)
                setUpInComment.handleComment(itemView, review.comments)
                btn_send.setOnClickListener {
                    val token = Constants.getToken(context)
                    val comment = et_comment.text.toString()
                    if (reviewViewModel.validate(null, comment)){
                        reviewViewModel.createComment(token, review.id.toString(), comment)
                    }
                }
                btn_send_edit.setOnClickListener {
                    val token = Constants.getToken(context)
                    val comment = et_comment_edit.text.toString()
                    if (reviewViewModel.validate(null, comment)){
                        reviewViewModel.updateReview(token, review.id.toString(), comment)
                    }
                }
                txt_comment.setOnCreateContextMenuListener{ menu, v, _ -> contextListener(menu, v, txt_comment, context, reviewViewModel, review) }
                et_comment.setOnCreateContextMenuListener{menu, v, _ -> contextListener(menu, v, et_comment, context, reviewViewModel, review) }
            }
        }

        private fun contextListener(menu: ContextMenu, v : View, view: View, context: Context, reviewViewModel: ReviewViewModel, review: Review){
            when(view){
                view.txt_comment -> {
                    val token = Constants.getToken(context)
                    if (token.equals("Bearer ${review.user.token}")){
                        val copy = menu.add(0, v.id, 0, "Salin")
                        menuItemClickListener(copy, context, reviewViewModel, review)
                        val edit = menu.add(0, v.id, 0, "Edit")
                        menuItemClickListener(edit, context, reviewViewModel, review)
                        val hapus = menu.add(0, v.id, 0, "Hapus")
                        menuItemClickListener(hapus, context, reviewViewModel, review)
                    }else{
                        val copy = menu.add(0, v.id, 0, "Salin")
                        menuItemClickListener(copy, context, reviewViewModel, review)
                    }
                }
                view.et_comment -> {
                    val paste = menu.add(0, v.id, 0, "Paste")
                    menuItemClickListener(paste, context, reviewViewModel, review)
                }
            }
        }

        private fun menuItemClickListener(menuItem: MenuItem, context: Context, reviewViewModel: ReviewViewModel, review: Review){
            menuItem.setOnMenuItemClickListener {
                when(it.title){
                    "Salin" -> reviewViewModel.copyText(context, itemView.txt_comment.text.toString())
                    "Paste" -> reviewViewModel.pasteText(context, itemView.et_comment)
                    "Edit" -> {
                        itemView.txt_comment.gone()
                        reviewViewModel.copyText(context, itemView.txt_comment.text.toString())
                        reviewViewModel.pasteText(context, itemView.et_comment_edit)
                        itemView.linear_comment_edit.visible()
                    }
                    "Hapus" -> {
                        val mesagge = "apakah anda yakin ?"
                        val token = Constants.getToken(context)
                        popup(mesagge, context, reviewViewModel, token, review.id.toString())
                    }
                }
                true
            }
        }

        private fun popup(message : String, context: Context, reviewViewModel: ReviewViewModel, token : String, id : String){
            AlertDialog.Builder(context).apply {
                setMessage(message)
                setPositiveButton("ya"){dialog, _ ->
                    dialog.dismiss()
                    reviewViewModel.deleteReview(token, id)
                }
                setNegativeButton("tidak"){dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    fun changelist(r : List<Review>){
        reviews.clear()
        reviews.addAll(r)
        notifyDataSetChanged()
    }
}
