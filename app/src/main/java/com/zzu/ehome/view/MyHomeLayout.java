package com.zzu.ehome.view;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzu.ehome.R;
import com.zzu.ehome.activity.LoginActivity1;
import com.zzu.ehome.application.Constants;
import com.zzu.ehome.bean.RelationDes;
import com.zzu.ehome.bean.User;
import com.zzu.ehome.db.EHomeDao;
import com.zzu.ehome.db.EHomeDaoImpl;
import com.zzu.ehome.utils.CommonUtils;
import com.zzu.ehome.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import static com.zzu.ehome.R.id.iv_head;
import static com.zzu.ehome.R.layout.home_relation;

/**
 * Created by Mersens on 2016/10/21.
 */

public class MyHomeLayout extends LinearLayout {
    private LayoutParams params = null;
    private Context context;
    private List<View>  list=null;
    private static final int ANIMATION_TIME = 20;
    private boolean isMenuOpened;
    private int j=0;
    private int i=j;
    private EHomeDao dao;

    private ShowRunnable showRunnable=new ShowRunnable();
    private HideRunnable hideRunnable=new HideRunnable();
    private LayoutTransition layoutTransition;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<RelationDes> items;

    public RelationDes getItemOld() {
        return itemOld;
    }

    public void setItemOld(RelationDes itemOld) {
        this.itemOld = itemOld;
    }

    private RelationDes itemOld;

    public RelationDes getMainItem() {
        return mainItem;
    }

    public void setMainItem(RelationDes mainItem) {
        this.mainItem = mainItem;
    }

    private RelationDes mainItem;
    private View mainView;

    public MyHomeLayout(Context context) {
        this(context, null);
    }

