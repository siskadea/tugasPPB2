package com.siskadea.tes

class data_mahasiswa {
    //Deklarasi Variable
//    var nim: String? = null
//    var nama: String? = null
//    var jurusan: String? = null
//    var key: String? = null
//    //Membuat Konstuktor kosong untuk membaca data snapshot constructor() {}
//    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
//    constructor(nim: String?, nama: String?, jurusan: String?) {
//        this.nim = nim
//        this.nama = nama
//        this.jurusan = jurusan
//    }

    //Deklarasi Variable
    var nim: String? = null
    var nama: String? = null
    var jurusan: String? = null
    var alamat: String? = null
    var jenis_kelamin: String? = null
    var key: String? = null
    //Membuat Konstuktor kosong untuk membaca data snapshot
    constructor() {}
    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User

    constructor(nim: String?, nama: String?, jurusan: String?, alamat:String?, jenis_kelamin:String?) {
        this.nim = nim
        this.nama = nama
        this.jurusan = jurusan
        this.alamat = alamat
        this.jenis_kelamin = jenis_kelamin
    }
}