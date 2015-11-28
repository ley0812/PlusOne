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

import java.io.InputStream;

import ac.plusone.R;
import ac.plusone.guide.GuideActivity;
import ac.plusone.map.MapActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int PROFILE_PIC_SIZE = 1500;
    private final int TIME_DELAY = 2000;
    private long back_pressed;

    private Button btn;
    private Button mapBtn;
    private Button btn_guide;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private ImageView profileImg;
    private TextView profileName;
    private MenuItem signOut, write, revoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        profileImg = (ImageView) headerView.findViewById(R.id.profile_imageView);
        profileName = (TextView) headerView.findViewById(R.id.profile_name);
        signOut = navMenu.getItem(0);
        write = navMenu.getItem(1);
        revoke = navMenu.getItem(2);

        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String userID = userPref.getString("currentUser", "");
        if(!userID.equals("")) {
            updateUI(true);
            profileName.setText(userPref.getString("currentUserName", getString(R.string.please_login)));
            new LoadProfileImage().execute(userPref.getString("currentUserPhoto", "aaaaa"));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
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

        btn = (Button)findViewById(R.id.board);
        btn.setOnClickListener(new View.OnClickListener() {
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
        btn_guide = (Button)findViewById(R.id.btn_guide);
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
            new LoadProfileImage().execute(acct.getPhotoUrl().toString());

            updateUI(true);

            SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = userPref.edit();
            editor.putString("currentUser", acct.getId());
            Log.d("###", "SignIn User : " + acct.getId());
            editor.putString("currentUserName", acct.getDisplayName());
            editor.putString("currentUserPhoto", acct.getPhotoUrl().toString());
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
            profileImg.setVisibility(View.VISIBLE);
            signOut.setVisible(true);
            write.setVisible(true);
            revoke.setVisible(true);
        }else {
            signInButton.setVisibility(View.GONE);
            profileName.setText(getString(R.string.please_login));
            profileImg.setVisibility(View.GONE);
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
            String personPhotoUrl = urls[0].substring(0, urls[0].toString().length() - 2)
                    + PROFILE_PIC_SIZE;
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
            profileImg.setImageBitmap(result);
        }
    }

}