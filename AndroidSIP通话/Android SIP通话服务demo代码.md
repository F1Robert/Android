### Android SIP通话服务demo代码

1.SIP服务器搭建

2.SIP注册

3.使用SIP服务功能(语音通话/视频通话)

```
import android.net.sip.*;
import android.view.SurfaceView;
import java.text.ParseException;

public class MySipActivity extends Activity {
    private SipManager sipManager = null;
    private SipProfile sipProfile = null;
    private SipAudioCall sipAudioCall = null;
    private SurfaceView localVideoView;
    private SurfaceView remoteVideoView;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

​    localVideoView = findViewById(R.id.local_video_view);
​    remoteVideoView = findViewById(R.id.remote_video_view);

​    initializeSip();
}

private void initializeSip() {
    try {
        SipProfile.Builder builder = new SipProfile.Builder("username", "domain");
        builder.setPassword("password");
        sipProfile = builder.build();

​        SipRegistrationListener listener = new SipRegistrationListener() {
​            public void onRegistering(String localProfileUri) {}
​            public void onRegistrationDone(String localProfileUri, long expiryTime) {}
​            public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {}
​        };

​        sipManager = SipManager.newInstance(this);
​        sipManager.open(sipProfile);
​        sipManager.setRegistrationListener(sipProfile.getUriString(), listener);
​    } catch (ParseException | SipException e) {
​        e.printStackTrace();
​    }
}

public void makeCall(String callee) {
    try {
        SipProfile.Builder builder = new SipProfile.Builder(callee);
        sipAudioCall = sipManager.makeAudioCall(sipProfile.getUriString(), builder.build(), listener, 30);
    } catch (SipException e) {
        e.printStackTrace();
    }
}

private SipAudioCall.Listener listener = new SipAudioCall.Listener() {
    @Override
    public void onCalling(SipAudioCall call) {
        // Called when the call is established and ready to be accepted
    }

​    @Override
​    public void onRinging(SipAudioCall call, SipProfile caller) {
​        // Called when the call is ringing
​    }

​    @Override
​    public void onCallEstablished(SipAudioCall call) {
​        // Called when the call is established
​    }

​    @Override
​    public void onCallEnded(SipAudioCall call) {
​        // Called when the call ends
​    }
};

public void endCall() {
    if (sipAudioCall != null) {
        try {
            sipAudioCall.endCall();
        } catch (SipException e) {
            e.printStackTrace();
        }
    }
}

public void receiveCall() {
    try {
        sipManager.setIncomingCallPendingIntent(createIncomingCallIntent());
        sipManager.acceptCall(sipAudioCall, 30);
    } catch (SipException e) {
        e.printStackTrace();
    }
}

private PendingIntent createIncomingCallIntent() {
    Intent intent = new Intent(this, MySipActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_DATA);
    return pendingIntent;
}

// Implement methods to handle video streaming

}


```

