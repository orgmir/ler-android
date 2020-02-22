package app.luisramos.thecollector.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.luisramos.thecollector.R
import app.luisramos.thecollector.data.Feed
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FeedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = adapter
//
//        swipeRefreshLayout.setOnRefreshListener {
//            viewModel.loadData()
//        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.add_subscription -> {
            showAddSubscriptionDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showAddSubscriptionDialog() {
        val view = layoutInflater.inflate(R.layout.view_edit_text, null)
        AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(R.string.add) { _, _ ->
                val editText = view.findViewById<TextInputEditText>(R.id.editText)
                viewModel.addSubscription(editText.text.toString().trim())
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}


class FeedAdapter : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var items: List<Feed> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.apply {
            textView1.text = item.title
            textView2.text = "Some random description from the blog"
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
