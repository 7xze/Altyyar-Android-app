package com.altayyar.app.components.viewthread

import android.os.Looper.getMainLooper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import at.connyduck.calladapter.networkresult.NetworkResult
import com.altayyar.app.appstore.EventHub
import com.altayyar.app.appstore.StatusChangedEvent
import com.altayyar.app.components.instanceinfo.InstanceInfoRepository
import com.altayyar.app.components.timeline.fakeStatus
import com.altayyar.app.components.timeline.fakeStatusViewData
import com.altayyar.app.db.AccountManager
import com.altayyar.app.db.AppDatabase
import com.altayyar.app.db.Converters
import com.altayyar.app.db.entity.AccountEntity
import com.altayyar.app.di.NetworkModule
import com.altayyar.app.entity.StatusContext
import com.altayyar.app.network.FilterModel
import com.altayyar.app.network.MastodonApi
import com.altayyar.app.usecase.TimelineCases
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(AndroidJUnit4::class)
class ViewThreadViewModelTest {

    private lateinit var api: MastodonApi
    private lateinit var eventHub: EventHub
    private lateinit var viewModel: ViewThreadViewModel
    private lateinit var db: AppDatabase

    private val threadId = "1234"
    private val moshi = NetworkModule.providesMoshi()

    /**
     * Execute each task synchronously.
     *
     * If you do not do this, and you have code like this under test:
     *
     * ```
     * fun someFunc() = viewModelScope.launch {
     *     _uiState.value = "initial value"
     *     // ...
     *     call_a_suspend_fun()
     *     // ...
     *     _uiState.value = "new value"
     * }
     * ```
     *
     * and a test like:
     *
     * ```
     * someFunc()
     * assertEquals("new value", viewModel.uiState.value)
     * ```
     *
     * The test will fail, because someFunc() yields at the `call_a_suspend_func()` point,
     * and control returns to the test before `_uiState.value` has been changed.
     */
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        shadowOf(getMainLooper()).idle()

        api = mock {
            onBlocking { getFilters() } doReturn NetworkResult.success(emptyList())
        }
        val instanceInfoRepo: InstanceInfoRepository = mock {
            onBlocking { isFilterV2Supported() } doReturn false
        }
        eventHub = EventHub()
        val filterModel = FilterModel(instanceInfoRepo, api)
        val timelineCases = TimelineCases(api, eventHub)
        val accountManager: AccountManager = mock {
            on { activeAccount } doReturn AccountEntity(
                id = 1,
                domain = "mastodon.test",
                accessToken = "fakeToken",
                clientId = "fakeId",
                clientSecret = "fakeSecret",
                isActive = true
            )
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addTypeConverter(Converters(moshi))
            .allowMainThreadQueries()
            .build()

        viewModel = ViewThreadViewModel(api, filterModel, timelineCases, db, eventHub, accountManager)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `should emit status and context when both load`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test"),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test"),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should emit status even if context fails to load`() = runTest {
        api.stub {
            onBlocking { status(threadId) } doReturn NetworkResult.success(fakeStatus(id = "2", inReplyToId = "1", inReplyToAccountId = "1"))
            onBlocking { statusContext(threadId) } doReturn NetworkResult.failure(IOException())
        }

        viewModel.loadThread(threadId)

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true)
                ),
                detailedStatusPosition = 0,
                revealButton = RevealButtonState.NO_BUTTON
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should emit error when status and context fail to load`() = runTest {
        api.stub {
            onBlocking { status(threadId) } doReturn NetworkResult.failure(IOException())
            onBlocking { statusContext(threadId) } doReturn NetworkResult.failure(IOException())
        }

        viewModel.loadThread(threadId)

        assertEquals(
            ThreadUiState.Error::class.java,
            viewModel.uiState.first().javaClass
        )
    }

    @Test
    fun `should emit error when status fails to load`() = runTest {
        api.stub {
            onBlocking { status(threadId) } doReturn NetworkResult.failure(IOException())
            onBlocking { statusContext(threadId) } doReturn NetworkResult.success(
                StatusContext(
                    ancestors = listOf(fakeStatus(id = "1")),
                    descendants = listOf(fakeStatus(id = "3", inReplyToId = "2", inReplyToAccountId = "1"))
                )
            )
        }

        viewModel.loadThread(threadId)

        assertEquals(
            ThreadUiState.Error::class.java,
            viewModel.uiState.first().javaClass
        )
    }

    @Test
    fun `should update state when reveal button is toggled`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)
        viewModel.toggleRevealButton()

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test", isExpanded = true),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test", isExpanded = true),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test", isExpanded = true)
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.HIDE
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should handle status changed event`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        eventHub.dispatch(StatusChangedEvent(fakeStatus(id = "1", spoilerText = "Test", favourited = false)))

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test", favourited = false),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test"),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should remove status`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        viewModel.removeStatus(fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test"))

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test"),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should change status expanded state`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        viewModel.changeExpanded(
            true,
            fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test")
        )

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test"),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test", isExpanded = true),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should change content collapsed state`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        viewModel.changeContentCollapsed(
            true,
            fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test")
        )

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test"),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test", isCollapsed = true),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    @Test
    fun `should change content showing state`() = runTest {
        mockSuccessResponses()

        viewModel.loadThread(threadId)

        viewModel.changeContentShowing(
            true,
            fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test")
        )

        assertEquals(
            ThreadUiState.Success(
                statusViewData = listOf(
                    fakeStatusViewData(id = "1", spoilerText = "Test"),
                    fakeStatusViewData(id = "2", inReplyToId = "1", inReplyToAccountId = "1", isDetailed = true, spoilerText = "Test", isShowingContent = true),
                    fakeStatusViewData(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test")
                ),
                detailedStatusPosition = 1,
                revealButton = RevealButtonState.REVEAL
            ),
            viewModel.uiState.first()
        )
    }

    private fun mockSuccessResponses() {
        api.stub {
            onBlocking { status(threadId) } doReturn NetworkResult.success(fakeStatus(id = "2", inReplyToId = "1", inReplyToAccountId = "1", spoilerText = "Test"))
            onBlocking { statusContext(threadId) } doReturn NetworkResult.success(
                StatusContext(
                    ancestors = listOf(fakeStatus(id = "1", spoilerText = "Test")),
                    descendants = listOf(fakeStatus(id = "3", inReplyToId = "2", inReplyToAccountId = "1", spoilerText = "Test"))
                )
            )
        }
    }
}
