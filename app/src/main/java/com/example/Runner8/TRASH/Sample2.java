package com.example.Runner8.TRASH;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Runner8.R;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public class Sample2 extends AppCompatActivity {

    ContextMenuDialogFragment contextMenuDialogFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        toolbar = findViewById(R.id.toolbar);

        initToolbar();
        initMenuFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.icn_close);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void initMenuFragment() {

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);        // permet de fermer le menu en cliquant sur une zone sans bouton
        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);

        contextMenuDialogFragment.setMenuItemClickListener(new Function2<View, Integer, Unit>() {
            @Override
            public Unit invoke(View view, Integer integer) {
                Toast.makeText(
                        getApplicationContext(),
                "Clicked on position: $position",
                        Toast.LENGTH_SHORT
                ).show();
                return null;
            }
        });



    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResourceValue(R.drawable.icn_close);


        MenuObject send = new MenuObject("Send message");
        send.setResourceValue(R.drawable.icn_1);

        MenuObject up = new MenuObject("Up button");
        up.setResourceValue(R.drawable.icn_2);

        MenuObject addFriend = new MenuObject("Add to friends");
        addFriend.setResourceValue(R.drawable.icn_3);

        MenuObject like = new MenuObject("Like you");
        like.setResourceValue(R.drawable.icn_4);

        MenuObject block = new MenuObject("Block user");
        block.setResourceValue(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(up);
        menuObjects.add(addFriend);
        menuObjects.add(like);
        menuObjects.add(block);

        return menuObjects;
    }

    private final void showContextMenuDialogFragment() {
        if (this.getSupportFragmentManager().findFragmentByTag("ContextMenuDialogFragment") == null) {
            ContextMenuDialogFragment var10000 = this.contextMenuDialogFragment;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("contextMenuDialogFragment");
            }

            var10000.show(this.getSupportFragmentManager(), "ContextMenuDialogFragment");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.context_menu)
            showContextMenuDialogFragment();

        return super.onOptionsItemSelected(item);
    }
}