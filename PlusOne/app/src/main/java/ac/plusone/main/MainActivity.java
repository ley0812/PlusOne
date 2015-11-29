package ac.plusone.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pkmmte.view.CircularImageView;

import java.io.InputStream;
import java.util.ArrayList;

import ac.plusone.R;
import ac.plusone.guide.GuideActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int PROFILE_PIC_SIZE = 1500;
    private final int TIME_DELAY = 2000;
    private long back_pressed;

    private TextView board_more;
    private ArrayList<BoardVO> mainBoardList;

    private Button mapBtn;
    private ImageView btn_guide;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private CircularImageView profileImg2;
    private TextView profileName, profileEmail;
    private MenuItem signOut, write, revoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.inflateHeaderView(R.layout.br_nav_header_main);
        Menu navMenu = navigationView.getMenu();

        signInButton = (SignInButton)headerView.findViewById(R.id.btn_sign_in);
        profileImg2 = (CircularImageView)headerView.findViewById(R.id.profile_image);
        profileName = (TextView) headerView.findViewById(R.id.profile_name);
        profileEmail = (TextView) headerView.findViewById(R.id.profile_email);
        signOut = navMenu.getItem(0);
        write = navMenu.getItem(1);
        revoke = navMenu.getItem(2);

        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String userID = userPref.getString("currentUser", "");
        if(!userID.equals("")) {
            updateUI(true);
            profileName.setText(userPref.getString("currentUserName", getString(R.string.please_login)));
            profileEmail.setText(userPref.getString("currentUserEmail", ""));
            new LoadProfileImage().execute(userPref.getString("currentUserPhoto", "a"));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d("###", "connection Fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        setMainBoard();

        board_more = (TextView) findViewById(R.id.board);
        board_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });
        mapBtn = (Button)findViewById(R.id.mapbtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        btn_guide = (ImageView) findViewById(R.id.btn_guide);
        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = null;
        if (result.isSuccess()) {

            acct = result.getSignInAccount();
            profileName.setText(acct.getDisplayName());
            profileEmail.setText(acct.getEmail());
            new LoadProfileImage().execute(acct.getPhotoUrl().toString());

            updateUI(true);

            SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = userPref.edit();
            editor.putString("currentUser", acct.getId());
            editor.putString("currentUserName", acct.getDisplayName());
            editor.putString("currentUserPhoto", acct.getPhotoUrl().toString());
            editor.putString("currentUserEmail", acct.getEmail().toString());
            editor.commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            updateUI(false);
        }
    }

    void updateUI(boolean signIn) {
        if(signIn) {
            signInButton.setVisibility(View.GONE);
            profileImg2.setVisibility(View.VISIBLE);
            signOut.setVisible(true);
            write.setVisible(true);
            revoke.setVisible(true);
        }else {
            signInButton.setVisibility(View.GONE);
            profileName.setText(getString(R.string.please_login));
            profileEmail.setText("");
            profileImg2.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            signOut.setVisible(false);
            write.setVisible(false);
            revoke.setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case R.id.plusone_sign_out:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                userPref.edit().clear().commit();
                                updateUI(false);
                            }
                        });
                Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                break;

            case R.id.plusone_write:
                startActivity(new Intent(this, BoardWriteActivity.class));
                break;

            case R.id.plusone_revoke:
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                updateUI(false);
                            }
                        });

                Toast.makeText(MainActivity.this, "Revoke Access.", Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String personPhotoUrl = urls[0].substring(0, urls[0].toString().length() - 2) + PROFILE_PIC_SIZE;
            Bitmap profileImage = null;
            try {
                InputStream in = new java.net.URL(personPhotoUrl).openStream();
                profileImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return profileImage;
        }
        protected void onPostExecute(Bitmap result) {
            profileImg2.setImageBitmap(result);
        }
    }




    public void setMainBoard() {
        RelativeLayout layout1, layout2, layout3, layout4, layout5;
        TextView board1, board11, board2, board22, board3, board33, board4, board44, board5, board55;

        layout1 = (RelativeLayout)findViewById(R.id.main_relative_layout1);
        layout2 = (RelativeLayout)findViewById(R.id.main_relative_layout2);
        layout3 = (RelativeLayout)findViewById(R.id.main_relative_layout3);
        layout4 = (RelativeLayout)findViewById(R.id.main_relative_layout4);
        layout5 = (RelativeLayout)findViewById(R.id.main_relative_layout5);

        board1 = (TextView)findViewById(R.id.board_main1);
        board11 = (TextView)findViewById(R.id.board_main11);
        board2 = (TextView)findViewById(R.id.board_main2);
        board22 = (TextView)findViewById(R.id.board_main22);
        board3 = (TextView)findViewById(R.id.board_main3);
        board33 = (TextView)findViewById(R.id.board_main33);
        board4 = (TextView)findViewById(R.id.board_main4);
        board44 = (TextView)findViewById(R.id.board_main44);
        board5 = (TextView)findViewById(R.id.board_main5);
        board55 = (TextView)findViewById(R.id.board_main55);
        mainBoardList = getIntent().getParcelableArrayListExtra("MainBoard");

        board1.setText(mainBoardList.get(0).getTitle());
        board11.setText(mainBoardList.get(0).getCategory());
        board2.setText(mainBoardList.get(1).getTitle());
        board22.setText(mainBoardList.get(1).getCategory());
        board3.setText(mainBoardList.get(2).getTitle());
        board33.setText(mainBoardList.get(2).getCategory());
        board4.setText(mainBoardList.get(3).getTitle());
        board44.setText(mainBoardList.get(3).getCategory());
        board5.setText(mainBoardList.get(4).getTitle());
        board55.setText(mainBoardList.get(4).getCategory());

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", mainBoardList.get(0));
                startActivity(intent);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", mainBoardList.get(1));
                startActivity(intent);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", mainBoardList.get(2));
                startActivity(intent);
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", mainBoardList.get(3));
                startActivity(intent);
            }
        });
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", mainBoardList.get(4));
                startActivity(intent);
            }
        });
    }

}
