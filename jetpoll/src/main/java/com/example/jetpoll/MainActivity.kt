package com.example.jetpoll

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.example.jetpoll.data.DataSource
import com.example.jetpoll.domain.RepoImpl
import com.example.jetpoll.presentation.PollViewModel
import com.example.jetpoll.presentation.PollViewModelFactory
import com.example.jetpoll.ui.home.PollMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase

/**
 * [EN]
 * JetPoll is an app that let you create polls with options that others can select
 * Currently WIP
 */

/**
 * [ES]
 * JetPoll es una aplicación en la cual puedes crear encuestas con opciones que otros pueden responder
 * Actualmente en construccion
 */

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PollViewModel> { PollViewModelFactory(RepoImpl(DataSource())) }
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFirebaseEmulators()
        currentUser = FirebaseAuth.getInstance().currentUser
        setContent {
            if (currentUser != null) {
                PollMain(viewModel = viewModel, backDispatcher = onBackPressedDispatcher)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun setUpFirebaseEmulators() {
        val host = "10.0.2.2"
        Firebase.firestore.useEmulator(host, 8080)
        Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false)
                .build()
        var functions: FirebaseFunctions = FirebaseFunctions.getInstance()
        functions.useEmulator(host, 5001)
    }
}