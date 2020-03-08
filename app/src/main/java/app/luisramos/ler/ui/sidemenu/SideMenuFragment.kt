package app.luisramos.ler.ui.sidemenu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.luisramos.ler.BuildConfig
import app.luisramos.ler.R
import app.luisramos.ler.ui.base.BaseFragment
import app.luisramos.ler.ui.sidemenu.SideMenuViewModel.UiState
import kotlinx.android.synthetic.main.fragment_main.recyclerView
import kotlinx.android.synthetic.main.fragment_side_menu.*

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.onItemClick = { viewModel.onItemTapped(it) }

        versionTextView.text = "${BuildConfig.VERSION_NAME}.${BuildConfig.VERSION_CODE}"

        viewModel.uiState.observe(viewLifecycleOwner, Observer {
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