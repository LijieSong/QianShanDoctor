package com.example.bjlz.qianshandoctor.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.adapter.GradViewAdapter;
import com.example.bjlz.qianshandoctor.adapter.MenuAdapter;
import com.example.bjlz.qianshandoctor.application.MyApplication;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.LogUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.PreferencesUtils;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ToastUtil;
import com.example.bjlz.qianshandoctor.utils.OtherTools.ScreenUtils;
import com.example.bjlz.qianshandoctor.utils.volley.ImageLoaderUtil;
import com.example.bjlz.qianshandoctor.views.TitleBarView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jtech.view.RecyclerHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：qianshandoctor
 * 类描述：MainActivity :主界面
 * 创建人：slj
 * 创建时间：2016-5-28 17:24
 * 修改人：slj
 * 修改时间：2016-5-28 17:24
 * 修改备注：
 * 邮箱:slj@bjlingzhuo.com
 */
public class MainActivity extends BaseActivity implements View.OnTouchListener, AdapterView.OnItemClickListener {
    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;

    /**
     * menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
     */
    private int leftEdge;

    /**
     * menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
     */
    private int rightEdge = 0;

    /**
     * menu完全显示时，留给content的宽度值。
     */
    private int menuPadding = 150;

    /**
     * 主内容的布局。
     */
    private View content;

    /**
     * menu的布局。
     */
    private View menu;

