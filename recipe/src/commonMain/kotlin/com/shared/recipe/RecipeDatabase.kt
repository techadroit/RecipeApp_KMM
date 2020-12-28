package com.shared.recipe

import kotlinx.serialization.Serializable
import org.kodein.db.DB
import org.kodein.db.TypeTable
import org.kodein.db.impl.factory
import org.kodein.db.inDir
import org.kodein.db.model.orm.Metadata
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import org.kodein.log.LoggerFactory
import org.kodein.log.newLogger

object RecipeDatabase {

    val db: DB = DB.factory
        .inDir(getApplicationFilesDirectoryPath())
        .open("recipe_database", TypeTable {
            root<RecipeEntity>()
        }, KotlinxSerializer())

    init {
        db.newLogger(LoggerFactory.default)
    }

    fun getDatabase() = db
}

@Serializable
data class RecipeEntity(
    override val id: Int,
    val imageUrl: String,
    val cookingTime: Int,
    val servings: Int,
    val title: String
) : Metadata
