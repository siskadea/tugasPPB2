package com.siskadea.tes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MyListData : AppCompatActivity(), RecyclerViewAdapter.dataListener {
    //Deklarasi Variable untuk RecyclerView
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
//Deklarasi Variable Database Reference & ArrayList dengan Parameter Class Model kita.
    val database = FirebaseDatabase.getInstance("https://tes1-a7a20-default-rtdb.asia-southeast1.firebasedatabase.app/")
//    val database = FirebaseDatabase.getInstance()
    private var dataMahasiswa = ArrayList<data_mahasiswa>()
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)
        recyclerView = findViewById(R.id.datalist)
        supportActionBar!!.title = "Data Mahasiswaku"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }
    //Baris kode untuk mengambil data dari Database & menampilkan kedalam Adapter
    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...",
            Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("Mahasiswa")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            val mahasiswa =
                                snapshot.getValue(data_mahasiswa::class.java)
                            //Mengambil Primary Key, digunakan untuk proses Update/Delete
                            mahasiswa?.key = snapshot.key
                            dataMahasiswa.add(mahasiswa!!)
                        }
                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = RecyclerViewAdapter(dataMahasiswa, this@MyListData)
                        //Memasang Adapter pada RecyclerView
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext,"Data Berhasil Dimuat",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Kode ini akan dijalankan ketika ada error, simpan ke LogCat
                    Toast.makeText(applicationContext, "Data Gagal Dimuat",
                        Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " +
                            databaseError.message)
                }
            })
    }


    //    private fun GetData() {
//
//        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...",
//            Toast.LENGTH_LONG).show()
//        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
//        val getReference = database.getReference()
//
//        getReference.child("Admin").child(getUserID).child("Mahasiswa")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for (snapshot in dataSnapshot.children) {
////Mapping data pada DataSnapshot ke dalam objek mahasiswa
//                            val mahasiswa = snapshot.getValue(data_mahasiswa::class.java)
////Mengambil Primary Key, digunakan untuk proses Update/Delete
//                            mahasiswa?.key = snapshot.key
//                            dataMahasiswa.add(mahasiswa!!)
//                        }
////Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
//                        adapter = RecyclerViewAdapter(dataMahasiswa, this@MyListData)
////Memasang Adapter pada RecyclerView
//                        recyclerView?.adapter = adapter
//                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
//                        Toast.makeText(applicationContext,"Data Berhasil Dimuat",
//                            Toast.LENGTH_LONG).show()
//                    }
//                }
//                override fun onCancelled(databaseError: DatabaseError) {
//// Kode ini akan dijalankan ketika ada error, simpan ke LogCat
//                    Toast.makeText(applicationContext, "Data Gagal Dimuat",
//                        Toast.LENGTH_LONG).show()
//                    Log.e("MyListActivity", databaseError.details + " " +
//                            databaseError.message)
//                }
//            })
//
//    }
    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private fun MyRecyclerView() {
//Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
//Membuat Underline pada Setiap Item Didalam List
        val itemDecoration = DividerItemDecoration(applicationContext,
            DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(applicationContext,
            R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_mahasiswa?, position: Int) {
        /* Kode ini akan dipanggil ketika method onDeleteData dipanggil dari adapter
        * pada RecyclerView melalui interface. kemudian akan menghapus data berdasarkan
        * primary key dari data tersebut Jika berhasil, maka akan memunculkan Toast */
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        //val getKey = intent.extras!!.getString("getPrimaryKey")
        val getKey = data?.key.toString()
        if(getReference != null){
            getReference.child("Admin")
                .child(getUserID)
                .child("Mahasiswa")
                .child(getKey!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData, "Data Berhasil Dihapus",
                        Toast.LENGTH_SHORT).show();
                }
        } else {
            Toast.makeText(this@MyListData, "Reference kosong",
                Toast.LENGTH_SHORT).show()
        }
    }

}