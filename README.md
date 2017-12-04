# GooglePlacesAndroid
This repo contains a demo app which uses play services and google places API to show nearby restaurants around a user

By default you will see restaurants near you in a recyclerView. 
Each item in the recyclerView will show icon, name and vicinity of the place
When you select a place, it will show that place on GoogleMaps app, and zoom to level-19

Due to time constraints, no UI was added to enter keyword and radius.
But you can configure radius and keyword for search in MainActivity.java

At the top of MainActivity file, there are two constants `KEYWORD` and `RADIUS` which you can use to change the values.
If you want to search using a keyword, please use `getNearbyPlaces(Location location, int radius, String keyword)` in 
NetworkManager.java (You will have to replace `mNetworkManager.getNearbyRestaurants()` in MainActivity with this method 
and provide your keyword)

**NOTE** I have not tested if keywords with spaces will break the url. If it does,and I think it should, then we have to encode 
or escape the spaces.
