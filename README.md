# InstaU ( Social Media App )  - Ktor Server (Work in Progress) 🚧

Welcome to the backend server code repository for the Social Media App! This server is built using Ktor, a powerful Kotlin framework for building asynchronous servers and clients.
This repository contains the backend server code for the Social Media App [here](https://github.com/Ayush-Kotlin-Dev/InstaU---Frontend-Android-App) built using Ktor.

## Getting Started 🚀

### Prerequisites
- [Kotlin](https://kotlinlang.org/)
- [Gradle](https://gradle.org/)

### Installation
1. Clone this repository. 
```kotlin
git clone https://github.com/Ayush-Kotlin-Dev/InstaU---Backend-Server.git
```

2. Build the project using Gradle.
```kotlin
cd socialapp
./gradlew build

```

3. Run the server locally using `./gradlew run`.
```kotlin
./gradlew run

```
## Usage ℹ️


### API Endpoints implemented as for now
- `/signup`: Creates a new account.
- `/signup`: Logins to an existing account
- `/follow`: Adds and removes a follower (following).
- `/post/create`: Creates a new Post
- `/post/{postId}` Returns the Post associated with this `postId` when it's a `GET` request. Deletes the Post when it's a `DELETE` request.
- `/posts/feed`: Returns paginated Posts from people you follow. Query parameters (currentUserId, page, limit)
- `/posts/{userId}`: Returns paginated Posts of this `userId`. Query parameters (currentUserId, page, limit)
- `/profile/{userId}`:  Returns the profile of the user with the given userId. Requires a GET request. The response will be a ProfileResponse object containing the user's profile information.
- `/profile/update` :  Updates the profile of the currently authenticated user. Requires a POST request with a ProfileUpdateParams object in the request body. The response will be a ProfileResponse object containing the updated profile information.
- `/follows`:  Returns a list of users that the currently authenticated user is following. Requires a GET request. The response will be a FollowsResponse object containing a list of User objects.
- `/follow/followers`: Returns a list of followers for a given user.
- `/follow/following`: Returns a list of users the given user is following.
- `/follow/suggestions`: Provides follow suggestions if a user is new .
- `/post/comments/create` : Creates a new comment on a post. Requires a POST request with a NewCommentParams object in the request body.
- `/post/comments/delete` : Deletes a comment from a post. Requires a DELETE request with a RemoveCommentParams object in the request body.
- `/posts/comments/{postId}` :: Returns all comments for a post with the given postId. Requires a GET request.
- `/post/likes/add`: Adds a like to a post. Requires a POST request with a LikeParams object in the request body.
- `/post/likes/remove` :  Removes a like from a post. Requires a DELETE request with a LikeParams object in the request body.
- `/search`: Searches for users by their name accepts parameter "name" return list of users .
-`/profile/suggestios : Suggests the List of popular users to the new users`

## Client Repository 📱

The client side of this Social Media App is built using Kotlin and Jetpack compose. Find the client repository [here](https://github.com/Ayush-Kotlin-Dev/InstaU---Frontend-Android-App).
