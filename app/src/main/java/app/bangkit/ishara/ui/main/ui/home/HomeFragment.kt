package app.bangkit.ishara.ui.main.ui.home

import TodaySignAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.FragmentHomeBinding
import kotlinx.coroutines.coroutineScope


data class TodaySign(
    val imagePath: Int,
    val alphabet: String
)


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pref: UserPreference? = null

    private lateinit var todaySignsList: List<TodaySign>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        pref = UserPreference.getInstance(requireActivity().application.dataStore)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.completedAlphabet.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        todaySignsList = prepareTodaySigns()
        binding.rvTodaySign.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTodaySign.adapter = TodaySignAdapter(requireContext(), todaySignsList)

        return root
    }

    private fun prepareTodaySigns(): List<TodaySign> {
        val signList = mutableListOf<TodaySign>()

        for (i in 'A'..'Z') {
            val imageName = "ic_man_$i".toLowerCase()
            val imageResId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)
            if (imageResId != 0) {
                signList.add(TodaySign(imageResId, i.toString()))
            }
        }

        return signList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}