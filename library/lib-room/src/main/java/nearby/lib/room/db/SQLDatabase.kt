package nearby.lib.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import nearby.lib.room.model.UserEntity

@Database(entities = [UserEntity::class], version = 12, exportSchema = false)
abstract class SQLDatabase : RoomDatabase(){
    abstract fun userFlow():UserFlowDao //flow操作类
}
