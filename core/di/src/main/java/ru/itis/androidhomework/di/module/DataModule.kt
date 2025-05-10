package ru.itis.androidhomework.di.module

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.androidhomework.BuildConfig
import ru.itis.androidhomework.data.remote.OpenTripMapApi
import ru.itis.androidhomework.BuildConfig.OPEN_TRIP_MAP_BASE_URL
import ru.itis.androidhomework.data.local.AppDatabase
import ru.itis.androidhomework.data.local.dao.FeatureDao
import ru.itis.androidhomework.data.local.dao.LocalDao
import ru.itis.androidhomework.data.local.dao.RequestHistoryDao
import ru.itis.androidhomework.data.local.dao.UserDao
import ru.itis.androidhomework.data.local.migrations.Migration_1_2
import ru.itis.androidhomework.data.remote.interceptors.AuthInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOpenTripMapApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): OpenTripMapApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(OPEN_TRIP_MAP_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)

        return retrofit.build().create(OpenTripMapApi::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ) : OkHttpClient {
        val builder = if (BuildConfig.DEBUG) {
            getUnsafeOkHttpClientBuilder()
        } else {
            OkHttpClient.Builder()
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        builder.addInterceptor(authInterceptor).addInterceptor(loggingInterceptor)

        return builder.build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context) : AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_LOG_KEY)
        .addMigrations(Migration_1_2())
        .build()

    @Provides
    @Singleton
    fun provideLocalDao(db: AppDatabase) : LocalDao = db.localDao

    @Provides
    @Singleton
    fun provideFeatureDao(db: AppDatabase) : FeatureDao = db.featuresDao

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) : UserDao = db.userDao


    @Provides
    @Singleton
    fun provideRequestHistoryDao(db: AppDatabase) : RequestHistoryDao = db.requestHistoryDao

    @SuppressLint("CustomX509TrustManager")
    private fun getUnsafeOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier { _, _ -> true }
            }

            return okHttpClient
        } catch (e: Exception) {
            return okHttpClient
        }
    }
}