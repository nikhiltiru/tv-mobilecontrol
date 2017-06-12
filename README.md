This application demonstrates controlling Android TV using mobile with NSD and Cast protocols

This app has a simple interface to control navigation on TV and also to send some text using NSD. You can extend this to exchange any information between TV and Mobile devices.
Both the devices have to be on the same network. 

The 'Up', 'Down', 'Left', 'Right' button will send commands to the TV to change the focus to Corresponding buttons on TV.
For this, the TV counterpart app is https://github.com/nikhiltiru/androidtvdemo.

Install androidtvdemo app on Android TV and this app on mobile.

The NSD and Cast related codes are taken from below references and modified a bit.
Go through the app/src/main/java/com/example/android/nsdchatclient/NsdHelper.java and app/src/main/java/com/example/android/nsdchatclient/ChatConnection.java files for better understanding.

Please note that error and exception handling is not exhaustive. So watch out for crashes and errors.
Tested this on Nexus Player and Sony BRAVIA TV.

## NSD
NSD stands for Network Service Discovery. Please see [this][NSD] link for more info:

## Google Cast
With Google Cast, you can cast the videos, music and images from mobile app to any Cast enabled device, in this case, Android TV.

In this app, the cast functionality is not yet built. You have to follow the below steps to add the Cast functionality to it.

1. Register your application and get the app id. (We are using the default player for this session)
Default app id: CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID

2. Add dependencies to your build.gradle
```sh
dependencies {
	...
    compile 'com.android.support:mediarouter-v7:25.0.0'
    compile 'com.google.android.gms:play-services-cast-framework:9.8.0'
	...
}
```

3. Add a CastOptionsProvider.java file to your project this will specify the app id of the receiver (see step 1)
```sh
import android.content.Context;

import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;


public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {
        return new CastOptions.Builder()
                .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
                .build();
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
```
4. Add the following meta-data to your AndroidManifest file (to specify the CastOptionsProvider file having the receiver app id)
```sh
	<meta-data
	android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
	android:value="<package name>.CastOptionsProvider" />
```

5. Add the Cast button in your browse.xml
```sh
<item
    android:id="@+id/media_route_menu_item"
    android:title="@string/media_route_menu_title"
    app:actionProviderClass="android.support.v7.app.MediaRouteActionProvider"
    app:showAsAction="always"/>
```

6. Add a MiniControllerFragment to your activity's layout before the settings (to play/pause the video)
```sh
<fragment
    android:id="@+id/castMiniController"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true"
    android:visibility="gone"
    class="com.google.android.gms.cast.framework.media.widget.MiniControllerFragment"/>
```

7. Initialize CastContext in your LocalPlayerActivity's onCreate()
```sh
    private CastContext mCastContext;
	...
	mCastContext = CastContext.getSharedInstance(this);
```

8. Override the onCreateOptionMenu() of LocalPlayerActivity: (see step 5)
```sh
private MenuItem mediaRouteMenuItem;

public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.browse, menu);
    mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
    return true;
}
```

9. For rest of the steps, refer:
https://codelabs.developers.google.com/codelabs/cast-videos-android/#5

After all the above changes, the finished project can be found at https://github.com/nikhiltiru/tv-mobilecontrol-cast


## References

### Cast

https://codelabs.developers.google.com/codelabs/cast-videos-android 

Google Cast & Android TV: Building connected experiences for the home - Google I/O 2016

https://www.youtube.com/watch?v=HukWdsRayqQ 

### NSD (Network service Discovery)

https://github.com/android/platform_development/tree/master/samples/training/NsdChat/src/com/example/android/nsdchat 

[NSD]: <https://developer.android.com/training/connect-devices-wirelessly/nsd.html>
