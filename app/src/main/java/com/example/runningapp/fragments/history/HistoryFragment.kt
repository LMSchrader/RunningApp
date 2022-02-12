package com.example.runningapp.fragments.history

import android.os.Bundle
import androidx.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.*
import com.example.runningapp.AppApplication
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentHistoryBinding
import com.example.runningapp.fragments.dialogs.CancelContinueDialogFragment
import com.example.runningapp.viewmodels.HistoryViewModel
import com.example.runningapp.viewmodels.HistoryViewModelFactory

class HistoryFragment : Fragment(), CancelContinueDialogFragment.CancelContinueDialogListener {
    private val historyViewModel: HistoryViewModel by activityViewModels {
        HistoryViewModelFactory((activity?.application as AppApplication).runHistoryRepository)
    }
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var forwardScene: Scene
    private lateinit var backwardScene: Scene
    private lateinit var callback: OnBackPressedCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
            }
        }

        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (historyViewModel.historyFragmentIsInSplitScreenMode) {
                doBackwardTransition()
            } else {
                callback.isEnabled = false
                activity?.onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        forwardScene = Scene.getSceneForLayout(
            root as ViewGroup,
            R.layout.fragment_history_scene,
            requireContext()
        )
        backwardScene = Scene.getSceneForLayout(root, R.layout.fragment_history, requireContext())

        if (historyViewModel.historyFragmentIsInSplitScreenMode) {
            doForwardTransition()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun doForwardTransition() {
        TransitionManager.go(forwardScene, null)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
            replace<HistoryViewPager2Fragment>(R.id.graph_fragment_container)
        }
        historyViewModel.historyFragmentIsInSplitScreenMode = true
    }

    fun doBackwardTransition() {
        TransitionManager.go(backwardScene, null)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
            remove(childFragmentManager.findFragmentById(R.id.graph_fragment_container)!!)
        }
        historyViewModel.historyFragmentIsInSplitScreenMode = false
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        // delete entry
        historyViewModel.currentRunHistoryEntryMetaDataWithMeasurements.value?.let { historyViewModel.delete(it) }
        doBackwardTransition()
    }
}