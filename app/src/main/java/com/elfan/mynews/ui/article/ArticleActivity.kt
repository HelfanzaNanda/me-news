package com.elfan.mynews.ui.article

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.elfan.mynews.R
import com.elfan.mynews.models.Category
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.content_article.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleActivity : AppCompatActivity() {

    private val articleViewModel : ArticleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        setSupportActionBar(findViewById(R.id.toolbar))

        observe()

    }

    private fun observe() {
        articleViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        articleViewModel.listenToCategories().observe(this, Observer { handleCategories(it) })
    }

    private fun handleCategories(it: List<Category>) {
        val adapter = ArrayAdapter(this@ArticleActivity, android.R.layout.simple_spinner_item, it).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_category?.adapter = adapter
        spinner_category?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category_id = it[position].id
                save(category_id.toString())
            }

        }
    }

    private fun save(category_id : String){

    }

    private fun handleUiState(it: ArticleState) {
        when(it){
            is ArticleState.Loading -> handleLoading(it.state)
            is ArticleState.ShowToast -> toast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) {
        fab_save.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun toast(m : String) = Toast.makeText(this, m, Toast.LENGTH_LONG).show()
}