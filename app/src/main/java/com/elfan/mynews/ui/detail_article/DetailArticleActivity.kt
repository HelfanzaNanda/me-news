package com.elfan.mynews.ui.detail_article

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.elfan.mynews.R
import com.elfan.mynews.models.Article
import com.elfan.mynews.models.CreateReview
import com.elfan.mynews.models.Review
import com.elfan.mynews.utils.Constants
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import com.stepstone.apprating.AppRatingDialog
import com.stepstone.apprating.listener.RatingDialogListener
import kotlinx.android.synthetic.main.activity_detail_article.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.recycler_view
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailArticleActivity : AppCompatActivity(), RatingDialogListener {

    private val reviewViewModel : ReviewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)
        setUpRecyclerComment()
        showDialogComment()
        setUi()
        observer()
    }

    private fun setUpRecyclerComment() {
        recycler_view.apply {
            adapter = ReviewAdapter(mutableListOf(), this@DetailArticleActivity, reviewViewModel)
            layoutManager = LinearLayoutManager(this@DetailArticleActivity)
        }
    }

    private fun observer() {
        reviewViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        reviewViewModel.listenToReviews().observe(this, Observer { handleReviews(it) })
    }

    private fun handleReviews(it: List<Review>) {
        val totalRating = it.sumBy { review ->review.rating!!  }
        val average = if (totalRating > 0) totalRating / getPassedArticle()?.totalComment!! else 0
        ratingBar.rating = average.toFloat()
        recycler_view.adapter?.let { adapter ->
            if (adapter is ReviewAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUiState(it: ReviewState) {
        when(it){
            is ReviewState.Loading -> handleLoading(it.state)
            is ReviewState.ShowToast -> toast(it.message)
            is ReviewState.Success -> fetchReviews()
            is ReviewState.SuccessComment -> handleSuccess("berhasil mengkomentari")
            is ReviewState.SuccessDeleteReview -> handleSuccess("berhasil delete komentar")
            is ReviewState.SuccessUpdateReview -> handleSuccess("berhasil update komentar")
        }
    }

    private fun handleSuccess(message: String) {
        toast(message)
        fetchReviews()
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            shimmer_layout.visible()
            shimmer_layout.startShimmer()
        }else{
            shimmer_layout.gone()
            shimmer_layout.stopShimmer()
        }
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    private fun setUi(){
        getPassedArticle()?.let {
            img_article.load(it.image)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                txt_content.justificationMode = JUSTIFICATION_MODE_INTER_WORD
            }
            txt_title.text = it.title
            txt_content.text = it.content
            txt_total_comment.text = "${it.totalComment} Komentar"
        }
    }

    private fun showDialogComment(){
        btn_review.setOnClickListener {
            AppRatingDialog.Builder()
                .setPositiveButtonText("Komentar")
                .setNegativeButtonText("Batal")
                .setNumberOfStars(5)
                .setDefaultRating(1)
                .setTitle("Rate this application")
                .setTitleTextColor(R.color.colorGrey)
                .setCommentTextColor(R.color.colorDrakGrey)
                .setCommentBackgroundColor(R.color.colorWhite)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setStarColor(R.color.colorPrimaryDark)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorGrey)
                .create(this@DetailArticleActivity)
                .show()
        }
    }

    override fun onPositiveButtonClicked(rate: Int, comment: String) = createReview(rate, comment)

    override fun onNegativeButtonClicked() {
        toast( "Negative button clicked")
    }

    override fun onNeutralButtonClicked() {
        toast("Neutral button clicked")
    }

    private fun createReview(rate : Int, comment: String){
        val token = Constants.getToken(this@DetailArticleActivity)
        val createReview = CreateReview(articleId = getPassedArticle()?.id, rating = rate, comment = comment)
        if (reviewViewModel.validate(rate, comment)){
            reviewViewModel.createReview(token, createReview)
        }
    }

    private fun getPassedArticle() : Article? = intent.getParcelableExtra("ARTICLE")
    private fun toast(message : String) = Toast.makeText(this@DetailArticleActivity, message, Toast.LENGTH_LONG).show()
    private fun fetchReviews() = reviewViewModel.fetchReviews(getPassedArticle()?.id.toString())


    override fun onResume() {
        super.onResume()
        fetchReviews()
    }
}