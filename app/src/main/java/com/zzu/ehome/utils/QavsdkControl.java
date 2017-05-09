package com.zzu.ehome.utils;

import android.content.Context;





import java.util.ArrayList;

/**
 * AVSDK 总控制器类
 */
public class QavsdkControl {
    private static final String TAG = "QavsdkControl";

    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static QavsdkControl instance = null;
    private static Context mContext;
//    private static AVRoomMulti avRoomMulti;
    private AVContextControl mAVContextControl = null;
    public interface onSlideListener {
        void onSlideUp();

        void onSlideDown();
    }

//    public static QavsdkControl getInstance() {
//        if (instance == null) {
//            instance = new QavsdkControl(mContext);
//        }
//        return instance;
//    }
//    public void setAvRoomMulti(AVRoomMulti room) {
//        avRoomMulti = room;
//    }


    public ArrayList<String> getRemoteVideoIds() {
        return remoteVideoIds;
    }

    private ArrayList<String> remoteVideoIds = new ArrayList<String>();


    public static void initQavsdk(Context context) {
        mContext = context;
    }


//    private QavsdkControl(Context context) {
//        mAVContextControl = new AVContextControl(context);
//
//    }


    public void addRemoteVideoMembers(String id) {
        remoteVideoIds.add(id);
    }

    public void removeRemoteVideoMembers(String id) {
        if (remoteVideoIds.contains(id))
            remoteVideoIds.remove(id);
    }

    public void clearVideoMembers() {
        remoteVideoIds.clear();
    }







//    /**
//     * 创建房间
//     *
//     * @param relationId 讨论组号
//     */
//    public void enterRoom(int relationId, String roomRole, boolean isAutoCreateSDKRoom) {
//        if (mAVRoomControl != null) {
//            mAVRoomControl.enterRoom(relationId, roomRole, isAutoCreateSDKRoom);
//        }
//    }
//
//    public void setAudioCat(int audioCat) {
//        if (mAVRoomControl != null) {
//            mAVRoomControl.setAudioCat(audioCat);
//        }
//    }
//
//    /**
//     * 关闭房间
//     */
//    public int exitRoom() {
//        if (mAVRoomControl == null)
//            return AvConstants.DEMO_ERROR_NULL_POINTER;
//        return mAVRoomControl.exitRoom();
//    }
//
//    /**
//     * 获取成员列表
//     *
//     * @return 成员列表
//     */
//    public ArrayList<AvMemberInfo> getMemberList() {
//        if (mAVRoomControl == null) {
//            return null;
//        }
//        return mAVRoomControl.getMemberList();
//    }
//
//
//    public ArrayList<AvMemberInfo> getScreenMemberList() {
//        if (mAVRoomControl == null) {
//            return null;
//        }
//        return mAVRoomControl.getScreenMemberList();
//    }

//    public AVRoom getRoom() {
//        AVContext avContext = getAVContext();
//
//        return avContext != null ? avContext.getRoom() : null;
//    }



//    public void setCameraPreviewChangeCallback() {
//        mAVVideoControl.setCameraPreviewChangeCallback();
//    }
//
//
//    public boolean setIsInStopContext(boolean isInStopContext) {
//        if (mAVContextControl == null)
//            return false;
//
//        return mAVContextControl.setIsInStopContext(isInStopContext);
//    }
//
//    public boolean getIsInEnterRoom() {
//        if (mAVRoomControl == null)
//            return false;
//        return mAVRoomControl.getIsInEnterRoom();
//    }
//
//    public boolean getIsInCloseRoom() {
//        if (mAVRoomControl == null)
//            return false;
//        return mAVRoomControl.getIsInCloseRoom();
//    }

//    public AVContext getAVContext() {
//        if (mAVContextControl == null)
//            return null;
//        return mAVContextControl.getAVContext();
//    }




}