package com.example.expenzify

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expenzify.data.model.ExpenseEntity
import com.example.expenzify.ui.theme.AcmeFont
import com.example.expenzify.ui.theme.DarkBlue
import com.example.expenzify.ui.theme.DarkGreen
import com.example.expenzify.ui.theme.DarkRed
import com.example.expenzify.viewmodel.AddExpenseViewModel
import com.example.expenzify.viewmodel.AddExpenseViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AddExpenseScreen(){
    val context = LocalContext.current
    // use View Model
    val viewModel = AddExpenseViewModelFactory(LocalContext.current)
        .create(AddExpenseViewModel::class.java)

    //Create a Coroutine
    val coroutineScope = rememberCoroutineScope()

    // UI for adding a new expense
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            // Add topBar
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){

                Image(painter = painterResource(id = R.drawable.ic_left_arrow), contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                )

                Text(
                    text = "Add  Transaction",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontFamily = FontFamily(
                        Font(R.font.acme_regular)
                    )
                )

                Image(painter = painterResource(id = R.drawable.ic_menu_dots), contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                )

            }

            DataForm(modifier = Modifier
                .padding(top = 30.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom) // Position just below the nameRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }, onAddTransactionClick = {
                    // Launch a coroutine to call addExpense
                Log.d("AddExpenseScreen1", "Adding expense: $it")
                coroutineScope.launch {
                    Log.d("AddExpenseScreen2", "Adding expense: $it")
                    val success = viewModel.addExpense(it)
                    if (!success) {
                        Toast.makeText(context, "Failed to add transaction", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DataForm(modifier: Modifier, onAddTransactionClick : (model: ExpenseEntity)->Unit){

    val type = remember { mutableStateOf("Income") }
    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())}
    val category = remember { mutableStateOf("")}
    val description = remember { mutableStateOf("")}
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    // UI for data form
    Column (modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .wrapContentHeight()
        .clip(RoundedCornerShape(10.dp))
        .background(Color.White)
        .padding(16.dp)
        .padding(bottom = 20.dp)
        .verticalScroll(rememberScrollState())
    ){
        //Add Radio Group for Type of Transaction
        RadioGroup(type = type)
        Spacer(modifier = Modifier.size(10.dp))

        //Add TextField for Title
        TextInputLayout(label = "Title", value = name.value, onValueChange = {
            name.value = it
        })
        Spacer(modifier = Modifier.size(20.dp))

        //Add TextField for Amount
        TextInputLayout(label = "Amount", value = amount.value, onValueChange = {
            amount.value = it
        })
        Spacer(modifier = Modifier.size(20.dp))

        //Add Date for Date
         DateInputLayout(
             label = "Date",
             value = Utils.dateFormatToHumanReadableFormat(date.value),
             onValueChange = {},
             onIconClick = {dateDialogVisibility.value = true},
             enabled = false
         )
        Spacer(modifier = Modifier.size(20.dp))

        //Add DropDown for Category
        CustomDropDown(listOfItems = listOf("Netflix", "Paypal", "Upwork", "Salary", "Freelance", "Work", "Transport"),
            onItemSelected = {
                category.value = it
            },
            label = "Category"
        )
        Spacer(modifier = Modifier.size(20.dp))

        //Add TextField for Description
        OptionalTextInputLayout(label = "Description (Optional)", value = description.value, onValueChange = {
            description.value = it
        })


        Spacer(modifier = Modifier.size(40.dp))

        //Add Button for Adding Transaction
        CustomButton(
            text = "Add Transaction",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkBlue),
            onClick = {
                val expense = ExpenseEntity(
                    id = null,
                    type = type.value,
                    title = name.value,
                    amount = amount.value.toDouble(),
                    date = Utils.dateFormatToHumanReadableFormat(date.value),
                    category = category.value,
                    description = description.value
                )

                Log.d("AddExpenseScreen1", "Adding expense: $expense")
                onAddTransactionClick(expense)
            },
            bgColor = DarkBlue,
            textColor = Color.White,
            textSize = 16.sp
        )


    }

    if(dateDialogVisibility.value){
        DatePickerDialogExample(
            onDismiss = {
                dateDialogVisibility.value = false
                        },
            onDateSelected = {
            date.value = it
            dateDialogVisibility.value = false
            }
        )
    }
}


@Composable
fun CustomButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
    bgColor: Color,
    textColor: Color,
    textSize: TextUnit
) {
    Button(
        onClick = onClick, // Pass the function directly
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor, // Background color
            contentColor = textColor // Text color
        )
    ) {
        Text(text = text, fontSize = textSize)
    }
}


//Custom Text Field
@Composable
fun TextInputLayout(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorText: String? = null
) {
    Column(modifier = modifier) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorText != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            } // label
        )
        if (errorText != null) {
            Text(
                text = errorText,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


//Custom Text Field
@Composable
fun OptionalTextInputLayout(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            } // label
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputLayout(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit, // Add a lambda for handling icon clicks
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorText: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorText != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange, // You can use any other drawable here
                    tint = DarkBlue,
                    contentDescription = "Calendar Icon",
                    modifier = Modifier.clickable(onClick = onIconClick) // Handle icon click
                )
            },
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Black
                )}, //label
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black, // Customize colors for the disabled state if needed
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Black
            )
        )
        if (errorText != null) {
            Text(
                text = errorText,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


//Custom Radio Group
@Composable
fun RadioGroup(
    type : MutableState<String>
) {

    Row(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            RadioButton(
                selected = type.value == "Income",
                onClick = { type.value = "Income" },
                colors = RadioButtonColors(DarkGreen, DarkGreen, Color.Gray, Color.Gray)
            )
            Text(
                text = "Income",
                modifier = Modifier
                    .clickable { type.value = "Income" },
                fontWeight = FontWeight.SemiBold,
                color = DarkGreen,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.size(45.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            RadioButton(
                selected = type.value == "Expense",
                onClick = { type.value = "Expense" },
                colors = RadioButtonColors(DarkRed, DarkRed, Color.Gray, Color.Gray)
            )
            Text(
                text = "Expense",
                modifier = Modifier
                    .clickable { type.value = "Expense" },
                fontWeight = FontWeight.SemiBold,
                color = DarkRed,
                fontSize = 18.sp
            )
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample(
    onDismiss: () -> Unit,
    onDateSelected: (date: Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = null)
    val selectedDate = datePickerState.selectedDateMillis?: 0L

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = {
                    onDateSelected(selectedDate)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Dismiss")
                }
            },
            text = {
                DatePicker(state = datePickerState)
            }
        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(listOfItems: List<String>,
                   onItemSelected: (item: String) -> Unit,
                   label: String,
                   errorText: String? = null
){
    //DropDown for Category
    val expanded = remember { mutableStateOf(false) }

    val selectedItem = remember { mutableStateOf("") }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}) {
        OutlinedTextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Black
                )}, //label
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black, // Customize colors for the disabled state if needed
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Black
            ),
            isError = errorText != null,
        )
        if (errorText != null) {
            Text(
                text = errorText,
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { /*TODO*/ }) {
            listOfItems.forEach{
                DropdownMenuItem(
                    text = { Text(text = it)},
                    onClick = { selectedItem.value = it
                expanded.value = false
                        onItemSelected(selectedItem.value)
                })
            }
        }
    }
}



//Preview
@Composable
@Preview(showBackground = true)
fun PreviewAddExpenseScreen(){
    AddExpenseScreen()
}