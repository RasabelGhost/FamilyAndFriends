package com.family.ghost.fam.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.family.ghost.fam.Profile.ProfileActivity;
import com.family.ghost.fam.Share.ShareActivity;
import com.family.ghost.fam.Util.MainfeedListAdapter;
import com.family.ghost.fam.about.AboutActivity;
import com.family.ghost.fam.adapter.FragmentAdapter;
import com.family.ghost.fam.cal.MainActivity;
import com.family.ghost.fam.map.MapActivity;
import com.family.ghost.fam.notification.NotificationActivity;
import com.family.ghost.fam.singleActivitys.AllMembersActivity;
import com.family.ghost.fam.singleActivitys.CalendarActivity;
import com.family.ghost.fam.singleActivitys.PayActivity;
import com.family.ghost.fam.singleActivitys.RandomActivity;
import com.family.ghost.fam.singleActivitys.RuleActivity;
import com.family.ghost.fam.singleActivitys.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.family.ghost.fam.Login.LoginActivity;
import com.family.ghost.fam.R;
import com.family.ghost.fam.Util.BottomNavigationViewHelper;
import com.family.ghost.fam.Util.FirebaseMethods;
import com.family.ghost.fam.Util.SectionsPagerAdapter;
import com.family.ghost.fam.Util.UniversalImageLoader;
import com.family.ghost.fam.Util.ViewCommentsFragment;
import com.family.ghost.fam.models.Photo;
import com.family.ghost.fam.opengl.AddToStoryDialog;
import com.family.ghost.fam.opengl.NewStoryActivity;
// for nav menu

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements
        MainfeedListAdapter.OnLoadMoreItemsListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{



    private DrawerLayout drawer;
    private FloatingActionButton fab;
    //private TabLayout mTabLayout;
    //private ViewPager mViewPager;
    private RelativeLayout relative_main;
    private ImageView img_page_start;

    //this for email view
    private AppCompatTextView textViewEmailnav;

    private AppCompatActivity activity = HomeActivity.this;

    private static boolean isShowPageStart = true;
    private final int MESSAGE_SHOW_DRAWER_LAYOUT = 0x001;
    private final int MESSAGE_SHOW_START_PAGE = 0x002;



    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_DRAWER_LAYOUT:
                    drawer.openDrawer(GravityCompat.START);
                    SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isFirst", false);
                    editor.apply();
                    break;

                case MESSAGE_SHOW_START_PAGE:
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setDuration(300);
                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            relative_main.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    relative_main.startAnimation(alphaAnimation);
                    break;
            }
        }
    };












    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");
        HomeFragment fragment = (HomeFragment)getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + mViewPager.getCurrentItem());
        if(fragment != null){
            fragment.displayMorePhotos();
        }
    }

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;
    private static final int RESULT_ADD_NEW_STORY = 7891;
    private final static int CAMERA_RQ = 6969;
    private static final int REQUEST_ADD_NEW_STORY = 8719;

    private Context mContext = HomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);

        setupFirebaseAuth();

        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();

        // nav menu
        initView();
        initViewPager();
        initObjects();

        // init the preferences data of Settings
        try {
            PreferenceManager.setDefaultValues(this, R.xml.preferences_settings, false);
        } catch (Exception e) {
        }


//        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
//
//        if (isShowPageStart) {
//            relative_main.setVisibility(View.VISIBLE);
//            Glide.with(HomeActivity.this).load(R.drawable.ic_launcher_big).into(img_page_start);
//            if (sharedPreferences.getBoolean("isFirst", true)) {
//                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 2000);
//            } else {
//                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 1000);
//            }
//            isShowPageStart = false;
//        }
//
//        if (sharedPreferences.getBoolean("isFirst", true)) {
//            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_DRAWER_LAYOUT, 2500);
//        }

    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(this);

//        relative_main = findViewById(R.id.relative_main);
//        img_page_start = findViewById(R.id.img_page_start);


        // this is for view email of user from Db

        textViewEmailnav = (AppCompatTextView) findViewById(R.id.text_nav_header);

    }

    private void initObjects() {
//        listUsers = new ArrayList<>();
//        databaseHelper = new DatabaseHelper(activity);
//
//        //      String emailFromIntent = getIntent().getStringExtra("EMAIL");
////        textViewEmailnav.setText(emailFromIntent);
//
//        getDataFromSQLite();
    }


    private void initViewPager() {
        //mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_title_main_1));

        List<Fragment> fragments = new ArrayList<>();
       // fragments.add(new CardsFragment());

        mViewPager.setOffscreenPageLimit(2);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapter);

        mViewPager.addOnPageChangeListener(pageChangeListener);
    }


    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 2) {
                fab.show();
            } else {
                fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_header:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.fab_main:
                Snackbar.make(view, getString(R.string.main_snack_bar), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.main_snack_bar_action), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_main_1:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.add_new_post:
                intent.setClass(this, ShareActivity.class);
                startActivity(intent);
                break;

//            case R.id.nav_recycler_and_swipe_refresh:
//                intent.setClass(this, NotificationActivity.class);
//                startActivity(intent);
//                break;

            case R.id.nav_calendar:
                intent.setClass(this, CalendarActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_remender:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_map:
                intent.setClass(this, MapActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_random:
                intent.setClass(this, RandomActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_rule:
                intent.setClass(this, RuleActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_settings:
               // intent.setClass(this, SettingsActivity.class);
                //startActivity(intent);
                break;

            case R.id.nav_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_pay:
                intent.setClass(this, PayActivity.class);
                startActivity(intent);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }










    public void openNewStoryActivity(){
        Intent intent = new Intent(this, NewStoryActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NEW_STORY);
    }

    public void showAddToStoryDialog(){
        Log.d(TAG, "showAddToStoryDialog: showing add to story dialog.");
        AddToStoryDialog dialog = new AddToStoryDialog();
        dialog.show(getFragmentManager(), getString(R.string.dialog_add_to_story));
    }


    public void onCommentThreadSelected(Photo photo, String callingActivity){
        Log.d(TAG, "onCommentThreadSelected: selected a coemment thread");

        ViewCommentsFragment fragment  = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putString(getString(R.string.home_activity), getString(R.string.home_activity));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();

    }

    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }


    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mFrameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: incoming result.");
        // Received recording or error from MaterialCamera

        if (requestCode == REQUEST_ADD_NEW_STORY) {
            Log.d(TAG, "onActivityResult: incoming new story.");
            if (resultCode == RESULT_ADD_NEW_STORY) {
                Log.d(TAG, "onActivityResult: got the new story.");
                Log.d(TAG, "onActivityResult: data type: " + data.getType());

                final HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + 1);
                if (fragment != null) {

                    FirebaseMethods firebaseMethods = new FirebaseMethods(this);
                    firebaseMethods.uploadNewStory(data, fragment);

                }
                else{
                    Log.d(TAG, "onActivityResult: could not communicate with home fragment.");
                }



            }
        }
    }


    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new CameraFragment()); //index 0
        adapter.addFragment(new HomeFragment()); //index 1
       // adapter.addFragment(new MessagesFragment()); //index 2
        mViewPager.setAdapter(adapter);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);

        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
      //  tabLayout.getTabAt(0).setIcon(R.drawable.ic_instagram_black);
      //  tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }


    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


     /*
    ------------------------------------ Firebase ---------------------------------------------
     */


     private void checkCurrentUser(FirebaseUser user){
         Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

         if(user == null){
             Intent intent = new Intent(mContext, LoginActivity.class);
             startActivity(intent);
         }
     }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mViewPager.setCurrentItem(HOME_FRAGMENT);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


}
