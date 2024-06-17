package app.bangkit.ishara.ui.main.ui.journey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.bangkit.ishara.data.models.Item
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.FragmentJourneyBinding
import app.bangkit.ishara.domain.adapter.JourneyAdapter

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null
    private val binding get() = _binding!!
    private lateinit var journeyViewModel: JourneyViewModel
    private lateinit var journeyAdapter: JourneyAdapter
    private var pref: UserPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pref = UserPreference.getInstance(requireActivity().application.dataStore)
        journeyViewModel = ViewModelProvider(this)[JourneyViewModel::class.java]
        _binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        journeyAdapter = JourneyAdapter()
        journeyAdapter.setOnItemClickCallback(object : JourneyAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Item) {
                // Handle item click
            }
        })

        binding.recyclerViewJourney.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = journeyAdapter
        }

        journeyViewModel.journeyList.observe(viewLifecycleOwner) { items ->
            Log.d("JourneyFragment", "Received items: $items")
            items.let {
                journeyAdapter.submitList(items)
            }
        }

        journeyViewModel.getJourney(
            jwtAccessToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vaXNoYXJhLWJhY2tlbmQteGl1bDRkbG5mYS1ldC5hLnJ1bi5hcHAvYXBpL3YxL2F1dGgvbG9naW4iLCJpYXQiOjE3MTg0Njg1MjgsImV4cCI6MTcxODU1NDkyOCwibmJmIjoxNzE4NDY4NTI4LCJqdGkiOiJCMmgxQXF3SVpuZ0I0bDVCIiwic3ViIjoiMyIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.Ms2nhARJ0I3tTlVMP3_j2zlw4Y4mIEsCHYDfl0gutyk"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
