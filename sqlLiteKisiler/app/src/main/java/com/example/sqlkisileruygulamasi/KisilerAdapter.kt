package com.example.sqlkisileruygulamasi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class KisilerAdapter(private val mContext : Context
                , private var kisilerListe : List<Kisiler>
                ,private val vt:VeriTabaniYardimcisi)
    : RecyclerView.Adapter<KisilerAdapter.CartTasarimTutucu>(){


    inner class CartTasarimTutucu(tasarim : View) : RecyclerView.ViewHolder(tasarim){

        var textViewkisibilgi : TextView
        var imageViewNokta : ImageView

        init {

            textViewkisibilgi = tasarim.findViewById(R.id.textviewKisiBilgi)
            imageViewNokta = tasarim.findViewById(R.id.imageViewNokta)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.kisi_card_tasarim,parent,false)
        return CartTasarimTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: CartTasarimTutucu, position: Int) {

        val kisi = kisilerListe.get(position)

        holder.textViewkisibilgi.text = "${kisi.kisi_adi} - ${kisi.kisi_tel}"
        holder.imageViewNokta.setOnClickListener {

            val popupmenu = PopupMenu(mContext,holder.imageViewNokta)
            popupmenu.menuInflater.inflate(R.menu.popup_menu,popupmenu.menu)

            popupmenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){

                    R.id.action_sil -> {
                        Snackbar.make(holder.itemView,"${kisi.kisi_adi} silinsin mi ?",Snackbar.LENGTH_LONG)
                            .setAction("EVET"){
                                KisilerDao().kisiSil(vt,kisi.kisi_id)
                                kisilerListe = KisilerDao().tumKisiler(vt)
                                notifyDataSetChanged()


                            }.show()

                        true

                    }
                    R.id.action_guncelle -> {

                        alertGoster(kisi)
                        true

                    }
                    else -> false

                }

            }

            popupmenu.show()


        }
    }



    override fun getItemCount(): Int {
        return kisilerListe.size
    }




    fun alertGoster(kisi : Kisiler){

        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
        val editTextad = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        editTextad.setText(kisi.kisi_adi)
        editTextTel.setText(kisi.kisi_tel)

        val ad = AlertDialog.Builder(mContext)
        ad.setTitle("Kişi Güncelle")
        ad.setView(tasarim)
        ad.setPositiveButton("Güncelle"){ dialogInterface, i ->

            val kisi_ad = editTextad.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            KisilerDao().kisiGuncelle(vt,kisi.kisi_id,kisi_ad,kisi_tel)
            kisilerListe = KisilerDao().tumKisiler(vt)
            notifyDataSetChanged()

            Toast.makeText(mContext, "$kisi_ad - $kisi_tel", Toast.LENGTH_SHORT).show()


        }
        ad.setNegativeButton("İptal"){dialogInterface, i ->



        }
        ad.create().show()


    }
}