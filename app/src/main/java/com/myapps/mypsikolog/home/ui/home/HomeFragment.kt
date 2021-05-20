package com.myapps.mypsikolog.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.consult.ConsultActivity
import com.myapps.mypsikolog.databinding.FragmentHomeBinding
import com.myapps.mypsikolog.ui.order.OrderActivity
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.PreferenceManager

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var preferenceManager: PreferenceManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        preferenceManager = PreferenceManager(requireActivity())

        binding.textUsername.text = String.format("%s",
            preferenceManager.getString(NAME_PATIENTS))

        binding.cardMyOrder.setOnClickListener {
            startActivity(Intent(activity, OrderActivity::class.java))
        }

        binding.cardViewConsult.setOnClickListener(this)
        binding.cardViewDiagnoze.setOnClickListener(this)
//        binding.cardMyOrder.setOnClickListener(this)
        binding.cardViewHelp.setOnClickListener(this)


//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.card_view_consult -> {
                val intent = Intent(context, ConsultActivity::class.java)
                startActivity(intent)
            }
            R.id.card_view_diagnoze -> {
                Toast.makeText(context, "diagnoze", Toast.LENGTH_SHORT).show()
            }
            
            R.id.card_view_help -> {
                Toast.makeText(context, "help", Toast.LENGTH_SHORT).show()
            }

            R.id.card_my_order ->{
                Toast.makeText(context, "my order", Toast.LENGTH_SHORT).show()
            }
        }

    }
}