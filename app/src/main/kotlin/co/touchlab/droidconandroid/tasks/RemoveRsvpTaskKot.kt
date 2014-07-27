package co.touchlab.droidconandroid.tasks

import android.content.Context
import co.touchlab.droidconandroid.data.DatabaseHelper
import co.touchlab.droidconandroid.data.Event
import com.j256.ormlite.dao.Dao
import java.util.UUID
import co.touchlab.android.superbus.appsupport.CommandBusHelper
import java.util.concurrent.Callable
import co.touchlab.droidconandroid.superbus.AddRsvpCommandKot
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.superbus.RemoveRsvpCommandKot

/**
 * Created by kgalligan on 7/20/14.
 */
class RemoveRsvpTaskKot(c : Context, val eventId : Long) : DatabaseTaskKot(c)
{
    class object
    {
        public fun createTask(c : Context, event: Event)
        {
            TaskQueue.execute(c, AddRsvpTaskKot(c, event.id))
        }
    }
    override fun run(context: Context?)
    {
        databaseHelper.performTransactionOrThrowRuntime(object : Callable<Void>
        {
            throws(javaClass<Exception>())
            override fun call(): Void?
            {
                val dao = databaseHelper.getEventDao()
                val event = dao.queryForId(eventId)
                if (event != null)
                {
                    event.rsvpUuid = null
                    dao.update(event)
                    CommandBusHelper.submitCommandSync(context, RemoveRsvpCommandKot(eventId))
                }

                return null
            }
        })
    }
    override fun handleError(e: Exception?): Boolean
    {
        throw UnsupportedOperationException()
    }
}