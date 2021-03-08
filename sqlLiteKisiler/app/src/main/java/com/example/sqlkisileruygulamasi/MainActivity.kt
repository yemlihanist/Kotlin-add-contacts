package com.example.sqlkisileruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

        private lateinit var kisilerListe : ArrayList<Kisiler>
        private lateinit var adapter : KisilerAdapter
        private lateinit var  vt : VeriTabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        toolbar.title = "Kişiler Ugulaması"
        setSupportActionBar(toolbar)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        vt = VeriTabaniYardimcisi(this)

        tumKisilerAl()



        fab.setOnClickListener {
            alertGoster()


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)

        val item = menu?.findItem(R.id.action_ara)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }
    fun alertGoster(){

        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarim,null)
        val editTextad = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText


        val ad = AlertDialog.Builder(this)
        ad.setTitle("Kişi Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){ dialogInterface, i ->

            val kisi_ad = editTextad.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()
            KisilerDao().kisiEkle(vt,kisi_ad,kisi_tel)
            tumKisilerAl()

            Toast.makeText(applicationContext, "$kisi_ad - $kisi_tel", Toast.LENGTH_SHORT).show()


        }
        ad.setNegativeButton("İptal"){dialogInterface, i ->



        }
        ad.create().show()


    }

    override fun onQueryTextSubmit(query: String): Boolean {
        aramaYap(query)
        Log.e("Gönderilen arama",query!!)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        aramaYap(newText)
        Log.e("Harf Girdikçe",newText!!)
        return true
    }

    fun tumKisilerAl(){


        kisilerListe = KisilerDao().tumKisiler(vt)

        adapter = KisilerAdapter(this,kisilerListe,vt)
        rv.adapter = adapter

    }
    fun aramaYap(aramaKelime : String){


        kisilerListe = KisilerDao().kisiAra(vt,aramaKelime)

        adapter = KisilerAdapter(this,kisilerListe,vt)
        rv.adapter = adapter

    }
}