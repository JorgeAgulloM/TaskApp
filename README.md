# TaskApp

### Version 1.0.0-Alpha01
## Ligth Mode. Designed with Figma software.
![Light Mode](https://github.com/JorgeAgulloM/TaskApp/blob/main/Design/Version_1.0.0_Alpha01/Light_Mode.png)

<br>
<br>

## Dark Mode. Designed with Figma software.
![Dark Mode](https://github.com/JorgeAgulloM/TaskApp/blob/main/Design/Version_1.0.0_Alpha01/Dark_Mode.png)

```text
    Version 1.0.0_Alpha01 - 02/08/2022

    With this App the user will be able to list tasks, complete, edit or delete them at will.
These tasks consist of a check, a title, a description, two dates and the author of the task.

The user must create an account, in local DB, by entering a name of at least 3 letters, 
a valid email address and a password of at least 8 digits including numbers, at least 
one lowercase and one uppercase letter. This will allow you access to the app. Once logged 
in, a "remember me" check can be activated so that it is not necessary to log in every time 
the app is started.

The user will have a history list showing all created tasks (deleted tasks will disappear). 

The user has a series of settings such as the light/dark theme, which can be activated manually 
or allow the application to activate it in case it is activated on the device. You can use the 
colors of the application or change them to automatic mode (only for devices with Android 12+ 
and above). You can use your system language or switch between English and Spanish. 
Finally, you can modify the maximum time that the "remember me" option will be able to enter 
without asking for the password again.

The user can navigate between the different screens using the top menu.

The user will be able to change his data at any time as long as he is logged in.

This app is developed with:
- JetPack Compose 1.3.0-beta01
- Dagger Hilt 2.42
- Room 2.4.3
- Datastore 1.0.0
- Coroutines 1.6.4
- LiveData 1.2.1
- MVVM
- Clean Architecture
- Material you (Material 3)
- Material Icons

SDK minimum 26
Target SDK 32
 
```

```text
Demo 
```
### Version 1.0.0-Alpha01
## Create Task.
https://www.youtube.com/embed/rOrmNMeZgoU
<br>
<br>

## Settings.
https://www.youtube.com/embed/ZUFTF1OtWs0

<br>
<br>

## Navigation.
https://www.youtube.com/embed/tdLMn0rjapo

<br>
<br>

### Version 1.1.0-Alpha01
Version 1.1.0_Alpha01 - 08/09/2022
- Datastore has been added to replace SharePreferences, since google has deprecated them. 

<br>
<br>

### Version 1.2.0-Alpha01
Version 1.2.0_Alpha01 - 13/09/2022
- Error checking has been added for both user input data and database load data, along with the 
logic necessary to display such errors to the user via the UI.

<br>
<br>

### Version 1.2.5-Alpha01
Version 1.2.5_Alpha01 - 13/09/2022
- Fixed errors in login (rememberMe), settings, changed the screen detail layout and changed the new task height.

<br>
<br>

### Version 1.3.6-Alpha01
Version 1.3.6_Alpha01 - 20/09/2022
- Refactor Architecture
- Implements Use Cases
- Fixed a bug in the animation of the checkBoxes.