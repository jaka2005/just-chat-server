package funn.j2k.justchat.data.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(db: Database? = null, block: Transaction.() -> T): T =
    newSuspendedTransaction(
        context = Dispatchers.IO,
        db = db,
        statement = block
    )
