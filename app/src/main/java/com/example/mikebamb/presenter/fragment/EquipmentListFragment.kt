package com.example.mikebamb.presenter.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mikebamb.R
import com.example.mikebamb.data.local.EquipmentEntity
import com.example.mikebamb.databinding.FragmentEquipmentListBinding
import com.example.mikebamb.presenter.adapter.EquipmentAdapter
import com.example.mikebamb.presenter.viewmodel.EquipmentListViewModel


class EquipmentListFragment : Fragment() {
    private var _binding: FragmentEquipmentListBinding? = null
    private val binding get() = _binding!!
    private var mAdapter = EquipmentAdapter(ArrayList())
    private val viewModel by activityViewModels<EquipmentListViewModel>()
    private val args: EquipmentListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEquipmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subSubCategory = args.equipmentsList
        setupRecyclerView()
        if (subSubCategory.isBlank()) {
            viewModel.localGetAllEquipments()
        } else {
            binding.editName.text = subSubCategory
            viewModel.localGetASubSubCategory(subSubCategory)
        }
        setHasOptionsMenu(true)
        getActionbar()
    }

    private fun getActionbar() {
        return (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        mAdapter.onItemClick = { position ->
            navigateToDescription(position)
        }
        viewModel.recyclerViewItems.observe(
            viewLifecycleOwner,
            {
                mAdapter.submitList(it)
                mAdapter.allEquipmentList = it as MutableList<EquipmentEntity>
            })
    }

    private fun navigateToDescription(position: Int) {
        val partNumberClicked = viewModel.recyclerViewItems.value?.get(position)?.partNumber!!
        val action =
            EquipmentListFragmentDirections.actionEquipmentListFragmentToDescriptionEquipmentFragment(
                partNumberClicked
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}