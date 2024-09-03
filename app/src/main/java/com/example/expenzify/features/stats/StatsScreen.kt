package com.example.expenzify.features.stats

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expenzify.R
import com.example.expenzify.Utils
import com.example.expenzify.data.model.ExpenseEntity
import com.example.expenzify.ui.theme.DarkBlue
import com.example.expenzify.ui.theme.DarkGreen
import com.example.expenzify.ui.theme.DarkRed
import com.example.expenzify.ui.theme.Oragne
import com.example.expenzify.viewmodel.StatsViewModel
import com.example.expenzify.viewmodel.StatsViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun StatsScreen(navController: NavController) {
    // Initialize the ViewModel
    val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)

    // State for the segmented option
    var selectedOption = remember { mutableStateOf("Expense") }

    //local context
    val contextLocal = LocalContext.current

    // UI for stats screen
    Scaffold (topBar = {
        // Add topBar
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(Oragne)
        ){

            Image(painter = painterResource(id = R.drawable.ic_left_arrow), contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 15.dp, top = 30.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )

            Text(
                text = "Statistics",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp),
                fontFamily = FontFamily(
                    Font(R.font.acme_regular)
                )
            )

            Image(painter = painterResource(id = R.drawable.ic_download), contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 15.dp, top = 30.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )

        }
    }){
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()){

            // Collect the expense entries from the ViewModel
            val expenseState = viewModel.expenseEntries.collectAsState(initial = emptyList())
            val incomeState = viewModel.incomeEntries.collectAsState(initial = emptyList())

            // Convert the expense, income, and balance entries to LineChart entries
            val expenseEntries = viewModel.getExpenseEntriesForChart(expenseState.value)
            val incomeEntries = viewModel.getIncomeEntriesForChart(incomeState.value)
            val balanceEntries = viewModel.getBalanceEntriesForChart(expenseState.value, incomeState.value)

            // Segmented Control with different function calls
            SegmentedControl(
                options = listOf("Expense", "Income", "Balance"),
                selectedOption = selectedOption.value,
                onOptionSelected = { option ->
                    selectedOption.value = option
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Display the LineChart based on the selected option
            LineChartSection(
                selectedOption = selectedOption.value,
                expenseEntries = expenseEntries,
                incomeEntries = incomeEntries,
                balanceEntries = balanceEntries
            )

            // Change text based on the selected option
            val topLabel = when (selectedOption.value) {
                "Income" -> "Top Earnings"
                "Expense" -> "Top Spendings"
                "Balance" -> "Top Transactions"
                else -> "Top Items"
            }
            Text(text = topLabel, Modifier.padding(top = 10.dp, start = 10.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Display the transaction list
            val topExpenseState = viewModel.topExpenseList.collectAsState(initial = emptyList())
            val topIncomeState = viewModel.topIncomeList.collectAsState(initial = emptyList())
            val topTransactionState = viewModel.topTransactionList.collectAsState(initial = emptyList())
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth(),
                list =
                when(selectedOption.value){
                    "Expense" -> topExpenseState.value
                    "Income" -> topIncomeState.value
                    "Balance" -> topTransactionState.value
                    else -> topExpenseState.value
                }
            )
        }
    }
}

@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>){
    LazyColumn(modifier = modifier.padding(horizontal = 10.dp)){
        items(list){ item->
            com.example.expenzify.features.home.TransactionItem(
                name = item.title!!,
                amount = "₹${"%.2f".format(item.amount)}",
                icon = Utils.getItemIcon(item),
                date = Utils.dateFormatToHumanReadableFormat(item.date).toString(),
                color = if(item.type == "Income") DarkGreen else DarkRed
            )
        }
    }
}




@Composable
fun LineChartSection(
    selectedOption: String,
    expenseEntries: List<Entry>,
    incomeEntries: List<Entry>,
    balanceEntries: List<Entry>
) {

    val red_fill = ContextCompat.getDrawable(LocalContext.current, R.drawable.red_gradient)
    val green_fill = ContextCompat.getDrawable(LocalContext.current, R.drawable.green_gradient)
    val blue_fill = ContextCompat.getDrawable(LocalContext.current, R.drawable.blue_gradient)
    val red_color = (android.graphics.Color.parseColor("#FFB61408"))
    val green_color = (android.graphics.Color.parseColor("#FF308d46"))
    val blue_color = (android.graphics.Color.parseColor("#1550C6"))

    when (selectedOption) {
        "Expense" -> red_fill?.let { LineChart(entries = expenseEntries, it, red_color) }
        "Income" -> green_fill?.let { LineChart(entries = incomeEntries, it, green_color) }
        "Balance" -> blue_fill?.let { LineChart(entries = balanceEntries, it, blue_color) }
    }
}



@Composable
fun LineChart(entries: List<Entry>, colorFill: Drawable, colorLine: Int) {
    val currentContext = LocalContext.current
    AndroidView(
        factory = { context ->
            val chartView = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
            chartView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { chartView ->
        val lineChart = chartView.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, "Expenses").apply {
            color = colorLine
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = (android.graphics.Color.parseColor("#FF03395F"))
            colorFill?.let {
                fillDrawable = colorFill
            }
        }

        lineChart.apply {
            xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val date = Utils.formatDateForChart(value.toLong())
                    return date
                }
            }

            data = com.github.mikephil.charting.data.LineData(dataSet)
            axisLeft.apply {
                isEnabled = false
                setDrawGridLines(false)
            }
            axisRight.apply {
                isEnabled = false
                setDrawGridLines(false)
            }
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            //description label
            description.isEnabled = false

            invalidate()
        }

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
fun SegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) DarkBlue else Color.Gray)
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//Preview
@Composable
@Preview(showBackground = true)
fun PreviewAddExpenseScreen(){
    StatsScreen(rememberNavController())
}