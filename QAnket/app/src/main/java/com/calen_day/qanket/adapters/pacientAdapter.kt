import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.calen_day.qanket.R
import com.calen_day.qanket.classes.User
class pacientAdapter(context: Context, items: MutableList<User>) : BaseAdapter() {
    private val context: Context =context
    private val items: MutableList<User> = items

    override fun getCount(): Int {
        return items.size //returns total of items in the list
    }
    override fun getItem(position: Int): Any {
        return items[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun getProduct(position: Int): User {
        return getItem(position) as User
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var v: View? = convertView
        if (v == null) {
            v = LayoutInflater.from(context)
                .inflate(R.layout.item_pacient, parent, false)
        }
        var txt:TextView;

        try{
            val p: User = getProduct(position);
            txt= v!!.findViewById(R.id.firstname)
            txt.text=p.firstname;
            txt= v!!.findViewById(R.id.lastname)
            txt.text=p.lastname;
            txt= v!!.findViewById(R.id.year)
            txt.text=p.year;
            var img=v.findViewById<ImageView>(R.id.face)
            if(p.imgBit==null){
                img.setBackgroundResource(R.drawable.user2)
            }else{
                img.setImageBitmap(p.imgBit);
            }
            println(p.lastname+"err<<<")
            var con:ConstraintLayout = v.findViewById(R.id.thisPacient);
            con.setTag(p.id);
        }catch (e:Exception){
            println(e.toString()+"err<<<")
        }
        return v
    }

}