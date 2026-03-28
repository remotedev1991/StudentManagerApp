# Room Database Migration Guide - Movie Database

## Overview
This document outlines the migration from version 1 to version 2 of the Movie Database schema.

## Migration History

### Version 1 → Version 2

#### Movie Entity Changes
**Before (v1):**
```kotlin
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val title: String,
)
```

**After (v2):**
```kotlin
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    val title: String,        // Movie title
    val genre: String,        // Movie genre (Action, Drama, etc)
    val releaseYear: Int,     // Release year (YYYY)
    val director: String,     // Director name
    val budget: Double,       // Budget in millions
    val description: String,  // Plot summary/description
)
```

**New Fields Added:**
- `genre`: Movie genre classification
- `releaseYear`: Year the movie was released
- `director`: Name of the movie director
- `budget`: Production budget in millions
- `description`: Plot summary or description

#### Actor Entity Changes
**Before (v1):**
```kotlin
@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val actorId: Int = 0,
    val name: String,
)
```

**After (v2):**
```kotlin
@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val actorId: Int = 0,
    val name: String,          // Actor's full name
    val birthYear: Int,        // Year of birth (YYYY)
    val nationality: String,   // Actor's nationality
    val profession: String,    // Role type (Actor, Actress, etc)
    val bio: String,           // Biography/details
)
```

**New Fields Added:**
- `birthYear`: Year the actor was born
- `nationality`: Actor's country of origin
- `profession`: Type of professional role
- `bio`: Actor biography or professional details

## Migration Strategy

### For Development Environment
The database is configured to use **destructive migration** with `.fallbackToDestructiveMigration()`:

```kotlin
val instance = androidx.room.Room.databaseBuilder(
    context,
    MovieDatabase::class.java,
    "movie_database"
)
.fallbackToDestructiveMigration() // Recreates database on version change
.build()
```

**Consequence:** All existing data in the tables will be deleted when upgrading to version 2.

### For Production Environment
For production, you should create explicit migrations instead:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Alter movies table
        database.execSQL("""
            ALTER TABLE movies ADD COLUMN genre TEXT NOT NULL DEFAULT 'Unknown'
        """)
        database.execSQL("""
            ALTER TABLE movies ADD COLUMN releaseYear INTEGER NOT NULL DEFAULT 2024
        """)
        database.execSQL("""
            ALTER TABLE movies ADD COLUMN director TEXT NOT NULL DEFAULT 'Unknown'
        """)
        database.execSQL("""
            ALTER TABLE movies ADD COLUMN budget REAL NOT NULL DEFAULT 0.0
        """)
        database.execSQL("""
            ALTER TABLE movies ADD COLUMN description TEXT NOT NULL DEFAULT ''
        """)
        
        // Alter actors table
        database.execSQL("""
            ALTER TABLE actors ADD COLUMN birthYear INTEGER NOT NULL DEFAULT 1990
        """)
        database.execSQL("""
            ALTER TABLE actors ADD COLUMN nationality TEXT NOT NULL DEFAULT 'Unknown'
        """)
        database.execSQL("""
            ALTER TABLE actors ADD COLUMN profession TEXT NOT NULL DEFAULT 'Actor'
        """)
        database.execSQL("""
            ALTER TABLE actors ADD COLUMN bio TEXT NOT NULL DEFAULT ''
        """)
    }
}
```

Then add to database configuration:
```kotlin
.addMigrations(MIGRATION_1_2)
```

## New DAO Methods Added

```kotlin
@Transaction
@Query("SELECT * FROM movies")
fun getAllMoviesWithActors(): List<MovieWithActors>

@Query("SELECT * FROM movies")
fun getAllMovies(): List<Movie>

@Query("SELECT * FROM actors")
fun getAllActors(): List<Actor>

@Delete
fun deleteMovie(movie: Movie)

@Delete
fun deleteActor(actor: Actor)
```

## Database Schema v2

### Movies Table
```sql
CREATE TABLE movies (
    movieId INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    genre TEXT NOT NULL,
    releaseYear INTEGER NOT NULL,
    director TEXT NOT NULL,
    budget REAL NOT NULL,
    description TEXT NOT NULL
)
```

### Actors Table
```sql
CREATE TABLE actors (
    actorId INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    birthYear INTEGER NOT NULL,
    nationality TEXT NOT NULL,
    profession TEXT NOT NULL,
    bio TEXT NOT NULL
)
```

### MovieActorCrossRef Table (Unchanged)
```sql
CREATE TABLE movieActorCrossRef (
    movieId INTEGER NOT NULL,
    actorId INTEGER NOT NULL,
    PRIMARY KEY (movieId, actorId),
    FOREIGN KEY (movieId) REFERENCES movies(movieId),
    FOREIGN KEY (actorId) REFERENCES actors(actorId)
)
```

## Testing the Migration

1. **Before Upgrading:**
   - Note any data in the database (it will be deleted with destructive migration)

2. **After Upgrading:**
   - Rebuild the app
   - Clear app data (Settings → Apps → StudentManager → Storage → Clear Data)
   - Relaunch the app
   - Create new movies and actors with the new fields

3. **Expected Behavior:**
   - All new field inputs should be saved correctly
   - Movies list should display with all new information
   - Actor cards should show all new details

## Rollback

If you need to rollback to version 1, simply revert the Movie and Actor entity files and set the database version back to 1 in MovieDatabase.kt.

## Best Practices for Future Migrations

1. **Development:** Use `fallbackToDestructiveMigration()` for quick iteration
2. **Production:** Always create explicit migrations to preserve user data
3. **Testing:** Test migrations on both upgrade and downgrade scenarios
4. **Documentation:** Keep migration history documented
5. **Version Control:** Commit all migration code and entity changes together

## Related Files Modified

- `MovieDatabase.kt` - Version bumped to 2, migration strategy added
- `Movie.kt` - 6 new fields added
- `Actor.kt` - 5 new fields added
- `MovieDao.kt` - New query methods added
- `MovieAdapter.kt` - New adapter created to display enhanced data
- `activity_movie.xml` - Redesigned UI layout
- `dialog_add_movie.xml` - Dialog for adding movies with all 6 fields
- `dialog_add_actor.xml` - Dialog for adding actors with all 5 fields
- `movie_item.xml` - Enhanced card layout to display all information
- `actor_item.xml` - Actor card layout for displaying in cast list

