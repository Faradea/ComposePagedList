package com.example.composetutorial

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.toLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava2.RxPagingSource
import androidx.paging.rxjava2.cachedIn
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class MainViewModel: ViewModel() {

    val repository = ArticlesRepository(ArticlesPagingSource())

    // toMutableStateList() нужен если list может меняться - например, добавление элементов
    // MediatorLiveData
//    private val _articles: Flow<PagingData<Article>> = Pager(PagingConfig(pageSize = 6)) {
//
//    }
//    val articles: List<Article>
//        get() = _articles

    fun getArticlesPaged(): LiveData<PagingData<Article>> {
        return repository.getArticles()
            .cachedIn(viewModelScope)
            .toLiveData()
    }
}

fun getArticlesList(page: Int) = ArticlesPaged(
    items = List(20) { i -> Article("Title $i page $page", "Desc $i") },
    totalPages = 5
)


data class Article(
    val title: String,
    val desc: String
)

// Если элементы списка - мутабельные
//class WellnessTask(
//    val id: Int,
//    val label: String,
//    initialChecked: Boolean = false
//) {
//    var checked by mutableStateOf(initialChecked)
//}

data class ArticlesPaged(
    val items: List<Article>,
    val totalPages: Int
)