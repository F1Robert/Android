# Android window manager创建显示窗口和界面

```
private void showFloatingWindow(AVEngineKit.CallSession session) {
    if (wm != null) {
        return;
    }
    session.restVideoViews();

    wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    params = new WindowManager.LayoutParams();

    int type;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    } else {
        type = WindowManager.LayoutParams.TYPE_PHONE;
    }
    params.type = type;
    params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

    params.format = PixelFormat.TRANSLUCENT;
    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    params.gravity = Gravity.CENTER;
    params.x = getResources().getDisplayMetrics().widthPixels;
    params.y = 0;

    view = LayoutInflater.from(this).inflate(R.layout.av_voip_float_view, null);
    view.setOnTouchListener(onTouchListener);
    wm.addView(view, params);
    if (session.getState() != AVEngineKit.CallState.Connected) {
        showUnConnectedCallInfo(session);
    } else {
        if (session.isScreenSharing()) {
            showScreenSharingView(session);
        } else if (session.isAudioOnly()) {
            showAudioView(session);
        } else {
            String nextFocusUserId = nextFocusUserId(session);
            if (nextFocusUserId != null) {
                showVideoView(session, nextFocusUserId);
            } else {
                showAudioView(session);
            }
        }
    }
}
```