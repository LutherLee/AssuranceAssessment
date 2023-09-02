The project fetch movies from [OMDbApi](https://www.omdbapi.com/) by page number incrementally
from 1 to 100. The list of movies are then displayed with pagination (order by page number fetched)
based on the search keyword provided.

Only local DB (Room persistence library) are used in this project. On user sign up, the password are
hashed using SHA-1 and is stored in database which is encrypted using SQLCipher. 

Realistically, any sensitive data (e.g. access token) should be stored in backend 
(either encrypted or hashed) as locally stored credentials are not secure. Additionally, 
you may want to use Proguard/R8 to obfuscate the code. 

## Project Structure
There is only one module (app module) which contains:
- Data layer - Contains data layer related files (dao, model, network, repository, database)
- Dependency Injection (DI) - Contains all the hilt modules defined to supply related dependencies
- UI (Presentation) layer - Contains all the views related class (fragment, viewmodel, adapter)
- Util - Contains all the utils class used in this project (e.g. extension functions)

## TechStack:
- Kotlin
- MVVM architecture
- [Coroutine](https://developer.android.com/kotlin/coroutines)
- [Kotlin Flow](https://developer.android.com/kotlin/flow)
- Material Design

__Network__
- [OkHttp](https://github.com/square/okhttp)
- [Retrofit](https://square.github.io/retrofit/)
- [Gson](https://github.com/google/gson)

__Security__
- [SQLCipher (for encrypting Room database)](https://github.com/sqlcipher/sqlcipher-android)
- [Security Library (for sharedPreferences encryption and decryption)](https://developer.android.com/topic/security/data)

__Dependency Injection (DI)__
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

__Image Loader__
- [Glide](https://github.com/bumptech/glide)

__[Android Jetpack](https://developer.android.com/jetpack)__
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Room Persistence Library (local db)](https://developer.android.com/training/data-storage/room)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)

__Logging__
- [Timber](https://github.com/JakeWharton/timber)

__Others__
- [DiagonalImageView (Used to crop image diagonally)](https://github.com/santalu/diagonal-imageview)

<br>
You can define your own API key obtained from OMDbApi in local.properties file

```
sdk.dir=........
API_KEY=YOUR_KEY_HERE
```



