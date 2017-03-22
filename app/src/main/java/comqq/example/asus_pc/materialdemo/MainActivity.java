package comqq.example.asus_pc.materialdemo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements setphoto {
    private Button open;
    private fragment_shouye fragment_shouye;
    private DrawerLayout drawlayout;
    private fragment_blog fragment_blog;
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction transaction = fm.beginTransaction();
    private CircleImageView head_img;
    private NavigationView navigationView;
    private Fragment lastfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        for(int i=0;i<20;i++) {
//            arrayList.add("1");
//        }
//        arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,arrayList);
//        list= (ListView) findViewById(R.id.list);
//        list.setAdapter(arrayAdapter);
        initView();
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        if (fragment_shouye == null) {
            fragment_shouye = new fragment_shouye();
        }
        transaction.replace(R.id.fragment_main, fragment_shouye);
        transaction.commit();
        lastfragment = fragment_shouye;
    }

    private void initView() {
        open= (Button) findViewById(R.id.btn_open_navigation);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawlayout.openDrawer(GravityCompat.START);
            }
        });
        drawlayout = (DrawerLayout) findViewById(R.id.drawlayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_home:
                        setFragment(fragment_shouye);
                        break;
                    case R.id.navigation_item_blog:
                        if (fragment_blog == null) {
                            fragment_blog = new fragment_blog();
                            setFragment(fragment_blog);
                            fm = getSupportFragmentManager();
                            transaction = fm.beginTransaction();
                            transaction.hide(fragment_shouye);
                            transaction.add(R.id.fragment_main,fragment_blog);
                            transaction.commit();
                            drawlayout.closeDrawer(GravityCompat.START);
                        } else {
                            setFragment(fragment_blog);
                        }
                        break;
                    case R.id.navigation_item_about:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, activity_about.class);
                        startActivity(intent);
                        drawlayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });
    }

    public void gotophoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1004);
    }

    private void setFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.hide(lastfragment);
        transaction.show(fragment);
        transaction.commit();
        lastfragment = fragment;
        drawlayout.closeDrawer(GravityCompat.START);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1004) {
            Uri imgUri = data.getData();
            //拿到图片后先裁剪
            Intent intent = new Intent();
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(imgUri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1003);
        }
        if (requestCode == 1003 && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap image = bundle.getParcelable("data");
            fragment_shouye.setImageBitmap(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
