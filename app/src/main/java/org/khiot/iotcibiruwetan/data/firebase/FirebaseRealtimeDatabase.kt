package org.khiot.iotcibiruwetan.data.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseRealtimeDatabase {
    private val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val kebunCabeRef: DatabaseReference = rootRef.child("kebunCabe")
    private val hidroponikRef: DatabaseReference = rootRef.child("Hidroponik")

    fun getKebunCabeData(listener: ValueEventListener) {
        kebunCabeRef.addValueEventListener(listener)
    }

    fun getHidroponikData(listener: ValueEventListener) {
        hidroponikRef.addValueEventListener(listener)
    }

    fun editKebunCabe(field: String, value: Any) {
        kebunCabeRef.child(field).setValue(value)
    }

    fun editHidroponik(field: String, value: Any) {
        hidroponikRef.child(field).setValue(value)
    }

    // Keep the old method for compatibility if needed, but update it to use kebunCabeRef
    fun editRTDB(field: String, value: Any) {
        kebunCabeRef.child(field).setValue(value)
    }
}
