package com.elfan.mynews

import android.app.Application
import com.elfan.mynews.repositories.*
import com.elfan.mynews.ui.detail_article.ReviewViewModel
import com.elfan.mynews.ui.login.LoginViewModel
import com.elfan.mynews.ui.main.home.HomeViewModel
import com.elfan.mynews.ui.main.home.article.ArticleViewModel
import com.elfan.mynews.ui.register.RegisterViewModel
import com.elfan.mynews.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyNews : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyNews)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

val repositoryModules = module {
    factory { ArticleRepository(get()) }
    factory { UserRepository(get()) }
    factory { CategoryRepository(get()) }
    factory { ReviewRepository(get()) }
    factory { CommentRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ArticleViewModel(get()) }
    viewModel { ReviewViewModel(get(), get()) }

}