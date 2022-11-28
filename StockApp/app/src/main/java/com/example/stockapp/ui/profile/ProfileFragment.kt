package com.example.stockapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.BaseApplication
import com.example.stockapp.databinding.FragmentProfileBinding
import com.example.stockapp.profilecard.ProfileCardAdapter
import com.example.stockapp.profilecard.ProfileCardViewModel
import com.example.stockapp.totalcard.TotalCardViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var adapter: ProfileCardAdapter
    private lateinit var profileViewModel: ProfileCardViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load profiles
        adapter = ProfileCardAdapter(this.context)
        binding.profileRecyclerView.adapter = adapter
        profileViewModel = ViewModelProvider(this)[ProfileCardViewModel::class.java]
        profileViewModel.init((activity?.application as BaseApplication).database.getDao())
        binding.profileRecyclerView.setHasFixedSize(true)

        binding.signOutButton.setOnClickListener {
            val instance = FirebaseAuth.getInstance()
            instance.signOut()
            activity?.finish()
        }

        return root

    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getPortfolioValue(object : ProfileCardViewModel.ComputeListener
        {
            override fun computeFinish(total: Double) {
                GlobalScope.launch(Dispatchers.Main) {
                    adapter.updateProfile("Default", total)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}