import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.calen_day.qanket.R
import com.calen_day.qanket.classes.User
class MyListAdapter(context: Context, items: MutableList<User>) : BaseAdapter() {
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
        val p: User = getProduct(position);
        var txt:TextView= v!!.findViewById(R.id.firstname)
        txt.text=p.firstname;
        return v
    }

}