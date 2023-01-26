package com.example.composetutorial

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import androidx.paging.rxjava2.flowable
import io.reactivex.Flowable
import io.reactivex.Single


class ArticlesRepository(private val pagingSource: ArticlesPagingSource) {

    fun getArticles(): Flowable<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 2000,
                prefetchDistance = 5,
                initialLoadSize = 20),
            pagingSourceFactory = { pagingSource }
        ).flowable
    }
}

class ArticlesPagingSource: RxPagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Article>> {
        val pageToLoad = params.key ?: 1
        val articlesList = getArticlesList(page = pageToLoad)
        return if (pageToLoad != 3) {
            Single.just(
                LoadResult.Page(
                    data = articlesList.items,
                    prevKey = if (pageToLoad == 1) null else pageToLoad - 1,
                    nextKey = if (pageToLoad == articlesList.totalPages) null else pageToLoad + 1
                )
            )
        } else {
            Single.just(
                LoadResult.Error(
                    Throwable(message = "Something went wrong")
                )
            )
        }
    }

}