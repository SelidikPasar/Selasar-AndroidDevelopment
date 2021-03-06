package com.selasarteam.selidikpasar.view.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.selasarteam.selidikpasar.R
import com.selasarteam.selidikpasar.databinding.FragmentProfileBinding
import com.selasarteam.selidikpasar.view.viewmodel.ProfileViewModel
import com.selasarteam.selidikpasar.view.viewmodel.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var factory: ViewModelFactory? = null
    private val viewModel: ProfileViewModel by viewModels { factory!! }

    private var auth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupData()
        setupAction()
        checkUserStatus()
    }

    private fun checkUserStatus() {
        auth = Firebase.auth
        currentUser = auth?.currentUser
        viewModel.getSession().observe(viewLifecycleOwner) {
            token = it.token
            if (!it.isLogin && currentUser == null) {
                setupView(false)
            } else {
                setupView(true)
            }
        }
    }

    private fun setupView(state: Boolean) {
        if (state) {
            binding.apply {
                whitespace.visibility = View.GONE
                ivNotLogin.visibility = View.GONE
                tvNotLogin.visibility = View.GONE
                btnRegisterHere.visibility = View.GONE

                ivAvatar.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
                tvEmail.visibility = View.VISIBLE
                btnChangeLang.visibility = View.VISIBLE
                btnLogout.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                whitespace.visibility = View.VISIBLE
                ivNotLogin.visibility = View.VISIBLE
                tvNotLogin.visibility = View.VISIBLE
                btnRegisterHere.visibility = View.VISIBLE

                ivAvatar.visibility = View.GONE
                tvName.visibility = View.GONE
                tvEmail.visibility = View.GONE
                btnChangeLang.visibility = View.GONE
                btnLogout.visibility = View.GONE
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireActivity())
    }

    private fun setupData() {
        binding.apply {
            viewModel.getSession().observe(viewLifecycleOwner) {
                tvName.text = it.name
                tvEmail.text = it.email
                Glide.with(requireContext())
                    .load(it.photo)
                    .apply(
                        RequestOptions
                            .circleCropTransform()
                            .placeholder(R.drawable.ic_user_loading)
                            .error(R.drawable.ic_user_error)
                    ).into(ivAvatar)
            }
        }

    }

    private fun setupAction() {
        binding.apply {
            btnChangeLang.setOnClickListener { startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS)) }
            btnLogout.setOnClickListener { userLogout() }

            btnRegisterHere.setOnClickListener {
                startActivity(Intent(requireActivity(), RegisterActivity::class.java))
            }
        }
    }

    private fun userLogout() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        viewModel.logout()
        auth?.signOut()
    }

    override fun onStart() {
        super.onStart()
        checkUserStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}