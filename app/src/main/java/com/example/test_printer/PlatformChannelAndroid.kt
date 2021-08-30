package com.example.test_printer

import com.google.gson.Gson
import java.io.Serializable


data class ChargeDto(
    var amount: String,
    var ip: String,
    var port: Int,
) : Serializable


data class InvoiceDto(

    var legalName: String?,
    var legalAddress: String?,
    var legalCity: String?,
    var innText: String?,
    var receiptTopText: List<InvoiceItem>,

    var receiptBottomText: String?,
    var storeName: String?,
    var time: String?,
    var date: String?,
    var orderId: String?,
    var goods: List<InvoiceItem>,
    var subTotal: String?,
    var total: String?,
    var cash: String?,
    var vat: String?,
    var change: String?,
) : Serializable

data class InvoiceItem(
    var title: String,
    var price: String,
    var q: String,
    var p: String,
) : Serializable


