package io.github.droidkaigi.confsched2018.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.github.droidkaigi.confsched2018.data.api.DroidKaigiApi
import io.github.droidkaigi.confsched2018.data.api.FeedApi
import io.github.droidkaigi.confsched2018.data.api.FeedFireStoreApi
import io.github.droidkaigi.confsched2018.data.api.response.mapper.ApplicationJsonAdapterFactory
import io.github.droidkaigi.confsched2018.data.api.response.mapper.LocalDateTimeJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
internal class NetworkModule {

    @Singleton @Provides
    fun provideDroidKaigiService(): DroidKaigiApi {
        val httpClient = OkHttpClient
                .Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://sessionize.com/api/v2/xtj7shk8/view/")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(ApplicationJsonAdapterFactory.INSTANCE)
                        .add(LocalDateTime::class.java, LocalDateTimeJsonAdapter())
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
                .create<DroidKaigiApi>(DroidKaigiApi::class.java)
    }

    @Singleton @Provides
    fun provideFeedApi(): FeedApi = FeedFireStoreApi()
}
