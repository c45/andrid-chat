package ru.startandroid.develop.firebasetest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.startandroid.develop.firebasetest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit private var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TAG = "myLogs"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        binding.button.setOnClickListener() {
            myRef.setValue(binding.et.text.toString())
        }
        onChangeListener(myRef)
    }

    private fun onChangeListener(databaseReference: DatabaseReference) {
        databaseReference.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    textView.append("\n")
                    textView.append(snapshot.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}