package com.example.moviesapp.util

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class GlideAppModule : AppGlideModule() {

    enum class CacheOptions {
        Memory, Local, All
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
        //builder.setDefaultTransitionOptions(Drawable.class, DrawableTransitionOptions.withCrossFade());
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        val builder = OkHttpClient.Builder()
        builder.connectionPool(ConnectionPool(THREAD_POOL_SIZE, 1, TimeUnit.SECONDS))
        val factory = OkHttpUrlLoader.Factory(builder.build())

        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    companion object {

        private val THREAD_POOL_SIZE = 10

        @JvmOverloads
        fun getRequest(context: Context, options: CacheOptions = CacheOptions.Local): GlideRequests {
            return GlideApp.with(context).applyDefaultRequestOptions(getStorageOptions(options))
        }

        @JvmOverloads
        fun getRequest(view: View, options: CacheOptions = CacheOptions.Local): GlideRequests {
            return GlideApp.with(view).applyDefaultRequestOptions(getStorageOptions(options))
        }

        private fun getStorageOptions(cacheOptions: CacheOptions): RequestOptions {
            val options = RequestOptions()
            return when (cacheOptions) {
                CacheOptions.Local ->  options.diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(true)
                CacheOptions.Memory -> options.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false)
                CacheOptions.All -> return options.diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false)
            }
        }
    }
}