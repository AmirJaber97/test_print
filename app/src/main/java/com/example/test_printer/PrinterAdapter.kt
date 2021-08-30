package net.geidea.GMB.pos

import android.content.res.Resources
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

    private fun formatTitle(width: Int): String? {

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
        val blank1: Int = width - title[0].length
        val blank2: Int = width - title[1].length
        val blank3: Int = width - title[2].length

        val arBlank1: Int = width - arabicTitle[0].length
        val arBlank2: Int = width - arabicTitle[1].length
        val arBlank3: Int = width - arabicTitle[2].length
        val divide2 = "-----------------------------------------------"

        sb.append(divide2)
        sb.append("\n")
        sb.append(title[0])
        sb.append(addblank(blank1))
        sb.append(title[1])
        sb.append(addblank(blank2))
        sb.append(title[2])
        sb.append(addblank(blank3))
        sb.append(title[3])

        sb.append("\n")

        sb.append(arabicTitle[3])
        sb.append(addblank(arBlank3))
        sb.append(arabicTitle[2])
        sb.append(addblank(arBlank2))
        sb.append(arabicTitle[1])
        sb.append(addblank(arBlank1))
        sb.append(arabicTitle[0])

        Log.v("1", sb.toString())


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
            sunmiPrinterService?.printText(string?.trim(), printerCallBack)
            sunmiPrinterService?.setAlignment(0, printerCallBack)
            sunmiPrinterService?.lineWrap(1, printerCallBack)
        }
    }

    private fun printGoods(menuBean: InvoiceDto, fontsizeContent: Int, divide2: String, payMode: Int, width: Int) {
        var blank1: Int
        var blank2: Int
        val blank3: Int
        val blank4: Int
        val blank5: Int
        var blank6: Int

        val maxNameWidth = width - 2
        val sb = StringBuffer()
        for (listBean in menuBean.goods) {
            sb.setLength(0)
            val name: String = listBean.title
            val name1 = if (name.length > maxNameWidth) name.substring(0, maxNameWidth) else ""
            blank1 = width - (if (name.length > maxNameWidth) name1 else name).length + 1
            blank2 = width - 2 - listBean.q.length
            blank6 = width - 2 - listBean.p.length
            sb.append(if (name.length > maxNameWidth) name1 else name)
            sb.append(addblank(blank1))
            sb.append(listBean.p)
            sb.append(addblank(blank6))
            sb.append(listBean.q)
            sb.append(addblank(blank2))
            sb.append(listBean.price)
            sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
            //  sunmiPrinterService?.clearBuffer()
            sunmiPrinterService?.lineWrap(1, printerCallBack)

            Log.v("5", sb.toString())

            if (name.length > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth)
            }

        }
        sunmiPrinterService?.printText(divide2, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        val total = "Total amount"
        val real = "Total Taxable amount"
        val excludingVAT = "(Excluding VAT)"
        val vat = "Total VAT"
        val contactNumber = "Contact number: 050258 87843"
        val storeAddress = "Store Address: Building, street, Area, City"



        blank1 = width * 4 - total.length - (menuBean.total?.length ?: 0)
        blank2 = width * 4 - real.length - (menuBean.cash?.length ?: 0)
        blank3 = width * 4 - vat.length - (menuBean.vat?.length ?: 0)
        blank4 = width * 3 - contactNumber.length
        blank5 = width * 3 - storeAddress.length

        sb.setLength(0)
        sb.append(vat)
        sb.append(addblank(blank3))
        sb.append(menuBean.vat)

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())

        sb.setLength(0)
        sb.append(total)
        sb.append(addblank(blank1))
        sb.append(menuBean.total)

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())

        sb.setLength(0)
        sb.append(real)
        sb.append(addblank(blank2))
        sb.append(menuBean.cash)

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())

        sb.setLength(0)
        sb.append(excludingVAT)
        sb.append(addblank(blank5))

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())

        sunmiPrinterService?.printText(divide2, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sb.setLength(0)
        sb.append(contactNumber)
        sb.append(addblank(blank4))
        sb.append(contactNumber)

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())

        sb.setLength(0)
        sb.append(storeAddress)
        sb.append(addblank(blank5))
        sb.append(contactNumber)

        sunmiPrinterService?.printTextWithFont(sb.toString() + "", "", 16F, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        Log.v("2", sb.toString())


        sb.setLength(0)
    }


    var isLog = false

    fun doPrintJobSunmi(inv: InvoiceDto) {
        val divedeImage = BitmapFactory.decodeResource(resources, R.drawable.ic_line_print)
        val divide = "************************************************" + ""
        val divide2 = "-----------------------------------------------" + ""
        val fontsizeContent = 0
        val mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_print)


        //TODO:hard
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(0, printerCallBack)
//        sunmiPrinterService?.printTextWithFont("مؤسسة كوب و قطة للحيوانات الاليفة", "", 28f, printerCallBack)
        sunmiPrinterService?.printTextWithFont("${inv.legalName}", "", 28f, printerCallBack)

        if (isLog) print("${inv.legalName}\n")
        //TODO:hard
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont("${inv.legalCity}", "", 28f, printerCallBack)
        if (isLog) print("${inv.legalCity}\n")
        //TODO:hard
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont("${inv.legalAddress}", "", 28f, printerCallBack)
        if (isLog) print("${inv.legalAddress}\n")

        //TODO:hard
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(1, printerCallBack)
        // sunmiPrinterService?.printTextWithFont("VAT: ٣١٠٧٤٠٧٢٧٧", "", 28f, printerCallBack)

        sunmiPrinterService?.printQRCode(inv.orderId, 4, 1, printerCallBack)
        if (isLog) print("QR from ${inv.orderId}\n")

        sunmiPrinterService?.printTextWithFont("VAT: ${inv.innText}", "", 28f, printerCallBack)
        if (isLog) print("VAT: ${inv.innText}\n")

        sunmiPrinterService?.lineWrap(1, printerCallBack)
        //sunmiPrinterService?.printTextWithFont(inv.storeName, "", 28f, printerCallBack)

        sunmiPrinterService?.setFontSize(24F, printerCallBack)


        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.printText(inv.time + " " + inv.date, printerCallBack)
        if (isLog) print("${inv.time + " " + inv.date}\n")
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printBitmap(divedeImage, printerCallBack)
        if (isLog) print("***img***\n")

        sunmiPrinterService?.lineWrap(2, printerCallBack)

        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printText("Order #:  " + inv.orderId, printerCallBack)

        if (isLog) print("Order #:  " + inv.orderId + "\n")

        sunmiPrinterService?.printBitmap(divedeImage, printerCallBack)

        sunmiPrinterService?.lineWrap(1, printerCallBack)

        val width = divide2.length * 5 / 12
        val goods = formatTitle(width)


        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printTextWithFont(goods, "", 10F, printerCallBack)

        if (isLog) print("goods: \n" + goods + "\n")

        sunmiPrinterService?.lineWrap(1, printerCallBack)

        sunmiPrinterService?.printText(divide2, printerCallBack)
        sunmiPrinterService?.lineWrap(1, printerCallBack)

        printGoods(inv, fontsizeContent, divide2, 0, width)
        sunmiPrinterService?.lineWrap(3, printerCallBack)

        /* //TODO: remove hard
         sunmiPrinterService?.setAlignment(0, printerCallBack)
         sunmiPrinterService?.printTextWithFont("شكراً لزيارتكم ", "", 20F, printerCallBack)
         sunmiPrinterService?.lineWrap(1, printerCallBack)*/

        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont("Powered by Geidea ", "", 20F, printerCallBack)
        sunmiPrinterService?.setAlignment(1, printerCallBack)

        sunmiPrinterService?.printBitmap(mBitmap, printerCallBack)
        sunmiPrinterService?.lineWrap(3, printerCallBack)

        sunmiPrinterService?.cutPaper(printerCallBack)

    }


    private fun doPrintJob() {

        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printBarCode("1231324346", 8, 40, 2, 2, printerCallBack)
        sunmiPrinterService?.lineWrap(3, printerCallBack)



        addProductPrintLine("Product 1 long name long title etcsfsdfsdfsdfsdf", "99", "999 SAR")
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        addProductPrintLine("Product 1 long name long title etcss", "999", "1 SAR")
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        addProductPrintLine("Product 1 long name long", "23", "9 SAR")
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        addProductPrintLine("Product 1 long name sdfsdfsdfsdfsdfsd sdfsdfsdfsdf sdf sdf sdf", "1", "9999 SAR")
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        addProductPrintLine("Product 1 long name", "1", "0 SAR")


        sunmiPrinterService?.lineWrap(2, printerCallBack)


        // sunmiPrinterService?.setFontSize(24F, printerCallBack)
        sunmiPrinterService?.printTextWithFont("Test Order BIG\n", "", 30F, printerCallBack) // 35F
        sunmiPrinterService?.lineWrap(2, printerCallBack)

        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont("Powered by Geidea", "", 16F, printerCallBack) // 20F
        sunmiPrinterService?.setAlignment(1, printerCallBack)

        val mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_print)
        sunmiPrinterService?.printBitmap(mBitmap, printerCallBack)

        sunmiPrinterService?.lineWrap(3, printerCallBack)
        sunmiPrinterService?.cutPaper(printerCallBack)

    }

    fun addProductPrintLine(title: String, q: String, price: String) {
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(0, printerCallBack)
        sunmiPrinterService?.printTextWithFont(title, "", 18F, printerCallBack) // 22F
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(2, printerCallBack)
        sunmiPrinterService?.printTextWithFont(" x$q ", "", 16F, printerCallBack) // 20F
        sunmiPrinterService?.setAlignment(2, printerCallBack)
        sunmiPrinterService?.printTextWithFont(price, "", 20F, printerCallBack) // 24F
        sunmiPrinterService?.lineWrap(1, printerCallBack)
        sunmiPrinterService?.setAlignment(1, printerCallBack)
        val mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_line_print)
        sunmiPrinterService?.printBitmap(mBitmap, printerCallBack)


    }

    fun openDrawerSunmi() {

        sunmiPrinterService?.openDrawer(printerCallBack)
    }


}