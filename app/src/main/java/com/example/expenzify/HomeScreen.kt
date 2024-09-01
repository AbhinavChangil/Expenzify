package com.example.expenzify

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.expenzify.data.model.ExpenseEntity
import com.example.expenzify.ui.theme.DarkBlue
import com.example.expenzify.ui.theme.DarkGreen
import com.example.expenzify.ui.theme.DarkRed
import com.example.expenzify.viewmodel.HomeViewModel
import com.example.expenzify.viewmodel.HomeViewModelFactory

@Composable
fun HomeScreen(){

    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current)
        .create(HomeViewModel::class.java)

    // UI for home screen
    Surface(modifier = Modifier.fillMaxSize()){
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow,list, card, recentTv, topBar) = createRefs()
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

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpenses(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom) // Position just below the nameRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance, income, expenses
            )
            Box(modifier = Modifier.fillMaxWidth().constrainAs(recentTv){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                Text(
                    text = "Recent Transaction",
                    fontSize = 18.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "See All",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(recentTv.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, list = state.value, viewModel)
        }
    }
}


@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expenses: String){
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
                Text(text = balance,
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
            CardRowItem(modifier = Modifier.align(Alignment.CenterStart), title = "Income", amount = income, image = R.drawable.ic_income)

            CardRowItem(modifier = Modifier.align(Alignment.CenterEnd), title = "Expense", amount = expenses, image = R.drawable.ic_expense)
        }
    }

}


@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel){
    LazyColumn(modifier = modifier.padding(horizontal = 10.dp)){
        items(list){ item->
            TransactionItem(
                name = item.title!!,
                amount = "₹${"%.2f".format(item.amount)}",
                icon = viewModel.getItemIcon(item),
                date = item.date.toString(),
                color = if(item.type == "Income") DarkGreen else DarkRed
            )
        }
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
fun TransactionItem(name: String, amount: String, icon: Int, date: String, color: Color){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)){
        Row{
            Image(
                painter = painterResource(id = icon), contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Column{
                Text(text = name, fontSize = 16.sp)
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