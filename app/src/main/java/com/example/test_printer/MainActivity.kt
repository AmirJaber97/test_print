package com.example.test_printer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunmi.peripheral.printer.SunmiPrinterService
import net.geidea.GMB.pos.PrinterAdapter

class MainActivity : AppCompatActivity() {
    lateinit var printerAdapter: PrinterAdapter
    var sunmiPrinterService: SunmiPrinterService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var fakeCharge = ChargeDto("20.0", "106.051.064.029", 12345)


        var fakeListForInvoice = InvoiceDto(
            "Store Name Long Name",
            "12:00:06",
            "Feb 31, 2021",
            "12345",
            listOf(
                InvoiceItem("SAFFRON MILK CAKE long long long long long long long", "20.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "1"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "1"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "999"),
                InvoiceItem("SAFFRON MILK CAKE", "290.00", "99"),
                InvoiceItem("SAFFRON MILK CAKE", "20.00", "99")
            ),
            "30.00",
            "40.00",
            "40.00",
            "0.00",
            "0.00",
            cash = "123",
            change = "0.01",
            total = "123",
            subTotal = "23",
            vat = "40"
        )
    }
}