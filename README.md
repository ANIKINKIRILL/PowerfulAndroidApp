# PowerfulAndroidApp
This app is incredibly awesome. Here I use all kind of Jetpack Components. Use Rest Api and Cache. Single Source of Truth Principle. Dagger 2, Kotlin. MVI Acrhitecture and a lot more
The data that has been looked already will be saved to a Cache using Room library. Then if the user does not have internet, the data from the cache will be shown in the app. The logic
is covered [here](https://developer.android.com/jetpack/guide#best-practices). The MVI architecture that I use in this app

## MVI Architecture
MVI (Model-View-Intent) architecture is the kind of MVVM (Model-View-ViewModel), but with more features. The most big advantage of this architecture is if your app has many fragments
in one activity, your ViewModel is getting massive and uncomprehendable. So MVI is ready to help! [HERE IS THE GUIDE](https://www.raywenderlich.com/817602-mvi-architecture-for-android-tutorial-getting-started)

![alt text](https://miro.medium.com/max/1538/1*w0QeeQqrnISXLhYkYZWoAg.png "MVVM Architecture")
![alt text](https://github.com/ANIKINKIRILL/PowerfulAndroidApp/blob/master/MVI_architecture.jpg "MVVM Architecture")

## JetPack Navigation Components and BottomNavController
Custom bottom navigation controller. Helps to easily manage the back stack of fragments
The core concept took from [here](https://stackoverflow.com/questions/50577356/android-jetpack-navigation-bottomnavigationview-with-youtube-or-instagram-like#_=_)

![alt text](https://github.com/ANIKINKIRILL/PowerfulAndroidApp/blob/master/bottom_nav_controller.jpg "BottomNavController")


## User Authentication Token
User cannot see and create any blog unless he has been logged in successfully and he has the memeber subscription. Each member subscription has his own TOKEN. After he logged in 
app checks this TOKEN and respond ERROR or SUCCESS. In this application I use the same logic as the most common apps do. If the user closes the appication, and opens it tomorrow, app will authenticate user automatically using saved AUTH TOKEN in SessionManager class. After user logged out, the AUTH TOKEN will be deleted from SessionManager class

## Here are stuff that I am using:
<ul>
<li><strong>Kotlin</strong>:</li>
<li>
<strong>Coroutines</strong>:<br>
<ol>
<li>Advanced coroutine management using jobs</li>
<li>Cancelling active jobs</li>
<li>Coroutine scoping</li>
</ol>
</li>
<li>
<strong>Navigation Components</strong>:<br>
<ol>
<li>Bottom Navigation View with fragments </li>
<li>Leveraging multiple navigation graphs (this is cutting edge content)</li>
</ol>
</li>
<li>
<strong>Dagger 2</strong>:<br>
<ol>
<li>custom scopes, fragment injection, activity injection, Viewmodel injection</li>
</ol>
</li>
<li>
<strong>MVI architecture</strong>:<br>
<ol>
<li>Basically this is MVVM with some additions</li>
<li>State management</li>
<li>Building a generic BaseViewModel</li>
<li>Repository pattern (NetworkBoundResource)</li>
</ol>
</li>
<li>
<strong>Room Persistence</strong>:<br>
<ol>
<li>SQLite on Android with Room Persistence library</li>
<li>Custom queries, inserts, deletes, updates</li>
<li>Foreign Key relationships</li>
<li>Multiple database tables</li>
</ol>
</li>
<li>
<strong>Cache</strong>:<br>
<ol>
<li>Database caching (saving data from network into local cache)</li>
<li>Single source of truth principal</li>
</ol>
</li>
<li>
<strong>Retrofit 2</strong>:<br>
<ol>
<li>Handling any type of response from server (success, error, none, etc...)</li>
<li>Returning LiveData from Retrofit calls (Retrofit Call Adapter)</li>
</ol>
</li>
<li>
<strong>ViewModels</strong>:<br>
<ol>
<li>Sharing a ViewModel between several fragments</li>
<li>Building a powerful generic BaseViewModel</li>
</ol>
</li>
<li>
<strong>WebViews</strong>:<br>
<ol>
<li>Interacting with the server through a webview (Javascript)</li>
</ol>
</li>
<li>
<strong>SearchView</strong>:<br>
<ol>
<li>Programmatically implement a SearchView</li>
<li>Execute search queries to network and db cache</li>
</ol>
</li>
<li>
<strong>Images</strong>:<br>
<ol>
<li>Selecting images from phone memory</li>
<li>Cropping images to a specific aspect ratio</li>
<li>Setting limitations on image size and aspect ratio</li>
<li>Uploading a cropped image to server</li>
</ol>
</li>
<li>
<strong>Network Request Management</strong>:<br>
<ol>
<li>Cancelling pending network requests (Kotlin coroutines)</li>
<li>Testing for network delays</li>
</ol>
</li>
<li>
<strong>Pagination</strong>:<br>
<ol>
<li>Paginating objects returned from server and database cache</li>
</ol>
</li>
<li>
<strong>Material Design</strong>:<br>
<ol>
<li>Bottom Navigation View with Fragments</li>
<li>Customizing Bottom Navigation Icon behavior</li>
<li>Handling Different Screen Sizes (ConstraintLayout)</li>
<li>Material Dialogs</li>
<li>Fragment transition animations</li>
</ol>
</li>
</ul>
<br>
