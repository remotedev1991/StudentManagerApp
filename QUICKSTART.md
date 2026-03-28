# Quick Start Guide - Movie & Actor Feature

## 🚀 Getting Started in 5 Minutes

### Step 1: Run the App
1. Open Android Studio
2. Select "Run" → "Run 'app'" (or press Shift+F10)
3. Wait for the app to build and launch
4. You'll see the "Movie & Actor Hub" screen

### Step 2: Add Your First Movie
1. Tap **"+ Add Movie"** button (blue button at bottom-right)
2. Fill in the form:
   ```
   Title:       Inception
   Genre:       Science Fiction
   Year:        2010
   Director:    Christopher Nolan
   Budget:      160
   Description: A mind-bending thriller about dreams within dreams
   ```
3. Tap **"Save"**
4. Your movie appears in the list!

### Step 3: Add Your First Actor
1. Tap **"+ Add Actor"** button (orange button at bottom-left)
2. Fill in the form:
   ```
   Name:        Leonardo DiCaprio
   Birth Year:  1974
   Nationality: USA
   Profession:  Actor
   Bio:         Renowned Hollywood actor known for iconic roles
   ```
3. Tap **"Save"**
4. Actor is now available in the system!

### Step 4: Link Actor to Movie
1. In your movie card, tap the **"+ Actor"** button
2. Check the actor(s) you want to assign:
   - ☑ Leonardo DiCaprio
3. Tap **"Assign"**
4. The actor now appears in the movie's cast list!

### Step 5: View Your Data
- **Movie Card Shows:**
  - Title, Genre, Year
  - Director and Budget
  - Full description
  - All assigned actors with birth year and nationality

---

## 📋 Sample Data to Try

### Movie 1: Inception
```
Title: Inception
Genre: Science Fiction
Year: 2010
Director: Christopher Nolan
Budget: 160.0
Description: A skilled thief who steals corporate secrets through dream-sharing technology
```

### Movie 2: The Dark Knight
```
Title: The Dark Knight
Genre: Action
Year: 2008
Director: Christopher Nolan
Budget: 185.0
Description: Batman faces a new criminal known as the Joker
```

### Movie 3: Interstellar
```
Title: Interstellar
Genre: Science Fiction
Year: 2014
Director: Christopher Nolan
Budget: 165.0
Description: A team of explorers travel through a wormhole in space
```

### Actors to Add
1. **Leonardo DiCaprio** - 1974, USA, Actor, "Oscar-winning actor"
2. **Christian Bale** - 1974, UK, Actor, "Versatile actor known for Batman role"
3. **Heath Ledger** - 1979, Australia, Actor, "Legendary actor and artist"
4. **Anne Hathaway** - 1982, USA, Actress, "Talented actress and musician"
5. **Matthew McConaughey** - 1969, USA, Actor, "Hollywood leading man"

---

## 🎨 UI Overview

### Main Screen
```
┌─────────────────────────────────┐
│   Movie & Actor Hub             │  ← Header with gradient
│   (Manage movies and actors)    │
├─────────────────────────────────┤
│                                 │
│  ┌──────────────────────────┐   │
│  │ Inception                │   │
│  │ Science Fiction • 2010   │   │
│  ├──────────────────────────┤   │ ← Movie Card
│  │ Director: C. Nolan       │   │
│  │ Budget: $160M            │   │
│  │ Description: A mind...   │   │
│  ├──────────────────────────┤   │
│  │ Cast              [+Actr]│   │
│  │ Leonardo DiCaprio (1974) │   │
│  │ Anne Hathaway (1982)     │   │
│  └──────────────────────────┘   │
│                                 │
│  [+ Add Actor] [+ Add Movie]    │
└─────────────────────────────────┘
```

### Add Movie Dialog
```
┌─────────────────────────────┐
│ Add New Movie               │
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │ Movie Title             │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Genre (Action, Drama)   │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Release Year (YYYY)     │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Director Name           │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Budget (in millions)    │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ Description / Plot      │ │
│ │ Summary...              │ │
│ └─────────────────────────┘ │
│ [Cancel]            [Save]  │
└─────────────────────────────┘
```

---

## 🔍 What's Happening Behind the Scenes

### Database Flow
```
User Input (Dialog)
    ↓
Validation Check
    ↓
Save to Room Database
    ↓
Create M:M Relationship
    ↓
Query Movies with Actors
    ↓
Display in ListView
```

### M:M Relationship
```
Movie (1) ──┐
            ├── MovieActorCrossRef ──┬── Actor (1)
Movie (2) ──┤                        ├── Actor (2)
Movie (3) ──┤                        ├── Actor (3)
            ├── MovieActorCrossRef ──┴── Actor (4)
```

One movie can have many actors.  
One actor can be in many movies.

---

## 💾 Database Details

### What Gets Saved
- **Movies Table**: Movie information (6 fields)
- **Actors Table**: Actor information (5 fields)
- **Junction Table**: Movie-Actor relationships

### What's a Junction Table?
A junction (or cross-reference) table bridges the M:M relationship:

```
movies table:
movieId | title    | genre | releaseYear | director | budget | description
1       | Inception| SciFi | 2010        | Nolan    | 160    | Dream movie...

actors table:
actorId | name     | birthYear | nationality | profession | bio
1       | Leo      | 1974      | USA        | Actor     | Oscar winner...

movieActorCrossRef table:
movieId | actorId
1       | 1          ← Means Inception has Leonardo DiCaprio
1       | 2          ← Means Inception has Anne Hathaway
```

---

## ⚠️ Important Notes

### First Launch
- App will create the database automatically
- No data will exist yet (empty state)
- This is normal!

### Clearing Data
If you need to start fresh:
1. Settings → Apps → StudentManager
2. Storage → Clear Data
3. Relaunch the app

### Editing
- Current version doesn't have edit feature
- To change data: Delete and re-add
- Edit feature can be added in future

---

## 🐛 Troubleshooting

### App Crashes on Launch
- ✅ Clear app cache (Settings → Apps → Storage → Clear Cache)
- ✅ Rebuild project (Build → Rebuild Project)

### Dialog Won't Open
- ✅ Make sure all fields are being validated
- ✅ Check logcat for any error messages

### Movies Not Showing
- ✅ Try restarting the app
- ✅ Check if database operations completed
- ✅ Look for any Toast notifications

### Delete Not Working
- ✅ Confirm the deletion dialog
- ✅ Wait for background task to complete

---

## 📚 More Information

For detailed information, see:
- **MOVIE_GUIDE.md** - Complete feature guide
- **MIGRATION.md** - Database schema details
- **DEPENDENCIES.md** - Dependency information
- **IMPLEMENTATION_SUMMARY.md** - What was built

---

## 🎬 Ready to Go!

You now have a fully functional Movie & Actor management system with M:M relationships!

**Start by:**
1. Adding 2-3 movies
2. Adding 3-5 actors
3. Linking actors to movies
4. Exploring the beautiful UI

**Enjoy!** 🎭

