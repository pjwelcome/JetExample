package com.example.jetpoll.domain

import com.example.jetpoll.data.model.Poll
import com.example.jetpoll.vo.Result
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun getPolls() : Flow<List<Poll>>
    suspend fun getAllPolls(): Result<List<Poll>>
    suspend fun createPoll(poll:Poll): Result<Boolean>
}