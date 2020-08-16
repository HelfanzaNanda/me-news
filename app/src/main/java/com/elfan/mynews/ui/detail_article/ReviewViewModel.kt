package com.elfan.mynews.ui.detail_article

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.Comment
import com.elfan.mynews.models.CreateReview
import com.elfan.mynews.models.Review
import com.elfan.mynews.repositories.CommentRepository
import com.elfan.mynews.repositories.ReviewRepository
import com.elfan.mynews.utils.ArrayResponse
import com.elfan.mynews.utils.SingleLiveEvent
import com.elfan.mynews.utils.SingleResponse
import kotlinx.android.synthetic.main.item_comment.view.*

class ReviewViewModel (private val reviewRepository: ReviewRepository,
                       private val commentRepository: CommentRepository) : ViewModel(){
    private val state : SingleLiveEvent<ReviewState> = SingleLiveEvent()
    private val reviews = MutableLiveData<List<Review>>()

    private fun isLoading(b : Boolean){ state.value = ReviewState.Loading(b) }
    private fun toast(m : String){ state.value = ReviewState.ShowToast(m) }
    private fun success(){ state.value = ReviewState.Success }
    private fun successComment(){ state.value = ReviewState.SuccessComment }
    private fun successDeleteReview(){ state.value = ReviewState.SuccessDeleteReview }
    private fun successUpdateReview(){ state.value = ReviewState.SuccessUpdateReview }

    fun fetchReviews(article_id : String) {
        isLoading(true)
        reviewRepository.fetchReview(article_id, object : ArrayResponse<Review>{
            override fun onSuccess(datas: List<Review>?) {
                isLoading(false)
                datas?.let { reviews.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun createReview(token : String, createReview: CreateReview){
        isLoading(true)
        reviewRepository.createReview(token, createReview, object : SingleResponse<CreateReview>{
            override fun onSuccess(data: CreateReview?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun updateReview(token: String, id: String, comment: String){
        isLoading(true)
        reviewRepository.updateReview(token,id, comment, object : SingleResponse<Review>{
            override fun onSuccess(data: Review?) {
                isLoading(false)
                data?.let { successUpdateReview() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun deleteReview(token: String, id : String){
        isLoading(true)
        reviewRepository.deleteReview(token, id, object : SingleResponse<Review>{
            override fun onSuccess(data: Review?) {
                isLoading(false)
                data?.let { successDeleteReview() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun createComment(token: String, review_id : String, comment: String){
        isLoading(true)
        commentRepository.createComment(token, review_id, comment, object : SingleResponse<Comment>{
            override fun onSuccess(data: Comment?) {
                isLoading(false)
                data?.let { successComment() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let {
                    toast(it.message.toString())
                    println(it.message)
                }
            }

        })
    }

    fun copyText(context: Context, comment : String){
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clipData = ClipData.newPlainText("text", comment)
        manager?.setPrimaryClip(clipData)
        toast("berhasil menyalin komentar")
    }

    fun pasteText(context: Context, editText: EditText){
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        if (manager != null && manager.primaryClip != null && manager.primaryClip!!.itemCount > 0) {
            editText.setText(manager.primaryClip!!.getItemAt(0).text.toString())
        }
    }

    fun validate(rate : Int?, comment : String) : Boolean{
        if (rate == 0){
            toast("rating harus lebih dari 0")
            return false
        }
        if (comment.isEmpty()){
            toast("komentar harus di isi")
            return false
        }
        return true
    }

    fun listenToState() = state
    fun listenToReviews() = reviews

}

sealed class ReviewState{
    data class Loading(var state : Boolean = false) : ReviewState()
    data class ShowToast(var message : String) : ReviewState()
    object Success : ReviewState()
    object SuccessComment : ReviewState()
    object SuccessDeleteReview : ReviewState()
    object SuccessUpdateReview : ReviewState()
}