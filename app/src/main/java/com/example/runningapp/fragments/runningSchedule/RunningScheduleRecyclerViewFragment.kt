package com.example.runningapp.fragments.runningSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.AppApplication
import com.example.runningapp.adapters.RunningScheduleAdapter
import com.example.runningapp.databinding.FragmentRecyclerViewBinding
import com.example.runningapp.util.OrientationUtil.StaticFunctions.isLandscapeMode
import com.example.runningapp.viewmodels.RunningScheduleViewModel
import com.example.runningapp.viewmodels.RunningScheduleViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunningScheduleRecyclerViewFragment : Fragment() {
    private val viewModel: RunningScheduleViewModel by activityViewModels {
        RunningScheduleViewModelFactory((activity?.application as AppApplication).runningScheduleRepository)
    }

    private var _binding: FragmentRecyclerViewBinding? = null

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = context?.let {
            RunningScheduleAdapter(it, viewModel.entries, { position ->
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


        // floating action button animation
        if (!context?.let { isLandscapeMode(it) }!!) {
            val recyclerView: RecyclerView = binding.recyclerView
            val fab: FloatingActionButton = (parentFragment as RunningScheduleFragment).getAddButton()

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


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}