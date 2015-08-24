# New Hope International Android App README #

This README documents the steps  necessary to get the application up and running

## Android Device ##

While not absolutely necessary, having an actual Android device is recommended. As great as the emulator is, there are some bugs and features that can only be easily tested on an actual physical device. Do you need the latest and greatest Android phone? Absolutely not. The basic requirement is that it needs to have Android 4.0+. 

Fortunately Android phones are actually fairly cheap. If you go to your Fred Meyer/Walmart/Target/Safeway store you can pick up those pre-paid phones for cheap, and not get the plan that comes along with it. Usually there are also sales. For example, I picked up a $99 LG Optimus Fuel for $19.99. 

## Android Studio ##

Finding the tools necessary for Android development is rather simple. Whether you have a Linux, Mac, or Windows machine, you can develop for Android by downloading Android Studio at http://developer.android.com/sdk/index.html

Unlike Xcode for iOS development, you do not even need to pay for the Google developer license to test on physical devices. 

The caveat though is that the Android Studio emulator will not run on Macs with a Core 2 Duo processor. It would be best to just test on a physical device. 

Many online guides will show setup procedures for Eclipse SDK development. However, Android Studio is now the official Android SDK. 

## Getting Familiar with Android ##

Just because you are a Java pro doesn't mean you can write an Android app from scratch. There's a bit of a learning curve and the following tutorials will help you overcome that. 
These will familiarize you with small aspects of the NHIC App. Some of these assume the use of Eclipse. Stick with Android Studio. 

1. Intro: http://www.techotopia.com/index.php/Creating_an_Example_Android_App_in_Android_Studio
2. Layouts: http://www.androidhive.info/2011/07/android-layouts-linear-layout-relative-layout-and-table-layout/
3. Navigation Drawer: http://www.learn2crack.com/2014/06/android-sliding-navigation-drawer-example.html, http://www.theappguruz.com/tutorial/android-sliding-menu-demo/
4. Tab Bar: http://javapapers.com/android/android-tab-layout-tutorial/
5. Media Player: http://code.tutsplus.com/tutorials/create-a-music-player-on-android-song-playback--mobile-22778
6. Rss Reader: http://www.androidpit.com/java-guide-2-program-your-own-rss-reader


## Source Control: Git ##

We will be using Git for source control for this project. Git was originally developed in 2005 for kernel projects by Linus Torvalds. For more information, please see: http://en.wikipedia.org/wiki/Git_%28software%29

For a quick explanation of what Source Control is, watch the following video
https://www.youtube.com/watch?v=xegsChaAZL4

1. Download and install Git from http://git-scm.com/downloads

2. Follow the wizard and choose all default options. 

## Setting Up ##

We will be following a Feature Branch workflow. What that means is that, for every feature, the developer will have their own branch to work with. Once they are done with that feature, they issue a pull request for their changes to be merged into the master repository. All releases will be cut from the master repository.

To create your own feature branch do the following:

1. Click on the branches tab, on the left hand side of the bitbucket website. 

2. Click "Create Branch" on the top right hand side.

3. Ensure that you are branching from master. Give the feature branch an appropriate title. It is suggested that you include the Issue number this feature branch will be addressing. 

4. This feature branch is intended only for that particular fix or feature. Once the feature has been completed, a pull request should be done to merge it back into master. The feature branch should then be marked close. 

Once you have completed making your own feature branch, you are ready to check out your project!

### Android Studio Configuration ###

1. Open Android Studio and update to the latest version before checking out the project from source control. You will have to restart Android Studio once the update is done. 

2. Assuming this is your first time downloading and operating Android Studio, select Configure->Settings.

3. On the left panel, expand Version Control and select Git. Ensure that the path to your Git executable is valid. You are able to press the Test button to verify that the path to git.exe is correct and functioning.

4. If you are on a windows machine and followed the wizard it should be at: C:\Program Files (x86)\Git\cmd\git.exe

### Checking out the project ###

1. Check out the New Hope International Android App project by clicking on "Check out project from Version Control". It will give you an option to check out from different sources. Click on Git. 

2. Put in the following for Vcs Repository URL: https://your-username@bitbucket.org/apps4christ/nhic-android-app.git

3. Press the Test button to make sure you have the right url. It will prompt you for your Bitbucket account password.

4. It will then prompt you to create a master password and to confirm it.

5. Navigate to a local directory you want to store the project

6. Give it a appropriate directory name such as NHIC-Android-App. 

7. Press ok and it will start the clone process for you. Once done, it will confirm that you have checked out an Android Studio project file and will ask if you want to open it. Press yes. 

8. A menu will pop up called "Import Project from Gradle". Make sure that it the "use default gradle wrapper (recommended)" radio button is selected. Press OK. 

### Working in the correct branch ###

1. Click on VCS->Git->Branches and select the feature branch you have created. Choose checkout as new local branch. This is the branch you should be working in. If you are in master, you will not be able to check in any of your changes. 

2. It will take a few minutes to build the Gradle project files. When the run button is no longer grayed out, test that everything has built properly by running and emulating the app on the provided Nexus 5 emulation image.

## Filing Bugs ##

In the course of your work, you may discover other bugs present. Do not fix them in your branch, rather file a new bug targeting them using our Issue Tracker. Please provide a clear title stating what the issue is. In the body of the bug, please report what device you are using, what Android version, and the steps required to reproduce the issue. The clearer you are, the easier it will be for the person who will be fixing the bug. 

## Contribution guidelines ##

Please only commit changes through Android Studio. To commit changes, click VCS on the toolbar,  Git->commit. Double check you are committing the correct files, write an informative commit message addressing:

1. Issue number you are fixing

Fix for Issue #100 - This an example of a bug

2. How did you fix it

I fixed this by doing the following....

Put your name in the author field using the following format: Your Name <user@somemail.com>. Then in the same VCS menu, click on Git->Push. This will push the changes to your feature branch. No one else should be pulling or putting changes in your feature branch. To ensure compatibility between your feature branch and master, please pull changes from master into your feature branch. 



## Who do I talk to? ##
For any questions please contact Mark Merin