package ru.startandroid.develop.firebasetest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.startandroid.develop.firebasetest.databinding.ActivityMainBinding
import ru.startandroid.develop.firebasetest.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySignBinding
    val TAG = "myLogs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null ) {
                    firebaseAuth(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d(TAG, "API exception")
            }
        }
        binding.btnSign.setOnClickListener {
            signIn()
            Log.d(TAG, "clicked")
        }
        checkAuth()
    }

    private fun getClient(): GoogleSignInClient {
        val signInRequest = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(this, signInRequest);
    }

    private fun signIn() {
        val signInClient = getClient();
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuth(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "success")
                checkAuth()
            } else {
                Log.d(TAG, "error occurred")
            }
        }
    }

    private fun checkAuth() {
    if (auth.currentUser != null) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
            }
        }
    }