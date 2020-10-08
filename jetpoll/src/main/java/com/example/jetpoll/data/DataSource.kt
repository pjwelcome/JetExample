package com.example.jetpoll.data

import android.util.Log
import com.example.jetpoll.data.model.Option
import com.example.jetpoll.data.model.Poll
import com.example.jetpoll.vo.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowViaChannel
import kotlinx.coroutines.tasks.await

class DataSource {
    private val firestore = Firebase.firestore
    private val pollReference = firestore.collection("polls")

    @ExperimentalCoroutinesApi
    fun getpolls () : Flow<List<Poll>> {
        firestore.firestoreSettings = firestoreSettings {
            host = "http://10.0.2.2:4001"
            isSslEnabled = false
            isPersistenceEnabled = false
        }
        return callbackFlow {
            pollReference.addSnapshotListener { data, error ->
                if (error != null) {
                    close(error)
                } else {
                    if (data != null) {
                        val messages = data.toObjects(Poll::class.java)
                        offer(messages)
                    } else {
                        close(CancellationException("No data received"))
                    }
                }
            }
           awaitClose  {
                cancel()
            }
        }
    }

    suspend fun getAllPolls(): Result<List<Poll>> {
        val pollList = mutableListOf<Poll>()
        val pollQuery = pollReference.get().await()
        for(poll in pollQuery){
            val id = poll.id
            val username = poll.toObject(Poll::class.java).userName
            val userPhoto = poll.toObject(Poll::class.java).userPhoto
            val question = poll.toObject(Poll::class.java).question
            val options = poll.toObject(Poll::class.java).options
            pollList.add(Poll(id,username,userPhoto,question,options))
        }
        return Result.Success(pollList.toList())
    }

    suspend fun createPoll(poll:Poll): Result<Boolean>{
        pollReference.add(poll).await()
        return Result.Success(true)
    }

    val dummyPollList = Result.Success(listOf(Poll(userName = "Lionel Messi",
            userPhoto = "https://www.mykhel.com/thumb/250x90x250/football/players/4/19054.jpg",
            question = "How many cups of coffee you drink each day ? ☕",options =  listOf(Option(3,"1 cups"),Option(6,"2 cups"),Option(1,"3 cups"))
    ),Poll(userName = "Gastón Saillén",
            userPhoto = "https://avatars2.githubusercontent.com/u/24615408?s=460&u=8a985792aa795ada276b4d567baba1c732ab02fb&v=4",
            question = "Do you like dogs ? ",options =  listOf(Option(3,"Yes :D"),Option(6,"Maybe :/"),Option(1,"No >:("))
    )))
}