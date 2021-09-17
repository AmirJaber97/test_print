package net.geidea.GMB.pos

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import com.example.test_printer.InvoiceDto
import com.example.test_printer.R
import com.sunmi.peripheral.printer.ICallback
import com.sunmi.peripheral.printer.SunmiPrinterService
import java.util.*

class PrinterAdapter(var sunmiPrinterService: SunmiPrinterService?, var resources: Resources) {


    var printerCallBack = object : ICallback {
        override fun asBinder(): IBinder? {
            return null
        }

        override fun onRunResult(isSuccess: Boolean) {
        }

        override fun onReturnString(result: String?) {
        }

        override fun onRaiseException(code: Int, msg: String?) {
        }

        override fun onPrintResult(code: Int, msg: String?) {
        }

    }

    private fun addblank(count: Int): String? {
        var count = count
        var st = ""
        if (count < 0) {
            count = 0
        }
        for (i in 0 until count) {
            st = "$st "
        }
        return st
    }

    private fun formatTitle(width: Int): String {

        val title = arrayOf(
            "Items",
            "Unit Price",
            "Quantity",
            "Item Subtotal (Inc. VAT)"
        )

        val arabicTitle = arrayOf(
            "المنتجات",
            "سعر الوحدة", "الكمية", "المجموع (شامل الضريبة)",
        )

        val sb = StringBuffer()

        val blank1: Int = (width/4) - title[0].length
        val blank2: Int = (width/4) - 4 - title[1].length
        val blank3: Int = (width/4) - 4 - title[2].length

        val arBlank1: Int = (width/4) - arabicTitle[0].length
        val arBlank2: Int = (width/4) - 4 - arabicTitle[1].length
        val arBlank3: Int = (width/4) - 4 - arabicTitle[2].length

        sb.append("\n")
        sb.append(title[0])
        sb.append(addblank(blank1))
        sb.append(title[1])
        sb.append(addblank(blank2))
        sb.append(title[2])
        sb.append(addblank(blank3))
        sb.append(title[3])

        Log.v("",sb.toString())


        sb.append("\n")
        sb.append(arabicTitle[3])
        sb.append(addblank(arBlank3))
        sb.append(arabicTitle[2])
        sb.append(addblank(arBlank2))
        sb.append(arabicTitle[1])
        sb.append(addblank(arBlank1))
        sb.append(arabicTitle[0])

        Log.v("",sb.toString())

        return sb.toString()
    }


    fun getStrList(inputString: String, length: Int): List<String?>? {
        var size = inputString.length / length
        if (inputString.length % length != 0) {
            size += 1
        }
        return getStrList1(inputString, length, size)
    }

    fun substring1(str: String, f: Int, t: Int): String {
        if (f > str.length) return ""
        return if (t > str.length) {
            str.substring(f, str.length)
        } else {
            str.substring(f, t)
        }
    }

    fun getStrList1(
        inputString: String, length: Int,
        size: Int
    ): List<String>? {
        val list: MutableList<String> = ArrayList()
        for (index in 0 until size) {
            val childStr: String = substring1(
                inputString, index * length,
                (index + 1) * length
            )
            list.add(childStr)
        }
        return list
    }

    private fun printNewline(str: String, width: Int) {
        val strings: List<String?> = getStrList(str, width) ?: listOf()
        for (string in strings) {
            sunmiPrinterService?.setAlignment(0, printerCallBack)
            sunmiPrinterService?.printText(string?.trim(), printerCallBack)
            sunmiPrinterService?.lineWrap(1, printerCallBack)
            Log.v("",string?.trim().toString())
        }

    }

    private fun printGoods(menuBean: InvoiceDto, divider: Bitmap, width1: Int) {
        var blank1: Int
        var blank2: Int
        var blank3: Int
        val blank4: Int
        val blank5: Int
        val blank6: Int
        val blank7: Int
        val blank8: Int
        val blank9: Int

        val width: Int = width1/4

        val maxNameWidth = width

        val sb = StringBuffer()

        for (listBean in menuBean.goods) {
            sb.setLength(0)
            val name: String = listBean.title
            val name1 = if (name.length > maxNameWidth) name.substring(0, maxNameWidth) else ""

            blank1 = width - (if (name.length > maxNameWidth) name1 else name).length + 1
            blank2 = width - 2 - listBean.p.length
            blank3 = width - 2 - listBean.q.length

            sb.append(if (name.length > maxNameWidth) name1 else name)
            sb.append(addblank(blank1))
            sb.append(listBean.p)
            sb.append(addblank(blank2))
            sb.append(listBean.q)
            sb.append(addblank(blank3))
            sb.append(listBean.price)
            sunmiPrinterService?.printText(sb.toString(), printerCallBack)
            sunmiPrinterService?.lineWrap(1, printerCallBack)

            Log.v("",sb.toString())

            if (name.length > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth)
            }

        }
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printBitmap(divider, printerCallBack)

        val total = "Total amount"
        val real = "Total Taxable amount"
        val excludingVAT = "(Excluding VAT)"
        val vat = "Total VAT"
        val contactNumber = "Contact number: 050258 87843"
        val storeAddress = "Store Address: Building, street, Area, City"

        blank4 = width * 3 - vat.length - (menuBean.vat?.length ?: 0)
        blank5 = width * 3 - total.length - (menuBean.total?.length ?: 0)
        blank6 = width * 3 - real.length - (menuBean.cash?.length ?: 0)
        blank7 = width * 3 - excludingVAT.length
        blank8 = width * 3 - contactNumber.length
        blank9 = width * 3 - storeAddress.length

