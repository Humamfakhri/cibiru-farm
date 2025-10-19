@file:OptIn(ExperimentalMaterial3Api::class)

package org.khiot.iotcibiruwetan.ui.screen.kebuncabe

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.khiot.iotcibiruwetan.R
import org.khiot.iotcibiruwetan.data.firebase.FirebaseRealtimeDatabase
import org.khiot.iotcibiruwetan.data.model.KebunCabeData
import org.khiot.iotcibiruwetan.data.model.Soil
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class WateringSchedule(
    val id: Int,
    val time: LocalTime,
    val duration: Int
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun KebunCabeScreen() {
    var kebunData by remember { mutableStateOf(KebunCabeData()) }
    var isManualMode by remember { mutableStateOf(false) }
    var valve1Enabled by remember { mutableStateOf(false) }
    var valve2Enabled by remember { mutableStateOf(false) }
    var soil1 by remember { mutableStateOf(Soil()) }
    var soil2 by remember { mutableStateOf(Soil()) }
    fun getValveImage(isEnabled: Boolean, soilStatus: String): Int {
        return if (isEnabled) {
            when (soilStatus) {
                "Kering" -> R.drawable.valve_on_kering
                "Lembap" -> R.drawable.valve_on_lembap
                else -> R.drawable.valve_on_basah
            }
        } else {
            when (soilStatus) {
                "Kering" -> R.drawable.valve_off_kering
                "Lembap" -> R.drawable.valve_off_lembap
                else -> R.drawable.valve_off_basah
            }
        }
    }

    val image1 = getValveImage(valve1Enabled, soil1.status)
    val image2 = getValveImage(valve2Enabled, soil2.status)

    var schedules by remember {
        mutableStateOf(
            listOf(
                WateringSchedule(1, LocalTime.of(6, 0), 5),
                WateringSchedule(2, LocalTime.of(17, 0), 5)
            )
        )
    }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingSchedule by remember { mutableStateOf<WateringSchedule?>(null) }

    LaunchedEffect(Unit) {
        FirebaseRealtimeDatabase.getKebunCabeData(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FIREBASE", "${snapshot.value}")
                snapshot.getValue(KebunCabeData::class.java)?.let {
                    Log.d("FIREBASE", "$it")
                    kebunData = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    LaunchedEffect(kebunData) {
        isManualMode = kebunData.mode == "manual"
        valve1Enabled = kebunData.valveOpen1
        valve2Enabled = kebunData.valveOpen2
        soil1 = kebunData.soil1
        soil2 = kebunData.soil2
    }

    LazyColumn(
        modifier = Modifier
            .overscroll(overscrollEffect = null)
            .fillMaxSize()
            .background(Color.White)
//            .background(Color(0xFFE8F4F8))
            .padding(16.dp, 0.dp)
            .overscroll(overscrollEffect = null),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Kebun Cabe",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column (
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(image1),
                            contentDescription = "Valve Off",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(0.dp))
                                .background(Color.LightGray),
//                        contentScale = ContentScale.FillWidth
                        )
                        Text(soil1.status, Modifier.alpha(.5f))
                    }
                    Column (
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(image2),
                            contentDescription = "Valve Off",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(0.dp))
                                .background(Color.LightGray),
//                        contentScale = ContentScale.FillWidth
                        )
                        Text(soil2.status, Modifier.alpha(.5f))
                    }
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
        item {
            // Mode Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE3E3E3)),
                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                isManualMode = !isManualMode
                                val newMode = if (isManualMode) "manual" else "auto"
                                FirebaseRealtimeDatabase.editRTDB("mode", newMode)
                            }
                        )
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mode Operasi:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = if (isManualMode) "Manual" else "Otomatis",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isManualMode) Color(0xFF6B7280) else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            Row (
                modifier = Modifier
                    .alpha(if (isManualMode) 0.5f else 1f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE3E3E3), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("06.00 - 06.05", style = MaterialTheme.typography.bodySmall)
                    Text("17.00 - 17.05", style = MaterialTheme.typography.bodySmall)
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_timer_24),
                    tint = if(isManualMode) Color(0xFF9CA3AF) else MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Column (
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("06.05 - 06.10", style = MaterialTheme.typography.bodySmall)
                    Text("17.05 - 17.10", style = MaterialTheme.typography.bodySmall)
                }            }
        }


        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ValveCard(
                    title = "Valve 1",
                    isEnabled = valve1Enabled,
                    isManualMode = isManualMode,
                    onToggle = {
                        valve1Enabled = !valve1Enabled
                        FirebaseRealtimeDatabase.editRTDB("valveOpen1", valve1Enabled)
                   },
                    modifier = Modifier.weight(1f)
                )

                ValveCard(
                    title = "Valve 2",
                    isEnabled = valve2Enabled,
                    isManualMode = isManualMode,
                    onToggle = {
                        valve2Enabled = !valve2Enabled
                        FirebaseRealtimeDatabase.editRTDB("valveOpen2", valve2Enabled)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

//        item {
//            // Schedule Section
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.White),
//                shape = RoundedCornerShape(12.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = "Jadwal Penyiraman",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Color(0xFF374151)
//                        )
//                        Spacer(modifier = Modifier.weight(1f))
//                        FilledTonalIconButton(
//                            onClick = { showAddDialog = true },
//                            colors = IconButtonDefaults.filledTonalIconButtonColors(
//                                containerColor = MaterialTheme.colorScheme.primary,
//                                contentColor = Color.White
//                            )
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Add,
//                                contentDescription = "Tambah Jadwal"
//                            )
//                        }
//                    }
//
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(12.dp)
//                    ) {
//                        schedules.forEach { schedule ->
//                            ScheduleItem(
//                                schedule = schedule,
//                                onEdit = { editingSchedule = schedule },
//                                onDelete = {
//                                    schedules = schedules.filter { it.id != schedule.id }
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }

    }


    // Add/Edit Dialog
    if (showAddDialog || editingSchedule != null) {
        ScheduleDialog(
            schedule = editingSchedule,
            onDismiss = {
                showAddDialog = false
                editingSchedule = null
            },
            onSave = { time, duration ->
                if (editingSchedule != null) {
                    // Edit existing
                    schedules = schedules.map {
                        if (it.id == editingSchedule!!.id) {
                            it.copy(time = time, duration = duration)
                        } else it
                    }
                } else {
                    // Add new
                    val newId = (schedules.maxOfOrNull { it.id } ?: 0) + 1
                    schedules = schedules + WateringSchedule(newId, time, duration)
                }
                showAddDialog = false
                editingSchedule = null
            }
        )
    }
}

