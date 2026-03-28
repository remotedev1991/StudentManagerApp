# Movie & Actor M:M Relationship - Complete Guide

## Overview
This is a complete implementation of a Many-to-Many (M:M) relationship using Android Room Database. The feature allows users to:
- ✅ Add movies with detailed information (6 fields)
- ✅ Add actors with complete details (5 fields)
- ✅ Link actors to movies through a junction table
- ✅ View all movies with their cast members
- ✅ Delete movies and their relationships
- ✅ Beautiful, intuitive UI with attractive dialogs and cards

## Key Features

### Movie Fields (6 Information Points)
1. **Title** - Movie name
2. **Genre** - Type of movie (Action, Drama, Comedy, etc.)
3. **Release Year** - Year the movie was released (YYYY)
4. **Director** - Name of the movie director
5. **Budget** - Production budget in millions (e.g., 150.5)
6. **Description** - Plot summary or detailed movie description

### Actor Fields (5 Information Points)
1. **Name** - Actor's full name
2. **Birth Year** - Year the actor was born (YYYY)
3. **Nationality** - Country of origin
4. **Profession** - Role type (Actor/Actress/etc)
5. **Bio** - Biography or professional background

## Database Schema

### M:M Relationship Structure

```
┌─────────────────┐         ┌─────────────────────────┐         ┌─────────────────┐
│    Movies       │         │ MovieActorCrossRef      │         │    Actors       │
├─────────────────┤         ├─────────────────────────┤         ├─────────────────┤
│ movieId (PK)    │────────▶│ movieId (FK)            │◀────────│ actorId (PK)    │
│ title           │         │ actorId (FK)            │         │ name            │
│ genre           │         │                         │         │ birthYear       │
│ releaseYear     │         │ (Composite PK)          │         │ nationality     │
│ director        │         │                         │         │ profession      │
│ budget          │         │                         │         │ bio             │
│ description     │         └─────────────────────────┘         └─────────────────┘
└─────────────────┘
```

### Entities Used
- **Movie.kt** - Movie entity with 6 fields
- **Actor.kt** - Actor entity with 5 fields
- **MovieActorCrossRef.kt** - Junction table for M:M relationship
- **MovieWithActors.kt** - Data class for querying movies with their actors

## UI Components

### Layouts Created

| Layout | Purpose |
|--------|---------|
| `activity_movie.xml` | Main activity with list view and action buttons |
| `dialog_add_movie.xml` | Dialog for adding new movies (6 input fields) |
| `dialog_add_actor.xml` | Dialog for adding new actors (5 input fields) |
| `dialog_assign_actors.xml` | Dialog for assigning actors to movies |
| `movie_item.xml` | Card layout for displaying individual movies with cast |
| `actor_item.xml` | Layout for displaying actor items within movie cards |

### Features of UI

1. **Beautiful Header** - Gradient background with movie hub title
2. **Movie Cards** - Attractive cards showing all movie information
3. **Cast Display** - Nested actor items showing cast members
4. **Action Buttons** - Smooth buttons with gradients for adding content
5. **Dialogs** - Professional dialogs with themed colors and inputs
6. **Empty State** - User-friendly message when no movies exist

## How to Use

### Adding a Movie
1. Tap **"+ Add Movie"** button at the bottom
2. Fill in all 6 fields in the dialog:
   - Title (e.g., "Inception")
   - Genre (e.g., "Science Fiction")
   - Release Year (e.g., "2010")
   - Director (e.g., "Christopher Nolan")
   - Budget (e.g., "160")
   - Description (e.g., "A mind-bending thriller...")
3. Tap **"Save"** to add the movie

### Adding an Actor
1. Tap **"+ Add Actor"** button at the bottom
2. Fill in all 5 fields:
   - Name (e.g., "Leonardo DiCaprio")
   - Birth Year (e.g., "1974")
   - Nationality (e.g., "USA")
   - Profession (e.g., "Actor")
   - Bio (e.g., "Renowned Hollywood actor...")
3. Tap **"Save"**

### Assigning Actors to a Movie
1. In any movie card, tap the **"+ Actor"** button
2. In the assignment dialog, check the actors you want to assign
3. Tap **"Assign"** to save the relationships
4. The movie card will update to show all assigned actors

### Deleting a Movie
1. In any movie card, tap the **Delete** button (trash icon)
2. Confirm the deletion
3. Movie and all its actor relationships will be deleted

## Files Modified/Created

### Database Files
- ✅ `MovieDatabase.kt` - Version 2 with destructive migration
- ✅ `Movie.kt` - Enhanced with 6 fields
- ✅ `Actor.kt` - Enhanced with 5 fields
- ✅ `MovieWithActors.kt` - Already present for queries
- ✅ `MovieActorCrossRef.kt` - Junction table for M:M
- ✅ `MovieDao.kt` - Updated with new query methods

