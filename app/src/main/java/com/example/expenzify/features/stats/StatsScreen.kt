package com.example.expenzify.features.stats

import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expenzify.R
import com.example.expenzify.Utils
import com.example.expenzify.ui.theme.DarkBlue
import com.example.expenzify.ui.theme.Oragne
import com.example.expenzify.viewmodel.StatsViewModel
import com.example.expenzify.viewmodel.StatsViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.round

@Composable
fun StatsScreen(navController: NavController) {
    // Initialize the ViewModel
    val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)


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
            val dataState = viewModel.expenseEntries.collectAsState(initial = emptyList())
            // Convert the expense entries to LineChart entries
            val entries = viewModel.getEntriesForChart(dataState.value)
            // Render the LineChart with the converted entries
            LineChart(entries = entries)
        }
    }
}

@Composable
fun LineChart(entries: List<Entry>){
    // Chart UI using XML layout file
    val currentContext = LocalContext.current

    AndroidView(factory = {
        val chartView = LayoutInflater.from(currentContext).inflate(R.layout.stats_line_chart, null)
        chartView
    }, modifier = Modifier.fillMaxWidth().height(250.dp)){
        chartView ->
        val lineChart = chartView.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, "Expenses").apply {
            color = (android.graphics.Color.parseColor("#FFB61408"))
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = (android.graphics.Color.parseColor("#FF03395F"))

            //CHART GRADIENT
            val drawable_gradient = ContextCompat.getDrawable(currentContext,R.drawable.chart_gradient)
            drawable_gradient?.let {
                fillDrawable = drawable_gradient
            }
        }



        lineChart.apply {
            setScaleEnabled(true) // Enable scaling
            setPinchZoom(true) // Allow pinch zooming
            // Limit the zoom level
            viewPortHandler.setMaximumScaleX(1f) // Set max X zoom level (5x)
            viewPortHandler.setMaximumScaleY(1f) // Set max Y zoom level (5x)
            // Check and adjust the zoom level if it exceeds the maximum
            if (viewPortHandler.scaleX > 1f) {
                viewPortHandler.setZoom(1f / viewPortHandler.scaleX, 1f, 0f, 0f)
            }
            if (viewPortHandler.scaleY > 1f) {
                viewPortHandler.setZoom(1f, 1f / viewPortHandler.scaleY, 0f, 0f)
            }


            // Setting X-Axis properties separately with .apply
            lineChart.xAxis.apply {
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val date = Utils.formatDateForChart(value.toLong())
                        // Log the value to debug
                        Log.d("LineChart1", "Date: $date")

                        return date
                    }
                }
                isEnabled = true
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                setLabelCount(5, true)
            }

            // Other chart settings
            lineChart.description.isEnabled = true
            lineChart.legend.isEnabled = true
            lineChart.setPinchZoom(true)

            // Setting Y-Axis properties
            lineChart.axisLeft.apply {
                isEnabled = true
                setDrawGridLines(false)
                granularity = 100f  // Setting gap between lines to 100 units
                axisMinimum = 0f  // Minimum value for Y-axis
                axisMaximum = (entries.maxOfOrNull { it.y } ?: 0f) + 200f  // Max value with padding
            }
        }

        //setting data into chart
        lineChart.data = com.github.mikephil.charting.data.LineData(dataSet)
        lineChart.invalidate()
    }

}


//Preview
@Composable
@Preview(showBackground = true)
fun PreviewAddExpenseScreen(){
    StatsScreen(rememberNavController())
}