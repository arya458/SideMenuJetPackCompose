# SideBarMenu Compose

[![](https://jitpack.io/v/arya458/SideMenuJetPackCompose.svg)](https://jitpack.io/#arya458/SideMenuJetPackCompose)


Gradle

    implementation 'com.github.arya458:SideMenuJetPackCompose:TAG'


Or
Add your AAR or JAR as a dependency

To use AAR in an app, proceed as follows:

Navigate to File > Project Structure > Dependencies. In the Declared Dependencies tab, click and select Jar Dependency in the menu.

In the Add Jar/Aar Dependency dialog, enter the path to your AAR or JAR file, then select the configuration to which the dependency applies. If the library should be available to all configurations, select the implementation configuration.

![](https://developer.android.com/static/studio/images/projects/psd-add-jar-dependency-dropdown.png)

Add AAR dependency in the Project Structure Dialog

![](https://developer.android.com/static/studio/images/projects/psd-add-aar-dependency.png)

Check your app’s build.gradle or build.gradle.kts file to confirm that a declaration similar to the following appears (depending on the build configuration you've selected):
Groovy
Kotlin

    implementation(files("my_path/my_lib.aar"))




How To Use

	val mySideMenuState = remember { mutableStateOf(MenuState.COLLAPSE)}
    SideMenu(
    { modifier ->  
    // Your Menu Compose View 
    },
    sideMenuState = mySideMenuState.value,
    onSideMenuStateChange = { mySideMenuState.value = it })
    {
    // Your Main Page Compose View
    }

Demo

![](https://raw.githubusercontent.com/arya458/SideMenuJetPackCompose/master/demo.webp)

Aria Danesh
