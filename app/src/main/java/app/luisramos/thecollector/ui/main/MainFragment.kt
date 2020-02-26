package app.luisramos.thecollector.ui.main

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.luisramos.thecollector.R
import app.luisramos.thecollector.ui.base.BaseFragment
import app.luisramos.thecollector.ui.sidemenu.FeedAdapter
import app.luisramos.thecollector.ui.subscription.AddSubscriptionFragment
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FeedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
        }

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it
        })
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.items = it
        })
    }

    override fun onResume() {
        super.onResume()
        toolbar?.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        viewModel.loadData()
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
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, AddSubscriptionFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}