package com.calen_day.qanket

open class VacReplacer {
    var vac = mutableMapOf<String,String>("а" to "a","б" to "b","в" to "v","г" to "g","д" to "d","е" to "e","ё" to "ё","ж" to "1","з" to "z","и" to "i","й" to "2","к" to "k","л" to "l","м" to "m",
        "н" to "n","о" to "o","п" to "p","р" to "r","с" to "s","т" to "t","у" to "y","ф" to "f","х" to "x","ц" to "c","ч" to "3","ш" to "4","щ" to "5","ъ" to "6","ы" to "7",
        "ь" to "8","э" to "9","ю" to "u","я" to "0",
        "А" to "A","Б" to "B","В" to "V","П" to "G","Д" to "D","Е" to "E","Ё" to "Ё","Ж" to "Ж","З" to "Z","И" to "I","Й" to "Й","К" to "K","Л" to "L","М" to "M",
        "Н" to "N","О" to "O","П" to "P","Р" to "R","С" to "S","Т" to "T","У" to "Y","Ф" to "F","Х" to "X","Ц" to "C","Ч" to "Ч","Ш" to "Щ","Щ" to "Щ","Ъ" to "Ъ","Ы" to "Ы",
        "Ь" to "Ь","Э" to "Э","Ю" to "U","Я" to "Я");
    var vacR = mutableMapOf<String,String>("a" to "а","b" to "б","v" to "в","g" to "г","d" to "д","e" to "е","ё" to "ё","1" to "ж","z" to "з","i" to "и","2" to "й","k" to "к","l" to "л","m" to "м","n" to "н","o" to "о",
        "p" to "п","r" to "р","s" to "с","t" to "т","y" to "у","f" to "ф","x" to "х","c" to "ц","3" to "ч","4" to "ш","5" to "щ","6" to "ъ","7" to "ы","8" to "ь","9" to "э","u" to "ю","0" to "я",
        "A" to "А","B" to "Б","V" to "В","P" to "П","D" to "Д","E" to "Е","Ё" to "Ё","Ж" to "Ж","Z" to "З","I" to "И","Й" to "Й","K" to "К","L" to "Л","M" to "М","N" to "Н","O" to "О","R" to "Р",
        "S" to "С","T" to "Т","Y" to "У","F" to "Ф","X" to "Х","C" to "Ц","Ч" to "Ч","Щ" to "Ш","Щ" to "Щ","Ъ" to "Ъ","Ы" to "Ы","Ь" to "Ь","Э" to "Э","U" to "Ю","Я" to "Я");
    var nums = mutableListOf<String>("0","1","2","3","4","5","6","7","8","9");
    var lat = mutableListOf<String>("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9");
    var kir = mutableListOf<String>("а","б","в","г","д","е","ё","ж","з","и","й","к","л","м","н","о", "п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я", "А","Б","В","П","Д","Е","Ё","Ж","З","И","Й","К","Л","М","Н","О","Р", "С","Т","У","Ф","Х","Ц","Ч","Ш","Щ","Ъ","Ы","Ь","Э","Ю","Я");
    fun replacePismeno(txt:String):String {
        var nText="";
        for (t in txt) {
            if (kir.contains(t.toString())) {
                for(v in vac){
                    if(t.equals(v)){
                        nText = nText + t.toString().replace(v.toString(), vac[v.toString()].toString());
                    }
                }
            }else{
                nText = nText + t;
            }
        }
        return nText;
    }
    fun replacePismenoR(txt:String):String{
        var nText="";
        try{
            var start=false;
            var i=0;
            while(i<txt.length){
            //for(i in 0..txt.length){
                var t=txt[i];
                if(i+1>=txt.length) {
                }else {
                    if(txt[i].toString() == "÷") {
                        if(txt[i+1].toString() == "_") {
                            start = true;
                        }
                    }
                    if (txt[i].toString() == "_") {
                        if(txt[i + 1].toString() == "÷") {
                            start = false;
                        }
                    }
                }
                if (start) {
                    nText = nText + t;
                }else {
                    //println(nText+"<<<");
                    if(!lat.contains(t.toString())) {
                        nText = nText + t;
                    }else {
                        for(v in lat) {
                            if(v==t.toString()) {
                                nText = nText + t.toString().replace(v, vacR.get(v).toString());
                            }
                        }
                    }
                }
                i++
            }
            nText=nText.replace("÷_","").replace("_÷","")
        }catch (e:Exception){
            print(e.toString()+"err<<<")
        }
        return nText;
    }
    fun ekr(txt:String):String{
        var nText="";
        var start=false;
        for(t in txt) {
            if (nums.contains(t.toString()) && !start || lat.contains(t.toString())&& !start) {
                nText = nText + "÷_" + t;
                start = true;
            }else if(start){
                if(nums.contains(t.toString()) || lat.contains(t.toString())) {
                    nText = nText + t;
                }else {
                    nText = nText + "_÷" + t;
                    start = false;
                }
            }
            else {
                nText = nText + t;
            }
        }
        if(nText.length>=2) {
            if(nums.contains(nText[nText.length - 1].toString()) || lat.contains(nText[nText.length - 1].toString())){
                nText = nText + "_÷"
            }
        }
        return nText;
    }
}