### Layout Files (All in `/res/layout/`)
- ✅ `activity_movie.xml` - Main activity layout
- ✅ `dialog_add_movie.xml` - Movie creation dialog
- ✅ `dialog_add_actor.xml` - Actor creation dialog
- ✅ `dialog_assign_actors.xml` - Actor assignment dialog
- ✅ `movie_item.xml` - Movie card layout
- ✅ `actor_item.xml` - Actor display in movie card

### Drawable Resources (All in `/res/drawable/`)
- ✅ `dialog_background.xml` - Dialog backgrounds
- ✅ `edittext_bg.xml` - EditText styling
- ✅ `button_save_background.xml` - Save button
- ✅ `button_cancel_background.xml` - Cancel button
- ✅ `button_add_background.xml` - Add button
- ✅ `button_fab_movie_background.xml` - Movie FAB
- ✅ `button_fab_actor_background.xml` - Actor FAB
- ✅ `movie_card_background.xml` - Movie card background
- ✅ `header_gradient.xml` - Header gradient
- ✅ `main_background.xml` - Main activity background
- ✅ `genre_badge_background.xml` - Genre tag styling
- ✅ `delete_button_background.xml` - Delete button
- ✅ `actor_item_background.xml` - Actor item styling
- ✅ `actor_avatar_background.xml` - Avatar circle
- ✅ `actors_bg.xml` - Cast list background

### Code Files (All in `/java/com/laddu/studentmanagerapp/moviedemo/`)
- ✅ `MovieActivity.kt` - Complete implementation with dialogs
- ✅ `MovieAdapter.kt` - Custom adapter for movie list

### Documentation
- ✅ `MIGRATION.md` - Database migration details
- ✅ `DEPENDENCIES.md` - All required dependencies
- ✅ `MOVIE_GUIDE.md` - This file!

## Database Migration

### Current Version: 2

The database was upgraded from version 1 to version 2 to support the enhanced Movie and Actor entities.

**Migration Strategy:** Destructive Migration (development)
- Automatically deletes and recreates tables on app launch
- Perfect for development and testing
- NOT suitable for production with real user data

For production, see `MIGRATION.md` for explicit migration SQL.

## Color Scheme

The UI uses a modern dark theme with these colors:
- **Primary Dark**: #1a237e (Deep Blue)
- **Primary Light**: #283593 (Light Blue)
- **Accent**: #FFB74D (Warm Orange)
- **Delete**: #c62828 (Deep Red)
- **Text**: #FFFFFF (White)
- **Hint**: #B0BEC5 (Light Gray)

## Data Flow

```
User Input (Dialog)
        ↓
Validation in Activity
        ↓
Insert into Database (Room)
        ↓
Reload movies from database
        ↓
Update adapter
        ↓
Display in ListView
```

## Example Data

To test the feature, try adding:

**Movie 1:**
- Title: Inception
- Genre: Science Fiction
- Release Year: 2010
- Director: Christopher Nolan
- Budget: 160.0
- Description: A skilled thief who steals corporate secrets through the use of dream-sharing technology.

**Movie 2:**
- Title: The Dark Knight
- Genre: Action
- Release Year: 2008
- Director: Christopher Nolan
- Budget: 185.0
- Description: When the menace known as The Joker wreaks havoc on Gotham City.

**Actors:**
- Leonardo DiCaprio (Born: 1974, USA, Actor)
- Christian Bale (Born: 1974, UK, Actor)
- Heath Ledger (Born: 1979, Australia, Actor)

Then assign actors to movies to see the M:M relationship in action!

## Technical Stack

- **Language:** Kotlin
- **Database:** Room (SQLite abstraction)
- **UI:** Android XML Layouts
- **Architecture:** MVVM (ViewModel pattern could be added)
- **Threading:** Kotlin Coroutines (thread{})
- **UI Thread Management:** runOnUiThread

## Best Practices Implemented

✅ Database operations on background threads  
✅ Proper resource loading with views  
✅ Dialog dismissal handling  
✅ Empty state management  
✅ Confirmation dialogs for destructive actions  
✅ Input validation in forms  
✅ Proper type conversion (Int ↔ Long)  
✅ Responsive UI with Toast notifications  

## Future Enhancements

Consider adding:
1. Edit functionality for movies and actors
2. Search and filter by genre
3. Actor statistics (movies count)
4. Image support for movies and actors
5. ViewModel for better data management
6. LiveData for reactive updates
7. Room database testing
8. Pagination for large movie lists
9. Export/Import functionality
10. Rating system for movies

## Troubleshooting

### Build Errors
If you see XML parsing errors:
- Check for emoji characters in XML (replace with text)
- Ensure all drawable files are properly formatted
- Verify colors.xml doesn't have duplicate closing tags

### Database Issues
- If you see migration errors, clear app data
- With destructive migration, app will recreate all tables

### UI Issues
- Empty state appears when no movies exist - this is normal
- Use "Clear Data" in app settings if database gets corrupted
- Rebuild the project if layouts aren't showing

## Need Help?

Refer to:
- `DEPENDENCIES.md` - For dependency information
- `MIGRATION.md` - For database schema details
- The code comments in MovieActivity.kt for implementation details

---

**Happy Movie Management! 🎬**

