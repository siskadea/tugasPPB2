package com.siskadea.tes

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText


class MainActivity : AppCompatActivity(), View.OnClickListener{
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        showdata.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private  fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View){
        when(v.getId()){
            R.id.save -> {
            //UNTUK SIMPAN DATA
                // Statement program untuk simpan data
//Mendapatkan UserID dari pengguna yang Terautentikasi
                val getUserID = auth!!.currentUser!!.uid
//Mendapatkan Instance dari Database
                val database = FirebaseDatabase.getInstance("https://tes1-a7a20-default-rtdb.asia-southeast1.firebasedatabase.app/")
//Menyimpan Data yang diinputkan User kedalam Variable
                
                val getNIM: String = nim.getText().toString()
                val getNama: String = nama.getText().toString()
                val getJurusan: String = jurusan.getText().toString()
                val getAlamat: String = alamat.getText().toString()
                val getJk: String = jenis_kelamin.getText().toString()
                
// Mendapatkan Referensi dari Database
                
                val getReference: DatabaseReference
                getReference = database.reference
                if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)) {
//Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                        .setValue(data_mahasiswa(getNIM, getNama, getJurusan, getAlamat, getJk))
                        .addOnCompleteListener(this) { //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            nim.setText("")
                            nama.setText("")
                            jurusan.setText("")
                            alamat.setText("")
                            jenis_kelamin.setText("")
                            Toast.makeText(this@MainActivity, "Data Tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout ->
                //UNTUK KELUAR
                AuthUI.getInstance().signOut(this).addOnCompleteListener(object : OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout Berhasil",
                        Toast.LENGTH_SHORT).show()
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                })

            R.id.showdata ->{
            //UNTUK MENAMPILKAN DATA
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}