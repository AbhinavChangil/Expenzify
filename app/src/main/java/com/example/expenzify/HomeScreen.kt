package com.example.expenzify

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.expenzify.ui.theme.DarkBlue

@Composable
fun HomeScreen(){
    Surface(modifier = Modifier.fillMaxSize()){
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow,list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 10.dp, end = 10.dp)
                .constrainAs(nameRow) {
                }) {
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp, start = 10.dp)
                ) {
                    Text(text = "Good Morning,",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp)) // Add space between the two texts
                    Text(text = "Abhinav Changil",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                }
                Image(
                    painter = painterResource(id = R.drawable.ic_bell_home),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp)
                )
            }
            CardItem(modifier = Modifier
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom) // Position just below the nameRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 10.dp)
            )
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                })
        }
    }
}


@Composable
fun CardItem(modifier: Modifier){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(DarkBlue)
        .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)){
                Text(text = "Total Balance", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "₹ 5000.00",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp))
            }
            Image(
                painter = painterResource(id = R.drawable.ic_menu_dots),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            CardRowItem(modifier = Modifier.align(Alignment.CenterStart), title = "Income", amount = "₹ 10000.00", image = R.drawable.ic_income)

            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd), title = "Expense", amount = "₹ 5000.00", image = R.drawable.ic_expense)
        }
    }
}


@Composable
fun TransactionList(modifier: Modifier){
    Column(modifier = modifier.padding(horizontal = 10.dp)){
        Box(modifier = Modifier.fillMaxWidth()){
            Text(text = "Transaction History", fontSize = 18.sp, modifier = Modifier, fontWeight = FontWeight.SemiBold)
            Text(text = "See All", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        TransactionItem(
            title = "Netflix",
            amount = "- ₹ 199",
            icon = R.drawable.ic_netflix,
            date = "Today",
            color = Color.Red
        )

        TransactionItem(
            title = "Starbucks",
            amount = "- ₹ 349",
            icon = R.drawable.ic_starbucks,
            date = "Today",
            color = Color.Red
        )

        TransactionItem(
            title = "Youtube",
            amount = "- ₹ 79",
            icon = R.drawable.ic_youtube,
            date = "Today",
            color = Color.Red
        )

        TransactionItem(
            title = "Upwork",
            amount = "+ ₹ 5199",
            icon = R.drawable.ic_upwork,
            date = "Today",
            color = Color.Green
        )
    }
}


@Composable
fun CardRowItem(modifier: Modifier,title: String, amount: String, image: Int){
    Column(modifier = modifier) {
        Row {
            Image(painter = painterResource(id = image), contentDescription = null)
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = title, fontSize = 16.sp, color = Color.White, modifier = Modifier.align(Alignment.CenterVertically))

        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = amount, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 6.dp))
    }
}


@Composable
fun TransactionItem(title: String, amount: String, icon: Int, date: String, color: Color){
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)){
        Row{
            Image(
                painter = painterResource(id = icon), contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Column{
                Text(text = title, fontSize = 16.sp)
                Text(text = date, fontSize = 16.sp)
            }
        }
        Text(
            text = amount,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd), color = color
        )
    }
}



@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen()
}