package by.equestriadev.nikishin_rostislav.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yandex.metrica.YandexMetrica;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.AppUtils;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.HomeGridAdapter;
import by.equestriadev.nikishin_rostislav.adapter.OnStartDragListener;
import by.equestriadev.nikishin_rostislav.adapter.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.adapter.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.adapter.holders.SimpleItemTouchHelperCallback;
import by.equestriadev.nikishin_rostislav.broadcast.AppReceiver;
import by.equestriadev.nikishin_rostislav.broadcast.ShortcutReceiver;
import by.equestriadev.nikishin_rostislav.model.AppShortcut;
import by.equestriadev.nikishin_rostislav.model.ApplicationInfo;
import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.AppStatistics;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;
import by.equestriadev.nikishin_rostislav.service.ShortcutService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Rostislav on 15.02.2018.
 */

public class GridFragment extends Fragment implements IUpdatable, OnStartDragListener {

    private static final int RQS_PICK_CONTACT = 1;

    @BindView(R.id.grid)
    RecyclerView mGrid;
    GridLayoutManager layoutManager;

    private ShortcutReceiver shortcutReceiver;
    private AppUtils mUtils;
    private AppDatabase mDb;
    private HomeGridAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private int kostil_position = -1;

    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_grid, container, false);
        ButterKnife.bind(this, v);
        // mGrid.setNestedScrollingEnabled(false);
        mDb = AppDatabase.getDatabase(getContext());
        mUtils = new AppUtils(getContext(), mDb);
        mAdapter = new HomeGridAdapter(getContext(), new HashMap<Integer, Shortcut>(), this);
        InitGrid();
        initAdapter(mAdapter);
        mGrid.setAdapter(mAdapter);
        update();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }


    @Override
    public void onStart() {
        super.onStart();
        InitReceiver();
    }

    public void InitGrid(){
        layoutManager = new GridLayoutManager(this.getContext(), 5);
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        mGrid.setLayoutManager(layoutManager);
        mGrid.addItemDecoration(new AppGridDecorator(spacingInPixels));
        mGrid.setHasFixedSize(true);
    }

    public void InitReceiver() {
        shortcutReceiver = new ShortcutReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(ShortcutService.BROADCAST_ACTION_ADD_SHORTCUT);
        intentFilter.addAction(ShortcutService.BROADCAST_ACTION_REMOVE_SHORTCUT);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(shortcutReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getContext().unregisterReceiver(this.shortcutReceiver);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    protected void initAdapter(final HomeGridAdapter adapter) {
        adapter.setOnBackgroundLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.add_shortcut_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_url:
                                callURLBuilder(position);
                                return true;
                            case R.id.add_contact:
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        ContactsContract.Contacts.CONTENT_URI);
                                kostil_position = position;
                                startActivityForResult(intent, RQS_PICK_CONTACT);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });
        adapter.setOnLongItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final Shortcut shortcut = mAdapter.getItemObject(position);
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.home_shortcut_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_from_home:
                                removeShortcut(shortcut);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popup.show();
            }
        });
        adapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final Shortcut shortcut = mAdapter.getItemObject(position);
                if(shortcut.getShortcutType() == ShortcutType.APPLICATION) {
                    YandexMetrica.reportEvent("Used app from home");
                    ApplicationInfo appInfo = ((AppShortcut) shortcut).getApplication();
                    mUtils.launchAppByApplicationInfo(appInfo);
                }
                else if(shortcut.getShortcutType() == ShortcutType.URL){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(shortcut.getUrl()));
                    startActivity(i);
                }
                else if(shortcut.getShortcutType() == ShortcutType.CONTACT){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                            String.valueOf(shortcut.getUrl()));
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mGrid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RQS_PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                if(contactData != null) {
                    Cursor cursor = getContext().getContentResolver().
                            query(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    Log.d(getClass().getName(), kostil_position + "");
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    final Shortcut shortcut = new Shortcut(
                            kostil_position,
                            id,
                            title,
                            ShortcutType.CONTACT
                    );
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDb.ShortcutModel().insertShortcuts(shortcut);
                            mUtils.callService(ShortcutService.BROADCAST_ACTION_ADD_SHORTCUT);
                            update();
                        }
                    }).start();
                    cursor.close();
                }

            }
        }
    }

    @Override
    public void update() {
        if (mAdapter != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<Integer, Shortcut> shortcuts = mUtils.getShortcuts();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setShortcutList(shortcuts);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void callURLBuilder(final int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Add URL shortcut");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(getContext());
        titleBox.setHint("Title");
        layout.addView(titleBox);

        final EditText urlBox = new EditText(getContext());
        urlBox.setHint("URL");
        layout.addView(urlBox);
        dialog.setView(layout);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!isEmpty(titleBox) && !isEmpty(urlBox)) {
                    final Shortcut shortcut = new Shortcut(position,
                            urlBox.getText().toString(),
                            titleBox.getText().toString(),
                            ShortcutType.URL);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDb.ShortcutModel().insertShortcuts(shortcut);
                            mUtils.callService(ShortcutService.BROADCAST_ACTION_ADD_SHORTCUT);
                        }
                    }).start();
                    dialog.cancel();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onEndDrag(final int fromPosition, final int toPosition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Shortcut a = mAdapter.getItemObject(fromPosition);
                Shortcut b = mAdapter.getItemObject(toPosition);
                if(a != null)
                    mDb.ShortcutModel().insertShortcuts(a);
                else
                    mDb.ShortcutModel().deleteShortcutByPosition(fromPosition);
                if(b != null)
                    mDb.ShortcutModel().insertShortcuts(b);
                else
                    mDb.ShortcutModel().deleteShortcutByPosition(toPosition);
            }
        }).start();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void removeShortcut(final Shortcut shortcut){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb.ShortcutModel().deleteShortcut(shortcut);
                mUtils.callService(ShortcutService.BROADCAST_ACTION_REMOVE_SHORTCUT);
                update();
            }
        }).start();
    }
}
