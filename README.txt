INTRODUCTION

This is a cryptocurrency price tracking app.  It displays the current and historical prices of various cryptocurrencies.
Additional features include the ability to set a personal crypto watchlist and to create and track your own personal
portfolio of cryptocurrencies.


MOTIVE

This was the final capstone project for my Android Developer Nanodegree course.  The app was not designed to be as
efficient as possible, but rather to show off the knowledge and skills gained throughout the course.


FEATURES IMPLEMENTED

Data Persistence:
 - Google Firebase used for user authentication online
 - Google Cloud Firestore used to store user Portfolio data online
 - Room ORM used to store watchlist items locally on the device
 - ViewModel used to persist data over the full Activity lifecycle
	- ViewModel stores the market data retrieved from CoinGecko API
	- ViewModel responsible for making the API calls

Libraries
 - Google AdMob used in a free app flavor (paid flavor has no ads)
 - Volley used for making network calls to CoinGecko API
 - MPAndroidChart used for displaying market data in chart form
 - Picasso used for displaying images
 - Gson library used for parsing some JSON data (rest of the JSON is parsed manually)

Other:
 - LiveData used in conjunction with ViewModel and Room to track and listen for changes in data
 - Dual pane layout available for tablet screens (in landscape mode)
 - AsyncTasks used to communicate to Room database
 - Widget displays Portfolio value and is updated each time the app is launched
