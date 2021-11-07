package com.udacoding.pos.ui.transaction

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.udacoding.pos.R
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.DialogPrintBinding
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.ui.pdfresult.ResultFileActivity
import com.udacoding.pos.ui.transaction.model.Data
import com.udacoding.pos.utils.Common
import com.udacoding.pos.utils.formatDate
import com.udacoding.pos.utils.formatDate1
import com.udacoding.pos.utils.toRupiah
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SuccessCheckoutFragment(
    val data_transaction: Data?,
) : BottomSheetDialogFragment() {

    lateinit var binding: DialogPrintBinding

    val file_name = "${data_transaction?.code}File.pdf"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DialogPrintBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPDF.setOnClickListener {
            createPdfFile(activity?.applicationContext?.let { it1 -> Common.getAppPath(it1) } + file_name)
        }

    }

    private fun createPdfFile(path: String) {
        if (File(path).exists())
            File(path).delete()
        try {
            val document = Document()
            //save
            PdfWriter.getInstance(document, FileOutputStream(path))
            //open
            document.open()
            //setting
            document.apply {
                pageSize = PageSize.A4
                addCreationDate()
                addAuthor("Udacoding")
                addCreator("Udacoding")
            }

            //font setting
            val colorAccent = BaseColor(0, 153, 204, 225)
            val headingFontSize = 10.0f
            val valueFontSize = 13.0f

            //custom font
//            val fontName = BaseFont.createFont("assets/fonts/brandon.otf", "UTF-8",BaseFont.EMBEDDED)

            //title document
            val titleStyle = Font(Font.FontFamily.TIMES_ROMAN, 13.0f, Font.NORMAL, BaseColor.BLACK)
            val titleStyleColorBlue =
                Font(Font.FontFamily.TIMES_ROMAN, 13.0f, Font.NORMAL, colorAccent)
            addNewitem(document, "Receipt Transaction", Element.ALIGN_CENTER, titleStyleColorBlue)
            addLineSpace(document)

            val headingStyle =
                Font(Font.FontFamily.TIMES_ROMAN, headingFontSize, Font.NORMAL, colorAccent)
            addNewitem(document, "Code:", Element.ALIGN_LEFT, headingStyle)

            val valueStyle =
                Font(Font.FontFamily.TIMES_ROMAN, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addNewitem(document, "${data_transaction?.code}", Element.ALIGN_LEFT, valueStyle)

            addLineSeparator(document)

            addNewitem(document, "User:", Element.ALIGN_LEFT, headingStyle)
            addNewitem(
                document,
                "${data_transaction?.user?.fullName}",
                Element.ALIGN_LEFT,
                valueStyle
            )

            addLineSeparator(document)

            addNewitem(document, "Customer:", Element.ALIGN_LEFT, headingStyle)
            addNewitem(
                document,
                "${data_transaction?.customer?.name}",
                Element.ALIGN_LEFT,
                valueStyle
            )

            addLineSeparator(document)

            addNewitem(document, "Phone Number:", Element.ALIGN_LEFT, headingStyle)
            addNewitem(
                document,
                "${data_transaction?.customer?.phoneNumber}",
                Element.ALIGN_LEFT,
                valueStyle
            )

            addLineSeparator(document)

//            val df = SimpleDateFormat("dd.MM.yyyy HH:mm:SS")
            val formattedDate: String = formatDate1(data_transaction?.createdAt)

            addNewitem(document, "Date:", Element.ALIGN_LEFT, headingStyle)
            addNewitem(
                document,
                "${formattedDate}",
                Element.ALIGN_LEFT,
                valueStyle
            )

            addLineSeparator(document)

            //Product Detail
            addLineSpace(document)
            addNewitem(document, "Product Details", Element.ALIGN_CENTER, titleStyle)

            addLineSeparator(document)

            //item1
            for (i in data_transaction?.detail?.indices ?: 0..1) {
                val item = data_transaction?.detail
                addNewItemWithLeftAndRight(
                    document,
                    "${item?.get(i)?.product} x ${item?.get(i)?.qty}",
                    toRupiah(item?.get(i)?.total?.toDouble()),
                    titleStyle,
                    valueStyle
                )
            }

            addLineSeparator(document)

            //total
            addLineSpace(document)
            addLineSpace(document)

            addNewItemWithLeftAndRight(
                document,
                "Total",
                data_transaction?.paymentMethod + " " + toRupiah(data_transaction?.totalPrice?.toDouble()),
                titleStyle,
                valueStyle
            )

            addLineSeparator(document)

            var note = "-"
            if (data_transaction?.note != null || data_transaction?.note != "") note = data_transaction?.note.toString()

            addNewitem(document, "Note:", Element.ALIGN_LEFT, headingStyle)
            addNewitem(
                document,
                note,
                Element.ALIGN_LEFT,
                valueStyle
            )

            addLineSeparator(document)

            //close
            document.close()

            Toast.makeText(activity?.applicationContext, "Success generate pdf", Toast.LENGTH_SHORT)
                .show()

            val intent = Intent(activity?.applicationContext, ResultFileActivity::class.java)
            intent.putExtra("path", path)
            intent.putExtra("phone", data_transaction?.customer?.phoneNumber)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } catch (e: Exception) {
            Log.d("TAG", "createPdfFile: ${e.message}")
            Toast.makeText(
                activity?.applicationContext,
                "createPdfFile: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(
        document: Document,
        textLeft: String,
        textRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {

        val chunkTextLeft = Chunk(textLeft, leftStyle)
        val chunkTextRight = Chunk(textRight, rightStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)


    }

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewitem(document: Document, s: String, align: Int, style: Font) {
        val chunk = Chunk(s, style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)

    }

    fun message(msg: String) {
        Toast.makeText(activity?.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}