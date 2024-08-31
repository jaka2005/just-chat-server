package funn.j2k.justchat.di

import funn.j2k.justchat.data.repository.MessageRepositoryImpl
import funn.j2k.justchat.data.repository.UserRepositoryImpl
import funn.j2k.justchat.domain.repository.MessageRepository
import funn.j2k.justchat.domain.repository.UserRepository
import org.jetbrains.exposed.sql.Database
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    }
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::MessageRepositoryImpl) bind MessageRepository::class
}
