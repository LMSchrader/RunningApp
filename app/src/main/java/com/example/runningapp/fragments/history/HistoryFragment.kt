package com.example.runningapp.fragments.history

import android.os.Bundle
import androidx.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.runningapp.R
import com.example.runningapp.databinding.FragmentHistoryBinding
import com.example.runningapp.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by activityViewModels()
    private var _binding: FragmentHistoryBinding? = null
    private lateinit var forwardScene: Scene
    private lateinit var backwardScene: Scene
    private lateinit var callback: OnBackPressedCallback


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
            }
        }
        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            doBackwardTransition()
            callback.isEnabled = false
        }

        //historyViewModel.isInSplitScreenMode = false
        //callback.isEnabled = false
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

        if (historyViewModel.isInSplitScreenMode) {
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
            add<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
            add<HistoryGraphFragment>(R.id.graph_fragment_container)
        }
        historyViewModel.isInSplitScreenMode = true
        callback.isEnabled = true
    }

    fun doBackwardTransition() {
        TransitionManager.go(backwardScene, null)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<HistoryRecyclerViewFragment>(R.id.recycler_view_fragment_container)
        }
        historyViewModel.isInSplitScreenMode = false
    }
}