package com.example.jetpoll.domain

import com.example.jetpoll.data.DataSource
import com.example.jetpoll.data.model.Poll
import com.example.jetpoll.vo.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class RepoImpl(private val dataSource: DataSource):Repo{
    @ExperimentalCoroutinesApi
    override fun getPolls(): Flow<List<Poll>> = dataSource.getpolls()
    override suspend fun getAllPolls(): Result<List<Poll>> = dataSource.getAllPolls()
    override suspend fun createPoll(poll: Poll): Result<Boolean> = dataSource.createPoll(poll)
}