package com.example.streamtv.streams

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streamtv.api.Api
import com.example.streamtv.databinding.StreamsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StreamsFragment : Fragment() {
    private var _binding: StreamsBinding? = null
    private var recyclerView: RecyclerView? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StreamsBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Api.streamsService.getStreams().enqueue(
            object : Callback<List<Stream>> {
                override fun onResponse(
                    call: Call<List<Stream>>,
                    response: Response<List<Stream>>
                ) {
                    val streams = response.body() as List<Stream>
                    val adapter = StreamsAdapter(requireContext(), streams)
                    recyclerView?.adapter = adapter
                }

                override fun onFailure(call: Call<List<Stream>>, t: Throwable) {
                    Log.v("Streams", "Error fetching streams: " + t.message)
                }
            }
        )
    }
}
