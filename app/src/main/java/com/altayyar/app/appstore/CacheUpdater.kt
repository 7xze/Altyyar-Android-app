package com.altayyar.app.appstore

import com.altayyar.app.db.AccountManager
import com.altayyar.app.db.AppDatabase
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Updates the database cache in response to events.
 * This is important for the home timeline and notifications to be up to date.
 */
class CacheUpdater @Inject constructor(
    eventHub: EventHub,
    accountManager: AccountManager,
    appDatabase: AppDatabase,
    moshi: Moshi
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val timelineDao = appDatabase.timelineDao()
    private val statusDao = appDatabase.timelineStatusDao()
    private val notificationsDao = appDatabase.notificationsDao()

    init {
        scope.launch {
            eventHub.events.collect { event ->
                val tayyarAccountId = accountManager.activeAccount?.id ?: return@collect
                when (event) {
                    is StatusChangedEvent -> statusDao.update(tayyarAccountId = tayyarAccountId, status = event.status)
                    is UnfollowEvent -> timelineDao.removeStatusesAndReblogsByUser(tayyarAccountId, event.accountId)
                    is BlockEvent -> removeAllByUser(tayyarAccountId, event.accountId)
                    is MuteEvent -> removeAllByUser(tayyarAccountId, event.accountId)

                    is DomainMuteEvent -> {
                        timelineDao.deleteAllFromInstance(tayyarAccountId, event.instance)
                        notificationsDao.deleteAllFromInstance(tayyarAccountId, event.instance)
                    }

                    is StatusDeletedEvent -> {
                        timelineDao.deleteAllWithStatus(tayyarAccountId, event.statusId)
                        notificationsDao.deleteAllWithStatus(tayyarAccountId, event.statusId)
                    }

                    is PollVoteEvent -> statusDao.setVoted(tayyarAccountId, event.statusId, event.poll)
                    is PollShowResultsEvent -> statusDao.setShowResults(tayyarAccountId, event.statusId)
                }
            }
        }
    }

    private suspend fun removeAllByUser(tayyarAccountId: Long, accountId: String) {
        timelineDao.removeAllByUser(tayyarAccountId, accountId)
        notificationsDao.removeAllByUser(tayyarAccountId, accountId)
    }

    fun stop() {
        this.scope.cancel()
    }
}
