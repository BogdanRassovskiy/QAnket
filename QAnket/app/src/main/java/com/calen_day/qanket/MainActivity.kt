package com.calen_day.qanket

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.calen_day.qanket.classes.Lang
import com.calen_day.qanket.classes.Sick
import com.calen_day.qanket.classes.SickHelper
import com.calen_day.qanket.classes.User
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.frag_enter.*
import kotlinx.android.synthetic.main.frag_reg.*
import kotlinx.android.synthetic.main.frag_scanner.view.*
import org.json.JSONObject
import pacientAdapter
import sickAdapter
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.Collections.min
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var global_url = "http://143.110.224.27:2135/"
    //var global_url = "http://10.68.2.91:8005/";

    var AppVersion="1";
    var choosenPacient=User();
    var choosenSick=Sick();
    var ActualVersion=AppVersion;
    var idList = mutableListOf<String>();
    var sendData:Map<*,*> = mutableMapOf("ne" to "ne");
    var sendSickData:Map<*,*> = mutableMapOf("ne" to "ne");
    var sendUpdateData:Map<*,*> = mutableMapOf("ne" to "ne");
    var numZip:Map<*,*> = mutableMapOf(" " to " ");
    var Users:ArrayList<User> = ArrayList<User>();
    var UsersSearch:ArrayList<User> = ArrayList<User>();
    var Langs:ArrayList<Lang> = ArrayList<Lang>();
    var Sicks:ArrayList<Sick> = ArrayList<Sick>();
    var SicksSearch:ArrayList<Sick> = ArrayList<Sick>();
    var myUser=User();
    var step="enter";
    var shtor=false;
    var countryList:ArrayList<String> = ArrayList();
    var nums = mutableListOf<String>("0","1","2","3","4","5","6","7","8","9");
    var avatarPhoto=1;
    var bitImage: Bitmap? = null
    var qrImage: Bitmap? = null
    var sendArr:ArrayList<String> = ArrayList();
    var animDo=false;
    var printDo=false;
    var animCount=0;
    var workMode=0
    var updateSleep=5000;
    var toastText="";
    var bark=0;
    var backUpUser=User();
    var backUpSick=Sick();
    var sickWriteMode=0;
    var sickHelper=SickHelper();
    var mkbList = mutableListOf<String>();
    var mkbArray = arrayOf("Хитрожопость");
    var sickHelperList = mutableListOf<Any>();
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        enter();
        countryGet();
        startCheckWorkMode();


    }

    fun backButtons(){
        println(step+" Step<<<")
        plus_rem();
        if(step=="sickHelpBtn"){rem_anim();};
        when(step){
            "tryReg"->
                wChange({enter()});
            "mainMenu"->
                wChange({enter()});
            "mySettings"-> {
                openShtor()
                wChange({ mainMenu() })
            }
            "docInterface"->
                wChange({mainMenu()});
            "editInfoBox"->
                wChange({mySettings()});
            "changePhoto"->
                wChange({mySettings()});
            "myQr"->
                wChange({mainMenu()});
            "thisPacient"->
                wChange({docInterface()});
            "sicks"->
                wChange({thisSick(s=choosenSick)});
            "thisSick"->
                wChange({thisPacient(choosenPacient.id)});
            "mySickStories"->
                wChange({mainMenu()});
            "sickHelpBtn"-> {
                rem_anim()
                step="thisSick";
            }

        }
    }
    fun placeButtons(v:View) {
        try {
            if(step=="sickHelpBtn"){rem_anim();};
            if(step!="mainMenu"){
                workMode = 0;
                var place=0;
                when (v.id) {
                    R.id.place1->place=1;
                    R.id.place2->place=2;
                    R.id.place3->place=3;
                    R.id.place4->place=4;
                    R.id.place5->place=5;
                    R.id.place6->place=6;
                    R.id.place7->place=7;
                    R.id.place8->place=8;
                    R.id.place9->place=9;
                    R.id.place10->place=10;
                    R.id.place11->place=11;
                    R.id.place12->place=12;
                    R.id.place13->place=13;
                    R.id.place14->place=14;
                    R.id.place15->place=15;
                    R.id.place16->place=16;
                    R.id.place17->place=17;
                    R.id.place18->place=18;
                    R.id.place19->place=19;
                    R.id.place20->place=20;
                    R.id.place21->place=21;
                    R.id.place22->place=22;
                    R.id.place23->place=23;
                }
                for(i in 0..Sicks.size-1){
                    if(Sicks[i].id==choosenSick.id){
                        Sicks[i].place=place.toString();
                        choosenSick.place=place.toString();
                        break;
                    }
                }
                println(place.toString()+" <<<place");
                if(sickWriteMode==0){
                    sendData = mutableMapOf("session" to myUser.session,"tag" to "sickPlace" ,"val" to place.toString(),"sickId" to choosenSick.id);
                    animCountPlus();
                    sendSick();
                }else if(sickWriteMode==1){
                    tprint(w("Сохранено"));
                    thisSick(s=choosenSick);
                }
            }else{
                tprint("ok")
            }
        } catch (e: Exception) {
            logger(e);
        }
    }
    fun sickHelpBtn(v:View){
        step="sickHelpBtn";
        println(mkbList.size.toString()+"<<<mkb");
        try {
            workMode = 0;
            var txt="";
            var s=sickHelper;
            when (v.id) {
                R.id.sickCheck1->txt=s.passPart
                R.id.sickCheck2->txt=s.complaints
                R.id.sickCheck3->txt=s.HistoryOfUnderlyingAndComorbidities
                R.id.sickCheck4->txt=s.AnamnesisOfLife
                R.id.sickCheck5->txt=s.DataFromAnObjectiveStudyOfThePatient
                R.id.sickCheck6->txt=s.SubstantiationOfThePreliminaryDiagnosisAndItsFormulation
                R.id.sickCheck7->txt=s.SurveyPlan
                R.id.sickCheck8->txt=s.DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants
                R.id.sickCheck9->txt=s.FinalClinicalDiagnosis
                R.id.sickCheck10->txt=s.DifferentialDiagnosis
                R.id.sickCheck11->txt=s.EtiologyAndPathogenesis
                R.id.sickCheck12->txt=s.PathologicalChangesInOrgans
                R.id.sickCheck13->txt=s.TreatmentOfTheUnderlyingDisease
                R.id.sickCheck14->txt=s.TreatmentOfThePatientAndItsRationale
                R.id.sickCheck15->txt=s.Forecast
                R.id.sickCheck16->txt=s.Prevention
                R.id.sickCheck17->txt=s.Epicrisis
                R.id.sickCheck18->txt=s.CurationDiary
                R.id.sickCheck19->txt=s.ListOfUsedLiterature
            }
            animInflate(R.layout.frag_sick_help);
            val animAlpha = AnimationUtils.loadAnimation(
                applicationContext, R.anim.anim_translate_down_back
            )
            val con: ConstraintLayout = findViewById(R.id.sickHelperConstr);
            con.startAnimation(animAlpha)
            txtWrite(R.id.helpText,txt);
        }catch (e:Exception){
            logger(e);
        }
    }
    fun Buttons(v: View){
        try{
            workMode=0;
            val func1: () -> Unit = { tryEnter()}
            val func2: () -> Unit = { tryReg()}
            if(step=="sickHelpBtn"){rem_anim();};
            when(v.id){
                R.id.enterBtn->
                    wChange(func1);
                R.id.regBtn->
                    wChange(func2);
                R.id.makeRegistration->
                    makeRegistration();
                R.id.imageManWoman->
                    print("");
                R.id.setShtor->
                    setShtor();
                R.id.setShtor2->
                    setShtor();
                R.id.mySettings->
                    wChange({mySettings()});
                R.id.saveSex->
                    getSaveInfo("sex");
                R.id.editLoginBtn->
                    wChange({ editInfoBox("login") });
                R.id.editPswdBtn->
                    wChange({ editInfoBox("pswd") });
                R.id.editPhoneBtn->
                    wChange({ editInfoBox("phone") });
                R.id.editFirstnameBtn->
                    wChange({ editInfoBox("firstname") });
                R.id.editLastnameBtn->
                    wChange({ editInfoBox("lastname") });
                R.id.editYearBtn->
                    wChange({ editInfoBox("year") });
                R.id.saveInfo->
                    saveInfoBox(v);
                R.id.saveCountry->
                    saveCountry();
                R.id.avatar->
                    chooseAvatarPhoto();
                R.id.beADoctor->
                    beADoctorSend();
                R.id.bottomBtn2->{
                    closeShtor();
                    if(step!="enter"){
                        wChange({ mainMenu() });
                    }
                }
                R.id.docInterface-> {
                    if (shtor) {
                        closeShtor();
                    }
                    sickWriteMode=0;
                    wChange({ docInterface() });
                }
                R.id.logoView->
                    animDo=false;
                R.id.qrPacSearch->
                    qrPacSearch();
                R.id.pacSearch->
                    pacSearch();
                R.id.myQr->
                    wChange({myQr()});
                R.id.thisPacient->
                    wChange({thisPacient(v.tag.toString())});
                R.id.sickSearch->
                    thisPacientInflate()
                R.id.thisSick->
                    wChange({if(sickWriteMode==1){sickWriteMode=0};thisSick(v);});
                    R.id.saveSick->
                    wChange({saveSick(v);});
                R.id.plusSick->
                    wChange({plusSick()});
                R.id.clearNewSick->
                    clearNewSick();
                R.id.saveNewSick->
                    saveNewSick();
                R.id.bottomBtn1->
                    mySickStories();
                else ->
                    tprint("without btn");
            }
        }catch (e:Exception){
            logger(e);
        }
    }
    fun mySickStories(){
        step="mySickStories";
        sickWriteMode=3;
        thisPacient(myUser.id);
    }
    fun saveNewSick(){
        var sick=SickToList(sickById("0"));
        print(sick+"<<<");
        sendData = mutableMapOf("session" to myUser.session,"tag" to "all","val" to sick,"sickId" to "0");
        animCountPlus();
        sendSick();
    }
    fun clearNewSick(){
        for(i in 0..Sicks.size-1){
            if(Sicks[i].id=="0"){
                Sicks[i]= Sick();
                break;
            }
        }
        tprint(w("Очищено"));
        plusSick();
    }
    fun plusSick(){
        step="plusSick";
        choosenSick=Sick();
        choosenSick.id="0";
        choosenSick.doc=myUser.id;
        choosenSick.owner=choosenPacient.id;
        sickWriteMode=1;
        var sickHave=false;
        for(s in Sicks){
            if(s.id=="0"){
                sickHave=true;
                break;
            }
        }
        if(sickHave){
        }else{
            Sicks.add(choosenSick);
            choosenSick.passPart=choosenPacient.firstname+"\n"+
                    choosenPacient.lastname+"\n"+
                    choosenPacient.year
        }
        println(Sicks.size.toString()+"<<<");
        thisSick(s=choosenSick);
        if(sickWriteMode!=3){
            plus_inflate(R.layout.plus_new_sick);
        }
    }
    fun saveSick(v:View){
        var txt="";
        if(v.tag=="FinalClinicalDiagnosis"||v.tag=="DifferentialDiagnosis"){
            txt=autoRead(R.id.editInfoEd)
        }else{
            txt=txtRead(R.id.editInfoEd)
        }
        backUpSick=choosenSick;
        for(i in 0..Sicks.size-1){
            if(Sicks[i].id==choosenSick.id){
                when(v.tag) {
                    "passPart" -> Sicks[i].passPart=txt;
                    "complaints" -> Sicks[i].complaints=txt;
                    "HistoryOfUnderlyingAndComorbidities" -> Sicks[i].HistoryOfUnderlyingAndComorbidities=txt;
                    "AnamnesisOfLife" -> Sicks[i].AnamnesisOfLife=txt;
                    "DataFromAnObjectiveStudyOfThePatient" -> Sicks[i].DataFromAnObjectiveStudyOfThePatient=txt;
                    "SubstantiationOfThePreliminaryDiagnosisAndItsFormulation" -> Sicks[i].SubstantiationOfThePreliminaryDiagnosisAndItsFormulation=txt;
                    "SurveyPlan" -> Sicks[i].SurveyPlan=txt;
                    "DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants" -> Sicks[i].DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants=txt;
                    "FinalClinicalDiagnosis" -> Sicks[i].FinalClinicalDiagnosis=txt;
                    "DifferentialDiagnosis" -> Sicks[i].DifferentialDiagnosis=txt;
                    "EtiologyAndPathogenesis" -> Sicks[i].EtiologyAndPathogenesis=txt;
                    "PathologicalChangesInOrgans" -> Sicks[i].PathologicalChangesInOrgans=txt;
                    "TreatmentOfTheUnderlyingDisease" -> Sicks[i].TreatmentOfTheUnderlyingDisease=txt;
                    "TreatmentOfThePatientAndItsRationale" -> Sicks[i].TreatmentOfThePatientAndItsRationale=txt;
                    "Forecast" -> Sicks[i].Forecast=txt;
                    "Prevention" -> Sicks[i].Prevention=txt;
                    "Epicrisis" -> Sicks[i].Epicrisis=txt;
                    "CurationDiary" -> Sicks[i].CurationDiary=txt;
                    "ListOfUsedLiterature" -> Sicks[i].ListOfUsedLiterature=txt;
                }
                choosenSick=Sicks[i];
                break;
            }
        }
        if(sickWriteMode==0){
            sendData = mutableMapOf("session" to myUser.session,"tag" to v.tag.toString(),"val" to txt,"sickId" to choosenSick.id);
            animCountPlus();
            sendSick();
        }else if(sickWriteMode==1){
            tprint(w("Сохранено"));
            thisSick(s=choosenSick);
        }
    }
    fun sicks(v:View){
        wChange{
            plus_rem();
            if(step=="sickHelpBtn"){rem_anim();};
            step="sicks";
            var tag=v.getTag().toString();
            var txt="";
            for(s in Sicks){
                if(s.id==choosenSick.id){
                    when(tag) {
                        "id" -> txt = s.id
                        "owner" -> txt = s.owner
                        "date" -> txt = s.date
                        "passPart" -> txt = s.passPart
                        "complaints" -> txt = s.complaints
                        "HistoryOfUnderlyingAndComorbidities" -> txt =
                            s.HistoryOfUnderlyingAndComorbidities
                        "AnamnesisOfLife" -> txt = s.AnamnesisOfLife
                        "DataFromAnObjectiveStudyOfThePatient" -> txt =
                            s.DataFromAnObjectiveStudyOfThePatient
                        "SubstantiationOfThePreliminaryDiagnosisAndItsFormulation" -> txt =
                            s.SubstantiationOfThePreliminaryDiagnosisAndItsFormulation
                        "SurveyPlan" -> txt = s.SurveyPlan
                        "DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants" -> txt =
                            s.DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants
                        "FinalClinicalDiagnosis" -> txt = s.FinalClinicalDiagnosis
                        "DifferentialDiagnosis" -> txt = s.DifferentialDiagnosis
                        "EtiologyAndPathogenesis" -> txt = s.EtiologyAndPathogenesis
                        "PathologicalChangesInOrgans" -> txt = s.PathologicalChangesInOrgans
                        "TreatmentOfTheUnderlyingDisease" -> txt = s.TreatmentOfTheUnderlyingDisease
                        "TreatmentOfThePatientAndItsRationale" -> txt =
                            s.TreatmentOfThePatientAndItsRationale
                        "Forecast" -> txt = s.Forecast
                        "Prevention" -> txt = s.Prevention
                        "Epicrisis" -> txt = s.Epicrisis
                        "CurationDiary" -> txt = s.CurationDiary
                        "ListOfUsedLiterature" -> txt = s.ListOfUsedLiterature
                        "doc" -> txt = s.doc
                        "sickPlace" -> txt = "sickPlace";
                    }
                    if(txt=="sickPlace"){
                        frag_inflate(R.layout.frag_main_menu);
                        if(choosenPacient.sex=="1"){
                            frag_inflate(R.layout.frag_main_menu_m);
                        }
                        if(sickWriteMode==3){
                            checkBodyState(user=myUser,sick=choosenSick);
                        }
                    }else{
                        if(choosenSick.doc==myUser.id){
                            if(tag=="FinalClinicalDiagnosis"||tag=="DifferentialDiagnosis"){
                                frag_inflate(R.layout.frag_change_sick_auto);
                                autoWrite(R.id.editInfoEd,txt);
                                autoHint(R.id.editInfoEd,w("Ввод"));

                                var mAutoCompleteTextView:AutoCompleteTextView=findViewById(R.id.editInfoEd);
                                println(mkbArray.toString()+"<<<mkbArray")
                                mAutoCompleteTextView.setAdapter(ArrayAdapter<Any?>(this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    mkbArray))
                            }else{
                                frag_inflate(R.layout.frag_change_sick);
                                edWrite(R.id.editInfoEd,txt);
                                edHint(R.id.editInfoEd,w("Ввод"));
                            }
                        }else{
                            frag_inflate(R.layout.frag_change_sick_txt);
                            txtWrite(R.id.editInfoEd,txt);
                        }
                        println(txt+"<<<<doc")
                        var btn=findViewById<Button>(R.id.saveSick);
                        btn.tag=tag;
                        wChangeB{};
                    }
                    break;
                }
            }
        }
    }
    fun thisSick(v:View?=null,s:Sick?=null){
        step="thisSick";
        if(v!=null){
            choosenSick=sickById(v.tag.toString());
        }
        if (s != null) {
            choosenSick=s
        };
        frag_inflate(R.layout.frag_this_sick);
        if(sickWriteMode==0){
            plus_rem();
        }else if(sickWriteMode==1){
            plus_inflate(R.layout.plus_new_sick);
        }
        wChangeB{}
        for(s in Sicks){
            if(s.id==choosenSick.id){
                btnWrite(R.id.sick1,w("Паспортная часть"));
                findViewById<Button>(R.id.sick1).setTag("passPart");
                btnWrite(R.id.sick2,w("Жалобы: основные и найденные при опросе по системам органов"));
                findViewById<Button>(R.id.sick2).setTag("complaints");
                btnWrite(R.id.sick3,w("Анамнез основного и сопутствующих заболеваний"));
                findViewById<Button>(R.id.sick3).setTag("HistoryOfUnderlyingAndComorbidities");
                btnWrite(R.id.sick4,w("Анамнез жизни"));
                findViewById<Button>(R.id.sick4).setTag("AnamnesisOfLife");
                btnWrite(R.id.sick5,w("Данные объективного исследования больного"));
                findViewById<Button>(R.id.sick5).setTag("DataFromAnObjectiveStudyOfThePatient");
                btnWrite(R.id.sick6,w("Обоснование предварительного диагноза и его формулировка"));
                findViewById<Button>(R.id.sick6).setTag("SubstantiationOfThePreliminaryDiagnosisAndItsFormulation");
                btnWrite(R.id.sick7,w("План обследования"));
                findViewById<Button>(R.id.sick7).setTag("SurveyPlan");
                btnWrite(R.id.sick8,w("Данные лабораторных и инструментальных исследований, заключения консультантов"));
                findViewById<Button>(R.id.sick8).setTag("DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants");
                btnWrite(R.id.sick9,w("Окончательный клинический  диагноз"));
                findViewById<Button>(R.id.sick9).setTag("FinalClinicalDiagnosis");
                btnWrite(R.id.sick10,w("Дифференциальный диагноз"));
                findViewById<Button>(R.id.sick10).setTag("DifferentialDiagnosis");
                btnWrite(R.id.sick11,w("Этиология и патогенез"));
                findViewById<Button>(R.id.sick11).setTag("EtiologyAndPathogenesis");
                btnWrite(R.id.sick12,w("Патологоанатомические изменения  в  органах"));
                findViewById<Button>(R.id.sick12).setTag("PathologicalChangesInOrgans");
                btnWrite(R.id.sick13,w("Лечение основного заболевания"));
                findViewById<Button>(R.id.sick13).setTag("TreatmentOfTheUnderlyingDisease");
                btnWrite(R.id.sick14,w("Лечение больного и его обоснование"));
                findViewById<Button>(R.id.sick14).setTag("TreatmentOfThePatientAndItsRationale");
                btnWrite(R.id.sick15,w("Прогноз"));
                findViewById<Button>(R.id.sick15).setTag("Forecast");
                btnWrite(R.id.sick16,w("Профилактика"));
                findViewById<Button>(R.id.sick16).setTag("Prevention");
                btnWrite(R.id.sick17,w("Эпикриз"));
                findViewById<Button>(R.id.sick17).setTag("Epicrisis");
                btnWrite(R.id.sick18,w("Дневник курации"));
                findViewById<Button>(R.id.sick18).setTag("CurationDiary");
                btnWrite(R.id.sick19,w("Список использованной литературы"));
                findViewById<Button>(R.id.sick19).setTag("ListOfUsedLiterature");
                btnWrite(R.id.sick20,w("Область"));
                findViewById<Button>(R.id.sick20).setTag("sickPlace");
                //var ind:View=findViewById(R.id.sickCheck1);
                //if(s.passPart==""){
                //    ind.setBackgroundResource(R.drawable.circle25);
                //}
                var ind:View;
                if(s.passPart==""){
                    ind=findViewById(R.id.sickCheck1);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.complaints==""){
                    ind=findViewById(R.id.sickCheck2);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.HistoryOfUnderlyingAndComorbidities==""){
                    ind=findViewById(R.id.sickCheck3);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.AnamnesisOfLife==""){
                    ind=findViewById(R.id.sickCheck4);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.DataFromAnObjectiveStudyOfThePatient==""){
                    ind=findViewById(R.id.sickCheck5);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.SubstantiationOfThePreliminaryDiagnosisAndItsFormulation==""){
                    ind=findViewById(R.id.sickCheck6);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.SurveyPlan==""){
                    ind=findViewById(R.id.sickCheck7);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants==""){
                    ind=findViewById(R.id.sickCheck8);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.FinalClinicalDiagnosis==""){
                    ind=findViewById(R.id.sickCheck9);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.DifferentialDiagnosis==""){
                    ind=findViewById(R.id.sickCheck10);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.EtiologyAndPathogenesis==""){
                    ind=findViewById(R.id.sickCheck11);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.PathologicalChangesInOrgans==""){
                    ind=findViewById(R.id.sickCheck12);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.TreatmentOfTheUnderlyingDisease==""){
                    ind=findViewById(R.id.sickCheck13);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.TreatmentOfThePatientAndItsRationale==""){
                    ind=findViewById(R.id.sickCheck14);
                    ind.setBackgroundResource(R.drawable.ic_menu_help)
                }
                if(s.Forecast==""){
                    ind=findViewById(R.id.sickCheck15);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.Prevention==""){
                    ind=findViewById(R.id.sickCheck16);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.Epicrisis==""){
                    ind=findViewById(R.id.sickCheck17);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.CurationDiary==""){
                    ind=findViewById(R.id.sickCheck18);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.ListOfUsedLiterature==""){
                    ind=findViewById(R.id.sickCheck19);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                if(s.place==""){
                    ind=findViewById(R.id.sickCheck20);
                    ind.setBackgroundResource(R.drawable.ic_menu_help);
                }
                break;
            }
        }
    }
    fun thisPacientInflate(vtxt: String? = null){
        var txt=vtxt;
        SicksSearch.clear();
        var text=edRead(R.id.searchText);
        var info="";
        var img=findViewById<ImageView>(R.id.face);
        img.setImageBitmap(choosenPacient.imgBit);
        txtWrite(R.id.firstname,choosenPacient.firstname);
        txtWrite(R.id.lastname,choosenPacient.lastname);
        txtWrite(R.id.year,choosenPacient.year);
        for(s in Sicks){
            if(s.owner==choosenPacient.id){
                info=s.id+s.owner+s.date+s.passPart+s.complaints+s.HistoryOfUnderlyingAndComorbidities+s.AnamnesisOfLife+s.DataFromAnObjectiveStudyOfThePatient+s.SubstantiationOfThePreliminaryDiagnosisAndItsFormulation+s.SurveyPlan+s.DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants+s.FinalClinicalDiagnosis+s.DifferentialDiagnosis+s.EtiologyAndPathogenesis+s.PathologicalChangesInOrgans+s.TreatmentOfTheUnderlyingDisease+s.TreatmentOfThePatientAndItsRationale+s.Forecast+s.Prevention+s.Epicrisis+s.CurationDiary+s.ListOfUsedLiterature
                if(txt==null){
                    txt=text;
                }
                if(txt.toString().lowercase() in info.lowercase()){
                    if(s.id!="0"){
                        SicksSearch.add(s);
                    }
                }
            }
        }
        val listView = findViewById(R.id.lView) as ListView;
        val myListAdapter = sickAdapter(this, SicksSearch);
        listView.adapter = myListAdapter;
        if(SicksSearch.size==0){
            tprint(w("По вашему запросу ничего не найдено"));
        }
    }
    fun thisPacient(id:String?=null){
        if(sickWriteMode!=3){
            step="thisPacient"
            plus_inflate(R.layout.plus_btn);
        }else{
            step="mySickStories";
        }
        choosenPacient=userById(id.toString());
        for(u in Users){
            if(u.id==choosenPacient.id){
                frag_inflate(R.layout.frag_sicks);
                wChangeB{};
                thisPacientInflate("");
                break;
            }
        }
    }
    fun myQr(){
        step="myQr";
        plus_rem();
        frag_inflate(R.layout.frag_my_qr);
        if(qrImage==null){
            animCountPlus();
            clearSend();
            sendData = mutableMapOf("session" to myUser.session);
            getMyQr();
        }else{
            var image:ImageView=findViewById(R.id.qrPlace);
            image.setImageBitmap(qrImage);
        }
        wChangeB{};
    }
    fun docSearchInflate(id: String? =null){
        UsersSearch.clear();
        var txt=edRead(R.id.pacSearchText);
        var info="";
        for(u in Users){
            if(id==null){
                info=u.id+u.login+u.phone+u.firstname+u.lastname+u.year
            }else{
                info=u.id
            }
            if(txt.lowercase() in info.lowercase()){
                UsersSearch.add(u);
            }
        }
        val listView = findViewById(R.id.lView) as ListView;
        val myListAdapter = pacientAdapter(this, UsersSearch);
        listView.adapter = myListAdapter;
    }
    fun pacSearch(){
        hideKeyboard(this);
        var txt=edRead(R.id.pacSearchText);
        if(txt.length==0){
            tprint(w("Не оставляйте поле пустым"));
        }else{
            docSearchInflate();
        }
    }
    fun qrPacSearch(){
        openShtorScan()
        bark=0;
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@MainActivity, R.anim.scanner_animation)
        var binding: View =findViewById(R.id.barcode_line);
        binding.startAnimation(aniSlide)
        try{
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity, android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askForCameraPermission()
            } else {
                setupControls()
            }
        }catch (e:Exception){
            logger(e);
        }
    }
    fun docInterface(qr:String?=null,fromScanner:Boolean?=null){
        try {
            step = "docInterface";
            plus_rem();
            frag_inflate(R.layout.frag_doc_menu);
            edHint(R.id.pacSearchText,w("Ввод"))
            wChangeB({});

            val listView = findViewById(R.id.lView) as ListView
            val myListAdapter = pacientAdapter(this, Users)
            listView.adapter = myListAdapter
            if(qr!=null){
                edWrite(R.id.pacSearchText,qr);
                closeShtor();
                if(fromScanner!=null){
                }
            }

        } catch (e:Exception){
            logger(e);
        }
    }
    fun animCountPlus() {
        animDo=true;
        val animCountPlusClass = animCountPlusClass()
        animCountPlusClass.start()
    }
    inner class animCountPlusClass : Thread() {
        override fun run() {
            startAnim();
            animCount=0;
            sleep(1000)
            while(true){
                println(animDo.toString()+"  "+animCount.toString()+"    <<<<anim")
                animCount++
                if(!animDo||animCount>=50){
                    stopAnim();
                    break;
                }
                sleep(100);
            }
        }
    }
    fun startAnim(){
        runOnUiThread {
            try {
                animInflate(R.layout.frag_anim);
                val animRotate = AnimationUtils.loadAnimation(
                    applicationContext, R.anim.rotate
                )
                val animAlpha = AnimationUtils.loadAnimation(
                    applicationContext, R.anim.alpha_forward
                )
                val con: ConstraintLayout = findViewById(R.id.ligthing);
                con.startAnimation(animRotate);
                val con2: ConstraintLayout = findViewById(R.id.constrAnim);
                con2.startAnimation(animAlpha);

            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun stopAnim(){
        runOnUiThread {
            try {
                val animAlpha = AnimationUtils.loadAnimation(
                    applicationContext, R.anim.alpha_back
                )
                val con2: ConstraintLayout = findViewById(R.id.constrAnim);
                con2.startAnimation(animAlpha);
                val endAnimClass = endAnimClass()
                endAnimClass.start()
                animDo=false;
            } catch (e:Exception) {
                logger(e)
            }
        }
    }
    inner class endAnimClass : Thread() {
        override fun run() {
            try {
                sleep(200)
                runOnUiThread {
                    try {
                        var linLayout: LinearLayout = findViewById(R.id.Anim)
                        linLayout.removeAllViews()
                    } catch (e:Exception) {
                        logger(e)
                    }
                }
            } catch (e:Exception) {
                logger(e)
            }
        }
    }
    fun beADoctorSend(){
        var doc="0";
        if(myUser.isDoc=="0"){
            doc="1";
        }
        sendData= mutableMapOf("key" to "isDoc","val" to doc,"session" to myUser.session);
        sendSaveInfo();
    }
    fun changePhoto(){
        step="changePhoto";
        frag_inflate(R.layout.frag_change_info_photo);
        wChangeB({});
        var img:ImageView = findViewById(R.id.avatar);
        img.setImageBitmap(bitImage);
        var btn:Button=findViewById(R.id.saveInfo);
        btn.setTag("photo");
        btnWrite(R.id.saveInfo,w("Сохранить"));
    }
    fun chooseAvatarPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK);
        photoPickerIntent.type = "image/*";
        startActivityForResult(photoPickerIntent, avatarPhoto);
    }
    fun saveCountry(){
        var country=spinRead(R.id.CountryListSpinner);
        sendData= mutableMapOf("key" to "country","val" to country,"session" to myUser.session);
        sendSaveInfo();
    }
    fun saveInfoBox(v:View){
        var tag=v.tag.toString();
        if(tag=="photo"){
            sendPhoto();
        }else{
            var ed:EditText=findViewById(R.id.editInfoEd);
            var info=ed.text.toString();
            if(info.length==0){
                tprint(w("Заполните поле"));
            }else{
                sendData= mutableMapOf("key" to tag,"val" to info,"session" to myUser.session);
                sendSaveInfo();
            }
        }
    }
    fun editInfoBox(info:String){
        step="editInfoBox";
        frag_inflate(R.layout.frag_change_info);
        edHint(R.id.editInfoEd,w("Введите"));
        btnWrite(R.id.saveInfo,w("Сохранить"));
        when(info){
            "login"->
                edWrite(R.id.editInfoEd,myUser.login);
            "phone"-> {
                frag_inflate(R.layout.frag_change_info_int);
                edWrite(R.id.editInfoEd,myUser.phone);
            }
            "firstname"-> {
                edWrite(R.id.editInfoEd,myUser.firstname);
            }
            "lastname"-> {
                edWrite(R.id.editInfoEd,myUser.lastname);
            }
            "year"-> {
                frag_inflate(R.layout.frag_change_info_int);
                edWrite(R.id.editInfoEd,myUser.year);
            }
        }
        wChangeB({});
        findViewById<Button>(R.id.saveInfo).setTag(info);
    }
    fun getSaveInfo(v:String){
        clearSend();
        var send="";
        when(v){
            "sex"->{
                var rb1:RadioButton=findViewById(R.id.regRadioMan);
                if(rb1.isChecked){
                    send="1";
                }else{
                    send="0";
                }
            }
        }
        sendData= mutableMapOf("key" to v,"val" to send,"session" to myUser.session);
        sendSaveInfo();
    }
    fun mySettings(){
        step="mySettings"
        plus_rem();
        closeShtor();
        frag_inflate(R.layout.frag_my_settings);
        wChangeB({});
        txtWrite(R.id.editLogin,myUser.login);
        txtWrite(R.id.editPswd,"* * * * *");
        txtWrite(R.id.editPhone,myUser.phone);
        txtWrite(R.id.editFirstName,myUser.firstname);
        txtWrite(R.id.editLastname,myUser.lastname);
        txtWrite(R.id.editYear,myUser.year);
        var doc=w("Стать доктором");
        head_inflate(R.layout.head_user);
        if(myUser.isDoc=="1"){
            doc=w("Стать пациентом");
            head_inflate(R.layout.head_doktor);
        }
        btnWrite(R.id.beADoctor,doc);
        spinAd(R.id.CountryListSpinner,countryList,myUser.country);
        var rb:RadioButton=findViewById(R.id.regRadioMan);
        var rb2:RadioButton=findViewById(R.id.regRadioWoman);
        if(myUser.sex=="0"){
            rb.setChecked(false);
            rb2.setChecked(true);
        }else{
            rb.setChecked(true);
            rb2.setChecked(false);
        }
        if(myUser.imgBit!=null){
            var img:ImageView=findViewById(R.id.avatar);
            img.setImageBitmap(myUser.imgBit);
        }
    }
    fun setShtor(){
        if(shtor){
            closeShtor();
        }else{
            openShtor()
        }
    }
    fun closeShtor(){
        try{
            cameraSource.stop()
        }catch (e:Exception){
        }
        var linLayout: LinearLayout = findViewById(R.id.allWindow)
        shtor=false;
        wLeft({},R.id.shtora,true);
        linLayout.removeAllViews()

        linLayout = findViewById(R.id.Anim)
        linLayout.removeAllViews()

    }
    fun openShtor(){
        var ltInflater = layoutInflater
        var view = ltInflater.inflate(R.layout.frag_set_short, null, false)
        var linLayout: LinearLayout = findViewById(R.id.allWindow)
        linLayout.removeAllViews()
        linLayout.addView(view)

        view = ltInflater.inflate(R.layout.frag_alpha_black, null, false)
        linLayout = findViewById(R.id.Anim)
        linLayout.removeAllViews()
        linLayout.addView(view)

        val animAlpha = AnimationUtils.loadAnimation(
            applicationContext, R.anim.alpha_forward
        )
        val con: ConstraintLayout = findViewById(R.id.alphaBlack);
        con.startAnimation(animAlpha)

        shtor=true;
        wLeftBack({},R.id.shtora,true);
        btnWrite(R.id.mySettings,w("Мои настройки"));
    }
    fun openShtorScan(){
        var ltInflater = layoutInflater
        var view = ltInflater.inflate(R.layout.frag_set_shtor_scan, null, false)
        var linLayout: LinearLayout = findViewById(R.id.allWindow)
        linLayout.removeAllViews()
        linLayout.addView(view)

        view = ltInflater.inflate(R.layout.frag_alpha_black, null, false)
        linLayout = findViewById(R.id.Anim)
        linLayout.removeAllViews()
        linLayout.addView(view)

        val animAlpha = AnimationUtils.loadAnimation(
            applicationContext, R.anim.alpha_forward
        )
        val con: ConstraintLayout = findViewById(R.id.alphaBlack);
        con.startAnimation(animAlpha)

        shtor=true;
        wLeftBack({},R.id.shtora,true);
    }

    fun checkBodyState(user:User=myUser,sick:Sick?=null){
        var sicks= mutableListOf<Int>();
        var sickViews= mutableListOf<Int>(
            R.id.place1,R.id.place2,R.id.place3,R.id.place4,
            R.id.place5,R.id.place6,R.id.place7,R.id.place8,
            R.id.place9,R.id.place10,R.id.place11,R.id.place12,
            R.id.place13,R.id.place14,R.id.place15,R.id.place16,
            R.id.place17,R.id.place18,R.id.place19,R.id.place20,
            R.id.place21,R.id.place22,R.id.place23
        );
        if(sick==null){
            for(s in Sicks){
                if(s.owner==user.id){
                    sicks.add(s.place.toInt());
                }
            }
        }else{
            sicks.add(sick.id.toInt());
        }
        println(sicks.toString()+"<<<Sicks");
        for(i in 0..sickViews.size-1){
            if(i+1 in sicks){
            }else{
                var btn = findViewById<Button>(sickViews[i]);
                btn.alpha=0.0f;
            }
        }
    }
    fun mainMenu(){
        step="mainMenu";
        frag_inflate(R.layout.frag_main_menu);
        bot_inflate(R.layout.bot_user)
        animDo=false;
        if(myUser.sex=="1"){
            frag_inflate(R.layout.frag_main_menu_m);
            //imageManWoman.setBackgroundResource(R.drawable.man);
        }
        wChangeB({});
        if(myUser.isDoc=="1"){
            head_inflate(R.layout.head_doktor)
        }else{
            head_inflate(R.layout.head_user);
        }
        checkBodyState();
        startUpdates();
    }
    fun makeRegistration(){
        var firstname=spaceClear(reg_firstname.text.toString());
        var lastname=spaceClear(reg_lastname.text.toString());
        var year=reg_year.text.toString();

        var login=spaceClear(reg_login.text.toString());
        var pswd=spaceClear(reg_pswd.text.toString());
        var phone=reg_phone.text.toString();
        var country=CountryListSpinner.selectedItem.toString();

        var sex="0";
        var sexMan:RadioButton = findViewById(R.id.regRadioMan);
        if(sexMan.isChecked){
            sex="1";
        }
        var txt="";
        if(login.length==0){
            txt="Введите логин";
        }else if(pswd.length==0){
            txt="Введите пароль";
        }else if(firstname.length==0){
            txt="Введите имя";
        }else if(lastname.length==0){
            txt="Введите фамилию";
        }else if(year.length!=4){
            txt="Неккоректный год";
        }else if(phone.length<9&&phone.length>12){
            txt="Неккоректный номер";
        }
        if(txt!=""){
            tprint(w(txt));
        }else{
            clearSend();
            sendData= mutableMapOf("firstname" to firstname,"lastname" to lastname,"year" to year,
                "login" to login,"pswd" to pswd,"phone" to phone,"country" to country,"sex" to sex);
            sendRegDatas();
        }
    }
    fun tryReg(){
        try{
            step="tryReg";
            frag_inflate(R.layout.frag_reg);
            wChangeB({});
            reg_firstname.hint=w("Имя");
            reg_lastname.hint=w("Фамилия");
            reg_year.hint=w("Год рождения");
            reg_login.hint=w("Логин");
            reg_pswd.hint=w("Пароль");
            reg_phone.hint=w("Телефон");
            makeRegistration.setText(w("Зарегистрироваться"));
            spinAd(R.id.CountryListSpinner,countryList);
        }catch (e: Exception){
            logger(e);
        }
    }
    fun tryEnter(){
        animCountPlus();
        val Login=spaceClear(enter_login.text.toString());
        val Pswd=spaceClear(enter_pswd.text.toString());

        write("login.txt",Login);
        write("pswd.txt",Pswd);
        if(myUser.login.equals(Login)){
        }else{
            myUser.changed="0";
        }
        myUser.login=Login;
        myUser.pswd=Pswd;
        clearSend();
        sendData= mutableMapOf("Login" to Login,"Pswd" to Pswd);
        sendEnterData();
    }
    fun enter(){
        try{
            step="enter";
            getLangs();
            getActualVersion();
            qrImage=null
            bot_rem();
            head_rem();
            frag_inflate(R.layout.frag_enter);
            wChangeB({});
            regBtn.text=w("Регистрация");
            enterBtn.text=w("Вход");
            enter_login.hint=w("Логин");
            enter_pswd.hint=w("Пароль");
            var Login=read("login.txt");
            var Pswd=read("pswd.txt");
            if(Login=="fileNotFound"){
                Login="";
                Pswd="";
            }
            enter_login.setText(Login);
            enter_pswd.setText(Pswd);
        }catch (e: IOException) {
            println("$e err<<<")
        }
    }
    fun startCheckWorkMode() {
        val startCheckWorkModeClass = startCheckWorkModeClass()
        startCheckWorkModeClass.start()
    }
    inner class startCheckWorkModeClass : Thread() {
        override fun run() {
            while(true){
                if(workMode>10){
                    workMode=11;
                    updateSleep=10000;
                }else{
                    updateSleep=5000;
                }
                workMode++;
                sleep(1000);
            }
        }
    }
    //senders
    fun sendSick() {
        val sendSickClass = sendSickClass()
        sendSickClass.start()
    }
    inner class sendSickClass : Thread() {
        override fun run() {
            var data = sendData;
            var resp:Map<*,*> = doPost(global_url + "send_sick/", data as Map<String, Any>);
            println(resp.toString()+"<<<")
            var text=w(resp["text"].toString());
            var res=resp["res"].toString();
            var err=resp["err"].toString()
            if(err=="1"){
            }else if(err=="0"){
                if(res=="1"){
                    runOnUiThread {
                        tprint(w("Сохранено"));
                        for (i in 0..Sicks.size - 1) {
                            if(sickWriteMode==1){
                                if (Sicks[i].id == "0") {
                                    Sicks[i].id = text;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(res!="1"){
                runOnUiThread {
                    tprint(w(text));
                    for (i in 0..Sicks.size - 1) {
                        if(sickWriteMode==0) {
                            if (Sicks[i].id == choosenSick.id) {
                                Sicks[i] = backUpSick;
                                choosenSick = backUpSick;
                                break;
                            }
                        }
                    }
                }
            }
            animDo=false;
        }
    }
    fun getHimSicks(u:User){
        sendSickData = mutableMapOf("session" to myUser.session,"ID" to u.id);
        val getHimSicksClass = getHimSicksClass()
        getHimSicksClass.start()
    }
    inner class getHimSicksClass : Thread() {
        override fun run() {
            var data = sendSickData;
            var resp:Map<*,*> = doPost(global_url + "get_sicks/", data as Map<String, Any>);
            var err=resp["err"].toString();
            var res=resp["res"].toString();
            var text=w(resp["text"].toString());
            var sicks=resp["sicks"].toString();
            var sickList=stringToArray(sicks);
            if(err=="1"){
            }else if(err=="0"){
                if(res=="0"){
                }else if(res=="1"){
                    fillSicks(sickList as ArrayList<Any>);
                }
            }
            animDo=false;
            if(res!="1"){
                runOnUiThread {
                    tprint(text);
                }
            }
        }
    }
    fun startUpdates() {
        val startUpdatesClass = startUpdatesClass();
        startUpdatesClass.start();
    }
    inner class startUpdatesClass : Thread() {
        override fun run() {
            var ses=myUser.session
            while (true){
                try{
                    //Users
                    var List= mutableListOf<Int>();
                    for(u in Users){
                        List.add(u.id.toInt());
                    }
                    var usersIds=zipList(List);
                    usersIds=zipNums(usersIds);
                    //Sicks
                    var SicksSend= mutableListOf<Sick>();
                    for(s in Sicks){
                        if(s.owner.toInt() in List){
                            SicksSend.add(s);
                        }
                    }
                    println(List.toString()+",<<<")
                    List=mutableListOf<Int>();
                    for(s in SicksSend){
                        List.add(s.id.toInt());
                    }
                    var sicksIds=zipList(List);
                    sicksIds=zipNums(sicksIds);
                    var arr = mutableListOf(
                        "users",usersIds,
                        "sicks",sicksIds,
                    );
                    var l = mutableListOf(arr)
                    var info = arrayToString(l);
                    sendUpdateData= mutableMapOf(
                        "session" to ses,
                        "info" to info,
                    );
                    var data = sendUpdateData;
                    var resp:Map<*,*> = doPost(global_url + "upd/", data as Map<String, Any>);
                    var res=resp["res"].toString();
                    var err=resp["err"].toString();
                    if(res=="505"){
                        println("Old session is over <<<")
                        break;
                    }else{
                        if(err=="0"){
                            var users=resp["users"].toString();
                            var sicks=resp["sicks"].toString();
                            if(sicks!=""){
                                var sickList=stringToArray(sicks);
                                animCountPlus();
                                fillSicks(sickList as ArrayList<Any>);
                                animDo=false;
                            }
                        }
                    }
                }catch (e:Exception){
                    logger(e);
                }
                sleep(updateSleep.toLong());
            }
        }
    }
    fun sendSaveInfo() {
        animCountPlus();
        val sendSaveInfoClass = sendSaveInfoClass()
        sendSaveInfoClass.start()
    }
    inner class sendSaveInfoClass : Thread() {
        override fun run() {
            var data = sendData;
            var resp:Map<*,*> = doPost(global_url + "change_my_data/", data as Map<String, Any>);
            println(resp.toString()+"<<<")
            var text=w(resp["text"].toString());
            var res=resp["res"].toString();
            var err=resp["err"].toString()
            var v=data["val"].toString();
            var k=data["key"].toString();
            if(err=="1"){
            }else if(err=="0"){
                for(i in 0..Users.size-1){
                    if(Users[i].id==myUser.id){
                        when(k){
                            "sex"->
                                Users[i].sex=v
                            "login"->
                                Users[i].login=v
                            "pswd"->
                                Users[i].pswd=v
                            "phone"->
                                Users[i].phone=v
                            "country"->
                                Users[i].country=v
                            "isDoc"->
                                Users[i].isDoc=v
                            "firstname"->
                                Users[i].firstname=v
                            "lastname"->
                                Users[i].lastname=v
                            "year"->
                                Users[i].year=v
                        }
                        myUser=Users[i];
                        break;
                    }
                }
                if(res=="1"){
                    runOnUiThread {
                        tprint(w("Сохранено"));
                        mySettings();
                        //if(step=="mySettings"){
                        //    mySettings();
                        //}
                    }
                }
            }
            if(res!="1"){
                runOnUiThread {
                    tprint(w(text));
                }
            }
            animDo=false;
        }
    }
    fun sendRegDatas() {
        val sendRegDatasClass = sendRegDatasClass()
        sendRegDatasClass.start()
    }
    inner class sendRegDatasClass : Thread() {
        override fun run() {
            var data = sendData;
            var resp:Map<*,*> = doPost(global_url + "registration/", data as Map<String, Any>);
            println(resp.toString()+"<<<")
            var text=w(resp["text"].toString());
            var res=resp["res"].toString();
            var err=resp["err"].toString()
            if(err=="1"){
            }else if(err=="0"){
                if(res=="0"){
                    runOnUiThread {
                        tprint(w(text));
                    }
                }else if(res=="1"){
                    myUser.session=resp["session"].toString();
                    if(res=="1"){
                        write("login.txt",sendData["login"].toString());
                        write("pswd.txt",sendData["pswd"].toString());
                        runOnUiThread {
                            tprint(w("Регистрация прошла успешно!"));
                            enter();
                        }
                    }
                }
            }
            animDo=false;
        }
    }
    fun getActualVersion() {
        val getActualVersionClass = getActualVersionClass()
        getActualVersionClass.start()
    }
    inner class getActualVersionClass : Thread() {
        override fun run() {
            try{
                clearSend();
                var data = sendData;
                var resp:Map<*,*> = doPost(global_url + "check_version/", data as Map<String, Any>);
                var err=resp["err"].toString()
                runOnUiThread {
                    tprint(resp.toString());
                }
                var version=resp["version"].toString();
                if(err=="1"){
                }else if(err=="0"){
                    ActualVersion=version;
                    if (ActualVersion==AppVersion){
                    }
                }
                println("version ActualVersion = $version AppVersion=$AppVersion ok<<<");
            }catch (e:Exception){
                logger(e);
                runOnUiThread {
                    tprint("ServerConnectErr")
                }
            }
        }
    }
    fun getLangs() {
        val getLangsClass = getLangsClass()
        getLangsClass.start()
    }
    inner class getLangsClass : Thread() {
        override fun run() {
            try{
                var data = sendData;
                var resp:Map<*,*> = doPost(global_url + "get_languages/", data as Map<String, Any>);

                var err=resp["err"].toString()
                var langs=resp["langs"].toString();
                if(err=="1"){
                }else if(err=="0"){
                    var arr:ArrayList<Any> = stringToArray(langs) as ArrayList<Any>;
                    fillLangs(arr);
                }

            }catch (e:Exception){
                logger(e)
                runOnUiThread {
                    tprint("ServerConnectErr")
                }
            }
        }
    }
    fun sendEnterData() {
        val sendEnterDataClass = sendEnterDataClass()
        sendEnterDataClass.start()
    }
    inner class sendEnterDataClass : Thread() {
        override fun run() {
            try{
                var data = sendData;
                var resp:Map<*,*> = doPost(global_url + "enter/", data as Map<String, Any>);
                var text=w(resp["text"].toString());
                var res=resp["res"].toString();
                var err=resp["err"].toString();
                var id=resp["id"].toString();
                if(err=="1"){
                }else if(err=="0"){
                    if(res=="0"){

                    }else if(res=="1"){
                        myUser.session=resp["session"].toString();
                        myUser.id=id;
                        //checkDatas(id);
                        clearSend();
                        sendData= mutableMapOf("var" to "id","val" to id);
                        getUser();
                    }
                }
                if(res!="1"){
                    runOnUiThread {
                        tprint(text);
                    }
                }
                animDo=false;
            }catch (e:Exception){
                logger(e);
                runOnUiThread{
                    tprint("ServerConnectErr")
                };
            }
        }
    }
    fun checkDatas(id:String){
        if(myUser.changed=="0"){
            clearSend();
            sendData= mutableMapOf("var" to "id","val" to id);
            getUser();
        }else{
            runOnUiThread {
                mainMenu();
            }
        }
    }
    fun getMyQr(){
        val getMyQrClass = getMyQrClass()
        getMyQrClass.start()
    }
    inner class getMyQrClass : Thread() {
        override fun run() {
            try{
                var bit: Bitmap? = null
                var img = "";
                var data="pType=qr"+
                        "&session="+myUser.session+
                        "&link="
                img = global_url + "get_photo/?" + data
                val newurl = URL(img)
                bit = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                runOnUiThread{
                    var image:ImageView=findViewById(R.id.qrPlace);
                    image.setImageBitmap(bit);
                    qrImage=bit;
                }
                animDo=false;
            }catch (e:Exception){
                logger(e);
            }

        }
    }
    fun getSickHelper(){
        val getSickHelperClass = getSickHelperClass();
        getSickHelperClass.start();
    }
    inner class getSickHelperClass : Thread() {
        override fun run() {
            try{
                sendData= mutableMapOf("session" to myUser.session);
                var data = sendData;
                var resp:Map<*,*>;
                var ed = getSharedPreferences("setting", MODE_PRIVATE).edit();
                var res = getSharedPreferences("setting",MODE_PRIVATE).getString("mkbBook", "");
                res=""
                if(res==""){
                    resp= doPost(global_url + "get_sick_helper/", data as Map<String, Any>);
                    var err=resp["err"].toString();
                    var res=resp["res"].toString();
                    var text=w(resp["text"].toString());
                    sickHelperList=resp["sicksH"] as MutableList<Any>;

                    if(err=="1"){
                    }else if(err=="0"){
                        if(res=="0"){
                        }else if(res=="1"){
                            fillSicksHelpers(sickHelperList as ArrayList<Any>);
                            // Сериализация
                            var bos = ByteArrayOutputStream()
                            var oos = ObjectOutputStream(bos)
                            oos.writeObject(sickHelperList);
                            oos.flush()
                            var b=bos.toByteArray();
                            val encodedData = Base64.encodeToString(b, Base64.DEFAULT)
                            ed.putString("mkbBook", encodedData);
                            ed.commit();
                            println(sickHelperList.size.toString()+"<<<1")
                        }
                    }
                    if(res!="1"){
                        runOnUiThread {
                            tprint(text);
                        }
                    }
                }else{
                    // десериализовать
                    //var bytes = bos.toByteArray()
                    var bytes = res
                    var b = Base64.decode(bytes, Base64.DEFAULT)
                    val bis = ByteArrayInputStream(b)
                    val ois = ObjectInputStream(bis)
                    sickHelperList = ois.readObject() as MutableList<Any>
                    fillSicksHelpers(sickHelperList as ArrayList<Any>);
                }
            }catch (e:Exception){
                logger(e);
            }
        }

    }
    fun getUser(){
        if("ne" in sendData.keys){
        }else{
            val getUserClass = getUserClass();
            getUserClass.start();
        }
    }
    inner class getUserClass : Thread() {
        override fun run() {
            var data = sendData;
            sendData= mutableMapOf("ne" to "ne");
            var resp:Map<*,*> = doPost(global_url + "get_user/", data as Map<String, Any>);
            var err=resp["err"].toString();
            var res=resp["res"].toString();
            var text=w(resp["text"].toString());
            var users=resp["users"].toString();
            var usersList=stringToArray(users);
            if(err=="1"){
            }else if(err=="0"){
                if(res=="0"){
                }else if(res=="1"){
                    idList.clear()
                    fillUsers(usersList as ArrayList<Any>);
                    runOnUiThread {
                        if(step=="enter"){
                            mainMenu();
                        }else if(step=="docInterface"){
                            if(idList.size==0){
                                tprint(w("По вашему запросу ничего не найдено"))
                            }else{
                                //tprint(w(idList.toString()+"<<<"))
                                //edWrite(R.id.pacSearchText,idList[0]);
                                //docSearchInflate(idList[0]);
                                //edWrite(R.id.pacSearchText,"");
                                thisPacient(idList[0]);
                            }
                        }
                    }
                }
            }
            if(myUser.id==idList[0]){
                if(myUser.isDoc=="1"){
                    getSickHelper();
                }
            }
            if(res!="1"){
                runOnUiThread {
                    tprint(text);
                }
            }
            getUserPhoto();
        }
    }
    fun getUserPhoto(){
        val getUserPhotoClass = getUserPhotoClass()
        getUserPhotoClass.start()
    }
    inner class getUserPhotoClass : Thread() {
        override fun run() {
            var data = ""
            var bit: Bitmap? = null
            var bm: Bitmap? = null
            var img = "";
            var ID = sendData["val"].toString();
            var U=User();
            for(i in 0..idList.size-1){
                U=userById(idList[i]);
                data="pType=user"+
                        "&ID="+U.id+
                        "&link="+U.imgLink
                try {
                    var fileName: String = U.imgLink.toString().replace(".txt","");
                    //var res = read(fileName)
                    var ed = getSharedPreferences("setting", MODE_PRIVATE).edit();
                    var res = getSharedPreferences("setting",MODE_PRIVATE).getString(fileName, "");
                    println(res+"<<<")
                    //val f: File = File(getExternalFilesDir(null), fileName);
                    var b: ByteArray
                    img = global_url + "get_photo/?" + data
                    if (res == "") {
                        val newurl = URL(img)
                        bit = BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
                        bm = bit
                        val baos = ByteArrayOutputStream()
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
                        b = baos.toByteArray()
                        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)

                        //write(fileName, encodedImage)
                        println("<<<Downloaded $fileName")
                        ed.putString(fileName, encodedImage);
                        ed.commit();
                    } else {
                        b = Base64.decode(res, Base64.DEFAULT)
                        val inputStream: InputStream = ByteArrayInputStream(b)
                        val o = BitmapFactory.Options()
                        bit = BitmapFactory.decodeStream(inputStream, null, o)
                        println("<<<Readed $fileName")
                    }
                    for(i in 0..Users.size-1){
                        if(Users[i].id==U.id){
                            Users[i].imgBit=bit;
                            if(myUser.id==U.id){
                                myUser=Users[i];
                            }
                            break;
                        }
                    }
                } catch (r:Exception) {
                    println("$r<<<errPhoto")
                }
            }

        }
    }
    fun sendPhoto() {
        try {
            bitImage == null
            sendArr.clear()
            sendArr.add("myAvatar|" + myUser.session + "|" +myUser.id+ "|" + "0" + "|^")
            val sendPhotoClass = sendPhotoClass()
            sendPhotoClass.start()
        } catch (e: java.lang.Exception) {
            logger(e)
        }
    }
    inner class sendPhotoClass : Thread() {
        //@RequiresApi(api = Build.VERSION_CODES.N)
        override fun run() {
            try {
                animCountPlus();
                var fileName = "image.jpg"
                val sendList: String = sendArr.get(0)
                val arr = stringToArray(sendList).get(0)
                var title = ""
                var text = ""
                var ses = ""
                var sphereId = ""
                var whatSend="";
                if (sendList.contains("myAvatar")) {
                    whatSend="myAvatar";
                    fileName = myUser.id + "myAvatar.jpg"
                }
                val f = File(getApplicationContext().getCacheDir(), fileName)
                try {
                    f.createNewFile()
                } catch (e: IOException) {
                    System.out.println(e)
                }
                val bitmap: Bitmap? = bitImage
                val bos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
                val bitmapdata = bos.toByteArray()
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(f)
                } catch (e: FileNotFoundException) {
                    System.out.println(e)
                }
                try {
                    fos!!.write(bitmapdata)
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    System.out.println(e)
                }
                try {
                    val url = URL(global_url + "send_photo/") // Your given URL.
                    val c = url.openConnection() as HttpURLConnection
                    c.requestMethod = "POST"
                    //c.setRequestProperty("Content-length", "0");
                    c.useCaches = false
                    c.allowUserInteraction = false
                    val timeout = 0
                    c.connectTimeout = timeout
                    c.readTimeout = timeout
                    val delimiter = "--"
                    val boundary: String = UUID.randomUUID().toString()
                    c.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                    c.doOutput = true
                    val out = DataOutputStream(c.outputStream)
                    //"--" + boundary + EOL + + EOL  + EOL + EOL
                    out.write("""$delimiter$boundary
                    """.toByteArray())
                    out.write("Content-Disposition: form-data; name=\"$fileName\";-$sendList-; filename=\"$fileName\"\r\n".toByteArray())
                    out.write("Content-Type: application/octet-stream\r\n".toByteArray())
                    out.write("Content-Transfer-Encoding: binary\r\n".toByteArray())
                    out.write("\r\n".toByteArray())
                    val buffer = ByteArray(128)
                    val size = -1
                    /*
                    while (-1 != (size = file.read(buffer))) {
                        out.write(buffer, 0, size);
                    }
                    */out.write(bitmapdata)
                    out.write("""$delimiter$boundary$delimiter
                    """.toByteArray())
                    out.flush()
                    c.connect()
                    val br1 = BufferedReader(InputStreamReader(c.inputStream))
                    val sb1 = StringBuilder()
                    var line1: String = br1.readLine()
                    var line="";
                    try{
                        while (line1 != null) {
                            line=line+line1;
                            line1 = br1.readLine()
                        }
                    }catch (e123:Exception){
                    }
                    br1.close();
                    when(whatSend){
                        "myAvatar"->{
                            println(line.toString()+"<<<")
                            runOnUiThread {
                                if(line.contains("IGOTIT")){
                                    tprint(w("Сохранено"));
                                    for(i in 0..Users.size-1){
                                        if(Users[i].id==myUser.id){
                                            Users[i].imgBit=bitImage;
                                            myUser=Users[i];
                                            break;
                                        }
                                    }
                                    wChange({mySettings()});
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    println(e.toString() + "err<<<")
                }
                animDo=false;
            } catch (et: java.lang.Exception) {
                logger(et)
            }
        }
    }
    //fillers
    fun fillSicksHelpers(arr:MutableList<Any>){
        try{
            var l:MutableList<Any>;
            for(ar in arr){
                l= ar as MutableList<Any>;
                var id=l.get(0).toString()
                var lang=l.get(1).toString()
                var passPart=l.get(2).toString()
                var complaints=l.get(3).toString()
                var HistoryOfUnderlyingAndComorbidities=l.get(4).toString()
                var AnamnesisOfLife=l.get(5).toString()
                var DataFromAnObjectiveStudyOfThePatient=l.get(6).toString()
                var SubstantiationOfThePreliminaryDiagnosisAndItsFormulation=l.get(7).toString()
                var SurveyPlan=l.get(8).toString()
                var DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants=l.get(9).toString()
                var FinalClinicalDiagnosis=l.get(10).toString()
                var DifferentialDiagnosis=l.get(11).toString()
                var EtiologyAndPathogenesis=l.get(12).toString()
                var PathologicalChangesInOrgans=l.get(13).toString()
                var TreatmentOfTheUnderlyingDisease=l.get(14).toString()
                var TreatmentOfThePatientAndItsRationale=l.get(15).toString()
                var Forecast=l.get(16).toString()
                var Prevention=l.get(17).toString()
                var Epicrisis=l.get(18).toString()
                var CurationDiary=l.get(19).toString()
                var ListOfUsedLiterature=l.get(20).toString()
                var mkb=l.get(21).toString().replace("$","^").replace("*","|");
                //mkb=unZipText(mkb);
                sickHelper = SickHelper(
                    id,
                    lang,
                    passPart,
                    complaints,
                    HistoryOfUnderlyingAndComorbidities,
                    AnamnesisOfLife,
                    DataFromAnObjectiveStudyOfThePatient,
                    SubstantiationOfThePreliminaryDiagnosisAndItsFormulation,
                    SurveyPlan,
                    DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants,
                    FinalClinicalDiagnosis,
                    DifferentialDiagnosis,
                    EtiologyAndPathogenesis,
                    PathologicalChangesInOrgans,
                    TreatmentOfTheUnderlyingDisease,
                    TreatmentOfThePatientAndItsRationale,
                    Forecast,
                    Prevention,
                    Epicrisis,
                    CurationDiary,
                    ListOfUsedLiterature,
                    mkb
                )
                try{
                    mkbList = stringToArray(mkb)[0] as MutableList<String>;
                    mkbArray=mkbList.toTypedArray();
                }catch (e:Exception){
                    println(ListOfUsedLiterature+"<<<mkb");
                }
            }
            println("writeSicksHELPERSComplete<<<");
        }catch (e:Exception){
            logger(e);
        }
    }
    fun fillSicks(arr:ArrayList<Any>){
        try{
            var l:ArrayList<Any>;
            for(ar in arr){
                l= ar as ArrayList<Any>;
                var id=l.get(0).toString()
                var owner=l.get(1).toString()
                var date=l.get(2).toString()
                var passPart=l.get(3).toString()
                var complaints=l.get(4).toString()
                var HistoryOfUnderlyingAndComorbidities=l.get(5).toString()
                var AnamnesisOfLife=l.get(6).toString()
                var DataFromAnObjectiveStudyOfThePatient=l.get(7).toString()
                var SubstantiationOfThePreliminaryDiagnosisAndItsFormulation=l.get(8).toString()
                var SurveyPlan=l.get(9).toString()
                var DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants=l.get(10).toString()
                var FinalClinicalDiagnosis=l.get(11).toString()
                var DifferentialDiagnosis=l.get(12).toString()
                var EtiologyAndPathogenesis=l.get(13).toString()
                var PathologicalChangesInOrgans=l.get(14).toString()
                var TreatmentOfTheUnderlyingDisease=l.get(15).toString()
                var TreatmentOfThePatientAndItsRationale=l.get(16).toString()
                var Forecast=l.get(17).toString()
                var Prevention=l.get(18).toString()
                var Epicrisis=l.get(19).toString()
                var CurationDiary=l.get(20).toString()
                var ListOfUsedLiterature=l.get(21).toString()
                var doc=l.get(22).toString()
                var place=l.get(23).toString()

                var uHave=false;
                var sick = Sick(
                    id,
                    owner,
                    date,
                    passPart,
                    complaints,
                    HistoryOfUnderlyingAndComorbidities,
                    AnamnesisOfLife,
                    DataFromAnObjectiveStudyOfThePatient,
                    SubstantiationOfThePreliminaryDiagnosisAndItsFormulation,
                    SurveyPlan,
                    DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants,
                    FinalClinicalDiagnosis,
                    DifferentialDiagnosis,
                    EtiologyAndPathogenesis,
                    PathologicalChangesInOrgans,
                    TreatmentOfTheUnderlyingDisease,
                    TreatmentOfThePatientAndItsRationale,
                    Forecast,
                    Prevention,
                    Epicrisis,
                    CurationDiary,
                    ListOfUsedLiterature,
                    doc,
                    place,
                )
                for(i in 0..Sicks.size-1){
                    if(Sicks[i].id==id){
                        Sicks[i]=sick
                        uHave=true;
                        break;
                    }
                }
                if(!uHave) {
                    Sicks.add(sick);
                }
            }
            println("<<<"+arr.toString())
            println("writeSicksComplete<<<")
            workMode=0;
            runOnUiThread{
                when(step){
                    "thisPacient"->{
                        if(Sicks.size<5){
                            thisPacient(choosenPacient.id);
                        }
                    }
                    "mySickStories"->{
                        if(Sicks.size<5){
                            thisPacient(choosenPacient.id);
                        }
                    }
                    "thisSick"->{
                        thisSick(s=choosenSick);
                    }
                    "mainMenu"->{
                        mainMenu();
                    }
                }
            }

        }catch (e:Exception){
            logger(e);
        }
    }
    fun SickToList(s:Sick):String{
        var list= mutableListOf<String>(
            s.id,
            s.owner,
            s.date,
            s.passPart,
            s.complaints,
            s.HistoryOfUnderlyingAndComorbidities,
            s.AnamnesisOfLife,
            s.DataFromAnObjectiveStudyOfThePatient,
            s.SubstantiationOfThePreliminaryDiagnosisAndItsFormulation,
            s.SurveyPlan,
            s.DataFromLaboratoryAndInstrumentalStudiesConclusionsOfConsultants,
            s.FinalClinicalDiagnosis,
            s.DifferentialDiagnosis,
            s.EtiologyAndPathogenesis,
            s.PathologicalChangesInOrgans,
            s.TreatmentOfTheUnderlyingDisease,
            s.TreatmentOfThePatientAndItsRationale,
            s.Forecast,
            s.Prevention,
            s.Epicrisis,
            s.CurationDiary,
            s.ListOfUsedLiterature,
            s.doc,
            s.place
        );
        var list2= mutableListOf(list);
        var str=arrayToString(list2);
        return str.toString();
    }
    fun fillUsers(arr:ArrayList<Any>){
        try{
            var l:ArrayList<Any>;
            for(ar in arr){
                l= ar as ArrayList<Any>;
                var id=l.get(0).toString();
                var login=l.get(1).toString();
                var pswd=l.get(2).toString();
                var work=l.get(3).toString();
                var session=l.get(4).toString();
                var country=l.get(5).toString();
                var sex=l.get(6).toString();
                var isDoc=l.get(7).toString();
                var phone=l.get(8).toString();
                var imgLink=l.get(9).toString();
                var firstname=l.get(10).toString();
                var lastname=l.get(11).toString();
                var year=l.get(12).toString();
                var adres=l.get(13).toString();
                var prof=l.get(14).toString();
                var uHave=false;
                idList.add(id);
                var user = User(
                    "1",
                    ""+id,
                    ""+login,
                    ""+pswd,
                    ""+work,
                    ""+session,
                    ""+country,
                    ""+sex,
                    ""+isDoc,
                    ""+phone,
                    ""+imgLink,
                    ""+firstname,
                    ""+lastname,
                    ""+year,
                    ""+adres,
                    ""+prof
                )
                for(i in 0..Users.size-1){
                    if(Users[i].id==id){
                        var b = Users[i].imgBit;
                        Users[i]=user
                        if(b!=null){
                            Users[i].imgBit=b;
                        };
                        uHave=true;
                        break;
                    }
                }
                if(!uHave) {
                    Users.add(user);
                }
                if(id==myUser.id){
                    myUser=user;
                }
                getHimSicks(user);
            }
            println("writeUsersComplete<<<")
        }catch (e:Exception){
            logger(e);
        }
    }
    fun userById(id:String):User{
        var U = User();
        for(u in Users){
            if(u.id==id){
                U=u;
                break;
            }
        }
        return U;
    }
    fun sickById(id:String):Sick{
        var U = Sick();
        for(u in Sicks){
            if(u.id==id){
                U=u;
                break;
            }
        }
        return U;
    }
    fun fillLangs(arr:ArrayList<Any>){
        var l:ArrayList<Any>;
        for(ar in arr){
            l= ar as ArrayList<Any>;
            var id=l.get(0).toString();
            var lang=l.get(1).toString();
            var text=l.get(2).toString();
            text=unZipText(text);
            Langs.add(Lang(
                id,
                lang,
                text
            ));
        }
        println("writeLangComplete<<<")
    }
    //inflaters
    fun animInflate(what:Int){
        var ltInflater = layoutInflater
        var view = ltInflater.inflate(what, null, false)
        var linLayout: LinearLayout = findViewById(R.id.Anim)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun rem_anim(){
        var linLayout: LinearLayout = findViewById(R.id.Anim)
        linLayout.removeAllViews()
    }
    fun allWinInflate(what:Int){
        var ltInflater = layoutInflater
        var view = ltInflater.inflate(what, null, false)
        var linLayout: LinearLayout = findViewById(R.id.allWindow)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun allWinRemove(){
        var linLayout: LinearLayout = findViewById(R.id.allWindow)
        linLayout.removeAllViews()
    }

    fun spinRead(what: Int):String{
        var spin="0";
        try{
            var sp:Spinner=findViewById(what);
            spin=sp.selectedItem.toString();
        }catch (e:Exception){
        }
        return spin;
    }
    fun spinAd(what: Int, arr: ArrayList<String>, choose: String? = null){
        val spinner: Spinner = findViewById(what);
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        spinner.adapter = adapter;
        var o = 0
        for (i in arr.indices) {
            if (arr.get(i) == choose) {
                o = i
                break
            }
        }
        spinner.setSelection(o)
    }
    fun edHint(kam:Int,what:String){
        var ed:EditText=findViewById(kam);
        ed.setHint(what);
    }
    fun autoHint(kam:Int,what:String){
        var ed:AutoCompleteTextView=findViewById(kam);
        ed.setHint(what);
    }
    fun txtWrite(kam:Int,what:String){
        var txt:TextView=findViewById(kam);
        txt.setText(what);
    }
    fun txtRead(kam:Int):String{
        var txt:TextView=findViewById(kam);
        return txt.text.toString();
    }
    fun edRead(kam:Int):String{
        var ed:EditText=findViewById(kam);
        return ed.text.toString();
    }
    fun autoRead(kam:Int):String{
        var ed:AutoCompleteTextView=findViewById(kam);
        return ed.text.toString();
    }
    fun edWrite(kam:Int,what:String){
        var ed:EditText=findViewById(kam);
        ed.setText(what);
    }
    fun autoWrite(kam:Int,what:String){
        var ed:AutoCompleteTextView=findViewById(kam);
        ed.setText(what);
    }
    fun btnHint(kam:Int,what:String){
        var btn:Button=findViewById(kam);
        btn.setHint(what);
    }
    fun btnWrite(kam:Int,what:String){
        var btn:Button=findViewById(kam);
        btn.setText(what);
    }
    fun plus_inflate(what: Int) {
        val ltInflater = layoutInflater
        val view = ltInflater.inflate(what, null, false)
        val linLayout: LinearLayout = findViewById(R.id.plusPlace)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun plus_rem() {
        val linLayout: LinearLayout = findViewById(R.id.plusPlace);
        linLayout.removeAllViews();
    }
    fun frag_inflate(what: Int) {
        val ltInflater = layoutInflater
        val view = ltInflater.inflate(what, null, false)
        val linLayout: LinearLayout = findViewById(R.id.generalLayout)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun bot_inflate(what: Int) {
        val ltInflater = layoutInflater
        val view = ltInflater.inflate(what, null, false)
        val linLayout: LinearLayout = findViewById(R.id.bottomLayout)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun bot_rem() {
        val linLayout: LinearLayout = findViewById(R.id.bottomLayout)
        linLayout.removeAllViews()
    }
    fun head_inflate(what: Int) {
        val ltInflater = layoutInflater
        val view = ltInflater.inflate(what, null, false)
        val linLayout: LinearLayout = findViewById(R.id.headLayout)
        linLayout.removeAllViews()
        linLayout.addView(view)
    }
    fun head_rem() {
        val linLayout: LinearLayout = findViewById(R.id.headLayout)
        linLayout.removeAllViews()
    }
    //animations
    fun wChange(func: () -> Unit){
        var r=rand(0,3);
        when(r){
            0->
                wLeft({func()},R.id.constr);
            1->
                wRight({func()},R.id.constr);
            2->
                wUp({func()});
            3->
                wDown({func()});
        }
    }
    fun wChangeB(func: () -> Unit){
        var r=rand(0,3);
        when(r){
            0->
                wLeftBack({func()},R.id.constr);
            1->
                wRightBack({func()},R.id.constr);
            2->
                wUpBack({func()});
            3->
                wDownBack({func()});
        }
    }
    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }
    fun wDown(func: () -> Unit) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_down
                    )
                    val con: ConstraintLayout = findViewById(R.id.constr);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun wDownBack(func: () -> Unit) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_down_back
                    )
                    val con: ConstraintLayout = findViewById(R.id.constr);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun wUp(func: () -> Unit) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_up
                    )
                    val con: ConstraintLayout = findViewById(R.id.constr);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun wUpBack(func: () -> Unit) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_up_back
                    )
                    val con: ConstraintLayout = findViewById(R.id.constr);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun wLeft(func: () -> Unit,what:Int,only:Boolean?=null) {
        runOnUiThread {
            try {
                if(!animDo){
                    var animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_left
                    )
                    if(only!=null){
                        animAlpha = AnimationUtils.loadAnimation(
                            applicationContext, R.anim.anim_translate_left_only
                        )
                    }
                    val con: ConstraintLayout = findViewById(what);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e:Exception) {
                logger(e)
            }
        }
    }
    fun wLeftBack(func: () -> Unit,what:Int,only:Boolean?=null) {
        runOnUiThread {
            try {
                if(!animDo){
                    var animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_left_back
                    )
                    if(only!=null){
                        animAlpha = AnimationUtils.loadAnimation(
                            applicationContext, R.anim.anim_translate_left_back_only
                        )
                    }
                    val con: ConstraintLayout = findViewById(what);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e:Exception) {
                logger(e)
            }
        }
    }
    fun wRight(func: () -> Unit,what:Int) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_right
                    )
                    val con: ConstraintLayout = findViewById(what);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    fun wRightBack(func: () -> Unit,what:Int) {
        runOnUiThread {
            try {
                if(!animDo){
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.anim_translate_right_back
                    )
                    val con: ConstraintLayout = findViewById(what);
                    con.startAnimation(animAlpha)
                }
                func()
            } catch (e: java.lang.Exception) {
                logger(e)
            }
        }
    }
    //funcs
    fun unZipText(text:String):String{
        return VacReplacer().replacePismenoR(text);
    }
    fun zipText(text:String):String{
        var text=VacReplacer().ekr(text);
        return VacReplacer().replacePismeno(text);
    }
    fun clearSend(){
        sendData= mutableMapOf("ne" to "ne");
    }
    fun doPost(s:String,data:Map<String,Any>):Map<*,*>{
        var resp="";
        var mapJson: Map<*, *> = mutableMapOf("res" to "null");
        // Create JSON using JSONObject
        var jsonObject = JSONObject()
        for(d in data){
            jsonObject.put(d.key,d.value);
        }
        // Convert JSONObject to String
        var jsonObjectString = jsonObject.toString()
        var url = URL(s)
        var httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.setRequestProperty("Content-Type", "application/json") // The format of the content we're sending to the server
        httpURLConnection.setRequestProperty("Accept", "application/json") // The format of response we want to get from the server
        httpURLConnection.doInput = true
        httpURLConnection.doOutput = true
        // Send the JSON we created
        var outputStreamWriter = OutputStreamWriter(httpURLConnection.outputStream)
        outputStreamWriter.write(jsonObjectString)
        outputStreamWriter.flush()
        // Check if the connection is successful
        var responseCode = httpURLConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            var response = httpURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            // Convert raw JSON to pretty JSON using GSON library
            var gson = GsonBuilder().setPrettyPrinting().create()
            var prettyJson = gson.toJson(JsonParser.parseString(response))
            mapJson= gson.fromJson(response, MutableMap::class.java);
        } else {
            println("<<<HTTPURLCONNECTION_ERROR"+ responseCode.toString())
        }
        return mapJson;
    }
    fun spaceClear(string: String): String {
        var string = string
        var str: CharArray
        str = string.toCharArray()
        if (str.size == 0) {
            return string
        }
        while (str[str.size - 1] == ' ') {
            str = string.toCharArray()
            string = ""
            for (i in str.indices) {
                if (str.size - 1 == i && str[str.size - 1] == ' ') {
                } else {
                    string += str[i]
                }
            }
        }
        while(string[0] == ' '){
            str = string.toCharArray()
            string = ""
            for (i in 1..str.size-1) {
                string += str[i]
            }
        }
        return string
    }
    fun doGet(s: String): String {
        var line: String="";
        try{
            val url = URL(s)
            val connection = url.openConnection()
            BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
                while (inp.readLine().also { line = it } != null) {
                    1+1
                }
            }
        }catch (e:Exception){
        }
        return line;
    }
    fun logger(e:Exception){
        println("$e err<<<")
    }
    fun toastPrint(text:String){
        val myToast= Toast.makeText(this,text,Toast.LENGTH_SHORT);
        myToast.show();
    }
    fun tprint(text:String) {
        toastText=text;
        if(!printDo){
            printDo=true;
            val tprintClass = tprintClass()
            tprintClass.start()
        }
    }
    inner class tprintClass : Thread() {
        override fun run() {
            try{
                var ltInflater = layoutInflater
                var linLayout: LinearLayout = findViewById(R.id.Notify);
                runOnUiThread {
                    var view = ltInflater.inflate(R.layout.frag_notify, null, false)
                    linLayout.removeAllViews()
                    linLayout.addView(view)
                    txtWrite(R.id.notifyText, toastText);
                }
                sleep(3000);
                runOnUiThread {
                    val animAlpha = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.alpha_back
                    )
                    val con2: ConstraintLayout = findViewById(R.id.alphaBlackNotify);
                    con2.startAnimation(animAlpha);
                }
                runOnUiThread {
                    linLayout.removeAllViews()
                }
                printDo=false;
            }catch (e:Exception){
                logger(e);
            }
        }
    }
    fun w(text:String): String {
        return text;
    }
    fun read(fileName: String?): String? {
        var res = ""
        val path = getExternalFilesDir(null)
        val file = File(path,fileName)
        try {
            BufferedReader(FileReader(file)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    res=res+line;
                }
            }
        } catch (e: FileNotFoundException) {
            res = "fileNotFound"
            println("$e err<<<")
        } catch (e: IOException) {
            e.printStackTrace()
            println("$e err<<<")
        }

        return res
    }
    fun write(nameFile: String?, value: String) {
        try {
            val path = getExternalFilesDir(null)

            val file = File(path,nameFile);
            val text = value;

            FileOutputStream(file).use { fos ->
                OutputStreamWriter(fos, Charsets.UTF_8).use { osw ->
                    BufferedWriter(osw).use { bf -> bf.write(text) }
                }
            }
        } catch (e: FileNotFoundException) {
            println("$e err<<<")
        } catch (e: IOException) {
            println("$e err<<<")
        }
    }
    fun arrayToString2(maxList: ArrayList<Any>): String? {
        var string = ""
        var Min: java.util.ArrayList<*>
        val m = ""
        for (i in maxList.indices) {
            Min = maxList[i] as java.util.ArrayList<*>
            for (y in Min.indices) {
                string = "$string$m|"
            }
            string = "$string^"
        }
        return string
    }
    fun arrayToString(maxList: MutableList<MutableList<String>>): String? {
        var string = ""
        var Min=mutableListOf<Any>(arrayOf(""));
        for (i in 0..maxList.size-1) {
            Min = maxList[i] as MutableList<Any>;
            for (y in 0..Min.size-1) {
                string = string+Min[y]+"|"
            }
            string = "$string^"
        }
        return string



    }
    fun stringToArray(string: String): MutableList<Any> {
        //val arr: ArrayList<Any> = ArrayList<Any>()
        //var ord: ArrayList<Any> = ArrayList<Any>()
        var arr = mutableListOf<Any>();
        var ord = mutableListOf<Any>();
        val str = string.toCharArray()
        var pos = ""
        for (i in str.indices) {
            if (str[i] == '^') {
                arr.add(ord)
                ord = ArrayList<Any>()
            } else {
                pos = if (str[i] == '|') {
                    ord.add(pos)
                    ""
                } else {
                    pos + str[i]
                }
            }
        }
        return arr
    }
    fun zipList(list:MutableList<Int>):String{
        val nList:MutableList<Int> = mutableListOf(0);nList.remove(0);
        val L:MutableList<Int> = mutableListOf(0);L.remove(0);
        var string="";
        for (l in list) {
            nList.add(l);
            while (nList.size != 0){
                var m = min(nList);
                L.add(m);
                nList.remove(m);
            }
            var pred = min(L);
            string = pred.toString();
            var V = 0;
            for (i in 0..L.size-1) {
                if (pred + 1 == L[i]){
                    if (i + 1 == L.size) {
                        if (V == L[i - 1]) {
                            string += "," + L[i].toString();
                        }else {
                            string += "-" + L[i - 1].toString() + "," + L[i].toString();
                            V = L[i];
                        }
                    }
                }
                else {
                    if (pred != L[i]) {
                        if( V == L[i - 1]){
                            string += "," + L[i].toString();
                        }else {
                            string += "-" + L[i - 1].toString() + "," + L[i].toString();
                        }
                        V = L[i];
                    }
                }
                pred = L[i];
            }
        }
        return string;
    }
    fun zipNums(text:String):String{
        var txt=text;
        for (n in numZip) {
            txt = txt.replace(n.key.toString(),n.value.toString());
        }
        return txt;
    }
    fun unZipNums(text:String):String{
        var txt=text;
        for (n in numZip) {
            txt = txt.replace(n.value.toString(),n.key.toString());
        }
        return txt;
    }
    fun unZipList(txt:String):MutableList<Int>{
        var text=txt+","
        var List:MutableList<Int> = mutableListOf(0);List.remove(0);
        var string="";
        var strin="";
        var A=0;
        var B=0;
        var C=0;
        for (i in 0..text.length-1) {
            var t = text[i].toString();
            if (t == ",") {
                if ("-" in string) {
                    string += ",";
                    for (s in string) {
                        if (s.toString() == "-"){
                            A = strin.toInt();
                            strin = ""
                        }else if(s.toString() ==","){
                            B = strin.toInt();
                            strin = ""
                            for(r in A..B) {
                                List.add(r);
                            }
                        }else{
                            strin += s;
                        }
                    }
                    }else{
                        List.add(string.toInt());
                    }
                    string = "";
            }else {
                string += t;
            }
        }
        return List;
    }
    fun countryGet() {
        countryList.add("Afghanistan")
        countryList.add("Albania")
        countryList.add("Algeria")
        countryList.add("Andorra")
        countryList.add("Angola")
        countryList.add("Antiguaand Barbuda")
        countryList.add("Argentina")
        countryList.add("Armenia")
        countryList.add("Australia")
        countryList.add("Austria")
        countryList.add("Azerbaijan")
        countryList.add("Bahamas")
        countryList.add("Bahrain")
        countryList.add("Bangladesh")
        countryList.add("Barbados")
        countryList.add("Belarus")
        countryList.add("Belgium")
        countryList.add("Belize")
        countryList.add("Benin")
        countryList.add("Bhutan")
        countryList.add("Bolivia")
        countryList.add("Bosniaand Herzegovina")
        countryList.add("Botswana")
        countryList.add("Brazil")
        countryList.add("Brunei")
        countryList.add("Bulgaria")
        countryList.add("Burkina Faso")
        countryList.add("Burundi")
        countryList.add("Côted Ivoire")
        countryList.add("Cabo Verde")
        countryList.add("Cambodia")
        countryList.add("Cameroon")
        countryList.add("Canada")
        countryList.add("Central African Republic")
        countryList.add("Chad")
        countryList.add("Chile")
        countryList.add("China")
        countryList.add("Colombia")
        countryList.add("Comoros")
        countryList.add("Congo(Congo-Brazzaville)")
        countryList.add("CostaRica")
        countryList.add("Croatia")
        countryList.add("Cuba")
        countryList.add("Cyprus")
        countryList.add("Czechia(Czech Republic)")
        countryList.add("Democratic Republic of the Congo")
        countryList.add("Denmark")
        countryList.add("Djibouti")
        countryList.add("Dominica")
        countryList.add("Dominican Republic")
        countryList.add("Ecuador")
        countryList.add("Egypt")
        countryList.add("ElSalvador")
        countryList.add("Equatorial Guinea")
        countryList.add("Eritrea")
        countryList.add("Estonia")
        countryList.add("Ethiopia")
        countryList.add("Fiji")
        countryList.add("Finland")
        countryList.add("France")
        countryList.add("Gabon")
        countryList.add("Gambia")
        countryList.add("Georgia")
        countryList.add("Germany")
        countryList.add("Ghana")
        countryList.add("Greece")
        countryList.add("Grenada")
        countryList.add("Guatemala")
        countryList.add("Guinea")
        countryList.add("Guinea-Bissau")
        countryList.add("Guyana")
        countryList.add("Haiti")
        countryList.add("HolySee")
        countryList.add("Honduras")
        countryList.add("Hungary")
        countryList.add("Iceland")
        countryList.add("India")
        countryList.add("Indonesia")
        countryList.add("Iran")
        countryList.add("Iraq")
        countryList.add("Ireland")
        countryList.add("Israel")
        countryList.add("Italy")
        countryList.add("Jamaica")
        countryList.add("Japan")
        countryList.add("Jordan")
        countryList.add("Kazakhstan")
        countryList.add("Kenya")
        countryList.add("Kiribati")
        countryList.add("Kuwait")
        countryList.add("Kyrgyzstan")
        countryList.add("Laos")
        countryList.add("Latvia")
        countryList.add("Lebanon")
        countryList.add("Lesotho")
        countryList.add("Liberia")
        countryList.add("Libya")
        countryList.add("Liechtenstein")
        countryList.add("Lithuania")
        countryList.add("Luxembourg")
        countryList.add("Madagascar")
        countryList.add("Malawi")
        countryList.add("Malaysia")
        countryList.add("Maldives")
        countryList.add("Mali")
        countryList.add("Malta")
        countryList.add("Marshall Islands")
        countryList.add("Mauritania")
        countryList.add("Mauritius")
        countryList.add("Mexico")
        countryList.add("Micronesia")
        countryList.add("Moldova")
        countryList.add("Monaco")
        countryList.add("Mongolia")
        countryList.add("Montenegro")
        countryList.add("Morocco")
        countryList.add("Mozambique")
        countryList.add("Myanmar(formerly Burma)")
        countryList.add("Namibia")
        countryList.add("Nauru")
        countryList.add("Nepal")
        countryList.add("Netherlands")
        countryList.add("New Zealand")
        countryList.add("Nicaragua")
        countryList.add("Niger")
        countryList.add("Nigeria")
        countryList.add("North Korea")
        countryList.add("North Macedonia")
        countryList.add("Norway")
        countryList.add("Oman")
        countryList.add("Pakistan")
        countryList.add("Palau")
        countryList.add("Palestine State")
        countryList.add("Panama")
        countryList.add("Papua New Guinea")
        countryList.add("Paraguay")
        countryList.add("Peru")
        countryList.add("Philippines")
        countryList.add("Poland")
        countryList.add("Portugal")
        countryList.add("Qatar")
        countryList.add("Romania")
        countryList.add("Russia")
        countryList.add("Rwanda")
        countryList.add("Saint Kitts and Nevis")
        countryList.add("SaintLucia")
        countryList.add("Saint Vincentandthe Grenadines")
        countryList.add("Samoa")
        countryList.add("SanMarino")
        countryList.add("Sao Tomeand Principe")
        countryList.add("SaudiArabia")
        countryList.add("Senegal")
        countryList.add("Serbia")
        countryList.add("Seychelles")
        countryList.add("SierraLeone")
        countryList.add("Singapore")
        countryList.add("Slovakia")
        countryList.add("Slovenia")
        countryList.add("Solomon Islands")
        countryList.add("Somalia")
        countryList.add("South Africa")
        countryList.add("South Korea")
        countryList.add("South Sudan")
        countryList.add("Spain")
        countryList.add("SriLanka")
        countryList.add("Sudan")
        countryList.add("Suriname")
        countryList.add("Sweden")
        countryList.add("Switzerland")
        countryList.add("Syria")
        countryList.add("Tajikistan")
        countryList.add("Tanzania")
        countryList.add("Thailand")
        countryList.add("Timor-Leste")
        countryList.add("Togo")
        countryList.add("Tonga")
        countryList.add("Trinidadand Tobago")
        countryList.add("Tunisia")
        countryList.add("Turkey")
        countryList.add("Turkmenistan")
        countryList.add("Tuvalu")
        countryList.add("Uganda")
        countryList.add("Ukraine")
        countryList.add("United Arab Emirates")
        countryList.add("United Kingdom")
        countryList.add("United States of America")
        countryList.add("Uruguay")
        countryList.add("Uzbekistan")
        countryList.add("Vanuatu")
        countryList.add("Venezuela")
        countryList.add("Vietnam")
        countryList.add("Yemen")
        countryList.add("Zambia")
        countryList.add("Zimbabwe")
        numZip = mutableMapOf("10" to "a","11" to "b","12" to "c","13" to "d","14" to "e","15" to "f","16" to "g","17" to "h","18" to "i","19" to "j","20" to "k","21" to "l","22" to "m","23" to "n","24" to "o","25" to "p","26" to "q","27" to "r","28" to "s","29" to "t","30" to "u","31" to "v","32" to "w",
            "33" to "x","34" to "y","35" to "z","36" to "A","37" to "B","38" to "C","39" to "D","40" to "E","41" to "F","42" to "G","43" to "H","44" to "I","45" to "J","46" to "K","47" to "L","48" to "M","49" to "N","50" to "O","51" to "P","52" to "Q","53" to "R","54" to "S","55" to "T","56" to "U",
            "57" to "V","58" to "W","59" to "X","60" to "Y","61" to "Z");
    }
    fun fillCountries(list:ArrayList<String>): Array<String?>? {
        val market_hints = arrayOfNulls<String>(list.size)
        try {
            for (i in list.indices) {
                market_hints[i] = list[i];
            }
        } catch (e: Exception) {
            logger(e)
        }
        return market_hints
    }
    private fun setupControls(){
        var result="null";
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()
        var binding: View = findViewById(R.id.cameraSurfaceView);
        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int,
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1 && bark==0) {
                    bark++;
                    println("<<<Started")
                    scannedValue = barcodes.valueAt(0).rawValue
                    //Don't forget to add this line printing value or finishing activity must run on main thread
                    runOnUiThread {
                        cameraSource.stop();
                        result=scannedValue;
                            try{
                                closeShtor();
                                if(result.length==8){
                                    sendData = mutableMapOf("var" to "ses","val" to result);
                                    animCountPlus();
                                    getUser();
                                }else{
                                    tprint(w("Неккоректный код"))
                                }
                                //edWrite(R.id.pacSearchText,result);
                                //docSearchInflate(result);
                                //edWrite(R.id.pacSearchText,"");

                            }catch (e:Exception){
                                logger(e);
                            }
                        //Toast.makeText(this@MainActivity, "value- $scannedValue", Toast.LENGTH_SHORT).show()
                        //finish()
                    }
                }else
                {
                    Toast.makeText(this@MainActivity, "value- else", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            avatarPhoto ->
                if (resultCode == RESULT_OK) {
                    try {
                        var imageUri = imageReturnedIntent?.data
                        var imageStream = contentResolver.openInputStream(imageUri!!)
                        var selectedImage = BitmapFactory.decodeStream(imageStream)
                        bitImage = selectedImage
                        changePhoto();

                    } catch (e: FileNotFoundException) {
                        logger(e);
                    }
                }else{
                    println("Error rrr<<<")
                }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            workMode=0;
            if (step == "enter") {
                val alertDialog: AlertDialog = AlertDialog.Builder(this)
                    .setMessage(w("Выйти?"))
                    .setPositiveButton(w("Да"),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            finish()
                            return@OnClickListener
                        })
                    .setNegativeButton(w("Нет"),
                        DialogInterface.OnClickListener { dialogInterface, i -> return@OnClickListener })
                    .show()
                false
            } else if(shtor){
                closeShtor();
                return false;
            }else {
                backButtons()
                return false;
            }
        } else super.onKeyDown(keyCode, event)

    }
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun append(arr: Array<String>, element: Any): Any {
        val list: MutableList<Any> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
}