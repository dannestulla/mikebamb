package com.gohan.qrmyship.main_app.presenter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gohan.qrmyship.databinding.FragmentAddEquipmentBinding
import com.gohan.qrmyship.main_app.data.toEquipmentEntity
import com.gohan.qrmyship.main_app.domain.EquipmentModel
import com.gohan.qrmyship.main_app.domain.myConstants
import com.gohan.qrmyship.main_app.domain.myConstants.CAN_EDIT
import com.gohan.qrmyship.main_app.presenter.viewmodel.DescriptionViewModel
import java.sql.Timestamp
import java.util.*


class AddEquipmentFragment : Fragment() {
    private var _binding: FragmentAddEquipmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DescriptionViewModel>()
    private var isEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEquipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addequipment.setOnClickListener { addNewItem() }
        checkIfUserCanEdit()
    }

    private fun addNewItem() {
        val equipmentQRnumber = binding.editPartNumber.text.toString()
        val equipmentQRname = binding.editEquipName.text.toString()
        val equipmentQRcode = getRandomString(8)
        if (equipmentQRnumber.isNotEmpty() || equipmentQRname.isNotEmpty()) {
            binding.editQrCode.setText(equipmentQRcode)
        }
        val newItem = EquipmentModel(
            binding.editPartNumber.text.toString(),
            binding.editEquipName.text.toString(),
            binding.editManufacturer.text.toString(),
            binding.editModel.text.toString(),
            binding.editInstallDate.text.toString(),
            binding.editOrderNumber.text.toString(),
            binding.editManuallink.text.toString(),
            binding.editHours.text.toString(),
            binding.editQrCode.text.toString(),
            binding.editComments.text.toString(),
            binding.editCategory1.text.toString(),
            binding.editCategory2.text.toString(),
            binding.editCategory3.text.toString(),
            binding.editObservations1.text.toString(),
            binding.editObservations2.text.toString(),
            binding.editObservations3.text.toString(),
            binding.editObservations4.text.toString(),
            binding.editObservations5.text.toString(),
            Timestamp(Date().time),
        )
        checkForEmpty(newItem)
        if (!isEmpty) {
            viewModel.localAddNewItem(newItem.toEquipmentEntity())
            viewModel.remoteAddNewItem(newItem.toEquipmentEntity())
            Toast.makeText(context, "New Item Added!", Toast.LENGTH_LONG).show()
            clearFields()
        } else {
            Toast.makeText(context, "Required Fields Are Empty!", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        binding.editPartNumber.setText("")
        binding.editEquipName.setText("")
        binding.editManufacturer.setText("")
        binding.editModel.setText("")
        binding.editInstallDate.setText("")
        binding.editOrderNumber.setText("")
        binding.editManuallink.setText("")
        binding.editHours.setText("")
        binding.editQrCode.setText("")
        binding.editComments.setText("")
        binding.editCategory1.setText("")
        binding.editCategory2.setText("")
        binding.editCategory3.setText("")
        binding.editObservations1.setText("")
        binding.editObservations2.setText("")
        binding.editObservations3.setText("")
        binding.editObservations4.setText("")
        binding.editObservations5.setText("")
    }

    private fun checkForEmpty(newItem: EquipmentModel): Boolean {
        isEmpty = false
        if (newItem.nameModel.trim().isBlank()) {
            binding.editEquipName.error = "Must not be empty!"
            isEmpty = true
        }
        if (newItem.partNumberModel.trim().isBlank()) {
            binding.editPartNumber.error = "Must not be empty!"
            isEmpty = true
        }
        if (newItem.category1Model.trim().isBlank()) {
            binding.editCategory1.error = "Must not be empty!"
            isEmpty = true
        }
        if (newItem.category2Model.trim().isBlank()) {
            binding.editCategory2.error = "Must not be empty!"
            isEmpty = true
        }
        if (newItem.category3Model.trim().isBlank()) {
            binding.editCategory3.error = "Must not be empty!"
            isEmpty = true
        }
        return isEmpty
    }

    private fun checkIfUserCanEdit() {
        val sharedPref =
            context?.getSharedPreferences(myConstants.SHARED_PREF, Context.MODE_PRIVATE)!!
        if (!sharedPref.getBoolean(CAN_EDIT, false)) {
            binding.addequipment.isEnabled = false
            binding.addequipment.isVisible = false
            binding.editCategory1.setFocusable(false)
            binding.editCategory2.setFocusable(false)
            binding.editCategory3.setFocusable(false)
            binding.editComments.setFocusable(false)
            binding.editEquipName.setFocusable(false)
            binding.editHours.setFocusable(false)
            binding.editInstallDate.setFocusable(false)
            binding.editManufacturer.setFocusable(false)
            binding.editManuallink.setFocusable(false)
            binding.editModel.setFocusable(false)
            binding.editObservations1.setFocusable(false)
            binding.editObservations2.setFocusable(false)
            binding.editObservations3.setFocusable(false)
            binding.editObservations4.setFocusable(false)
            binding.editObservations5.setFocusable(false)
            binding.editOrderNumber.setFocusable(false)
            binding.editPartNumber.setFocusable(false)
            binding.editQrCode.setFocusable(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}



