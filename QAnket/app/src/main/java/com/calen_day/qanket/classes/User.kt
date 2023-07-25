package com.calen_day.qanket.classes

import android.graphics.Bitmap

class User {
    var changed: String
    var id: String
    var login: String
    var pswd: String
    var work: String
    var session: String
    var country:String
    var sex: String
    var isDoc: String
    var phone: String
    var imgLink: String?=null;
    var imgBit: Bitmap?=null;
    var firstname: String
    var lastname: String
    var year: String
    var adres: String
    var prof: String
    constructor(
        _changed : String,
        _id: String,
        _login: String,
        _pswd: String,
        _work: String,
        _session: String,
        _country: String,
        _sex: String,
        _isDoc: String,
        _phone: String,
        _imgLink: String,
        _firstname: String,
        _lastname: String,
        _year: String,
        _adres: String,
        _prof: String,
    ) {
        changed = _changed
        id = _id
        login = _login
        pswd = _pswd
        work = _work
        session = _session
        country = _country
        sex = _sex
        isDoc = _isDoc
        phone = _phone
        imgLink = _imgLink
        firstname = _firstname
        lastname = _lastname
        year = _year
        adres = _adres
        prof = _prof
    }
    constructor() {
        changed = "0"
        id = "0"
        login = "0"
        pswd = "0"
        work = "0"
        session = "0"
        country = "0"
        sex = "0"
        isDoc = "0"
        phone = "0"
        imgLink = "0"
        firstname = "0"
        lastname = "0"
        year = "0"
        adres = "0"
        prof = "0"
    }
}