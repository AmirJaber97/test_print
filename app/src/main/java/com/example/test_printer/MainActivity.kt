package com.example.test_printer

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterException
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.SunmiPrinterService
import kotlinx.android.synthetic.main.activity_main.*
import net.geidea.GMB.pos.PrinterAdapter

class MainActivity : AppCompatActivity() {
    var printerAdapter: PrinterAdapter? = null
    lateinit var invoiceList: InvoiceDto
    var sunmiPrinterService: SunmiPrinterService? = null

    var mockGoods = listOf(
        InvoiceItem("SAFFRON MILK CAKE long long long long long long long", "20.00", "99", "4"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "13"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "42"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "13"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "51"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "1", "11"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "1", "1"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "41"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "133"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "999", "45"),
        InvoiceItem("SAFFRON MILK CAKE", "290.00", "99", "65"),
        InvoiceItem("SAFFRON MILK CAKE", "20.00", "99", "75")
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        bindPrintService()
        printerAdapter = PrinterAdapter(sunmiPrinterService, resources)

        invoiceList = InvoiceDto(
            "Store Name Long Name",
            "12:00:06",
            "Feb 31, 2021",
            "12345",
            mockGoods,
            "30.00",
            "40.00",
            "40.00",
            "0.00",
            "0.00",
            cash = "123",
            change = "0.01",
            total = "123",
            subTotal = "23",
            vat = "40", goods = mockGoods

        )
        button.setOnClickListener { _printJob() }
    }


    private fun bindPrintService() {
        try {
            InnerPrinterManager.getInstance().bindService(this, object : InnerPrinterCallback() {
                override fun onConnected(service: SunmiPrinterService) {
                    sunmiPrinterService = service
                    println("printer connected $service")
                    printerAdapter = PrinterAdapter(sunmiPrinterService, resources)
                }

                override fun onDisconnected() {
                    println("printer disconnected")
                    sunmiPrinterService = null
                }
            })
        } catch (e: InnerPrinterException) {
            e.printStackTrace()
            AlertDialog.Builder(this)
                .setMessage("E - init : ${e.localizedMessage}")
                .setPositiveButton(android.R.string.yes) { dialog, which ->

                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

        }
    }

    private fun _printJob() {
        try {
            printerAdapter?.doPrintJobSunmi(inv = invoiceList)
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                .setMessage("E - print : ${e.localizedMessage}")
                .setPositiveButton(android.R.string.yes) { dialog, which ->

                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }
}