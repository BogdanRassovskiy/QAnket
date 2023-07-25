
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.calen_day.qanket.R
import com.calen_day.qanket.classes.Sick
class sickAdapter(context: Context, items: MutableList<Sick>) : BaseAdapter() {
    private val context: Context =context
    private val items: MutableList<Sick> = items

    override fun getCount(): Int {
        return items.size //returns total of items in the list
    }
    override fun getItem(position: Int): Any {
        return items[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun getProduct(position: Int): Sick {
        return getItem(position) as Sick
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var v: View? = convertView
        if (v == null) {
            v = LayoutInflater.from(context)
                .inflate(R.layout.item_sick, parent, false)
        }
        var txt:TextView;
        try{
            val p: Sick = getProduct(position);
            txt= v!!.findViewById(R.id.name)
            if(p.FinalClinicalDiagnosis!=""){
                txt.text=p.FinalClinicalDiagnosis;
            }else if(p.DifferentialDiagnosis!=""){
                txt.text=p.DifferentialDiagnosis;
            }else{
                txt.text=p.date;
            }
            txt= v!!.findViewById(R.id.date)
            txt.text=p.date;


            var con:ConstraintLayout = v.findViewById(R.id.thisSick);
            con.setTag(p.id);
        }catch (e:Exception){
            println(e.toString()+"err<<<")
        }
        return v
    }

}