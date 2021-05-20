package com.myapps.mypsikolog.home.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ProfileFragmentBinding
import com.myapps.mypsikolog.utils.Constants.Companion.ADDRESS_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.EMAIL_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.PHONE_PATIENTS
import com.myapps.mypsikolog.utils.PreferenceManager

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private var _binding: ProfileFragmentBinding? =null
    private lateinit var preferenceManager: PreferenceManager

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireActivity())

        binding.textNameUser.text = String.format("%s", preferenceManager.getString(NAME_PATIENTS))
        binding.textEmailUser.text = String.format("%s", preferenceManager.getString(EMAIL_PATIENTS))
        binding.textPhoneUser.text = String.format("%s", preferenceManager.getString(PHONE_PATIENTS))
        binding.textAddressUser.text = String.format("%s", preferenceManager.getString(ADDRESS_PATIENTS))

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}