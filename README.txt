TCSS 450 - Spring 2017
Group 4
Ryan Potter, Alexander Terikov, Edie Campbell

Project Phase II

Features:
Register with a username and password
Login with a username and password
Use camera to take a photograph, store that photograph to device memory
Upload image to Google Cloud Vision API using the API in Label mode to identify parts items in the image
Display results from Google Cloud Vision API call
External Webservice setup to handle login and registration attempts
Parses data returned from Cloud Vision API, then calls the Wikipedia API to supplement the displayed plant information

Use Cases:

Login Use Case Completed
Register Use Case Completed
Retrieve Image Data Use Case Completed
Identify New Plant Use Case Completed
View Previously Identified Plants Use Case Completed
  Note the Gallery that is used to display the plants will not update when you take a picture. This is a bug we were unable   to fix.

You can see are Icon when installed and our logo when you first start the app.


NOTES ON RUNNING THIS APP:

If you have run this app rpeviously, you will need to use a file manager and navigate to picturs/CameraSample and delete the entire CameraSample directory. A but we discovered in our previously released build populated this file with a lot of garbage data that look like jpegs, but have no real values in them. If you simply delete this directory from your photo app it may leave these junk values behind. Additionally if you do not delete this folder and there were junk values, it will cause blank spaces to appear in the gallery.

This application is uploading a high quality image to Google's servers and as a result it can use up data quickly. Please be aware of this and if you have limited data test this app while you are on wifi.


GITHUB REPO:

If you wish to take a look at our repo please feel free. The branch titled Prototype is the brach that will alwasy be the most up to date with something working.

https://github.com/Rpotter8/tcss450App/tree/Prototype

Meeting Minutes:

https://docs.google.com/document/d/1lObqqt47_oztEKSevoCSmn-dzcwnSWDKvDS5dsV_7as/edit
