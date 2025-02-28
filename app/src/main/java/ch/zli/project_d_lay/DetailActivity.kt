package ch.zli.project_d_lay

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import ch.zli.project_d_lay.databinding.ActivityDetailBinding
import java.util.Calendar

class DetailActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanedSmsForm()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PlanedSmsForm(
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // State variables for form fields
    var time by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // Define time picker dialog
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            time = String.format("%02d:%02d", selectedHour, selectedMinute)
        },
        hour,
        minute,
        true // 24-hour format
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Form content - takes up most of the screen
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Time Input
            Text(
                text = "Time",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("HH:mm") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { timePickerDialog.show() }) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_recent_history),
                            contentDescription = "Select Time"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone Number Input
            Text(
                text = "Phone number",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter phone number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message Input
            Text(
                text = "Message",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 120.dp), // Using about a quarter of screen height for a typical device
                placeholder = { Text("Enter your message") },
                minLines = 4
            )
        }

        // Buttons row at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Cancel Button
            Button(
                onClick = {
                    keyboardController?.hide()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cancel")
            }

            // Save Button
            Button(
                onClick = {
                    keyboardController?.hide()
                    // Validate and save data
                    if (validateInputs(time.toString(), phoneNumber.toString(), message.toString(), context)) {
                        // saveData(time, phoneNumber, message, context)
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}

// Function to validate inputs
private fun validateInputs(
    time: String,
    phoneNumber: String,
    message: String,
    context: Context
): Boolean {
    // Time validation (HH:mm format)
    if (!time.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
        Toast.makeText(context, "Invalid time format. Please use HH:mm", Toast.LENGTH_SHORT).show()
        return false
    }

    // Phone number validation (simple check)
    if (phoneNumber.isBlank() || !phoneNumber.matches(Regex("^[+]?[0-9]{10,15}$"))) {
        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
        return false
    }

    // Message validation
    if (message.isBlank()) {
        Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}
