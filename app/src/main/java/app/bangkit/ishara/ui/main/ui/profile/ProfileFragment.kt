package app.bangkit.ishara.ui.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.FragmentProfileBinding
import app.bangkit.ishara.ui.auth.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private var pref: UserPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = UserPreference.getInstance(requireActivity().application.dataStore)
        lifecycleScope.launch {
            pref?.getJwtAccessToken()?.collect { token ->
                Log.d("ProfileFragment", "Access token: $token")
                if (token.isNotEmpty()) {
                    profileViewModel.fetchUserProfile(token)
                }
            }
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.pbProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.tvEmail.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            binding.tvName.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }

        profileViewModel.profileData.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.tvName.text = it.data?.name
                binding.tvEmail.text = it.data?.email
                binding.tvTotalStars.text = it.data?.totalStars.toString()
            }
        }


        binding.btnLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}