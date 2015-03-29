# New Hope International Android App README #

This README documents the steps  necessary to get the application up and running

### Android Studio ###

Finding the tools necessary for Android development is rather simple. Whether you have a Linux, Mac, or Windows machine, you can develop for Android by downloading Android Studio at http://developer.android.com/sdk/index.html

Unlike Xcode for iOS development, you do not even need to pay for the Google developer license to test on physical devices. 

The caveat though is that the Android Studio emulator will not run on Macs with a Core 2 Duo processor. It would be best to just test on a physical device. 

Many online guides will show setup procedures for Eclipse SDK development. However, Android Studio is now the official Android SDK. 

### Setting Up ###

We will be following a Feature Branch workflow. What that means is that, for every feature, the developer will have their own branch to work with. Once they are done with that feature, they issue a pull request for their changes to be merged into the master repository. All releases will be cut from the master repository.

To create your own feature branch do the following:

1. Click on the branches tab, on the left hand side of the bitbucket website. 

2. Click "Create Branch" on the top right hand side.

3. Ensure that you are branching from master. Give the feature branch an appropriate title. It is suggested that you include the Issue number this feature branch will be addressing. 

4. This feature branch is intended only for that particular fix or feature. Once the feature has been completed, a pull request should be done to merge it back into master. The feature branch should then be marked close. 

Once you have completed making your own feature branch, you are ready to check out your project!

1. Open Android Studio and update to the latest version before checking out the project from source control. You will have to restart Android Studio once the update is done. 

2. Check out the New Hope International Android App project by clicking on "Check out project from Version Control". It will give you an option to check out from different sources. Click on Git. 

3. Put in the following for Vcs Repository URL: https://your-username@bitbucket.org/apps4christ/nhic-android-app.git

4. Navigate to a local directory you want to store the project

5. Give it a appropriate directory name such as NHIC-Android-App. 

6. Press ok and it will start the clone process for you. Once done, it will confirm that you have checked out an Android Studio project file and will ask if you want to open it. Press yes. 

7. A menu will pop up called "Import Project from Gradle". Make sure that it the "use default gradle wrapper (recommended)" radio button is selected. Press OK. 

8. Click on VCS->Git->Branches and select the feature branch you have created. Choose checkout as new local branch. This is the branch you should be working in. If you are in master, you will not be able to check in any of your changes. 

9. It will take a few minutes to build the Gradle project files. When the run button is no longer grayed out, test that everything has built properly by running and emulating the app on the provided Nexus 5 emulation image.

### Contribution guidelines ###

Please only commit changes through Android Studio. To commit changes, click VCS on the toolbar,  Git->commit. Double check you are committing the correct files, write an informative commit message addressing:

1. Issue number you are fixing

Fix for Issue #100 - This an example of a bug

2. How did you fix it

I fixed this by doing the following....

Put your name in the author field using the following format: Your Name <user@somemail.com>. Then in the same VCS menu, click on Git->Push. This will push the changes to your feature branch. No one else should be pulling or putting changes in your feature branch. To ensure compatibility between your feature branch and master, please pull changes from master into your feature branch. 



### Who do I talk to? ###
For any questions please contact Mark Merin