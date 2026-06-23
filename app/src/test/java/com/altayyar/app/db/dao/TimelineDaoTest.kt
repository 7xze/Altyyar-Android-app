package com.altayyar.app.db.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.altayyar.app.components.timeline.fakeHomeTimelineData
import com.altayyar.app.components.timeline.fakePlaceholderHomeTimelineData
import com.altayyar.app.components.timeline.insert
import com.altayyar.app.db.AppDatabase
import com.altayyar.app.db.Converters
import com.altayyar.app.di.NetworkModule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(AndroidJUnit4::class)
class TimelineDaoTest {
    private lateinit var timelineDao: TimelineDao
    private lateinit var db: AppDatabase

    private val moshi = NetworkModule.providesMoshi()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addTypeConverter(Converters(moshi))
            .allowMainThreadQueries()
            .build()
        timelineDao = db.timelineDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertGetStatus() = runTest {
        val setOne = fakeHomeTimelineData(id = "3")
        val setTwo = fakeHomeTimelineData(id = "20", reblogAuthorServerId = "R1")
        val ignoredOne = fakeHomeTimelineData(id = "1")
        val ignoredTwo = fakeHomeTimelineData(id = "2", tayyarAccountId = 2)

        db.insert(
            listOf(setOne, setTwo, ignoredOne),
            tayyarAccountId = 1
        )
        db.insert(
            listOf(ignoredTwo),
            tayyarAccountId = 2
        )

        val pagingSource = timelineDao.getHomeTimeline(1)

        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, false))

        val loadedStatuses = (loadResult as PagingSource.LoadResult.Page).data

        assertEquals(2, loadedStatuses.size)
        assertEquals(listOf(setTwo, setOne), loadedStatuses)
    }

    @Test
    fun overwriteDeletedStatus() = runTest {
        val oldStatuses = listOf(
            fakeHomeTimelineData(id = "3"),
            fakeHomeTimelineData(id = "2"),
            fakeHomeTimelineData(id = "1")
        )

        db.insert(oldStatuses, 1)

        // status 2 gets deleted, newly loaded status contain only 1 + 3
        val newStatuses = listOf(
            fakeHomeTimelineData(id = "3"),
            fakeHomeTimelineData(id = "1")
        )

        val deletedCount = timelineDao.deleteRange(1, newStatuses.last().id, newStatuses.first().id)
        assertEquals(3, deletedCount)

        db.insert(newStatuses, 1)

        // make sure status 2 is no longer in db
        val pagingSource = timelineDao.getHomeTimeline(1)

        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 100, false))

        val loadedStatuses = (loadResult as PagingSource.LoadResult.Page).data

        assertEquals(newStatuses, loadedStatuses)
    }

    @Test
    fun deleteRange() = runTest {
        val statuses = listOf(
            fakeHomeTimelineData(id = "100"),
            fakeHomeTimelineData(id = "50"),
            fakeHomeTimelineData(id = "15"),
            fakeHomeTimelineData(id = "14"),
            fakeHomeTimelineData(id = "13"),
            fakeHomeTimelineData(id = "13", tayyarAccountId = 2),
            fakeHomeTimelineData(id = "12"),
            fakeHomeTimelineData(id = "11"),
            fakeHomeTimelineData(id = "9")
        )

        db.insert(statuses - statuses[5], 1)
        db.insert(listOf(statuses[5]), 2)

        assertEquals(3, timelineDao.deleteRange(1, "12", "14"))
        assertEquals(0, timelineDao.deleteRange(1, "80", "80"))
        assertEquals(0, timelineDao.deleteRange(1, "60", "80"))
        assertEquals(0, timelineDao.deleteRange(1, "5", "8"))
        assertEquals(0, timelineDao.deleteRange(1, "101", "1000"))
        assertEquals(1, timelineDao.deleteRange(1, "50", "50"))

        val loadParams: PagingSource.LoadParams<Int> = PagingSource.LoadParams.Refresh(null, 100, false)

        val statusesAccount1 = (timelineDao.getHomeTimeline(1).load(loadParams) as PagingSource.LoadResult.Page).data
        val statusesAccount2 = (timelineDao.getHomeTimeline(2).load(loadParams) as PagingSource.LoadResult.Page).data

        val remainingStatusesAccount1 = listOf(
            fakeHomeTimelineData(id = "100"),
            fakeHomeTimelineData(id = "15"),
            fakeHomeTimelineData(id = "11"),
            fakeHomeTimelineData(id = "9")
        )

        val remainingStatusesAccount2 = listOf(
            fakeHomeTimelineData(id = "13", tayyarAccountId = 2)
        )

        assertEquals(remainingStatusesAccount1, statusesAccount1)
        assertEquals(remainingStatusesAccount2, statusesAccount2)
    }

    @Test
    fun deleteAllForInstance() = runTest {
        val statusWithRedDomain1 = fakeHomeTimelineData(
            id = "15",
            tayyarAccountId = 1,
            domain = "mastodon.red",
            authorServerId = "1"
        )
        val statusWithRedDomain2 = fakeHomeTimelineData(
            id = "14",
            tayyarAccountId = 1,
            domain = "mastodon.red",
            authorServerId = "2"
        )
        val statusWithRedDomainOtherAccount = fakeHomeTimelineData(
            id = "12",
            tayyarAccountId = 2,
            domain = "mastodon.red",
            authorServerId = "2"
        )
        val statusWithBlueDomain = fakeHomeTimelineData(
            id = "10",
            tayyarAccountId = 1,
            domain = "mastodon.blue",
            authorServerId = "4"
        )
        val statusWithBlueDomainOtherAccount = fakeHomeTimelineData(
            id = "10",
            tayyarAccountId = 2,
            domain = "mastodon.blue",
            authorServerId = "5"
        )
        val statusWithGreenDomain = fakeHomeTimelineData(
            id = "8",
            tayyarAccountId = 1,
            domain = "mastodon.green",
            authorServerId = "6"
        )

        db.insert(listOf(statusWithRedDomain1, statusWithRedDomain2, statusWithBlueDomain, statusWithGreenDomain), 1)
        db.insert(listOf(statusWithRedDomainOtherAccount, statusWithBlueDomainOtherAccount), 2)

        timelineDao.deleteAllFromInstance(1, "mastodon.red")
        timelineDao.deleteAllFromInstance(1, "mastodon.blu") // shouldn't delete anything
        timelineDao.deleteAllFromInstance(1, "greenmastodon.green") // shouldn't delete anything

        val loadParams: PagingSource.LoadParams<Int> = PagingSource.LoadParams.Refresh(null, 100, false)

        val statusesAccount1 = (timelineDao.getHomeTimeline(1).load(loadParams) as PagingSource.LoadResult.Page).data
        val statusesAccount2 = (timelineDao.getHomeTimeline(2).load(loadParams) as PagingSource.LoadResult.Page).data

        assertEquals(listOf(statusWithBlueDomain, statusWithGreenDomain), statusesAccount1)
        assertEquals(listOf(statusWithRedDomainOtherAccount, statusWithBlueDomainOtherAccount), statusesAccount2)
    }

    @Test
    fun `should return null as topId when db is empty`() = runTest {
        assertNull(timelineDao.getTopId(1))
    }

    @Test
    fun `should return correct topId`() = runTest {
        val statusData = listOf(
            fakeHomeTimelineData(
                id = "4",
                tayyarAccountId = 1,
                domain = "mastodon.test",
                authorServerId = "1"
            ),
            fakeHomeTimelineData(
                id = "33",
                tayyarAccountId = 1,
                domain = "mastodon.test",
                authorServerId = "2"
            ),
            fakeHomeTimelineData(
                id = "22",
                tayyarAccountId = 1,
                domain = "mastodon.test",
                authorServerId = "2"
            )
        )

        db.insert(statusData, 1)

        assertEquals("33", timelineDao.getTopId(1))
    }

    @Test
    fun `should return correct top placeholderId`() = runTest {
        val statusData = listOf(
            fakeHomeTimelineData(id = "1000"),
            fakePlaceholderHomeTimelineData(id = "99"),
            fakeHomeTimelineData(id = "97"),
            fakePlaceholderHomeTimelineData(id = "96"),
            fakeHomeTimelineData(id = "90"),
            fakePlaceholderHomeTimelineData(id = "80"),
            fakeHomeTimelineData(id = "77")
        )

        db.insert(statusData)

        assertEquals("99", timelineDao.getTopPlaceholderId(1))
    }

    @Test
    fun `should correctly delete all by user`() = runTest {
        val statusData = listOf(
            // will be deleted because it is a direct post
            fakeHomeTimelineData(id = "0", tayyarAccountId = 1, authorServerId = "1"),
            // different تيار Account
            fakeHomeTimelineData(id = "1", tayyarAccountId = 2, authorServerId = "1"),
            // different author
            fakeHomeTimelineData(id = "2", tayyarAccountId = 1, authorServerId = "2"),
            // different author and reblogger
            fakeHomeTimelineData(id = "3", tayyarAccountId = 1, authorServerId = "2", statusId = "100", reblogAuthorServerId = "3"),
            // will be deleted because it is a reblog
            fakeHomeTimelineData(id = "4", tayyarAccountId = 1, authorServerId = "2", statusId = "101", reblogAuthorServerId = "1"),
            // not a status
            fakePlaceholderHomeTimelineData(id = "5"),
            // will be deleted because it is a self reblog
            fakeHomeTimelineData(id = "6", tayyarAccountId = 1, authorServerId = "1", statusId = "102", reblogAuthorServerId = "1"),
            // will be deleted because it direct post reblogged by another user
            fakeHomeTimelineData(id = "7", tayyarAccountId = 1, authorServerId = "1", statusId = "103", reblogAuthorServerId = "3"),
            // different تيار Account
            fakeHomeTimelineData(id = "8", tayyarAccountId = 2, authorServerId = "3", statusId = "104", reblogAuthorServerId = "2"),
            // different تيار Account
            fakeHomeTimelineData(id = "9", tayyarAccountId = 2, authorServerId = "3", statusId = "105", reblogAuthorServerId = "1"),
        )

        db.insert(statusData - statusData[1] - statusData[8] - statusData [9], tayyarAccountId = 1)
        db.insert(listOf(statusData[1], statusData[8], statusData [9]), tayyarAccountId = 2)

        timelineDao.removeAllByUser(1, "1")

        val loadedHomeTimelineItems: MutableList<String> = mutableListOf()
        val accountCursor = db.query("SELECT id FROM HomeTimelineEntity ORDER BY id ASC", null)
        accountCursor.moveToFirst()
        while (!accountCursor.isAfterLast) {
            val id: String = accountCursor.getString(accountCursor.getColumnIndex("id"))
            loadedHomeTimelineItems.add(id)
            accountCursor.moveToNext()
        }
        accountCursor.close()

        val expectedHomeTimelineItems = listOf("1", "2", "3", "5", "8", "9")

        assertEquals(expectedHomeTimelineItems, loadedHomeTimelineItems)
    }

    @Test
    fun `should correctly delete statuses and reblogs by user`() = runTest {
        val statusData = listOf(
            // will be deleted because it is a direct post
            fakeHomeTimelineData(id = "0", tayyarAccountId = 1, authorServerId = "1"),
            // different تيار Account
            fakeHomeTimelineData(id = "1", tayyarAccountId = 2, authorServerId = "1"),
            // different author
            fakeHomeTimelineData(id = "2", tayyarAccountId = 1, authorServerId = "2"),
            // different author and reblogger
            fakeHomeTimelineData(id = "3", tayyarAccountId = 1, authorServerId = "2", statusId = "100", reblogAuthorServerId = "3"),
            // will be deleted because it is a reblog
            fakeHomeTimelineData(id = "4", tayyarAccountId = 1, authorServerId = "2", statusId = "101", reblogAuthorServerId = "1"),
            // not a status
            fakePlaceholderHomeTimelineData(id = "5"),
            // will be deleted because it is a self reblog
            fakeHomeTimelineData(id = "6", tayyarAccountId = 1, authorServerId = "1", statusId = "102", reblogAuthorServerId = "1"),
            // will NOT be deleted because it direct post reblogged by another user
            fakeHomeTimelineData(id = "7", tayyarAccountId = 1, authorServerId = "1", statusId = "103", reblogAuthorServerId = "3"),
            // different تيار Account
            fakeHomeTimelineData(id = "8", tayyarAccountId = 2, authorServerId = "3", statusId = "104", reblogAuthorServerId = "2"),
            // different تيار Account
            fakeHomeTimelineData(id = "9", tayyarAccountId = 2, authorServerId = "3", statusId = "105", reblogAuthorServerId = "1"),
        )

        db.insert(statusData - statusData[1] - statusData[8] - statusData [9], tayyarAccountId = 1)
        db.insert(listOf(statusData[1], statusData[8], statusData [9]), tayyarAccountId = 2)

        timelineDao.removeStatusesAndReblogsByUser(1, "1")

        val loadedHomeTimelineItems: MutableList<String> = mutableListOf()
        val accountCursor = db.query("SELECT id FROM HomeTimelineEntity ORDER BY id ASC", null)
        accountCursor.moveToFirst()
        while (!accountCursor.isAfterLast) {
            val id: String = accountCursor.getString(accountCursor.getColumnIndex("id"))
            loadedHomeTimelineItems.add(id)
            accountCursor.moveToNext()
        }
        accountCursor.close()

        val expectedHomeTimelineItems = listOf("1", "2", "3", "5", "7", "8", "9")

        assertEquals(expectedHomeTimelineItems, loadedHomeTimelineItems)
    }
}
