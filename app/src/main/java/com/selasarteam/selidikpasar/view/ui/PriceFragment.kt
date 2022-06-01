package com.selasarteam.selidikpasar.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.selasarteam.selidikpasar.R
import com.selasarteam.selidikpasar.databinding.FragmentPriceBinding
import com.selasarteam.selidikpasar.view.model.ViewModelFactory

class PriceFragment : Fragment() {

    private var _binding: FragmentPriceBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPriceBinding.inflate(inflater, container, false)

        setupViewModel()
        setupMore()

        return binding.root
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    private fun setupMore(){
//        val priceFragment = PriceFragment()
        val itemFragment = ItemFragment()


        binding.ibMore.setOnClickListener{
//            val intent = Intent(activity, ItemFragment::class.java)
//            activity?.startActivity(intent)

            childFragmentManager.beginTransaction().apply {
                replace(R.id.price_fragment, itemFragment)
                commit()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}