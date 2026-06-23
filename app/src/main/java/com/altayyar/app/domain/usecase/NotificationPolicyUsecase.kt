package com.altayyar.app.domain.usecase

import at.connyduck.calladapter.networkresult.NetworkResult
import at.connyduck.calladapter.networkresult.fold
import at.connyduck.calladapter.networkresult.onSuccess
import com.altayyar.app.data.local.AccountManager
import com.altayyar.app.data.local.AppDatabase
import com.altayyar.app.data.local.entity.NotificationPolicyEntity
import com.altayyar.app.entity.NotificationPolicy
import com.altayyar.app.data.remote.MastodonApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException

class NotificationPolicyUsecase @Inject constructor(
    private val api: MastodonApi,
    private val db: AppDatabase,
    accountManager: AccountManager
) {

    private val accountId = accountManager.activeAccount!!.id

    private val _state: MutableStateFlow<NotificationPolicyState> = MutableStateFlow(NotificationPolicyState.Loading)
    val state: StateFlow<NotificationPolicyState> = _state.asStateFlow()

    val info: Flow<NotificationPolicyEntity?> = db.notificationPolicyDao().notificationPolicyForAccount(accountId)

    suspend fun getNotificationPolicy() {
        _state.value.let { state ->
            if (state is NotificationPolicyState.Loaded) {
                _state.value = state.copy(refreshing = true)
            } else {
                _state.value = NotificationPolicyState.Loading
            }
        }

        api.notificationPolicy().fold(
            { policy ->
                db.notificationPolicyDao().update(
                    NotificationPolicyEntity(
                        tayyarAccountId = accountId,
                        pendingRequestsCount = policy.summary.pendingRequestsCount,
                        pendingNotificationsCount = policy.summary.pendingNotificationsCount,
                    )
                )
                _state.value = NotificationPolicyState.Loaded(refreshing = false, policy = policy)
            },
            { t ->
                if (t is HttpException && t.code() == 404) {
                    _state.value = NotificationPolicyState.Unsupported
                } else {
                    _state.value = NotificationPolicyState.Error(t)
                }
            }
        )
    }

    suspend fun updatePolicy(
        forNotFollowing: String? = null,
        forNotFollowers: String? = null,
        forNewAccounts: String? = null,
        forPrivateMentions: String? = null,
        forLimitedAccounts: String? = null
    ): NetworkResult<NotificationPolicy> {
        return api.updateNotificationPolicy(
            forNotFollowing = forNotFollowing,
            forNotFollowers = forNotFollowers,
            forNewAccounts = forNewAccounts,
            forPrivateMentions = forPrivateMentions,
            forLimitedAccounts = forLimitedAccounts
        ).onSuccess { notificationPolicy ->
            _state.value = NotificationPolicyState.Loaded(false, notificationPolicy)
        }
    }

    suspend fun updateCounts(notificationCount: Int) =
        db.notificationPolicyDao().updateCounts(accountId, notificationCount)
}

sealed interface NotificationPolicyState {

    data object Loading : NotificationPolicyState
    data object Unsupported : NotificationPolicyState
    data class Error(
        val throwable: Throwable
    ) : NotificationPolicyState
    data class Loaded(
        val refreshing: Boolean,
        val policy: NotificationPolicy
    ) : NotificationPolicyState
}