        sb.setLength(0)
        sb.append(vat)
        sb.append(addblank(blank4))
        sb.append(menuBean.vat)

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(total)
        sb.append(addblank(blank5))
        sb.append(menuBean.total)

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(real)
        sb.append(addblank(blank6))
        sb.append(menuBean.cash)

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(excludingVAT)
        sb.append(addblank(blank7))

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(contactNumber)
        sb.append(addblank(blank8))
//        sb.append(contactNumber)

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(storeAddress)
        sb.append(addblank(blank9))
//        sb.append(contactNumber)

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printBitmap(divider, printerCallBack)

        sb.setLength(0)
    }


    var isLog = false

    fun doPrintJobSunmi(inv: InvoiceDto) {
        val divideImage = BitmapFactory.decodeResource(resources, R.drawable.ic_line_print)
        val mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_print)
        val divide = "------------------------------------------------------------------------"
        val width = divide.length
//            divide.length * 5 / 12
        val goodsTitle = formatTitle(width)

        val title = arrayOf(
            "VAT registration number:",
            "Date, Time:",
            "Invoice Number:"
        )

        val arabicTitle = arrayOf(
            "الرقم الضريبي",
            "التاريخ، الوقت", "رقم الفاتورة"
        )

        sunmiPrinterService?.setAlignment(1, printerCallBack)

        sunmiPrinterService?.printQRCode(inv.orderId, 8, 1, printerCallBack)

        sunmiPrinterService?.setFontSize(15F, printerCallBack)

        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.setAlignment(0, printerCallBack)


        sunmiPrinterService?.printText(divide, printerCallBack)

        val padding1: Int = 28
        val padding2: Int = 19
        val padding3: Int = 25

//        val blank1: Int = (width/2) - (title[0].length - inv.innText!!.length)
//        val blank2: Int = (width/2) - (arabicTitle[0].length)
//        val blank3: Int = (width/2) - (title[1].length - inv.date!!.length)
//        val blank4: Int = (width/2) - (arabicTitle[1].length)
//        val blank5: Int = (width/2) - (title[2].length - inv.orderId!!.length)
//        val blank6: Int = (width/2) - (arabicTitle[2].length)

        val blank1: Int = padding1 - title[0].length
        val blank2: Int = padding2 - (inv.innText?.length ?: 0)
        val blank3: Int = padding3 - arabicTitle[0].length

        val blank4: Int = padding1 - title[1].length
        val blank5: Int = padding2 - (inv.date?.length ?: 0)
        val blank6: Int = padding3 - arabicTitle[1].length

        val blank7: Int = padding1 - title[2].length
        val blank8: Int = padding2 - (inv.orderId?.length ?: 0)
        val blank9: Int = padding3 - arabicTitle[2].length

        val sb = StringBuffer()

        Log.v("",divide)

        sb.append("\n")
        sb.append(title[0])
        sb.append(addblank(blank1))
        sb.append(addblank(blank2))
        sb.append(inv.innText?.trim())
        sb.append(addblank(blank3))
        sb.append(arabicTitle[0])

        Log.v("",sb.toString())
        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sb.setLength(0)

        sb.append("\n")
        sb.append(title[1])
        sb.append(addblank(blank4))
        sb.append(addblank(blank5))
        sb.append(inv.date?.trim())
        sb.append(addblank(blank6))
        sb.append(arabicTitle[1])

        Log.v("",sb.toString())
        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sb.setLength(0)

        sb.append("\n")
        sb.append(title[2])
        sb.append(addblank(blank7))
        sb.append(addblank(blank8))
        sb.append(inv.orderId?.trim())
        sb.append(addblank(blank9))
        sb.append(arabicTitle[2])

        Log.v("",sb.toString())

        sunmiPrinterService?.printText(sb.toString(), printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)


//        sunmiPrinterService?.printText("VAT registration number: ${inv.innText}", printerCallBack)
//        sunmiPrinterService?.lineWrap(1, printerCallBack)
//        sunmiPrinterService?.printText("Date, Time: ${inv.time + " " + inv.date}", printerCallBack)
//        sunmiPrinterService?.lineWrap(1, printerCallBack)
//        sunmiPrinterService?.printText("Invoice Number: ${inv.orderId}", printerCallBack)
//        sunmiPrinterService?.lineWrap(1, printerCallBack)
//        sunmiPrinterService?.printTextWithFont(inv.storeName, "", 28f, printerCallBack)

        sunmiPrinterService?.lineWrap(3, printerCallBack)

        sunmiPrinterService?.printText(goodsTitle, printerCallBack)


        Log.v("",goodsTitle)

        sunmiPrinterService?.lineWrap(2, printerCallBack)

        printGoods(inv, divideImage, width)

        sunmiPrinterService?.lineWrap(3, printerCallBack)


        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont("Powered by Geidea ", "", 20F, printerCallBack)
        sunmiPrinterService?.setAlignment(1, printerCallBack)

        sunmiPrinterService?.printBitmap(mBitmap, printerCallBack)
        sunmiPrinterService?.lineWrap(3, printerCallBack)

        sunmiPrinterService?.cutPaper(printerCallBack)

    }

    fun openDrawerSunmi() {
        sunmiPrinterService?.openDrawer(printerCallBack)
    }


}