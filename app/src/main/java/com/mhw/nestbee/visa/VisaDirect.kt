package com.mhw.nestbee.visa

import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yarolegovich on 08.10.2016.
 */
class VisaDirect(val accountNumber: String, val expirationDate: String) {

    companion object {
        private val DAY_OF_YEAR = SimpleDateFormat("ddd", Locale.US)
        private val TIMESTAMP = SimpleDateFormat("YYYY-MM-DDThh:mm:ss", Locale.US)

        private val ACQUIRING_BIN = "408999"
        private val COUNTRY_CODE = 804
        private val CURRENCY_CODE = "UAH"

        private val BUSINESS_APP_ID = "PP"

        private val CARD_ACCEPTOR_NAME = "Acceptor 1"
        private val CARD_ACCEPTOR_TERM_ID = "365539"
        private val CARD_ACCEPTOR_ID_CODE = "VMT200911026070"
    }

    fun transfer(to: String, amount: String) {
        val payload = JSONObject().put("SystemsTraceAuditNumber", systemTraceAuditNumber())
                .put("RetrievalReferenceNumber", retrievalReferenceNumber(systemTraceAuditNumber()))
                .put("DateAndTimeLocalTransaction", dateAndTimeLocalTransaction())
                .put("AcquiringBin", ACQUIRING_BIN)
                .put("AcquirerCountryCode", COUNTRY_CODE)
                .put("SenderPrimaryAccountNumber", accountNumber)
                .put("SenderCardExpiryDate", expirationDate)
                .put("SenderCurrencyCode", CURRENCY_CODE)
                .put("Amount", amount)
                .put("BusinessApplicationID", BUSINESS_APP_ID)
                .put("CardAcceptor", JSONObject()
                        .put("Name", CARD_ACCEPTOR_NAME)
                        .put("TerminalId", CARD_ACCEPTOR_TERM_ID)
                        .put("IdCode", CARD_ACCEPTOR_ID_CODE))
    }

    fun systemTraceAuditNumber(): Int {
        val r = Random()
        return (1..6).map { Math.abs(r.nextInt(9)) + 1 }
                .map(Int::toString)
                .reduce { acc, num -> acc + num }
                .toInt()
    }

    fun retrievalReferenceNumber(auditNumber: Int): String {
        val lastDigitOfYear = Calendar.getInstance()[Calendar.YEAR] % 10
        val dayOfYear = DAY_OF_YEAR.format(System.currentTimeMillis())
        return lastDigitOfYear.toString() + dayOfYear + auditNumber.toInt()
    }

    fun dateAndTimeLocalTransaction() = TIMESTAMP.format(System.currentTimeMillis())


}