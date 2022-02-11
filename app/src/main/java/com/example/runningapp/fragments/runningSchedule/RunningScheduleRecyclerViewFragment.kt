package com.example.runningapp.fragments.runningSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.adapters.RunningScheduleRecyclerViewAdapter
import com.example.runningapp.data.RunningScheduleEntry
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode
import com.example.runningapp.util.RecyclerViewItemTouchHelper
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.DividerItemDecoration

class RunningScheduleRecyclerViewFragment : Fragment(),
    RecyclerViewItemTouchHelper.RecyclerItemTouchHelperListener {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private var adapter: RecyclerView.Adapter<RunningScheduleRecyclerViewAdapter.ViewHolder>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // add recycler view adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = context?.let {
            RunningScheduleRecyclerViewAdapter(viewModel.entries, { position ->
                viewModel.currentEntry.value = position?.let { it1 ->
                    viewModel.entries.value?.get(
                        it1
                    )
                }
                if (!context?.let { isLandscapeMode(it) }!!) {
                    view?.findNavController()
                        ?.navigate(R.id.action_nav_running_schedule_to_nav_running_schedule_entry)
                } else {
                    if (parentFragmentManager.findFragmentById(R.id.rightFragment) == null) {
                        (parentFragment as? RunningScheduleFragment)?.addSecondFragment()
                    }
                }
            }, viewLifecycleOwner)
        }
        binding.recyclerView.adapter = adapter


        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        // swipe to delete items in portrait mode
        if(!context?.let { isLandscapeMode(it) }!!) {
            val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this)
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)
        }

        animateFloatingActionButtonWhenScrollingRecyclerView()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateFloatingActionButtonWhenScrollingRecyclerView() {
        if (!context?.let { isLandscapeMode(it) }!!) {
            val recyclerView: RecyclerView = binding.recyclerView
            val fab: FloatingActionButton =
                (parentFragment as RunningScheduleFragment).getAddButton()

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 10 && fab.isShown) {
                        fab.hide()
                    }

                    if (dy < -10 && !fab.isShown) {
                        fab.show()
                    }

                    if (!recyclerView.canScrollVertically(-1)) {
                        fab.show()
                    }
                }
            })
        }
    }

    /**
     * callback when recycler view is swiped
     * Delete recycler view item on swiped
     * undo option in snackbar
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is RunningScheduleRecyclerViewAdapter.ViewHolder) {
            val deletedItem: RunningScheduleEntry =
                viewModel.entries.value?.get(viewHolder.adapterPosition) ?: return

            viewModel.delete(deletedItem)

            view?.let {
                Snackbar.make(it, deletedItem.title, Snackbar.LENGTH_LONG)
                    .setAction(
                        getString(R.string.undo)
                    ) {
                        viewModel.insert(deletedItem)
                    }
            }?.show()
        }
    }
}