# Overview
A demo application using [NewsApi](https://newsapi.org/docs)

This demo uses Jetpack compose for UI, Retrofit for API calls, Hilt/dagger for dependency injection and Room for persistent local storage.

This project uses JDK17. Please check under `File -> Settings -> Build, Execution, Deployement -> Build tools -> Gradle` that the appropriate Gradke JDK is set (tested on `jbr-17`)

Note: There is a limit of 100 requests per 24 hours, this limit is handled by the app.

To ensure the best performance, please build the application's **Release** variant, Jetpack Compose isn't performant when in Debug mode.

# Project structure
The project is structured using three layers:
- App layer
- Data layer
- Domain layer

## App layer
The App layer (or presentation layer) is responsible for handling all of the UI elements shown to the user.

## Data layer
The data layer is responsible for handling data (fetching from API's, saving data to the local database etc.).

## Domain layer
The domain layer consists of the shared models, repositories and usecases used by the Data and App layers.

# What was implemented
The app consists of two 'modules':
1. Sign up/sign in ie. User onboarding
1. Main news related screens

### Sign up/sign in
Signing up is done locally, meaning there is not registration to an external API or backend. All users are stored locally on the device itself.

When opening the app, we have three possibilities:
1. Automatically navigating to the Create Profile screen if no profile is present on the device
2. Selecting a profile if a profile is present on the device BUT we are not 'signed in' as the profile
3. Automatically navigating to the Main news screens if we are signed in as a profile

### Main news screens
On the main part of the application, we have the three main screens:
1. Top stories/top headlines
2. All stories
3. Profile

Top stories and all stories screens have the ability to filter the results by date range, a search query, news sources and ordering (ascending and descending)

**Note: NewsAPI doesn't support items ordering, this was done locally using `reverseLayout`, but a better solution would be to just send this order parameter to the backend and the backend can serve the ordered list back to the app**

### Tests
Unfortunately I didn't have enough time to write proper tests, but I did include a 'test' example in the App layer for testing UI.

Tests for the Data layer are important to ensure the data integrity.
