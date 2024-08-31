package com.example.expenzify

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expenzify.ui.theme.AcmeFont
import com.example.expenzify.ui.theme.DarkBlue

@Composable
fun AddExpenseScreen(){
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
                    text = "Add  Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontFamily = AcmeFont
                )

                Image(painter = painterResource(id = R.drawable.ic_menu_dots), contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                )

            }

            DataForm(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom) // Position just below the nameRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 20.dp)
            )
        }
    }
}


@Composable
fun DataForm(modifier: Modifier){
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
        TextInputLayout(label = "Type", value = "", onValueChange = {})
        Spacer(modifier = Modifier.size(8.dp))
        TextInputLayout(label = "Name", value = "", onValueChange = {})
        Spacer(modifier = Modifier.size(8.dp))
        TextInputLayout(label = "Category", value = "", onValueChange = {})
        Spacer(modifier = Modifier.size(8.dp))
        TextInputLayout(label = "Amount", value = "", onValueChange = {})
        Spacer(modifier = Modifier.size(8.dp))
        TextInputLayout(label = "Date", value = "", onValueChange = {})
        Spacer(modifier = Modifier.size(30.dp))
        CustomButton(
            text = "Add Expense",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(DarkBlue),
            onClick = { /*TODO*/ },
            bgColor = DarkBlue,
            textColor = Color.White,
            textSize = 16.sp
        )

    }
}


@Composable
fun CustomButton(text: String, modifier: Modifier, onClick: () -> Unit, bgColor: Color, textColor: Color, textSize: TextUnit){
    Button(onClick = { onClick },
        modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor, // Background color
            contentColor = textColor // Text color
        )
    ) {
        Text(text = text, fontSize = textSize)
    }
}

@Composable
fun TextInputLayout(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorText != null,
            modifier = Modifier.fillMaxWidth()
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


@Composable
@Preview(showBackground = true)
fun PreviewAddExpenseScreen(){
    AddExpenseScreen()
}