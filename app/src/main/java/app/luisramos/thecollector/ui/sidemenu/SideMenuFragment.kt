package app.luisramos.thecollector.ui.sidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.luisramos.thecollector.R
import app.luisramos.thecollector.ui.base.BaseFragment
import app.luisramos.thecollector.ui.sidemenu.SideMenuViewModel.UiState
import kotlinx.android.synthetic.main.main_fragment.*

class SideMenuFragment : BaseFragment() {

    companion object {
        fun newInstance() = SideMenuFragment()
    }

    private val viewModel: SideMenuViewModel by viewModels()
    private val adapter = FeedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_side_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.onItemClick = { viewModel.onItemTapped(it) }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadData()
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it == UiState.Loading
            when (it) {
                is UiState.Error ->
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                is UiState.Success ->
                    adapter.items = it.items
            }
        })
        viewModel.selectedFeed.observe(viewLifecycleOwner, Observer {
            adapter.selectedFeed = it
        })
    }
}