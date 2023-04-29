package com.sanryoo.matcher

import android.app.Application
import android.content.Context
import android.util.Log
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class MatcherApp : Application() {

    @Inject
    @Named(value = MyString.USER)
    lateinit var user: MutableStateFlow<User>

    override fun onCreate() {
        super.onCreate()
        getId()
    }

    private fun getId() {
        val sf = getSharedPreferences("matcher_sf", Context.MODE_PRIVATE)
        val id = sf.getLong("id", 0L)
        user.value = user.value.copy(id = id)
    }
}