@Composable
fun ValveCard(
    title: String,
    isEnabled: Boolean,
    isManualMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = Color.White
//    val containerColor = if (isManualMode)
//        Color.White
//    else
//        Color.White.copy(alpha = 0.5f)

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .alpha(if (isManualMode) 1f else 0.5f),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, Color(0xFFE3E3E3)),
        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isEnabled -> MaterialTheme.colorScheme.primary
                            else -> Color(0xFFE5E7EB)
                        }
                    )
                    .clickable(enabled = isManualMode) { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isEnabled) "ON" else "OFF",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnabled) Color.White else Color(0xFF9CA3AF)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleItem(
    schedule: WateringSchedule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F9FF)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF374151)
                )
                Text(
                    text = "Durasi: ${schedule.duration} menit",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = Color(0xFFEF4444)
                )
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation", "DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleDialog(
    schedule: WateringSchedule?,
    onDismiss: () -> Unit,
    onSave: (LocalTime, Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(schedule?.time?.hour ?: 6) }
    var selectedMinute by remember { mutableIntStateOf(schedule?.time?.minute ?: 0) }
    var duration by remember { mutableStateOf((schedule?.duration ?: 5).toString()) }
    var showHourPicker by remember { mutableStateOf(false) }
    var showMinutePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (schedule != null) "Edit Jadwal" else "Tambah Jadwal",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Waktu Penyiraman:",
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hour Selector
                    OutlinedTextField(
                        value = String.format("%02d", selectedHour),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Jam") },
                        modifier = Modifier
                            .width(80.dp)
                            .clickable { showHourPicker = true }
                    )

                    Text(":", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    // Minute Selector
                    OutlinedTextField(
                        value = String.format("%02d", selectedMinute),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Menit") },
                        modifier = Modifier
                            .width(80.dp)
                            .clickable { showMinutePicker = true }
                    )
                }

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { char -> char.isDigit() } },
                    label = { Text("Durasi (menit)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val durationInt = duration.toIntOrNull() ?: 5
                    val time = LocalTime.of(selectedHour, selectedMinute)
                    onSave(time, durationInt)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF6B7280)
                )
            ) {
                Text("Batal")
            }
        }
    )

    // Hour Picker Dialog
    if (showHourPicker) {
        AlertDialog(
            onDismissRequest = { showHourPicker = false },
            title = { Text("Pilih Jam") },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items((0..23).toList()) { hour ->
                        TextButton(
                            onClick = {
                                selectedHour = hour
                                showHourPicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(String.format("%02d", hour))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHourPicker = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    // Minute Picker Dialog
    if (showMinutePicker) {
        AlertDialog(
            onDismissRequest = { showMinutePicker = false },
            title = { Text("Pilih Menit") },
            text = {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items((0..59).toList()) { minute ->
                        TextButton(
                            onClick = {
                                selectedMinute = minute
                                showMinutePicker = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(String.format("%02d", minute))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMinutePicker = false }) {
                    Text("Tutup")
                }
            }
        )
    }
}
