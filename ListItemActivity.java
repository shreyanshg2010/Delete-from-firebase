package co.shrey.fireapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;



public class ListItemActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "ListItemActivity";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private DatabaseReference rootref = database.getReference();
    private DatabaseReference childref = rootref.getRef();
    List<UserInformation> list;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle mBundleRecyclerViewState;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager mLayoutmanager;
    DividerItemDecoration dividerItemDecoration;
    Adapter madapter;
    UserInformation userInfo;
    TextView name_tv,address_tv,age_tv;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_layout);
         name_tv = (TextView) findViewById(R.id.name);
         address_tv = (TextView) findViewById(R.id.address);
         age_tv = (TextView) findViewById(R.id.age);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getdata();
    }
    protected void getdata() {
        list =new ArrayList<>();
        childref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                /*uinfo=new UserInformation();*/
                    userInfo = data.getValue(UserInformation.class);
                    userInfo.setKey(data.getKey());
                    /*Log.e("TAG", userInfo.getAddress() + userInfo.getAge() + userInfo.getName());*/
                    list.add(userInfo);
                }
              //  setupList();
                madapter = new Adapter(ListItemActivity.this,list);
                mLayoutmanager = new LinearLayoutManager(ListItemActivity.this);
                dividerItemDecoration = new DividerItemDecoration(ListItemActivity.this, mLayoutmanager.getOrientation());
                recyclerView.setLayoutManager(mLayoutmanager);
                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.setAdapter(madapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //for search button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
      SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
     searchView.setOnQueryTextListener(this);
        return true;
    }
    //to recover the state when we press back in element from the list
   @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        // restore RecyclerView state
      /*  if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            try{
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);}
            catch(Exception e){


            }
        }*/
        if (mBundleRecyclerViewState != null && recyclerView != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }
//searching an element from the list
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<UserInformation> newList = new ArrayList<>();
        madapter.setfilter(newText);
        return true;
    }
/*    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.action_android){
                return true;
        }
                return super.onOptionsItemSelected(item);


    }*/
/*      private void updateui() {
        address_tv.setText(userInfo.getAddress());
        age_tv.setText(userInfo.getAge());
        name_tv.setText(userInfo.getName());
    }*/
}
