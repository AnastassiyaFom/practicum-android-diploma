package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.common.NetworkClient
import ru.practicum.android.diploma.common.network.RetrofitNetworkClient
import ru.practicum.android.diploma.common.network.VacancyApi
import ru.practicum.android.diploma.favorites.data.db.AppDatabase
import ru.practicum.android.diploma.favorites.data.db.VacancyDao
import ru.practicum.android.diploma.filters.data.CountriesRepositoryImpl
import ru.practicum.android.diploma.filters.data.FiltersRepositoryImpl
import ru.practicum.android.diploma.filters.data.StorageClient
import ru.practicum.android.diploma.filters.data.StorageFiltersClient
import ru.practicum.android.diploma.filters.domain.CountriesRepository
import ru.practicum.android.diploma.filters.domain.FiltersRepository
import ru.practicum.android.diploma.filters.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.SearchVacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.SearchVacanciesRepository
import ru.practicum.android.diploma.vacancy.data.ExternalNavigatorImpl
import ru.practicum.android.diploma.vacancy.data.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT_SECONDS = 30L
private const val READ_TIMEOUT_SECONDS = 60L
private const val BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
private const val DATABASE_NAME = "database.db"

val dataModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.API_ACCESS_TOKEN}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single<VacancyApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacancyApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME)
            .build()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<SearchVacanciesRepository> {
        SearchVacanciesRepositoryImpl(get())
    }

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(get(), get())
    }

    single<FiltersRepository> {
        FiltersRepositoryImpl(get(), get())
    }

    factory { Gson() }

    single<VacancyDao> { get<AppDatabase>().vacancyDao() }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<CountriesRepository> {
        CountriesRepositoryImpl(get())
    }

    single<StorageClient<FilterParameters>> {
        StorageFiltersClient(
            androidContext(),
            object : TypeToken<FilterParameters>() {}.type
        )
    }
}
