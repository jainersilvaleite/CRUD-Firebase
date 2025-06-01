package com.jainer.crudfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jainer.crudfirebase.ui.theme.CRUDFirebaseTheme

class MainActivity : ComponentActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        databaseReference = FirebaseDatabase.getInstance().getReference("Texts")

        setContent {
            CRUDFirebaseTheme {
                AppNavigation(databaseReference)
            }
        }
    }
}
