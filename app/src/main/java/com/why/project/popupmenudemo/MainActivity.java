package com.why.project.popupmenudemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initToolBar();//初始化toolbar
	}

	private void initToolBar() {
		mToolbar = findViewById(R.id.toolbar_base);
		mToolbar.setTitle("");//这样设置的话，自带的标题就不会显示
		//设置自定义的标题（居中）
		TextView toolBarTitle = mToolbar.findViewById(R.id.toolbarTitle);
		toolBarTitle.setText("标题");
		setSupportActionBar(mToolbar);//由于toolbar只是一个普通控件，我们将ToolBar设置为ActionBar
		//设置导航图标要在setSupportActionBar方法之后
		//mToolbar.setNavigationIcon(null);//设置为空的话，就会不显示左侧的图标
		//对NavigationIcon添加点击
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//添加menu 菜单点击事件
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()){
					case R.id.action_more:
						//showPopupMenu(item.getActionView());//这样写报错：  java.lang.IllegalStateException: MenuPopupHelper cannot be used without an anchor
						showPopupMenu(findViewById(R.id.action_more));
						break;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_one_menu, menu);//toolbar添加menu菜单
		return true;
	}

	@SuppressLint("RestrictedApi")
	private void showPopupMenu(View ancherView){
		//创建弹出式菜单对象（最低版本11）
		PopupMenu popupMenu = new PopupMenu(this, ancherView);//第二个参数是绑定的那个view
		//获取菜单填充器
		MenuInflater inflater = popupMenu.getMenuInflater();
		//填充菜单
		inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
		//使用反射，强制显示菜单图标
		try {
			Field field = popupMenu.getClass().getDeclaredField("mPopup");
			field.setAccessible(true);
			MenuPopupHelper mPopup = (MenuPopupHelper) field.get(popupMenu);
			mPopup.setForceShowIcon(true);
		} catch (Exception e) {
		}
		//绑定菜单项的点击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_share:
						Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
						break;

					case R.id.action_publish:
						Toast.makeText(MainActivity.this, "发布", Toast.LENGTH_SHORT).show();
						break;
				}
				return true;
			}
		});
		//显示(这一行代码不要忘记了)
		popupMenu.show();
	}
}
