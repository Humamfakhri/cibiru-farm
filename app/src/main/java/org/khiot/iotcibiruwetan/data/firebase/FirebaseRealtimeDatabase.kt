package org.khiot.iotcibiruwetan.data.firebase

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseRealtimeDatabase {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("kebunCabe")

    fun getKebunCabeData(listener: ValueEventListener) {
        database.addValueEventListener(listener)
    }

    fun editRTDB(field: String, value: Any) {
        database.child(field).setValue(value)
            .addOnSuccessListener {
                Log.d("FIREBASE", "$field updated to $value")
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Failed to update $field", e)
            }
    }
}