    /**
     * menu布局的参数，通过此参数来更改leftMargin的值。
     */
    private LinearLayout.LayoutParams menuParams;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
     */
    private boolean isMenuVisible;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    private ListView recyclerView;
    private GridView grid_button;
    private boolean isLogin = false;//记录是否登录
    private long exitTime = 0;//记录点击时间
    private TitleBarView title_bar;//标题
    private String userName;//用户名
    private Button btn_login, btn_shou_ye, btn_ji_yin_jian_ce, btn_user_center;
    private Context mContext;
    private TextView text_nicheng, text_ni_cheng, text_guan_zhu, text_shou_yi, text_ping_lun;
    private ImageView text3;
    private ImageView circle_image_tou_xiang;
    private MenuAdapter adapter = null;//侧滑菜单的布局适配器
    private GradViewAdapter gradradadapter;//主界面布局条目适配器
    //侧滑条目的item
    private int[] button_image = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo};
    private String[] button_name = {"我的团队", "绩效考核", "随访平台", "预约管理", "消息","关于"};
    //主界面的item
    private int[] gride_image = {R.drawable.logo, R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo, R.drawable.logo};
    private String[] gride_name = {"用户管理", "预约管理", "健康全信息", "健康私人报告", "远程问诊", "随访平台"};
    private String imgUrl = "http://pics.sc.chinaz.com/Files/pic/icons128/6201/2r.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        getIsLogin();//获取登录状态
        getData();
        initView();
        initData();
        setOnClick();
    }

    private void getIsLogin() {
        isLogin = PreferencesUtils.getBoolean(getApplicationContext(), "isLogin", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIsLogin();
        userName = PreferencesUtils.getUserId(getApplicationContext());
        if (isLogin == true) {
            text3.setVisibility(View.VISIBLE);
//            this.title_bar.setTitleText(userName);
            text_nicheng.setText(userName);
            text_ni_cheng.setText(userName);
            this.btn_login.setText(R.string.exit);
        }
    }

    @Override
    public void initView() {
        this.title_bar = (TitleBarView) findViewById(R.id.title_bar);
        this.title_bar.setTitleText(R.string.app_name);
        this.title_bar.setLeftBtnIcon(R.drawable.menu);
//        RoundImageView imageView = this.title_bar.getImageTitle();
//        this.title_bar.setImageTitleShow();
//        ImageLoaderUtil.setImageLoader(imgUrl, imageView,R.drawable.logo,R.drawable.menu);
//        this.title_bar.getView().setBackgroundColor(CommonUtil.getColor(R.color.backColor));

        recyclerView = (ListView) findViewById(R.id.jrecyclerview);
        grid_button = (GridView) findViewById(R.id.grid_button);

        this.btn_login = (Button) findViewById(R.id.btn_login);
        btn_shou_ye = (Button) findViewById(R.id.btn_shou_ye);
        btn_ji_yin_jian_ce = (Button) findViewById(R.id.btn_ji_yin_jian_ce);
        btn_user_center = (Button) findViewById(R.id.btn_user_center);

        text_nicheng = (TextView) findViewById(R.id.text_nicheng);
        text_ni_cheng = (TextView) findViewById(R.id.text_ni_cheng);
        text_guan_zhu = (TextView) findViewById(R.id.text_guan_zhu);
        text_shou_yi = (TextView) findViewById(R.id.text_shou_yi);
        text_ping_lun = (TextView) findViewById(R.id.text_ping_lun);

        text3 = (ImageView) findViewById(R.id.image_tou_xiang);
        circle_image_tou_xiang = (ImageView) findViewById(R.id.circle_image_tou_xiang);
        ImageLoaderUtil.setImageLoader(imgUrl, circle_image_tou_xiang,R.drawable.logo,R.drawable.menu);
        ImageLoaderUtil.setImageLoader(imgUrl, text3,R.drawable.logo,R.drawable.menu);
        content = findViewById(R.id.content);
        menu = findViewById(R.id.menu);
    }

    @Override
    public void initData() {
//        this.title_bar.setTitleText(userName);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        // 将menu的宽度设置为屏幕宽度减去menuPadding
        menuParams.width = screenWidth - menuPadding;
        // 左边缘的值赋值为menu宽度的负数
        leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度
        content.getLayoutParams().width = screenWidth;
        /**
         * 侧滑条目的适配器
         */
        List<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < button_image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", button_image[i]);
            map.put("text", button_name[i]);
            menuList.add(map);
        }
        if (isLogin == true) {
            adapter = new MenuAdapter(mContext, menuList);
            recyclerView.setAdapter(adapter);
        }
        /**
         * 主页面的适配器
         */

        List<HashMap<String, Object>> gradeViewList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < gride_image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", gride_image[i]);
            map.put("text", gride_name[i]);
            gradeViewList.add(map);
        }
        if (isLogin == true) {
            gradradadapter = new GradViewAdapter(mContext, gradeViewList);
            grid_button.setAdapter(gradradadapter);
        }
    }

    @Override
    public void getData() {
        screenWidth = ScreenUtils.getScreenWidth(MainActivity.this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userName = bundle.getString("userName");
        }
    }

    @Override
    public void setOnClick() {
        recyclerView.setOnItemClickListener(this);//侧滑菜单条目点击事件
        content.setOnTouchListener(this);//主界面触摸监听
        grid_button.setOnItemClickListener(this);//主界面grid监听
        btn_shou_ye.setOnClickListener(this);//首页按钮监听
        btn_ji_yin_jian_ce.setOnClickListener(this);//基因检测按钮监听
        btn_user_center.setOnClickListener(this);//用户中心按钮监听
        //用户信息的点击
        text_ni_cheng.setOnClickListener(this);
        text_nicheng.setOnClickListener(this);
        text_guan_zhu.setOnClickListener(this);
        text_shou_yi.setOnClickListener(this);
        text_ping_lun.setOnClickListener(this);
        //头像的点击事件
        text3.setOnClickListener(this);
        circle_image_tou_xiang.setOnClickListener(this);
        // 登录按钮的点击事件
        this.btn_login.setOnClickListener(this);
        //菜单的点击事件
        this.title_bar.setLeftBtnOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuVisible != true) {
                    scrollToMenu();
                } else {
                    scrollToContent();
                }
            }
        });
    }

    @Override
    public void WeightOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                PreferencesUtils.clear(getApplicationContext());

                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        LogUtils.error("退出成功");
                        MyApplication.getInstance().close();
                        System.exit(0);
                    }

                    @Override
                    public void onError(int i, String s) {
                        MyApplication.getInstance().close();
                        System.exit(0);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
            case R.id.text_nicheng:
            case R.id.text_ni_cheng:
            case R.id.image_tou_xiang:
            case R.id.btn_user_center:
            case R.id.circle_image_tou_xiang:
                MyApplication.startActivity(MainActivity.this, InformationActivity.class);
                break;
            case R.id.btn_shou_ye:
                MyApplication.startActivity(MainActivity.this, MainActivity.class);
                break;
            case R.id.btn_ji_yin_jian_ce:
                MyApplication.startActivity(MainActivity.this, TextActivity.class);
//                ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "基因检测被点击", CommonUtil.getColor(R.color.transparent), 16f, CommonUtil.getColor(R.color.black));
//                MyApplication.startActivity(MainActivity.this, CameraTestActivity.class);

//                Z5egjNSxyGU3E2gfBz_iJyBBePg8x-x8
//                    ScreenUtils.joinQQGroup(MainActivity.this, "Z5egjNSxyGU3E2gfBz_iJyBBePg8x-x8");
                break;
            case R.id.text_guan_zhu:
                ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "关注被点击", CommonUtil.getColor(R.color.transparent), 16f,CommonUtil.getColor(R.color.black));

//                MyApplication.startActivity(MainActivity.this, InformationActivity.class);
                break;
            case R.id.text_shou_yi:
                ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "收益被点击", CommonUtil.getColor(R.color.transparent), 16f, CommonUtil.getColor(R.color.black));

