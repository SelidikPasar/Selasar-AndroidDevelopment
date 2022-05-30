package com.selasarteam.selidikpasar.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.selasarteam.selidikpasar.BuildConfig
import com.selasarteam.selidikpasar.data.local.entity.NewsEntity
import com.selasarteam.selidikpasar.data.local.room.NewsDao
import com.selasarteam.selidikpasar.data.remote.service.ApiService
import com.selasarteam.selidikpasar.utils.Result
import com.selasarteam.selidikpasar.utils.SessionPreferences

class MainRepository private constructor(
    private val newsDao: NewsDao,
    private val preferences: SessionPreferences,
    private val apiService: ApiService
) {
    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(BuildConfig.API_KEY)
            val articles = response.articles
            val newsList = articles.map { article ->
                NewsEntity(
                    article.title,
                    article.author,
                    article.publishedAt,
                    article.content,
                    article.urlToImage,
                    article.url
                )
            }
            newsDao.deleteAll()
            newsDao.insertNews(newsList)
        } catch (e: Exception) {
            Log.d(TAG, "getHeadlineNews: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<NewsEntity>>> =
            newsDao.getNews().map { Result.Success(it) }
        emitSource(localData)
    }

    companion object {
        private const val TAG = "MainRepository"

        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            newsDao: NewsDao,
            preferences: SessionPreferences,
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(newsDao, preferences, apiService)
            }.also { instance = it }
    }
}