    public MyHomeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }
    final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void init() {
        layoutTransition=new LayoutTransition();
        setLayoutTransition(layoutTransition);
        list=new ArrayList<>();
        inflater= LayoutInflater.from(context);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER;
        params.rightMargin=10;
        setGravity(Gravity.RIGHT);
        dao=new EHomeDaoImpl(context);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0F, 0F).
                setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING));
        layoutTransition.setAnimator(LayoutTransition.APPEARING, animator1);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0F, 0F).
                setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animator2);
    }

    public void addMainItem(final RelationDes item){
        mainView=inflater.inflate(R.layout.home_relation,null);
        ImageView imageView=(ImageView) mainView.findViewById(R.id.ivpeople);
        TextView tv_name=(TextView)mainView.findViewById(R.id.tvname);
        if (TextUtils.isEmpty(item.getUser_Icon())||item.getUser_Icon().contains("vine.gif")) {
            imageView.setImageResource(R.drawable.icon_user_tx1);
        }else {
            if(item.getUser_Icon().contains( Constants.JE_BASE_URL3 )){
                Glide.with(context).load(item.getUser_Icon()).into(imageView);
            }else {
                Glide.with(context).load(Constants.JE_BASE_URL3 + item.getUser_Icon().replace("~", "").replace("\\", "/")).into(imageView);
            }
        }
        tv_name.setText(item.getRelationship());
        mainView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuOpened) {
                    hidePromotedActions();
                    isMenuOpened = false;
                } else {
                    isMenuOpened = true;
                    showPromotedActions();
                }
            }
        });
        this.mainItem=item;
        LayoutParams mainparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mainparams.gravity=Gravity.CENTER;

        addView(mainView,mainparams);
        mainView.findViewById(R.id.ivpeople).setBackgroundResource(R.drawable.btn_home);
        mainView.findViewById(R.id.tvname).setVisibility(View.INVISIBLE);
    }

    public void addItem(List<RelationDes> items,OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
        this.items=items;
        for(int i=0;i<items.size();i++){

            View item=inflater.inflate(home_relation,null);
            ImageView imageView=(ImageView)item.findViewById(R.id.ivpeople);
            String url="";
            if(items.get(i).getRelationship().contains("添加亲人")){
                imageView.setImageResource(R.drawable.icon_file1);
                TextView tv_name = (TextView) item.findViewById(R.id.tvname);
                tv_name.setText(items.get(i).getRelationship());
            }else {
                if (TextUtils.isEmpty(items.get(i).getUser_Icon())||items.get(i).getUser_Icon().contains("vine.gif")) {
                    imageView.setImageResource(R.drawable.icon_user_tx1);
                } else {
                    if(items.get(i).getUser_Icon().contains(Constants.JE_BASE_URL3 )){
                        Glide.with(context).load(items.get(i).getUser_Icon()).into(imageView);
                    }else {
                        url = Constants.JE_BASE_URL3 + items.get(i).getUser_Icon().replace("~", "").replace("\\", "/");
                        Glide.with(context).load(url).into(imageView);
                    }
                }
                items.get(i).setUser_Icon(url);

                TextView tv_name = (TextView) item.findViewById(R.id.tvname);
                tv_name.setText(items.get(i).getRelationship());
            }
            item.setOnClickListener(new MyOnClicKListener(i));
            item.setVisibility(View.GONE);
            list.add(item);
            addView(item,params);
        }
    }

    private void hidePromotedActions() {
        if(mainView!=null) {
            mainView.findViewById(R.id.ivpeople).setBackgroundResource(R.drawable.btn_home);
            mainView.findViewById(R.id.tvname).setVisibility(View.INVISIBLE);
            invalidate();
            handler.postDelayed(hideRunnable, ANIMATION_TIME);
        }
    }

    private void showPromotedActions() {
        mainView.findViewById(R.id.ivpeople).setBackgroundColor(Color.TRANSPARENT);
        mainView.findViewById(R.id.tvname).setVisibility(View.VISIBLE);
        invalidate();
        handler.postDelayed(showRunnable,ANIMATION_TIME);
    }


    class  HideRunnable implements  Runnable{
        @Override
        public void run() {
            j++;
            if(j<=list.size()){
                list.get(j-1).setVisibility(View.GONE);
                handler.postDelayed(hideRunnable,ANIMATION_TIME);
            }else{
                handler.removeCallbacks(hideRunnable);
                j=0;
            }
        }
    }

    class  ShowRunnable implements  Runnable{
        @Override
        public void run() {
            i++;
            if(i<=list.size()){
                list.get(i-1).setVisibility(View.VISIBLE);
                handler.postDelayed(showRunnable,ANIMATION_TIME);
            }else{
                handler.removeCallbacks(showRunnable);
                i=0;
            }
        }
    }

    public class MyOnClicKListener implements OnClickListener{
        private int pos;
        public MyOnClicKListener(int pos){
            this.pos=pos;
        }
        @Override
        public void onClick(View view) {
            if(CommonUtils.isFastClick()){
                return;
            }

            if(items.get(pos).getRelationship().contains("添加亲人")){
                Intent i = new Intent(context, LoginActivity1.class);
                i.putExtra("relation", "rela");
                context.startActivity(i);
                return;
            }



            onItemClickListener.onItemClick(pos);
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(int pos);
    }
    public void changePostion(int postion){

        RelationDes tmp=new RelationDes();
        tmp.setUser_Icon(mainItem.getUser_Icon());
        tmp.setRelationship(mainItem.getRelationship());
        tmp.setUser_FullName(mainItem.getUser_FullName());
        tmp.setRUserID(mainItem.getRUserID());
        tmp.setUser_Name(mainItem.getUser_Name());
        itemOld=tmp;

        mainItem.setUser_Icon(items.get(postion).getUser_Icon());
        mainItem.setRelationship(items.get(postion).getRelationship());
        mainItem.setUser_FullName(items.get(postion).getUser_FullName());
        mainItem.setRUserID(items.get(postion).getRUserID());
        mainItem.setUser_Name(items.get(postion).getUser_Name());

        items.get(postion).setUser_Icon(itemOld.getUser_Icon());
        items.get(postion).setRelationship(itemOld.getRelationship());
        items.get(postion).setUser_FullName(itemOld.getUser_FullName());
        items.get(postion).setRUserID(itemOld.getRUserID());
        items.get(postion).setUser_Name(itemOld.getUser_Name());
        ImageView imageView=(ImageView) mainView.findViewById(R.id.ivpeople);
        TextView tv_name=(TextView)mainView.findViewById(R.id.tvname);
        if(TextUtils.isEmpty(mainItem.getUser_Icon())){
            imageView.setImageResource(R.mipmap.pic_photo2);
        }else {
            Glide.with(context).load(mainItem.getUser_Icon()).into(imageView);
        }
        tv_name.setText(mainItem.getRelationship());

        View otherView=list.get(postion);
        ImageView imageViewOther=(ImageView)otherView.findViewById(R.id.ivpeople);
        if(tmp.getUser_Icon()!=null) {
            if (TextUtils.isEmpty(itemOld.getUser_Icon().replace("~", "").replace("\\", "/"))) {
                imageViewOther.setImageResource(R.mipmap.pic_photo2);
            } else {
                Glide.with(context).load(itemOld.getUser_Icon()).into(imageViewOther);
            }
        }else{
            imageViewOther.setImageResource(R.mipmap.pic_photo2);
        }

        TextView tv_nameOther=(TextView)otherView.findViewById(R.id.tvname);
        tv_nameOther.setText(itemOld.getRelationship());
        String orders="";
        for(int i=0;i<items.size()-1;i++){

            if(i==items.size()-2){
                orders+=items.get(i).getRUserID();
            }else{
                orders+=items.get(i).getRUserID()+",";
            }

        }
        User dbUser=dao.findUserInfoById(SharePreferenceUtil.getInstance(context).getHomeId());
        dbUser.setOrder(orders);
        dao.updateUserInfo(dbUser,SharePreferenceUtil.getInstance(context).getHomeId());
        invalidate();
        if (isMenuOpened) {
            hidePromotedActions();
            isMenuOpened = false;
        } else {
            isMenuOpened = true;
            showPromotedActions();
        }
    }
    public void hide(){

        hidePromotedActions();
        isMenuOpened = false;
    }

}