//                MyApplication.startActivity(MainActivity.this, InformationActivity.class);
                break;
            case R.id.text_ping_lun:
                ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, "评论被点击", getResources().getColor(R.color.transparent), 16f, getResources().getColor(R.color.black));

//                MyApplication.startActivity(MainActivity.this, InformationActivity.class);
                break;
        }
    }

    @Override
    public void onRecycleViewItemClick(RecyclerHolder holder, View view, int position) {

    }

    @Override
    public boolean onRecycleItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * 按2次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出的判断
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
//            ToastUtil.shortDiyToast(MainActivity.this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
//            EMClient.getInstance().logout(true, new EMCallBack() {
//                @Override
//                public void onSuccess() {
                    LogUtils.error("退出成功");
//            EMClient.getInstance().logout(true);
                    MyApplication.getInstance().close();
                    System.exit(0);
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    MyApplication.getInstance().close();
//                    System.exit(0);
//                }
//
//                @Override
//                public void onProgress(int i, String s) {
//
//                }
//            });

        }
    }
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                content.setFocusableInTouchMode(true);
                menu.setFocusableInTouchMode(true);
                if (isMenuVisible) {
                    menuParams.leftMargin = distanceX;
                } else {
                    menuParams.leftMargin = leftEdge + distanceX;
                }
                if (menuParams.leftMargin < leftEdge) {
                    menuParams.leftMargin = leftEdge;
                } else if (menuParams.leftMargin > rightEdge) {
                    menuParams.leftMargin = rightEdge;
                }
                menu.setLayoutParams(menuParams);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
                xUp = event.getRawX();
                if (wantToShowMenu()) {
                    if (shouldScrollToMenu()) {
                        scrollToMenu();
                    } else {
                        scrollToContent();
                    }
                } else if (wantToShowContent()) {
                    if (shouldScrollToContent()) {
                        scrollToContent();
                    } else {
                        scrollToMenu();
                    }
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
     *
     * @return 当前手势想显示content返回true，否则返回false。
     */
    private boolean wantToShowContent() {
        return xUp - xDown < 0 && isMenuVisible;
    }

    /**
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
     *
     * @return 当前手势想显示menu返回true，否则返回false。
     */
    private boolean wantToShowMenu() {
        return xUp - xDown > 0 && !isMenuVisible;
    }

    /**
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将menu展示出来。
     *
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToMenu() {
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
     *
     * @return 如果应该滚动将content展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为50.
     */
    private void scrollToMenu() {
        new ScrollTask().execute(50);
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-50.
     */
    private void scrollToContent() {
        new ScrollTask().execute(-50);
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event content界面的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isMenuVisible != true) {
            switch(gride_name[position]){
                case "用户管理":
                    MyApplication.startActivity(MainActivity.this,UserManagerActivity.class);
                break;
                case "预约管理":
//                    MyApplication.startActivity(MainActivity.this,UserManagerActivity.class);
                    break;
                case "健康全信息":
                    MyApplication.startActivity(MainActivity.this,HealthMessageActivity.class);
                    break;
                case "健康私人报告":
                    MyApplication.startActivity(MainActivity.this, HealthReportActivity.class);
                    break;
                case "远程问诊":
                    MyApplication.startActivity(MainActivity.this,RemoteInterrogationActivity.class);
                    break;
                case "随访平台":
//                    MyApplication.startActivity(MainActivity.this,UserManagerActivity.class);
                    break;

                default:
                    ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext,"不存在的快捷方式", CommonUtil.getColor(R.color.transparent), 16f, CommonUtil.getColor(R.color.black));
                    break;
            }
        } else {
            switch (button_name[position]){
                case "关于":
                    MyApplication.startActivity(MainActivity.this,SettingActivity.class);
                    break;
                default:
                    ToastUtil.shortDiyToastColorBgTextSizeTextColor(mContext, button_name[position], CommonUtil.getColor(R.color.transparent), 16f, CommonUtil.getColor(R.color.black));
                    break;
            }
        }
    }
   private String MIUI = "Xiaomi";
    private String FLYME = "Meizu";
    private String HUAWEI = "HUAWEI";
    private String QIHU = "360";
    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = menuParams.leftMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
                String brand = CommonUtil.getBuildBrand();
                if (brand!=null){
                    if (brand.equals(FLYME) || brand.equals(MIUI)){
                        sleep(0);
                    }else{
                        sleep(20);
                    }
                }else{
                    sleep(20);
                }
            }
            if (speed[0] > 0) {
                isMenuVisible = true;
            } else {
                isMenuVisible = false;
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            menuParams.leftMargin = leftMargin[0];
            menu.setLayoutParams(menuParams);
        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            menuParams.leftMargin = leftMargin;
            menu.setLayoutParams(menuParams);
        }
    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
