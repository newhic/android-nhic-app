# New Hope International Android App README #

This README documents the steps  necessary to get the application up and running

### How do I get set up? ###

Finding the tools necessary for Android development is rather simple. Whether you have a Linux, Mac, or Windows machine, you can develop for Android by downloading Android Studio at http://developer.android.com/sdk/index.html

Unlike Xcode for iOS development, you do not even need to pay for the Google developer license to test on physical devices. 

The caveat though is that the Android Studio emulator will not run on Macs with a Core 2 Duo processor. It would be best to just test on a physical device. 

Many online guides will show setup procedures for Eclipse SDK development. However, Android Studio is now the official Android SDK. 

Please update Android Studio to the latest version before checking out the project from source control. Also click on the Android ADK symbol and update any files requiring an update. 

To check project out from source control, put in the following URL as the source:

https://your-username@bitbucket.org/apps4christ/nhic-android-app.git

Follow the setup wizard and let it build the project structure for you. Depending on the speed of your system, it may take awhile to even reach a point where you can compile and run the project. 

When the run button is no longer grayed out, test that everything has built properly by running and emulating the app on the provided Nexus 5 emulation image.

You are good to go!

### Contribution guidelines ###

Please only commit code that compiles and does not crash. Understandably some code may lead to unforeseen crashes, but by compiling and testing on the emulator you should be able to catch it before committing. 

### Who do I talk to? ###
For any questions please contact Mark Merin