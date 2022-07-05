package com.example.noteappwithkmm.di


import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.common.utils.GenericMessageInfoQueueUtil
import com.example.noteappwithkmm.dataSource.cache.DriverFactory
import com.example.noteappwithkmm.dataSource.cache.NoteDataBaseFactory
import com.example.noteappwithkmm.dataSource.cache.NoteDatabase
import com.example.noteappwithkmm.dataSource.cache.repositories.NoteCacheService
import com.example.noteappwithkmm.domain.interactors.delte_note.DeleteNoteUseCase
import com.example.noteappwithkmm.domain.interactors.get_note_details.GetNoteByIdUseCase
import com.example.noteappwithkmm.domain.interactors.get_notes.SearchNoteOrAllNoteUseCase
import com.example.noteappwithkmm.domain.interactors.insert_update_note.InsertNoteUseCase
import com.example.noteappwithkmm.domain.repositories.NoteCacheServiceImpl
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonAppModule = module {

    single<DataTimeUtil> {  DataTimeUtil() }
    single<GenericMessageInfoQueueUtil> {  GenericMessageInfoQueueUtil() }

    single<NoteDatabase> { NoteDataBaseFactory(get()).createDataBase() }
    single<NoteCacheService> {  NoteCacheServiceImpl(get(),get()) }
    single<InsertNoteUseCase> {  InsertNoteUseCase(get()) }
    single<SearchNoteOrAllNoteUseCase> {  SearchNoteOrAllNoteUseCase(get()) }
    single<GetNoteByIdUseCase> {  GetNoteByIdUseCase(get()) }
    single<DeleteNoteUseCase> {  DeleteNoteUseCase(get()) }


}



fun initKoin(
    vararg othersModule: Module,
): KoinApplication = startKoin {
    this.printLogger()
    val list = othersModule.toMutableList()
    list.add(commonAppModule)

    modules(list.toList())

}